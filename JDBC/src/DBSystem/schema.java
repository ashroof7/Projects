package DBSystem;

import java.io.IOException;

public interface schema {
	public boolean read() throws Exception;

	public String[] getTableNames();

	public String[][] getColNames();

	public String[][] getColTypes();

	public void write(String UserName, String Password) throws IOException;

	public void set(String[] tables, String[][] names, String[][] types);

	public String getUser();

	public String getPassword();
}
