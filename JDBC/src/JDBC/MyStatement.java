package JDBC;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import Validator.Operation;
import Validator.QueryValidator;
import java.security.NoSuchAlgorithmException;

public class MyStatement implements Statement {

	private QueryBatch batches;
	private MyConnection thisCon;
	private QueryValidator validator;
	private int sec;

	public MyStatement(MyConnection conn) throws NoSuchAlgorithmException, Exception {
		batches = new QueryBatch();
		thisCon = conn;
		validator = new QueryValidator(thisCon.getUrl(), "schema",
				thisCon.getUsername(), thisCon.getPassword());
		sec = 0;
	}

	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void addBatch(String sql) throws SQLException {
		// add the command to the command list of this statement
		// validate the command first
		batches.addBatch(sql);
	}

	public void cancel() throws SQLException {
		// TODO Auto-generated method stub

	}

	public void clearBatch() throws SQLException {
		batches.clear();
	}

	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub

	}

	public void close() throws SQLException {
		// closing the jdbc resources and the ResultSet object if exist
		batches = null;
	}

	public boolean execute(String sql) throws SQLException {
		validator.createQuery(sql);
		int type = validator.getQueryType();
		if (type == 0)// Select
		{
			executeQuery(sql);
			return true;
		} else {
			executeUpdate(sql);
			return false;
		}
	}

	public boolean execute(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean execute(String arg0, int[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean execute(String arg0, String[] arg1) throws SQLException {

		return false;
	}

	public int[] executeBatch() throws SQLException {
		int[] ans = new int[batches.size()];
		try {
			ans = batches.executeBatches(this);
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
		return ans;
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		validator.createQuery(sql);
		Operation op = validator.getOperation();
		try {
			op.execute();
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
		return new MyResultset(this, op.getResultTable());
	}

	public int executeUpdate(String arg0) throws SQLException {
		validator.createQuery(arg0);
		Operation op = validator.getOperation();
		try {
			op.execute();
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
		return op.getUpdateCount();
	}

	public int executeUpdate(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int executeUpdate(String arg0, int[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int executeUpdate(String arg0, String[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		return thisCon;
	}

	public int getFetchDirection() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getMaxFieldSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMaxRows() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean getMoreResults() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean getMoreResults(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public int getQueryTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return sec;
	}

	public ResultSet getResultSet() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getResultSetConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getResultSetHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getResultSetType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getUpdateCount() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setCursorName(String arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setEscapeProcessing(boolean arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setFetchDirection(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setFetchSize(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setMaxFieldSize(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setMaxRows(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setPoolable(boolean arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setQueryTimeout(int seconds) throws SQLException {
		sec = seconds;

	}

    public void closeOnCompletion() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isCloseOnCompletion() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
