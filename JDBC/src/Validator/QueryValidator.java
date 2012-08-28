package Validator;

import java.sql.SQLException;
import java.util.Scanner;

import org.apache.log4j.Logger;
//import java.util.Scanner;

import DBSystem.*;
import java.security.NoSuchAlgorithmException;

/**
* validates the query syntax then sends it to operation
* using a factory method
* to execute the operation
*/

public class QueryValidator {

    private Database db;
    private Operation op;
    private int queryType = -1;
    private String condition = "(\\s+(not\\s*)?(\\(?)\\s*([a-z]\\w*)+\\s*([><=]|([><]=)|(<>)|\\s+(between|like|in)\\s+)\\s*('(((\\ )|\\w|\\.)*)'|true|false|null|((((\\-)(\\d+))|(\\d+))((\\.\\d+){0,1})))\\s*(\\)?)\\s*|"
            + "\\s+(not\\s*)?((\\()\\s*([a-z]\\w*)+\\s*([><=]|([><]=)|(<>)|\\s+(between|like|in)\\s+)\\s*('(((\\ )|\\w|\\.)*)'|false|true|null|((((\\-)\\d+)|(\\d+))((\\.\\d+){0,1})))\\s*(\\))\\s*))+";
    private String where = "where((" + condition + "(and|or))|("
            + condition + ")$)+";
    private Logger loggy;

	// constrauctor
    public QueryValidator(String url, String schema, String username,
            String password) throws NoSuchAlgorithmException, Exception {
        db = MyDatabase.connect(username, password);
        loggy = Log.getLogger();
    }

    public Operation getOperation() {
        return op;
    }
	/**
	*checks if the query is a drop one 
	*@param String query
	*@return boolean
	*/
    private boolean validateDropSyntax(String query) {
        String regex = "drop\\s+[a-z]\\w*\\s*:?";
        return query.toLowerCase().matches(regex);
    }
/**
	*checks if the query is a select one 
	*@param String query
	*@return boolean
	*/
    private boolean validateSelectSyntax(String query) {

        // "\\s+([a-z]\\w+)(\\s*,\\s*([a-z]\\w+))*\\s+";
        String selection = "\\s+(((([a-z]\\w*)\\s*)((,(\\s*)([a-z]\\w*)(\\s*))*))|\\*)";
        String regex = "select" + selection + "\\sfrom\\s+([a-z]\\w*)\\s*(\\s+"
                + where + ")?";
        query = query.toLowerCase();
        return query.matches(regex);
    }

	/**
	*checks if the query is an insert one 
	*@param String query
	*@return boolean
	*/
    private boolean validateInsertSyntax(String query) {
        String regexIns, column, columns, defCol, regexValues, value, values, defVal, full;

        regexIns = "\\binsert\\b(\\s)\\binto\\b(\\s+)([a-z]\\w*)(\\s+)";
        column = "((\\s*)([a-z]\\w*)(\\s*))";
        columns = column + "((," + column + ")*)";
        defCol = "((" + "(\\()" + columns + "(\\))" + ")*)(\\s*)";
        regexValues = "\\bvalues\\b(\\s+)";
        value = "((\\s*)('((\\w|(\\ )|\\.)*)'|((((\\-)(\\d+))|(\\d+))((\\.\\d+){0,1}))|false|true|null)(\\s*))";
        values = value + "((," + value + ")*)";
        defVal = "(\\()" + values + "(\\))";
        full = regexIns + defCol + regexValues + defVal + "(\\s*)";

        query = query.toLowerCase();

        return query.matches(full);
    }
	/**
	*checks if the query is a delete one 
	*@param String query
	*@return boolean
	*/
    private boolean validateDeleteSyntax(String query) {
        String regex = "delete(\\s+)(\\*\\s)?\\s*from\\s+[a-z]\\w+\\s*("
                + where + ")?";
        query = query.toLowerCase();
        return query.matches(regex);
    }
/**
	*checks if the query is a create one 
	*@param String query
	*@return boolean
	*/
    private boolean validateCreateSyntax(String query) {
        String pat3, pat4, pat5, full;

        pat3 = "\\bvarchar\\b(\\()(\\d+)(\\))|\\binteger\\b|\\bdouble\\b|\\bboolean\\b|\\bprimary_key\\b";
        pat4 = "(\\s*)([a-z]\\w*)(\\s)(" + pat3 + ")(\\s*)";
        pat5 = pat4 + "((," + pat4 + ")*)";
        full = "\\bcreate\\b(\\s)\\btable\\b(\\s+)([a-z]\\w*)(\\s+)((\\()"
                + pat5 + "(\\)))(\\s*)";

        query = query.toLowerCase();

        return query.matches(full);
    }

	/**
	*checks if the query is an update one 
	*@param String query
	*@return boolean
	*/
    private boolean validateUpdateSyntax(String query) {
        String regexUpdate, regexSet, value, assignment, multipAssign, full;

        regexUpdate = "(\\bupdate\\b)(\\s+)([a-z](\\w*))(\\s)";
        regexSet = "(\\s*)\\bset\\b(\\s)";
        value = "('((\\w|(\\ )|\\.)+)'|(((\\-\\d+)|(\\d+))((\\.\\d+){0,1}))|true|false|null)";
        assignment = "(\\s*)([a-z]\\w*)(\\s*)=(\\s*)" + value + "(\\s*)";
        multipAssign = assignment + "((," + assignment + ")*)";
        full = regexUpdate + regexSet + multipAssign + "((\\s)" + where
                + ")?";

        query = query.toLowerCase();

        return query.matches(full);
    }

	/**
	* a factroy method that select which operation type
	* will be executed
	* @param String query
	* @return void
	*/

	
    public void createQuery(String query) throws SQLException {
        query = query.trim();
        if (validateSelectSyntax(query)) // selection Pattern
        {
            op = new Selection(query, db);
            queryType = 0;
        } else if (validateCreateSyntax(query)) // create Pattern
        {
            op = new Creation(query, db);
            queryType = 1;
        } else if (validateInsertSyntax(query)) // insertion
        {
            op = new Insertion(query, db);
            queryType = 2;
        } else if (validateUpdateSyntax(query)) // update
        {
            op = new Updating(query, db);
            queryType = 3;
        } else if (validateDropSyntax(query)) // drop
        {
            op = new Dropping(query, db);
            queryType = 4;
        } else if (validateDeleteSyntax(query)) {
            op = new Deletion(query, db);
            queryType = 5;
        } else {
            queryType = -1;
            loggy.error("SQL syntax error");
            throw new SQLException("invalid command please check syntax");

        }
    }

    public int getQueryType() {
        return queryType;
    }
}
