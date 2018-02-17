package tdg.teide;

import java.util.List;

import com.google.common.collect.Lists;

import tdg.teide.filters.link_vote.LinkVoter;
import tdg.teide.filters.overlap.Overlapper;
import tdg.teide.filters.rule_vote.RuleVoter;

public class FiltersMain {

	public static void main(String[] args) {
		
		
		List<String> resultTables = Lists.newArrayList();
		
		Overlapper overlaper = new Overlapper(0.2);
		String overlapperResults = overlaper.applyFilter();
		
		
		LinkVoter linkVoter = new LinkVoter(1.0, overlapperResults);
		String linkVoteResults = linkVoter.applyFilter();
		resultTables.add(linkVoteResults);
		
		RuleVoter ruleVoter = new RuleVoter(0.0, overlapperResults);
		String ruleVoteResults = ruleVoter.applyFilter();
		resultTables.add(ruleVoteResults);
		
		// OR retrieve 
		
		
	}

	
	
}
