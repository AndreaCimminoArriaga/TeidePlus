package tdg.teide.filters.link_vote;

import tdg.teide.filters.AbstractTeideFilterMetric;

public class LinkVoteMetric extends AbstractTeideFilterMetric{

	public LinkVoteMetric(String inputTable, String resultsTable, String filterMetricName) {
		super(inputTable, resultsTable, filterMetricName);		
	}

	/*
	 * VERSION 1.0 - NO PATH ACUMULATOR
	
	@Override
	public String applyFilter() {
		StringBuffer queryMetricFilter = null;
		// Prepare query to create a new view with the filter metric
		queryMetricFilter = new StringBuffer();
		queryMetricFilter.append("SELECT  IRI_SOURCE, IRI_TARGET, COUNT(*) AS ").append(this.metricName);
		queryMetricFilter.append(" FROM ").append(this.inputTable).append(" GROUP BY IRI_SOURCE, IRI_TARGET");
		// Execute query
		this.execute(queryMetricFilter.toString());
		
		return this.resultsTable;
	}
	*/
	
	@Override
	public String applyFilter() {
		StringBuffer queryMetricFilter = null;
		// Prepare query to create a new view with the filter metric
		queryMetricFilter = new StringBuffer();
		queryMetricFilter.append("SELECT  IRI_SOURCE, IRI_TARGET, COUNT(*) AS ").append(this.metricName);
		queryMetricFilter.append(" FROM ").append(this.inputTable).append(" GROUP BY IRI_SOURCE, IRI_TARGET, NEIGHBOR_SOURCE_PATH, NEIGHBOR_TARGET_PATH  ");
		// Execute query
		this.execute(queryMetricFilter.toString());
		
		return this.resultsTable;
	}
}
