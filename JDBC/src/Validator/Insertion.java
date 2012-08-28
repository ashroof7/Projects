package Validator;

import DBSystem.Database;
import DBSystem.Log;

/**
*represents the Insert into
*commands
*extends Operation
*
*/

public class Insertion extends Operation {

	public Insertion(String query, Database db) {
		current = db;
		this.query = query.toLowerCase().trim();
		loggy = Log.getLogger();
	}
/**
*extracts insert parameters 
*to be used in execution
*@param void
*@return void
*/
	
	private void insertExtract() {
		setTable("into");
		setValues(query.indexOf("values") + 6, query.length());
		setColumns(query.indexOf(tableName) + tableName.length(),
				query.indexOf("values"));
	}
/**
*completes the extraction process
*@param void
*@return void
*/
	@Override
	protected void getParameters() {
		// TODO Auto-generated method stub
		super.getParameters();
		insertExtract();
	}
/**
*executes the command
*using the DPSystem package
*
*@param void
*@return void
*/
	
	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		this.getParameters();

		current.openTable(tableName);

		if (columns.length == 0|| tableName.length()==1)
			columns = current.getColumnNames();

		if (columns.length != values.length) {
			loggy.error("Number of values assigned is greater than the number of columns or vice versa in table \""
					+ tableName + "\"");
			throw new IllegalStateException(
					"number of values assigned is greater than the number of columns or vice versa");
		}

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
				loggy.error("Column is not existed in table \"" + tableName
						+ "\"");
				throw new IllegalStateException("column not found");
			}
		}

		String[] colTypes = current.getColumnTypes();

		for (int l = 0; l < values.length; l++)
			values[l] = values[l].replaceAll("%", " ");

		for (int k = 0; k < values.length; k++) {
			String value = values[k];
			if (value.matches("'((\\w|\\s|\\.)*)'")) {
				if (!colTypes[indeces[k]]
						.matches("\\bvarchar\\b(\\()(\\d+)(\\))")) {
					loggy.error("Entered a string value and the column isn't of type string in table \""
							+ tableName + "\"");
					throw new IllegalStateException(
							"the value assigned is string and the column isn't of string type");
				} else {
					String type = colTypes[indeces[k]];
					int allowedcharsno = Integer.parseInt(type.substring(
							type.indexOf('(') + 1, type.indexOf(')')));

					if (value.length() - 2 > allowedcharsno) {
						loggy.error("The entered string value is larger than the column can hold in table \""
								+ tableName + "\"");
						throw new IllegalStateException(
								"The char of the value you are using is larger than that of the column");
					}
				}
			} else if (value.matches("(-\\d+)|(\\d+)")) {
				if (!colTypes[indeces[k]].matches("integer")
						&& !colTypes[indeces[k]].matches("double")) {
					loggy.error("The value entered is integer and the column isn't of type integer in table \""
							+ tableName + "\"");
					throw new IllegalStateException(
							"the value assigned is integer and the column isn't of integer type");
				}
                                Integer.parseInt(value);
			} else if (value.matches("(((\\-\\d+)|(\\d+))((\\.\\d+){0,1}))")) {
				if (!colTypes[indeces[k]].matches("double")) {
					loggy.error("The value entered is double and the column isn't of type double in table \""
							+ tableName + "\"");
					throw new IllegalStateException(
							"the value assigned is double and the column isn't of double type");
				}
			Double.parseDouble(value);
                        }

			else if (value.matches("true|false")) {
				if (!colTypes[indeces[k]].matches("boolean")) {
					loggy.error("The value entered is boolean and the column isn't of type boolean in table \""
							+ tableName + "\"");
					throw new IllegalStateException(
							"the value assigned is boolean and the column isn't of boolean type");
				}
			} else if (value.matches("null")) {
			} else {
				loggy.error("Invalid value entered in table \"" + tableName
						+ "\"");
				throw new IllegalStateException("invalid value");
			}

		}

		current.insertIntoTable(tableName, indeces, values);
		updateCount = 1;
	}
}
