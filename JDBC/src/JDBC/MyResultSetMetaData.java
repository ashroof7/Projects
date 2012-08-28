package JDBC;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import DBSystem.ResultTable;

public class MyResultSetMetaData implements ResultSetMetaData {
	private ResultTable resultTable;
	private HashMap<String, Integer> types = new HashMap<String, Integer>();

	public MyResultSetMetaData(ResultTable result) {
		resultTable = result;
		buildHashMap();
	}

	private void buildHashMap() {
		types.put("array", java.sql.Types.ARRAY);
		types.put("boolean", java.sql.Types.BOOLEAN);
		types.put("char", java.sql.Types.CHAR);
		types.put("float", java.sql.Types.FLOAT);
		types.put("integer", java.sql.Types.INTEGER);
		types.put("double", java.sql.Types.DOUBLE);
		types.put("null", java.sql.Types.NULL);
		types.put("string", java.sql.Types.VARCHAR);
		types.put("object", java.sql.Types.JAVA_OBJECT);
		types.put("array", java.sql.Types.ARRAY);
		types.put("date", java.sql.Types.DATE);
	}

	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCatalogName(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getColumnClassName(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getColumnCount() throws SQLException {
		int nColumns;
		try {
			nColumns = resultTable.columnCount();
		} catch (Exception e) {
			throw new SQLException("Null Result Table");
		}
		return nColumns;
	}

	public int getColumnDisplaySize(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getColumnLabel(int arg0) throws SQLException {
		String name = "";
		try {
			name = resultTable.getColumnLabel(arg0-1);
		} catch (Exception e) {
			throw new SQLException("Null Table");
		}
		return name;
	}

	public String getColumnName(int index) throws SQLException {
		String name = "";
		try {
			name = resultTable.getColumnName(index - 1);
		} catch (Exception e) {
			throw new SQLException("Null Table");
		}
		return name;
	}

	public int getColumnType(int arg0) throws SQLException {
		String type = "";
		try {
			type = resultTable.getColumnType(arg0 - 1);
		} catch (Exception e) {
			throw new SQLException("Null Table");
		}
		if (type.contains("varchar"))
			type = "string";
		return types.get(type);
	}

	public String getColumnTypeName(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getPrecision(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getScale(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getSchemaName(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTableName(int arg0) throws SQLException {
		return resultTable.getParentTableName();
	}

	public boolean isAutoIncrement(int arg0) throws SQLException {
		return true;
	}

	public boolean isCaseSensitive(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCurrency(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isDefinitelyWritable(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public int isNullable(int arg0) throws SQLException {
		if (arg0 <= 0 || arg0 > getColumnCount())
			throw new SQLException();

		return columnNullable;
	}

	public boolean isReadOnly(int arg0) throws SQLException {
		if (arg0 <= 0 || arg0 > getColumnCount())
			throw new SQLException();
		return false;
	}

	public boolean isSearchable(int arg0) throws SQLException {
		if (arg0 <= 0 || arg0 > getColumnCount())
			throw new SQLException();
		return true;
	}

	public boolean isSigned(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isWritable(int arg0) throws SQLException {
		if (arg0 <= 0 || arg0 > getColumnCount())
			throw new SQLException();
		return true;
	}

}
