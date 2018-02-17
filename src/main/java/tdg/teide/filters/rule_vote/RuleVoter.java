package tdg.teide.filters.rule_vote;

import tdg.teide.filters.AbstractTeideFilterManager;

public class RuleVoter extends AbstractTeideFilterManager {

	
	/*
	 * Generate the table: rule, nº of links
	 */
	public RuleVoter(Double threshold, String inputTable) {
		super("d", threshold, inputTable);
		
		this.metric = new RuleVoteMetric(inputTable, this.metricTable, this.filterName);
		RuleVoteFilter.originalInputTable = inputTable;
		this.filter = new RuleVoteFilter(threshold, this.metricTable, this.filterTable, this.filterName);
	
	}

	
	
}
