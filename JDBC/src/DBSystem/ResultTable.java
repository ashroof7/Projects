package DBSystem;

public interface ResultTable {
	public int getColumnIndex(String columnName);

	public String getColumnName(int index);

	public String getColumnLabel(int index);

	public String getColumnType(int index);

	public String getValue(int row, int column);

	public int size();

	public int columnCount();

	public String getParentTableName();

	public String toString();

}
