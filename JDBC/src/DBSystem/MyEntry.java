package DBSystem;

public class MyEntry implements Entry {
	private String[] values;

	public MyEntry(String[] value) {
		values = value;
	}

	@Override
	public void changeValue(String value, int index) {
		values[index] = value;
	}

	@Override
	public boolean equal(String[] value, int[] index) {
		for (int i = 0; i < index.length; i++)
			if (values[index[i]].equals(value[i]))
				return false;
		return true;
	}

	@Override
	public String[] getValues() {
		return values;
	}
}
