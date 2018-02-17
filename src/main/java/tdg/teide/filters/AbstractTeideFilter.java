package tdg.teide.filters;

import tdg.teide.cache.H2Cache;

public abstract class AbstractTeideFilter implements TeideFilterComponent{

	protected Double threshold;
	protected String inputTable, resultsTable;
	protected String filterName;
	
	public AbstractTeideFilter(Double threshold, String inputTable, String resultsTable, String filterName) {
		
		if(threshold==null || filterName==null || filterName.isEmpty() ||inputTable==null || inputTable.isEmpty() || resultsTable==null || resultsTable.isEmpty())
			throw new IllegalArgumentException();
		
		this.inputTable = inputTable;
		this.filterName = filterName;
		this.resultsTable = resultsTable;
		
		this.threshold = threshold;
	}
	
	public String getResultsTable() {
		return resultsTable;
	}
			
	protected void execute(String query) {
		
		StringBuffer viewQuery = null;
		// init view query
		viewQuery = new StringBuffer();
		viewQuery.append("CREATE VIEW ").append(resultsTable).append(" AS ").append(query).append(" ");	
		// execute query to create a view
		H2Cache.updateTableQuery(viewQuery.toString());
		
	}

}
