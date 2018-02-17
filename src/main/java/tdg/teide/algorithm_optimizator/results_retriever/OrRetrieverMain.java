package tdg.teide.algorithm_optimizator.results_retriever;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import tdg.evaluator.LinksEvaluator;
import tdg.evaluator.model.ConfusionMatrix;
import tdg.evaluator.parameters.LinksEvaluatorParameters;
import tdg.evaluator.parameters.LinksEvaluatorParametersReader;
import tdg.link_discovery.middleware.utils.FrameworkUtils;
import tdg.teide.cache.H2Cache;
import tdg.teide.filters.link_vote.LinkVoter;
import tdg.teide.filters.overlap.Overlapper;
import tdg.teide.filters.rule_vote.RuleVoter;

public class OrRetrieverMain {

	private static List<String> goldLinks = Lists.newArrayList();

	
	public static void main(String[] args) {
		cleanDatabse();
		String parametersFile =args[0].trim(); //"./restaurants-input.json";
		Double overlap = Double.valueOf(args[1]);
		Double g = Double.valueOf(args[2]);
		Double d = Double.valueOf(args[3]);

		
		Overlapper overlaper = new Overlapper(overlap);
		String overlapperResults = overlaper.applyFilter();
		
		LinkVoter linkVoter = new LinkVoter(g, overlapperResults);
		String linkVoteResults = linkVoter.applyFilter();
		
		RuleVoter ruleVoter = new RuleVoter(d, overlapperResults);
		String ruleVoteResults = ruleVoter.applyFilter();

		String results  = evaluateResults(linkVoteResults, ruleVoteResults, parametersFile);
		System.out.println("Teide result after filtering [OR]: "+results);
		cleanDatabse();
	}

	
	
	
	
	private static String evaluateResults(String tableNameG, String tableNameD, String parametersFile) {
		LinksEvaluatorParameters parametersEvaluator = null;
		LinksEvaluator evaluator = null;
		String result = null;
		try {
			parametersEvaluator =  LinksEvaluatorParametersReader.parseFromJSON(parametersFile);
			// Retrieve links
			if(goldLinks.isEmpty())
				goldLinks = FrameworkUtils.readGoldLinks(parametersEvaluator.getGoldStandard());
			
			Set<String> links = Sets.newHashSet(H2Cache.retrieveLinks(tableNameG));
			links.addAll(H2Cache.retrieveLinks(tableNameD));
			
			// Evaluate links
			evaluator = new LinksEvaluator(parametersEvaluator);
			ConfusionMatrix matrix = evaluator.evaluateEffectiveness(Sets.newHashSet(goldLinks), links);
			
			result = matrix.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	
	
	private static void cleanDatabse() {
		Overlapper overlaper = new Overlapper(0.1);
		LinkVoter linkVoter = new LinkVoter(1.0, "overlap_filtered");
		RuleVoter ruleVoter = new RuleVoter(1.0, "overlap_filtered");
		linkVoter.deleteViews();
		ruleVoter.deleteViews();
		overlaper.deleteViews();
	}

}
