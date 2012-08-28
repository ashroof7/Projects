package Validator;

import DBSystem.Database;
import DBSystem.Log;

/**
*this class represents 
* delete from command
*and it extends the Operation
*class
*/
public class Deletion extends Operation {

	//constructor
	public Deletion(String query, Database db) {
	
		this.query = query.toLowerCase().trim();
		current = db;
		loggy = Log.getLogger();
	}
	/**
	*extracts the name of the table
	*and the where condition for the table
	*to delete from
	*@param void
	*@return void
	*/
	private void deleteExtract() {
		if (query.contains("where")) {
			setTable("from");
			whereExtract();
		} else {
			setTable("from");
			all = true;
		}
	}
	
	/**
	*this methods overrides the get parameters method in the Operation class 
	*it completes the delete Extract extraction
	*@param void
	*@return void
	*/
	@Override
	protected void getParameters() {
		// TODO Auto-generated method stub
		super.getParameters();
		deleteExtract();
	}

	/**
	*executes the delete command 
	*using the underlying functions
	*from the DBSystem package
	*
	*@param void
	*@return void
	*@throw Exception
	*/
	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		this.getParameters();
		boolean found = false;
		if (condition != null) {

			int j;
			current.openTable(tableName);
			String[] allCol = current.getColumnNames();
			for (j = 0; j < allCol.length; j++) {
				if (condition[0].equals(allCol[j])) {
					found = true;
					break;
				}
			}

			if (!found) {
				loggy.error("Deleting using a non existing column from table \""
						+ tableName + "\"");
				throw new IllegalStateException("some columns aren't found");
			}
			String[] colTypes = current.getColumnTypes();
			String value = condition[2];

			value = value.replaceAll("%", " ");

			if (value.matches("'((\\w|\\s|\\.)*)'")) {
				if (!colTypes[j].matches("\\bvarchar\\b(\\()(\\d+)(\\))")) {
					loggy.error("Entered a string value and the column hold another type in table \""
							+ tableName + "\"");
					throw new IllegalStateException(
							"the value assigned is string and the column isn't of string type");
				} else {
					String type = colTypes[j];
					int allowedcharsno = Integer.parseInt(type.substring(
							type.indexOf('(') + 1, type.indexOf(')')));

					if (value.length() - 2 > allowedcharsno) {
						loggy.error("Entered string value that larger than the column can hold in table \""
								+ tableName + "\"");
						throw new IllegalStateException(
								"The chars of the value you are using is larger than that of the column");
					}
				}

			} else if (value.matches("(-\\d+)|(\\d+)")) {
				if (!colTypes[j].matches("integer")
						&& !colTypes[j].matches("double")) {
					loggy.error("Entered value is integer and the column isn't of type integer in table \""
							+ tableName + "\"");
					throw new IllegalStateException(
							"the value assigned is integer and the column isn't of integer type");
				}
                                Integer.parseInt(value);
			} else if (value.matches("(((\\-\\d+)|(\\d+))((\\.\\d+){0,1}))")) {
				if (!colTypes[j].matches("double")) {
					loggy.error("Entered value is double and the column isn't of type double in table \""
							+ tableName + "\"");
					throw new IllegalStateException(
							"the value assigned is double and the column isn't of double type");
				}
                                Double.parseDouble(value);
			} else if (value.matches("false|true")) {
				if (!colTypes[j].matches("boolean")) {
					loggy.error("Entered value is boolean and the column isn't of type boolean in table \""
							+ tableName + "\"");
					throw new IllegalStateException(
							"the value assigned is boolean and the column isn't of boolean type");
				}
			}

			else if (value.matches("null")) {
			}

			else {
				loggy.error("Entered invalid value in table \"" + tableName
						+ "\"");
				throw new IllegalStateException("invalid value");
			}

		}
		updateCount = current.removeFromTable(tableName, condition);
	}
}
