package Validator;

import java.util.Arrays;
import DBSystem.Database;
import DBSystem.Log;

/**
*represents update command
*extends Operation
*
*/
public class Updating extends Operation {

/**
*extracts the update values needed
*@param void
*@return void
*/
	private void updateExtract() {
		setTable("update");
		if (query.contains("where")) {
			whereExtract();
			query = query.replace("where", "#");
			query = query.substring(0, query.indexOf("#"));
		}
		setValues(query.indexOf("set") + 3, query.length());

	}
	//constructor
	public Updating(String query, Database db) {
		this.query = query.toLowerCase().trim();
		current = db;
		loggy = Log.getLogger();
	}

	/**
	*completes parameters extraction
	* @param void
	* @return void
	*/
	
	@Override
	protected void getParameters() {
		super.getParameters();
		updateExtract();
		String[] valus = new String[values.length / 3];
		columns = new String[values.length / 3];
		for (int i = 0; i < values.length; i += 3) {
			columns[i / 3] = values[i].trim();
			valus[i / 3] = values[i + 2].trim();
		}
		values = valus;


	}

	/**
	*executes the Update commonad calling th DBSystem
	*package
	* @param void
	* @return void
	*/
	
	@Override
	public void execute() throws Exception {
		this.getParameters();
		current.openTable(tableName);
		String[] allCol = current.getColumnNames();
		int[] indeces = new int[columns.length];

		boolean found = false;

		for (int i = 0; i < columns.length; i++) {
			found = false;
			for (int j = 0; j < allCol.length; j++) {
				if (columns[i].equals(allCol[j])) {
					found = true;
					indeces[i] = j;
					break;
				}
			}
			if (!found) {
				loggy.error("Some columns entered doesn't exist in table \""
						+ tableName + "\"");
				throw new IllegalStateException("some columns aren't found");
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
					loggy.error("The value entered is string and the column isn't of type string in table \""
							+ tableName + "\"");
					throw new IllegalStateException(
							"the value assigned is string and the column isn't of string type");
				}

				else {
					String type = colTypes[indeces[k]];
					int allowedcharsno = Integer.parseInt(type.substring(
							type.indexOf('(') + 1, type.indexOf(')')));
					if (value.length() - 2 > allowedcharsno) {
						loggy.error("The string entered is larger than the column can hold in table \""
								+ tableName + "\"");
						throw new IllegalStateException(
								"the char of the value you are using is larger than that of the column");
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
					loggy.error("The value entered is double and the column isn't of double type in table \""
							+ tableName + "\"");
					throw new IllegalStateException(
							"the value assigned is double and the column isn't of double type");
				}
                                Double.parseDouble(value);
			} else if (value.matches("true|false")) {
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

		if (condition != null) {
			found = false;
			int j;
			for (j = 0; j < allCol.length; j++) {
				if (condition[0].equals(allCol[j])) {
					found = true;
					break;
				}
			}

			if (!found) {
				loggy.error("Some columns entered doesn't exist in table \""
						+ tableName + "\"");
				throw new IllegalStateException("some columns aren't found");
			}
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
						loggy.error("The string entered is larger than the column can hold in table \""
								+ tableName + "\"");
						throw new IllegalStateException(
								"the char of the value you are using is larger than that of the column");
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
			}

			else if (value.matches("(((\\-\\d+)|(\\d+))((\\.\\d+){0,1}))")) {
				if (!colTypes[j].matches("double")) {
					loggy.error("The value entered is double and the column isn't of type double in table \""
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
			} else {
				loggy.error("Invalid value entered in table \"" + tableName
						+ "\"");
				throw new IllegalStateException("invalid value");
			}
		}
		updateCount = current.setTable(tableName, indeces, values, condition);
	}
}
