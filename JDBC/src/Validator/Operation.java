package Validator;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import DBSystem.*;

/**
*this class is the super class fro all operations
*from which 
*each commond class extends
*
*/

abstract public class Operation {

	// hold the input commond
	protected String query;
	// holds the table name to execute the commond on
	protected String tableName;
	// holds the currents databas variable
	protected Database current;
	// gets the ResultTable for the operation
	protected ResultTable tab;
	// holds the columns used in the commonds
	protected String[] columns;
	// carrys the condition according to which the commond will be executed
	protected String[] condition;
	// holds values that will be Updated or inserted
	protected String[] values;
	protected int updateCount;
	protected boolean all;
	protected Logger loggy;

	/**
	* makes the primary operations to extract
	* values from the commond
	* @param void
	* @return void
	*/
	protected void getParameters() {
		query = query.toLowerCase();
		replace();
		Pattern pat = Pattern.compile("^\\s*(\\w+)");
		Matcher match = pat.matcher(query);
		match.find();
		all = false;
	}

	/**
	*execution will be implemented in each child class
	*/
	public abstract void execute() throws Exception;
	
	/**
	*extracts the condations used in the where keyword
	*@param void
	*@return void
	*/
	
	protected void whereExtract() {

		query = query.replace(">  =", " >= ");
		query = query.replace("<  =", " <= ");
		query = query.replace("<  >", " <> ");
		int whereIndex = query.indexOf("where");
		if (whereIndex == -1)
			return;
		condition = query.substring(whereIndex + 5).trim().split("\\s+");
	}

	public ResultTable getResultTable() {
		return tab;
	}

	public int getUpdateCount() {
		return updateCount;
	}
	
	/**
	*extracts the columns to be used in the operation
	*@param int start, int end
	*@return void
	*/
	
	
	protected void setColumns(int start, int end) {
		columns = query.substring(start, end).replaceAll("\\(|\\)", "").trim()
				.split("\\s+|,\\s?");
		LinkedList<String> temp = new LinkedList<String>();
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].equals(""))
				continue;
			temp.add(columns[i]);
		}
		columns = new String[temp.size()];
		int i = 0;
		for (String string : temp) {
			columns[i++] = string;
		}
	}

	/**
	*extracts the values in the commond
	*@param int start , int end
	*@return void
	*
	*/
	protected void setValues(int start, int end) {
		values = query.substring(start, end).replaceAll("\\(|\\)", "").trim()
				.split("\\s+|,\\s?");
		LinkedList<String> temp = new LinkedList<String>();
		for (int i = 0; i < values.length; i++) {
			if (values[i].equals(""))
				continue;
			temp.add(values[i]);
		}
		values = new String[temp.size()];
		int i = 0;
		for (String string : temp) {
			values[i++] = string;
		}

	}

	
	/**
	*extracts the table name
	*@param startwith
	*@return 
	*/
	protected void setTable(String startWith) {
		Matcher match = Pattern.compile(startWith + "\\s+(\\w+)")
				.matcher(query);
		match.find();
		tableName = match.group(1);

	}

	/**
	*performes some processes for extraction
	*@param void
	*@return void
	*/
	protected void replace() {
		String temp = "";
		for (int i = 0; i < query.length(); i++) {
			switch (query.charAt(i)) {
			case '>':
				temp += " > ";
				break;
			case '<':
				temp += " < ";
				break;
			case '=':
				temp += " = ";
				break;
			case '(':
				temp += " ( ";
				break;
			case ')':
				temp += " ) ";
				break;
			case '\'':
				temp += '\'';
				while (query.charAt(++i) != '\'') {
					temp += query.charAt(i) == ' ' ? '%' : query.charAt(i);
				}
				temp += '\'';
				;
				break;

			default:
				temp += query.charAt(i);
				break;
			}
		}
		query = temp;
	}
}
