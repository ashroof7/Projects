package Validator;

import java.sql.SQLException;

import DBSystem.Database;
import DBSystem.Log;

/**
*this class represents
*create table command
*and it extends the Operation
*class
*/

public class Creation extends Operation {

	private String[] types; // holdes the types of columns in the table

	
	//constructor
	public Creation(String sql, Database db) {
		query = sql.toLowerCase().trim();
		current = db;
		loggy = Log.getLogger();
	}
	/**
	 *extracts table name, 
	 *the columns' names and type 
	 *for a new table to be created
	 * @param void
	 *            
	 * @return void
	 * 
	 */
	protected void getParameters() {
		String k;
		int i;
		k = "";
		for (i = 0; i < query.length(); i++) {
			k += query.charAt(i);
			if (k.matches("create(\\s+)table"))
				break;
		}
		tableName = query.substring(i + 1, query.indexOf('(')).trim();
		k = query.substring(query.indexOf('(') + 1, query.lastIndexOf(')'))
				.trim();

		String[] f = k.split(",");

		columns = new String[f.length];
		types = new String[f.length];

		for (i = 0; i < f.length; i++) {
			f[i] = f[i].trim();
			String[] g = f[i].split("\\s");
			columns[i] = g[0];
			types[i] = g[1];
		}
	}

	/**
	*executes the create command
	*calling some functions from 
	*the DBSystem packge
	*@param void
	*@return void
	*@throw Exception
	*/
	@Override
	public void execute() throws Exception {
		this.getParameters();
		try {
			for (int i = 0; i < columns.length; i++) {
				for (int j = i + 1; j < columns.length; j++)
					if (columns[i].equals(columns[j])) {
						loggy.error("Two columns are duplicated on createion table \""
								+ tableName + "\"");
						throw new SQLException(
								"columns duplicates aren't allowed");
					}
			}
			current.createTable(tableName, columns, types);
			updateCount = 0;
		} catch (SQLException e) {
			throw new SQLException("Invalid SQL statement");
		}
                
                for(int i = 0;i<columns.length;i++)
                {
                    if(tableName.equalsIgnoreCase(columns[i]))
                    {
                        throw new SQLException("it'sn't allowed to have a coluumn of the same name as the table");
                    }
                }
	}

}
