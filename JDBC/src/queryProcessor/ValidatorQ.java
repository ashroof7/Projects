package queryProcessor;

import static java.util.regex.Pattern.compile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorQ {

//	static String condition = "(\\s+(\\(?)\\s*([a-z]\\w+)+\\s*([=><]{1,2}|\\s+(between|like|in)\\s+)\\s*('?[a-z]\\w+'?|\\d)\\s*(\\)?)\\s*|"+
//		 	 "\\s+((\\()\\s*([a-z]\\w+)+\\s*([=><]{1,2}|\\s+(between|like|in)\\s+)\\s*('?[a-z]\\w+'?|\\d)\\s*(\\))\\s*))+";
//		 	static String where = "where(("+condition+"(and|or))|("+condition+")$)+";
    static String condition = "(\\s+(not\\s*)?(\\(?)\\s*([a-z]\\w+)+\\s*([=><]{1,2}|\\s+(between|like|in)\\s+)\\s*('?[a-z]\\w+'?|\\d)\\s*(\\)?)\\s*|"
            + "\\s+(not\\s*)?((\\()\\s*([a-z]\\w+)+\\s*([=><]{1,2}|\\s+(between|like|in)\\s+)\\s*('?[a-z]\\w+'?|\\d)\\s*(\\))\\s*))+";
    static String where = "where((" + condition + "(and|or))|(" + condition + ";?)$)+";

    public static boolean validateCreateSyntax(String query) {
        String pat3, pat4, pat5, full;

        pat3 = "\\bvarchar\\b(\\()(\\d+)(\\))|\\binteger\\b|\\bdouble\\b|\\bprimary_key\\b";
        pat4 = "(\\s*)(\\w+)(\\s)(" + pat3 + ")(\\s*)";
        pat5 = pat4 + "((," + pat4 + ")*)";
        full = "\\bcreate\\b(\\s)\\btable\\b(\\s+)(\\w+)(\\s*)" + "(\\())" + pat5 + "(\\))\\s*";

        query = query.toLowerCase();
        return query.matches(full);
    }

    public static boolean validateConditionSyntax(String query) {
        String prop, single, full;
        prop = "((\\s*)((\\w+)(\\s*)([=><]{1,2}|\\blike\\b|\\bbetween\\b|\\bin\\b)(\\s*)('((\\w|\\s)+)'|(\\d+)|\\bnull\\b))(\\s*))";
        single = "((\\s*)(\\bnot\\b*)(\\s*)((((\\()|(\\s))+)(\\s*)((((\\bnot\\b)(\\s+))*)" + prop + ")(\\)*))(\\s*))";
        full = single + "((and|or)(\\s*)(\\(*)" + single + "(\\)*)*)";
        query = query.toLowerCase();
        return query.matches(single);
    }

    public static boolean validateUpdateSyntax(String query) {
        String regexUpdate, regexSet, value, assignment, multipAssign, full;

        regexUpdate = "\\bupdate\\b(\\s+)(\\w+)(\\s)";
        regexSet = "(\\s*)\\bset\\b(\\s)";
        value = "('((\\w|\\s)+)'|(\\d+)|\\bnull\\b)";
        assignment = "(\\s*)(\\w+)=" + value + "(\\s*)";
        multipAssign = assignment + "((," + assignment + ")*)";

        full = regexUpdate + regexSet + multipAssign;

        query = query.toLowerCase();

        return query.matches(full);
    }

    public static boolean validateInsertSyntax(String query) {
        String regexIns, column, columns, defCol, regexValues, value, values, defVal, full;

        regexIns = "\\binsert\\b(\\s)\\binto\\b(\\s+)(\\w+)(\\s+)";
        column = "((\\s*)(\\w+)(\\s*))";
        columns = column + "((," + column + ")*)";
        defCol = "((" + "(\\()" + columns + "(\\))" + ")*)(\\s*)";
        regexValues = "\\bvalues\\b(\\s+)";
        value = "((\\s*)('((\\w|\\s)+)'|(\\d+)|\\bnull\\b)(\\s*))";
        values = value + "((," + value + ")*)";
        defVal = "(\\()" + values + "(\\))";
        full = regexIns + defCol + regexValues + defVal + "(\\s*)";

        query = query.toLowerCase();
        return query.matches(full);
    }

    public static boolean validateSelectSyntax(String query) {
        String selection =
                //	 			"\\s+([a-z]\\w+)(\\s*,\\s*([a-z]\\w+))*\\s+";
                "\\s+((([a-z]\\w+)\\s*,?\\s*)((,(\\s*)([a-z]\\w+)(\\s*))*)|\\*)";
        String regex = "\\s*select" + selection + "\\s+from\\s+([a-z]\\w+)\\s*(\\s+" + where + ")?";
        return query.matches(regex);
    }

    public static boolean validateDropSyntax(String query) {
        String regex = "drop\\s+[a-z]\\w+\\s*:?";
        return query.toLowerCase().matches(regex);
    }

    public static boolean validateDeleteSyntax(String query) {
        String regex = "delete\\s+\\*?\\s+from\\s+[a-z]\\w+\\s*(" + where + ")?;?";
// * with where
        return query.toLowerCase().matches(regex);
    }

    public static void main(String[] args) {
//	 String input = "select ahmed  , dsjfds  ,  dfhds  from table where (tre  > das) and (leo > 4 );";
        String input = "select col1, col2,col3 from tabletest where teteo   > fsd and fdh  < fdsfuh";
        System.out.println(validateSelectSyntax(input));
    }
}