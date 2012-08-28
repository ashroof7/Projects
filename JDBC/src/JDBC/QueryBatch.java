package JDBC;

import java.sql.SQLException;
import java.util.ArrayList;

public class QueryBatch {
	private ArrayList<String> batches;

	public QueryBatch() {
		batches = new ArrayList<String>();
	}

	public void addBatch(String sql) {
		batches.add(sql);
	}

	public ArrayList<String> getBatches() {
		return batches;
	}

	public void clear() {
		batches.clear();
	}

	public int size() {
		return batches.size();
	}

	public int[] executeBatches(MyStatement st) throws SQLException {
		int[] updateCounts = new int[batches.size()];
		try {
			for (int i = 0; i < updateCounts.length; i++)
				updateCounts[i] = st.executeUpdate(batches.get(i));
		} catch (Exception e) {
			throw new SQLException();
		}
		return updateCounts;
	}
}
