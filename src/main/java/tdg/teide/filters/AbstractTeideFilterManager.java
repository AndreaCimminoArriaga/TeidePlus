package tdg.teide.filters;

import tdg.teide.cache.H2Cache;

public abstract class AbstractTeideFilterManager {


	protected String metricTable, filterTable;
	protected String inputTable;
	protected String filterName;
	
	protected TeideFilterComponent filter;
	protected TeideFilterComponent metric;
	
	public AbstractTeideFilterManager(String filterName, Double threshold, String inputTable) {
		if(filterName==null || filterName.isEmpty() || inputTable==null || inputTable.isEmpty() || threshold == null)
			throw new IllegalArgumentException();
		

		this.filterName = filterName;
		this.inputTable = inputTable;
		this.metricTable = filterName.concat("_metric");
		this.filterTable = filterName.concat("_filtered");
	}
	
	
	public void deleteViews() {
		StringBuffer dropFilterTableQuery = null;
		// Delete previous results stored in the view
		dropFilterTableQuery = new StringBuffer();
		dropFilterTableQuery.append("DROP TABLE IF EXISTS ").append(filterTable);
		// execute query to drop a view
		H2Cache.updateTableQuery(dropFilterTableQuery.toString());
		
		StringBuffer dropMetricTableQuery = null;
		// Delete previous results stored in the view
		dropMetricTableQuery = new StringBuffer();
		dropMetricTableQuery.append("DROP TABLE IF EXISTS ").append(metricTable);
		// execute query to drop a view
		H2Cache.updateTableQuery(dropMetricTableQuery.toString());
		
		
	}
	

	public String applyFilter() {
		String resultsTable = null;
		// Delete previous results
		this.deleteViews();
		// apply metric
		if(metric!=null)
			metric.applyFilter();
		// apply filter
		if(filter!=null)
			resultsTable = filter.applyFilter();
		// return table with results
		return resultsTable;
	}


	public TeideFilterComponent getMetric() {
		return metric;
	}


	public void setMetric(TeideFilterComponent metric) {
		this.metric = metric;
	}
	
	
}
