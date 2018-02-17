package tdg.teide.filters.rule_vote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tdg.teide.cache.H2Cache;
import tdg.teide.filters.AbstractTeideFilter;

public class RuleVoteFilter extends AbstractTeideFilter{

	protected static String originalInputTable;
	
	public RuleVoteFilter(Double threshold, String inputTable, String resultsTable, String filterName) {
		super(threshold, inputTable, resultsTable, filterName);
	}

	@Override
	public String applyFilter() {
		StringBuffer filterQuery = null;
		// Get threshold filter
		Double maxD = getMaxD();
		Double gThreshold = maxD*this.threshold;
		String gThresholdString= String.valueOf(gThreshold);
		// Filter table of rules selecting those that generate a number of links higher than maxD and store in the table the links that they generate and the supporting rule as well
		filterQuery = new StringBuffer();
		filterQuery.append("SELECT IRI_SOURCE, IRI_TARGET, SUPPORTING_RULE FROM ").append(originalInputTable);
		filterQuery.append(" WHERE SUPPORTING_RULE IN ( SELECT SUPPORTING_RULE FROM ").append(inputTable).append(" WHERE ").append(filterName).append(" >= ").append(gThresholdString).append(" )");
		// execute filter
		this.execute(filterQuery.toString());
		
		return this.resultsTable;
		
	}

	private Double getMaxD() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet queryResult = null;
		StringBuffer query = null;
		Double maxD = 0.0;
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
				maxD = Double.valueOf(queryResult.getString("R"));
			}

			// Close connection, statement, and query results
			queryResult.close();
			statement.close();
			connection.close();
	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return maxD;
	}

}
