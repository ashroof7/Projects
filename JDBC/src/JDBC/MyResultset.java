package JDBC;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import DBSystem.ResultTable;

public class MyResultset implements ResultSet {
	public ResultTable resultTable;// Current table that contains user's query
									// results
	public Statement currStatement;
	private int currRecord = 0;// initially before the first row

	public MyResultset(Statement st, ResultTable results) {
		currStatement = st;
		resultTable = results;
	}

	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean absolute(int index) throws SQLException {
		// true if the cursor is moved to a position in this ResultSet object;
		// false if the cursor is before the first row or after the last row
		try {
			currRecord = index < 0 ? resultTable.size() + index + 1 : index;
		} catch (Exception e) {// error in database access
			throw new SQLException("Database Error!");
		}
		return currRecord > 0 && currRecord <= resultTable.size();
	}

	public void afterLast() throws SQLException {
		try {
			currRecord = resultTable.size() + 1;
		} catch (Exception e) {// error in database
			throw new SQLException("Database Error!");
		}
	}

	public void beforeFirst() throws SQLException {
		currRecord = 0;
	}

	public void cancelRowUpdates() throws SQLException {
		// TODO Auto-generated method stub

	}

	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub

	}

	public void close() throws SQLException {
		resultTable = null;// garbage collector
	}

	public void deleteRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	public int findColumn(String col) throws SQLException {
		int colIndex = -1; // no column exists with the given name
		try {
			if (resultTable.size() == 0)
				throw new SQLException();
			if ((colIndex = resultTable.getColumnIndex(col)) == -1)
				throw new SQLException("Database Error!");
		} catch (Exception e) {
			throw new SQLException("Database Error!");
		}
		return colIndex + 1;
	}

	public boolean first() throws SQLException {
		return absolute(1);
	}

	public Array getArray(int columnIndex) throws SQLException {
		try {
			resultTable.getValue(currRecord - 1, columnIndex - 1);
		} catch (Exception e) {
			throw new SQLException();
		}
		return (Array) ((Object) resultTable.getValue(currRecord - 1,
				columnIndex - 1));
	}

	public Array getArray(String arg0) throws SQLException {
		int columnIndex = 0;
		try {
			columnIndex = resultTable.getColumnIndex(arg0);
		} catch (Exception e) {
			throw new SQLException("Database Error!");
		}
		return getArray(columnIndex + 1);
	}

	public InputStream getAsciiStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream getAsciiStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getBigDecimal(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getBigDecimal(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("deprecation")
	public BigDecimal getBigDecimal(int arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("deprecation")
	public BigDecimal getBigDecimal(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream getBinaryStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public InputStream getBinaryStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Blob getBlob(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Blob getBlob(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getBoolean(int colIndex) throws SQLException {
		try {// checking entry type
			if (!resultTable.getColumnType(colIndex - 1).equals("boolean"))
				throw new SQLException();
		} catch (Exception e) {
			throw new SQLException("Incompatible Type");
		}
		String val = "";
		try {
			val = resultTable.getValue(currRecord - 1, colIndex - 1);
			if (val.equals("null"))
				return false;
		} catch (Exception e) {
			throw new SQLException("Database Error!");
		}
		return Boolean.parseBoolean(val);
	}

	public boolean getBoolean(String arg0) throws SQLException {
		int colIndex;
		try {// checking entry type
			colIndex = resultTable.getColumnIndex(arg0);
			if (!resultTable.getColumnType(colIndex).equals("boolean"))
				throw new SQLException("Incompatible Type");
		} catch (Exception e) {
			throw new SQLException("Database Error!");
		}
		return getBoolean(colIndex + 1);
	}

	public byte getByte(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public byte getByte(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public byte[] getBytes(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] getBytes(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Reader getCharacterStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Reader getCharacterStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Clob getClob(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Clob getClob(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getConcurrency() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getCursorName() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate(int colIndex) throws SQLException {
		try {// checking entry type
			if (!resultTable.getColumnType(colIndex - 1).equals("date"))
				throw new SQLException();
		} catch (Exception e) {
			throw new SQLException("Incompatible Type");
		}
		try {
			if (resultTable.getValue(currRecord - 1, colIndex - 1).equals(
					"null"))
				return null;
		} catch (Exception e) {
			throw new SQLException("Database Error!");
		}
		return Date.valueOf(resultTable.getValue(currRecord - 1, colIndex - 1));
	}

	public Date getDate(String arg0) throws SQLException {
		int colIndex;
		try {// checking entry type
			colIndex = resultTable.getColumnIndex(arg0);
			if (!resultTable.getColumnType(colIndex).equals("date"))
				throw new SQLException("Incompatible Type");
		} catch (Exception e) {
			throw new SQLException("Database Error!");
		}
		return getDate(colIndex + 1);
	}

	public Date getDate(int arg0, Calendar arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate(String arg0, Calendar arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public double getDouble(int colIndex) throws SQLException {
		try {// checking entry type
			if (!resultTable.getColumnType(colIndex - 1).equals("double"))
				throw new SQLException("Incompatible Type");
		} catch (Exception e) {
			throw new SQLException();
		}
		try {
			if (resultTable.getValue(currRecord - 1, colIndex - 1).equals(
					"null"))
				return 0;
		} catch (Exception e) {
			throw new SQLException("Database Error!");
		}
		return Double.parseDouble(resultTable.getValue(currRecord - 1,
				colIndex - 1));
	}

	public double getDouble(String arg0) throws SQLException {
		int colIndex;
		try {// checking entry type
			colIndex = resultTable.getColumnIndex(arg0);
			if (!resultTable.getColumnType(colIndex).equals("double"))
				throw new SQLException("Incompatible Type");
		} catch (Exception e) {
			throw new SQLException("Database Error!");
		}
		return getDouble(colIndex + 1);
	}

	public int getFetchDirection() throws SQLException {
		return FETCH_FORWARD;
	}

	public int getFetchSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public float getFloat(int colIndex) throws SQLException {
		try {// checking entry type
			if (!resultTable.getColumnType(colIndex - 1).equals("float"))
				throw new SQLException("Incompatible Type");
		} catch (Exception e) {
			throw new SQLException("Database Error!");
		}
		try {
			if (resultTable.getValue(currRecord - 1, colIndex - 1).equals(
					"null"))
				return 0;
		} catch (Exception e) {
			throw new SQLException("Database Error!");
		}
		return Float.parseFloat(resultTable.getValue(currRecord - 1,
				colIndex - 1));
	}

	public float getFloat(String arg0) throws SQLException {
		int colIndex;
		try {// checking entry type
			colIndex = resultTable.getColumnIndex(arg0);
			if (!resultTable.getColumnType(colIndex).equals("float"))
				throw new SQLException("Incompatible Type");
		} catch (Exception e) {
			throw new SQLException("Database Error!");
		}
		return getFloat(colIndex + 1);
	}

	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getInt(int colIndex) throws SQLException {
		try {// checking entry type
			if (!resultTable.getColumnType(colIndex - 1).equals("integer"))
				throw new SQLException();
		} catch (Exception e) {
			throw new SQLException("Incompatible Type");
		}
		try {
			if (resultTable.getValue(currRecord - 1, colIndex - 1).equals(
					"null"))
				return 0;
		} catch (Exception e) {
			throw new SQLException("Database Error!");
		}
		return Integer.parseInt(resultTable.getValue(currRecord - 1,
				colIndex - 1));
	}

	public int getInt(String arg0) throws SQLException {
		int colIndex;
		try {// checking entry type
			colIndex = resultTable.getColumnIndex(arg0);
			if (!resultTable.getColumnType(colIndex).equals("integer"))
				throw new SQLException("Incompatible Type");
		} catch (Exception e) {
			throw new SQLException("Database Error!");
		}
		return getInt(colIndex + 1);
	}

	public long getLong(int colIndex) throws SQLException {
		try {// checking entry type
			if (!resultTable.getColumnType(colIndex - 1).equals("long"))
				throw new SQLException("Incompatible Type");
		} catch (Exception e) {
			throw new SQLException("Database Error!");
		}
		try {
			if (resultTable.getValue(currRecord - 1, colIndex - 1).equals(
					"null"))
				return 0;
		} catch (Exception e) {
			throw new SQLException("Database Error!");
		}
		return Long.parseLong(resultTable
				.getValue(currRecord - 1, colIndex - 1));
	}

	public long getLong(String arg0) throws SQLException {
		int colIndex;
		try {// checking entry type
			colIndex = resultTable.getColumnIndex(arg0);
			if (!resultTable.getColumnType(colIndex).equals("long"))
				throw new SQLException("Incompatible Type");
		} catch (Exception e) {
			throw new SQLException("Database Error!");
		}
		return getLong(colIndex + 1);
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		MyResultSetMetaData resultMetaData;
		try {
			resultMetaData = new MyResultSetMetaData(resultTable);
		} catch (Exception e) {
			throw new SQLException("Database Error!");
		}
		return resultMetaData;
	}

	public Reader getNCharacterStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Reader getNCharacterStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public NClob getNClob(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public NClob getNClob(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNString(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNString(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getObject(int colIndex) throws SQLException {
		try {
			if (resultTable.getValue(currRecord - 1, colIndex - 1).equals(
					"null"))
				return null;
		} catch (Exception e) {
			throw new SQLException("Database Error!");
		}
		return (Object) (resultTable.getValue(currRecord - 1, colIndex - 1));
	}

	public Object getObject(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getObject(int arg0, Map<String, Class<?>> arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getObject(String arg0, Map<String, Class<?>> arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Ref getRef(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Ref getRef(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getRow() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public RowId getRowId(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public RowId getRowId(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLXML getSQLXML(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLXML getSQLXML(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public short getShort(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public short getShort(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Statement getStatement() throws SQLException {
		return currStatement;
	}

	public String getString(int colIndex) throws SQLException {
		try {// checking entry type
			if (!resultTable.getColumnType(colIndex - 1).equals("varchar(20)"))
				throw new SQLException();
		} catch (Exception e) {
			throw new SQLException();
		}
		try {
			if (resultTable.getValue(currRecord - 1, colIndex - 1).equals(
					"null"))
				return null;
		} catch (Exception e) {
			throw new SQLException();
		}
		return resultTable.getValue(currRecord - 1, colIndex - 1);
	}

	public String getString(String arg0) throws SQLException {
		int colIndex;
		try {// checking entry type
			colIndex = resultTable.getColumnIndex(arg0);
			if (!resultTable.getColumnType(colIndex).equals("string"))
				throw new SQLException("Incompatible Type");
		} catch (Exception e) {
			throw new SQLException("Database Error!");
		}
		return getString(colIndex + 1);
	}

	public Time getTime(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Time getTime(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Time getTime(int arg0, Calendar arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Time getTime(String arg0, Calendar arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Timestamp getTimestamp(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Timestamp getTimestamp(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Timestamp getTimestamp(int arg0, Calendar arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Timestamp getTimestamp(String arg0, Calendar arg1)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public URL getURL(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public URL getURL(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("deprecation")
	public InputStream getUnicodeStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("deprecation")
	public InputStream getUnicodeStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void insertRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	public boolean isAfterLast() throws SQLException {
		try {
			if (resultTable.size() == 0)
				return false;
		} catch (Exception e) {// database error occurs
			throw new SQLException("Database Error!");
		}
		return currRecord == resultTable.size() + 1;
	}

	public boolean isBeforeFirst() throws SQLException {
		try {
			if (resultTable.size() == 0)
				return false;
		} catch (Exception e) {// database error occurs
			throw new SQLException("Database Error!");
		}
		return currRecord == 0;
	}

	public boolean isClosed() throws SQLException {
		boolean closed = false;
		try {
			closed = currStatement.isClosed();
		} catch (Exception e) {
			throw new SQLException();
		}
		return closed;
	}

	public boolean isFirst() throws SQLException {
		return currRecord == 1 && resultTable.size() != 0;
	}

	public boolean isLast() throws SQLException {
		boolean last = false;
		try {
			last = (currRecord == resultTable.size())
					&& (resultTable.size() != 0);
		} catch (Exception e) {// error in database
			throw new SQLException("Database Error!");
		}
		return last;
	}

	public boolean last() throws SQLException {
		return absolute(-1);
	}

	public void moveToCurrentRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	public void moveToInsertRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	public boolean next() throws SQLException {
		currRecord++;
		boolean b;
		try {
			b = currRecord <= resultTable.size();
		} catch (Exception e) {
			throw new SQLException("Empty Table");
		}
		return b;
	}

	public boolean previous() throws SQLException {
		currRecord--;
		return currRecord > 0;
	}

	public void refreshRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	public boolean relative(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean rowDeleted() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean rowInserted() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean rowUpdated() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setFetchDirection(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setFetchSize(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateArray(int arg0, Array arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateArray(String arg0, Array arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateAsciiStream(int arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateAsciiStream(String arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateAsciiStream(int arg0, InputStream arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateAsciiStream(String arg0, InputStream arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateAsciiStream(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateAsciiStream(String arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBigDecimal(int arg0, BigDecimal arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBigDecimal(String arg0, BigDecimal arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBinaryStream(int arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBinaryStream(String arg0, InputStream arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBinaryStream(int arg0, InputStream arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBinaryStream(String arg0, InputStream arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBinaryStream(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBinaryStream(String arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBlob(int arg0, Blob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBlob(String arg0, Blob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBlob(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBlob(String arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBlob(int arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBlob(String arg0, InputStream arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBoolean(int arg0, boolean arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBoolean(String arg0, boolean arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateByte(int arg0, byte arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateByte(String arg0, byte arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBytes(int arg0, byte[] arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateBytes(String arg0, byte[] arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateCharacterStream(int arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateCharacterStream(String arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateCharacterStream(int arg0, Reader arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateCharacterStream(String arg0, Reader arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateCharacterStream(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateCharacterStream(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateClob(int arg0, Clob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateClob(String arg0, Clob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateClob(String arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateClob(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateClob(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateDate(int arg0, Date arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateDate(String arg0, Date arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateDouble(int arg0, double arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateDouble(String arg0, double arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateFloat(int arg0, float arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateFloat(String arg0, float arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateInt(int arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateInt(String arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateLong(int arg0, long arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateLong(String arg0, long arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateNCharacterStream(int arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateNCharacterStream(String arg0, Reader arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateNCharacterStream(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateNCharacterStream(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateNClob(int arg0, NClob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateNClob(String arg0, NClob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateNClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateNClob(String arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateNClob(int arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateNClob(String arg0, Reader arg1, long arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateNString(int arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateNString(String arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateNull(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateNull(String arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateObject(int arg0, Object arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateObject(String arg0, Object arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateObject(int arg0, Object arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateObject(String arg0, Object arg1, int arg2)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateRef(int arg0, Ref arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateRef(String arg0, Ref arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateRow() throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateRowId(int arg0, RowId arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateRowId(String arg0, RowId arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateSQLXML(int arg0, SQLXML arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateSQLXML(String arg0, SQLXML arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateShort(int arg0, short arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateShort(String arg0, short arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateString(int arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateString(String arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateTime(int arg0, Time arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateTime(String arg0, Time arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateTimestamp(int arg0, Timestamp arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void updateTimestamp(String arg0, Timestamp arg1)
			throws SQLException {
		// TODO Auto-generated method stub

	}

	public boolean wasNull() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
