package Validator;

import DBSystem.Database;

/**
*represents the Drop table commond
*extends the operation class
*
*/
public class Dropping extends Operation {

// constructor
	public Dropping(String query, Database db) {
		this.query = query.toLowerCase();
		current = db;
	}
/**
*extracts the table name to be dropped
*@param void
*@return void
*/
	private void dropExtract() {
		setTable("drop");
	}
/**
*completes the extraction process done 
*in the dropExdtract method
*@param void
*@return void
*/	
	@Override
	protected void getParameters() {
		// TODO Auto-generated method stub
		super.getParameters();
		dropExtract();
	}
/**
*executes the droping method
*calling functions from the DBSystem
*
*@param void
*@return void
*
*/
	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		getParameters();
		updateCount = current.dropTable(tableName);
	}
}
