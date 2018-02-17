package tdg.teide;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import tdg.evaluator.LinksEvaluator;
import tdg.evaluator.model.ConfusionMatrix;
import tdg.evaluator.parameters.LinksEvaluatorParameters;
import tdg.evaluator.parameters.LinksEvaluatorParametersReader;
import tdg.link_discovery.middleware.framework.configuration.FrameworkConfiguration;
import tdg.link_discovery.middleware.log.Logger;
import tdg.link_discovery.middleware.utils.FilesUtils;
import tdg.link_discovery.middleware.utils.FrameworkUtils;
import tdg.pathfinder.parameters.PathFinderParameters;
import tdg.pathfinder.parameters.PathFinderParametersReader;
import tdg.teide.cache.H2Cache;
import tdg.teide.filters.link_vote.LinkVoter;
import tdg.teide.filters.overlap.Overlapper;
import tdg.teide.filters.rule_vote.RuleVoter;
import tdg.teide.parameters.InputReader;
import tdg.teide.parameters.TeideParameters;

public class TeideMain {

	private static String parametersFile;

	
	public static void main(String[] args) throws Exception {
		// Disable jena logs and use ours
		org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);
		// -- Teide
		TeideParameters parameters = null;
		Teide teide = null; 
		// -- PathFinder
		PathFinderParameters parametersSearch = null;
		// -- Thresholds
		Double overlap = null;
		Double g = null;
		Double d = null;
		
		File db = new File("h2cache.mv.db");
		if(db.exists())
			db.delete();
		
		// Retrieve configuration file
		parametersFile = args[0].trim();
		overlap = Double.parseDouble(args[1].trim());
		g = Double.parseDouble(args[2].trim());
		d = Double.parseDouble(args[3].trim());
		
		// Execute Teide and store results
		parameters = InputReader.parseFromJSON(parametersFile);
		parametersSearch = PathFinderParametersReader.parseFromJSON(parametersFile);
		FrameworkConfiguration.traceLog = new Logger(new StringBuffer(parameters.getResultsFolder()).append("/execution-log.txt").toString(), 1);
		teide = new Teide(parameters, parametersSearch);
		FrameworkConfiguration.traceLog.addLogLine(TeideMain.class.getCanonicalName(), "Executing Teide Algorithm");
		long startTime = System.nanoTime();
		
		// Execute core
		teide.execute();
		
		// Apply filters
		Overlapper overlaper = new Overlapper(overlap);
		String overlapperResults = overlaper.applyFilter();
		LinkVoter linkVoter = new LinkVoter(g, overlapperResults);
		String linkVoteResults = linkVoter.applyFilter();
		RuleVoter ruleVoter = new RuleVoter(d, overlapperResults);
		String ruleVoteResults = ruleVoter.applyFilter();
		
		// Evaluate
		String results = evaluateResults(linkVoteResults, ruleVoteResults,  parametersFile, parameters);
		FileWriter writer = new FileWriter(LinksEvaluatorParametersReader.parseFromJSON(parametersFile).getResultsFile()); 
		writer.write("\"technique\",\"tp\",\"tn\",\"fp\",\"fn\",\"P\",\"R\",\"F\"\n");
		writer.write(results);
		writer.close();
		
		
		long stopTime = System.nanoTime();
	    long elapsedTime = (stopTime - startTime)/1000000;
	    FrameworkConfiguration.traceLog.addLogLine(TeideMain.class.getCanonicalName(), "Executed in "+elapsedTime+" (ms)");
	
	}
	
	
	private static String evaluateResults(String tableNameG, String tableNameD, String parametersFile, TeideParameters parameters) {
		LinksEvaluatorParameters parametersEvaluator = null;
		LinksEvaluator evaluator = null;
		String result = null;
		FileWriter writer = null;
		List<String> goldLinks = Lists.newArrayList();
		try {
			parametersEvaluator =  LinksEvaluatorParametersReader.parseFromJSON(parametersFile);
			// Retrieve links
			goldLinks = FrameworkUtils.readGoldLinks(parametersEvaluator.getGoldStandard());
			
			Set<String> linksG = Sets.newHashSet(H2Cache.retrieveLinks(tableNameG));
			Set<String> linksD = Sets.newHashSet(H2Cache.retrieveLinks(tableNameD));
			Set<String> links = Sets.intersection(linksG, linksD);
						
			writer = new FileWriter(parameters.getFilteredLinksFile()); 
			for(String str: links) {
			  writer.write(str);
			}
			
			// Evaluate links
			evaluator = new LinksEvaluator(parametersEvaluator);
			ConfusionMatrix matrix1 = evaluator.evaluateEffectiveness(Sets.newHashSet(goldLinks), Sets.newHashSet(H2Cache.retrieveLinks("links")));
			ConfusionMatrix matrix2 = evaluator.evaluateEffectiveness(Sets.newHashSet(goldLinks), links);
			
			result = "\"main rule\"," +matrix1.toCSVLine() +"\n\"teide+\","+ matrix2.toCSVLine();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(writer!=null)
					writer.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			writer = new FileWriter(parameters.getOutputMainRuleLinksFile()); 
			for(String str: Sets.newHashSet(H2Cache.retrieveLinks("links"))) {
			  writer.write(str);
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(writer!=null)
					writer.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		
		return result;
	}

}
