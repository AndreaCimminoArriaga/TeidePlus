package tdg.teide.filters.link_vote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tdg.teide.cache.H2Cache;
import tdg.teide.filters.AbstractTeideFilter;

public class LinkVoteFilter extends AbstractTeideFilter{

	public LinkVoteFilter(Double threshold, String inputTable, String resultsTable, String filterName) {
		super(threshold, inputTable, resultsTable, filterName);
		
	}

	@Override
	public String applyFilter() {
		StringBuffer filterQuery = null;
		Double maxG = getMaxG();
		Double gThreshold = maxG*this.threshold;
		String gThresholdString= String.valueOf(gThreshold);
		
		filterQuery = new StringBuffer();
		filterQuery.append("SELECT  IRI_SOURCE, IRI_TARGET, ").append(this.filterName).append(" FROM ").append(inputTable);
		filterQuery.append(" WHERE ").append(filterName).append( ">=").append(gThresholdString);
		
		this.execute(filterQuery.toString());
		
		return this.resultsTable;
	}
	
	
	private Double getMaxG() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet queryResult = null;
		StringBuffer query = null;
		Double maxG = 0.0;
		query = new StringBuffer();
		query.append("SELECT MAX(").append(this.filterName).append(") AS R FROM ").append(inputTable);

		try {
			// Create connection
			connection = H2Cache.getConnection();
			connection.setAutoCommit(false);
			// Prepare query to retrieve stored links
			statement = connection.prepareStatement(query.toString());
			statement.setFetchSize(10);
			// Execute query
			queryResult = statement.executeQuery();
			while (queryResult.next()) {
				// Retrieve max g
				maxG = Double.valueOf(queryResult.getString("R"));
			}

			// Close connection, statement, and query results
			queryResult.close();
			statement.close();
			connection.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return maxG;
	}

}
