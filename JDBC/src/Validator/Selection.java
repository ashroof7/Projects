package Validator;

import DBSystem.Database;
import DBSystem.Log;

/**
*represents the Select command
*extends Operation Class
*
*/

public class Selection extends Operation {

	// constructor
	public Selection(String sql, Database c) {
		query = sql.toLowerCase().trim();
		current = c;
		loggy = Log.getLogger();
	}

	/**
	*completes extraction the values needed in the executions
	*@param void
	*@return void
	*
	*/
	@Override
	protected void getParameters() {
		super.getParameters();
		selectExtract();
	}
	
	/**
	*extracts the values needed in the executions
	*@param void
	*@return void
	*
	*/
	
	private void selectExtract() {
		if (query.contains("*")) {
			all = true;
		} else {
			setColumns(query.indexOf("select") + 6, query.indexOf("from"));
		}
		setTable("from");
		whereExtract();
	}

	/**
	*
	*executes the operation
	*using the DBSystem
	*@param void 
	*@return void
	*@throw Exception
	*/
	
	@Override
	public void execute() throws Exception {
		this.getParameters();

		// execution is for one condition only
		current.openTable(tableName);
		if (columns == null)
			columns = current.getColumnNames();

		String[] allCol = current.getColumnNames();
		int[] indeces = new int[columns.length];

		boolean found = false;
		for (int i = 0; i < columns.length; i++) {
			for (int j = 0; j < allCol.length; j++) {
				if (columns[i].equals(allCol[j])) {
					found = true;
					indeces[i] = j;
					break;
				}
			}
			if (!found) {
				loggy.error("Some columns were not found in table \""
						+ tableName + "\"");
				throw new IllegalStateException("some columns aren't found");
			}
		}

		found = false;

		if (condition != null) {
			int j;
			for (j = 0; j < allCol.length; j++) {
				if (condition[0].equals(allCol[j])) {
					found = true;
					break;
				}
			}

			if (!found) {
				loggy.error("Some columns were not found in table \""
						+ tableName + "\"");
				throw new IllegalStateException("some columns aren't found");
			}

			String[] colTypes = current.getColumnTypes();
			String value = condition[2];
			value = value.replaceAll("%", " ");
			if (value.matches("'((\\w|\\s|\\.)*)'")) {
				if (!colTypes[j].matches("\\bvarchar\\b(\\()(\\d+)(\\))")) {
					loggy.error("The value entered is string and the column isn't of type string in table \""
							+ tableName + "\"");
					throw new IllegalStateException(
							"the value assigned is string and the column isn't of string type");
				} else {
					String type = colTypes[j];
					int allowedcharsno = Integer.parseInt(type.substring(
							type.indexOf('(') + 1, type.indexOf(')')));

					if (value.length() - 2 > allowedcharsno) {
						loggy.error("Looking for a string that larger than the column can hold in table \""
								+ tableName + "\"");
						throw new IllegalStateException(
								"the char of the value you are using as a comparison in where is larger than that of the column");
					}
				}
			} else if (value.matches("(-\\d+)|(\\d+)")) {
				if (!colTypes[j].matches("integer")
						&& !colTypes[j].matches("double")) {
					loggy.error("The value entered is integer and the column isn't of type integer in table \""
							+ tableName + "\"");
					throw new IllegalStateException(
							"the value assigned is integer and the column isn't of integer type");
				}
                                Integer.parseInt(value);
			} else if (value.matches("(((\\-\\d+)|(\\d+))((\\.\\d+){0,1}))")) {
				if (!colTypes[j].matches("double")) {
					loggy.error("The value entered is double and the column isn't of double type in table \""
							+ tableName + "\"");
					throw new IllegalStateException(
							"the value assigned is double and the column isn't of double type");

				}
                                Double.parseDouble(value);
			} else if (value.matches("false|true")) {
				if (!colTypes[j].matches("boolean")) {
					loggy.error("The value entered is boolean and the column isn't of type boolean in table \""
							+ tableName + "\"");
					throw new IllegalStateException(
							"the value assigned is boolean and the column isn't of boolean type");
				}
			} else if (value.matches("null")) {
			}

			else {
				loggy.error("Invalid value entered in table \"" + tableName
						+ "\"");
				throw new IllegalStateException("invalid value");
			}
		}
                tab = current.selectFromTable(tableName, indeces, condition);
                
	}

}
