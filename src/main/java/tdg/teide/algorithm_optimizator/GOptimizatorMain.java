package tdg.teide.algorithm_optimizator;

import java.util.List;

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

public class GOptimizatorMain {

	private static List<String> goldLinks = Lists.newArrayList();
	private static H2Cache cache;

	public static void main(String[] args) {
		cleanDatabse();
		String parametersFile =args[0].trim(); //"./restaurants-input.json";
		Double overlap = Double.valueOf(args[1]);
		cache = new H2Cache();
		
		Overlapper overlaper = new Overlapper(overlap);
		String overlapperResults = overlaper.applyFilter();
		
		double i = 0.0;
		while( i<1.01) {
			LinkVoter linkVoter = new LinkVoter(i, overlapperResults);
			String linkVoteResults = linkVoter.applyFilter();
			String result = evaluateResults(linkVoteResults, parametersFile);
			System.out.println("LinkVote threshold: "+i+"   has "+result);
			i += 0.01;
		}
		
		cleanDatabse();
	}
	
	
	private static String evaluateResults(String tableName, String parametersFile) {
		LinksEvaluatorParameters parametersEvaluator = null;
		LinksEvaluator evaluator = null;
		String result = null;
		try {
			parametersEvaluator =  LinksEvaluatorParametersReader.parseFromJSON(parametersFile);
			// Retrieve links
			if(goldLinks.isEmpty())
				goldLinks = FrameworkUtils.readGoldLinks(parametersEvaluator.getGoldStandard());
			
			List<String> links = cache.retrieveLinks(tableName);
			// Evaluate links
			evaluator = new LinksEvaluator(parametersEvaluator);
			ConfusionMatrix matrix = evaluator.evaluateEffectiveness(Sets.newHashSet(goldLinks), Sets.newHashSet(links));
			
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
