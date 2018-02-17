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
import tdg.teide.filters.TeideFilterComponent;
import tdg.teide.filters.link_vote.LinkVoter;
import tdg.teide.filters.overlap.Overlapper;
import tdg.teide.filters.rule_vote.RuleVoter;

public class OverlapOptimizatorMain {

	private static List<String> goldLinks = Lists.newArrayList();


	public static void main(String[] args) {
		cleanDatabse();
		String parametersFile ="./dblp-dedup-input.json";
		
		
		double i = 0.0;
		while( i<=1) {
			Overlapper overlaper = new Overlapper(i);
			String overlapperResults = overlaper.applyFilter();
			String result = evaluateResults(overlapperResults, parametersFile);
			System.out.println("["+overlapperResults+"] Overlap threshold: "+i+"   has "+result);
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
			List<String> links = H2Cache.retrieveLinks(tableName);
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
