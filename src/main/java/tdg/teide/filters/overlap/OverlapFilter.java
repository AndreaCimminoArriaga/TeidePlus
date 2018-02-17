package tdg.teide.filters.overlap;

import tdg.teide.filters.AbstractTeideFilter;

public class OverlapFilter extends AbstractTeideFilter{


	public OverlapFilter(Double threshold, String inputTable, String resultsTable, String filterName) {
		super(threshold, inputTable, resultsTable, filterName);
		
	}

	@Override
	public String applyFilter() {
		StringBuffer query = null;
		// Init filter query
		query = new StringBuffer();
		query.append("SELECT DISTINCT * FROM ").append(this.inputTable).append(" WHERE ");
		// 	filter criteria
		query.append(filterName).append(" >= ").append(threshold);
		// execute filter
		execute(query.toString());
		
		return this.resultsTable;
	}
	
	

	
	

	
	
}
