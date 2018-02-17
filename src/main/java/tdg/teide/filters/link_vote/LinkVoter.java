package tdg.teide.filters.link_vote;

import tdg.teide.filters.AbstractTeideFilterManager;

public class LinkVoter extends AbstractTeideFilterManager{

	
	/*
	 * Generate the table: source_iri, target_iri, nº of rules that generated such link
	 */
	public LinkVoter(Double threshold, String inputTable) {
		super("g", threshold, inputTable);
		
		this.metric = new LinkVoteMetric(inputTable, this.metricTable, this.filterName);
		this.filter = new LinkVoteFilter(threshold, this.metricTable, this.filterTable, this.filterName);
	}
	
	
	
}
