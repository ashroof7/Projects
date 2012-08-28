package DBSystem;

import java.util.ArrayList;
import java.util.Arrays;

public class MyResultTable implements ResultTable {
	private ArrayList<Entry> result;
	private String[] types;
	private String[] names;
	private String parentTableName;

	public MyResultTable(ArrayList<Entry> table, String[] types,
			String[] names, String parentName) {
		result = table;
		this.types = types;
		this.names = names;
		parentTableName = parentName;
	}

	@Override
	public int getColumnIndex(String columnName) {
		int i;
		for (i = 0; i < names.length; i++)
			if (names[i].equalsIgnoreCase(columnName))
				return i;
		return -1;
	}

	@Override
	public String getColumnType(int index) {
		return types[index];
	}

	@Override
	public String getValue(int row, int column) {
		return result.get(row).getValues()[column];
	}

	@Override
	public int size() {
		return result.size();
	}

	@Override
	public int columnCount() {
		return names.length;
	}

	@Override
	public String getColumnLabel(int index) {
		return names[index];
	}

	@Override
	public String getColumnName(int index) {
		return names[index];
	}

	public String toString() {
		StringBuilder out = new StringBuilder("");
		for (int i = 0; i < names.length; i++)
			out.append(names[i] + " ");
		out.append('\n');
		for (Entry e : result) {
			out.append(Arrays.toString(e.getValues()));
			out.append('\n');
		}
		return out.toString();
	}

	@Override
	public String getParentTableName() {
		return parentTableName;
	}

}
