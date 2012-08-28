package parse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import function.CosFun;
import function.ExpFun;
import function.Function;
import function.Polynomial;
import function.Product;
import function.SinFun;
import function.XFun;

public class Parser {

	Pattern pat;
	Matcher mat;
	LinkedList<String> prods = new LinkedList<String>();
	ArrayList<LinkedList<String>> poly = new ArrayList<LinkedList<String>>();

	public final static String cof = "[\\+\\-]?\\d+(\\.\\d+)?";
	public final static String fn = "[\\w\\-\\+\\{\\}\\^]*";

	public final static String x1 = "[\\-\\+]?(" + cof+ ")?x(\\^\\{[\\w\\{\\}\\+\\-]\\})?";
	public final static String x = "\\(" + x1 + "\\)||" + x1;

	public final static String sin = "[\\-\\+]?sin\\((" + x + ")\\)";
	public final static String cos = "[\\-\\+]?cos\\((" + x + ")\\)";

	public final static String e = "[\\-\\+]?e(\\^\\{(" + x + ")\\})?";

	public final static String d = "[\\+\\-]\\d";

	// public final static String product =
	// "\\-?(\\*?(\\(?)("+sin+"||"+cos+"||"+e+"||"+x+"||"+d+")(\\)?))";
	// public final static String all = ""+product+"+";

	private Polynomial function;

	public Parser(String s) throws Exception {
		s = s.replaceAll("\\s", "");
		level1(s);
		System.out.println("level1 compeleted\n"+prods);
		System.out.println();
		
		
		level2();
		System.out.println("level2 compeleted\n");
		for (LinkedList<String> p : poly) {
			System.out.println(p);
		}
		System.out.println();
		function = new Polynomial();
		makeFn();
	}

	public void level1(String in) {
		int level = 0;
		char[] s = in.toCharArray();
		StringBuilder c = new StringBuilder();

		for (int i = 0; i < s.length; i++) {
			if (s[i] == '(' || s[i] =='{') {
				level++;
			} else if (s[i] == ')' || s[i] =='}') {
				level--;
			} else if (level == 0 && (s[i] == '+' || s[i] == '-')) {
				if (i != 0)
					prods.add(c.toString());
				c = new StringBuilder();
			}
			c.append(s[i]);
		}
		prods.add(c.toString());
	}

	public void level2() throws Exception {
		char c[];
		StringBuilder b = new StringBuilder();

		LinkedList<String> temp;
		int level;

		for (String s : prods) {
			c = s.toCharArray();
			b = new StringBuilder();
			temp = new LinkedList<String>();
			level = 0;
			boolean take = false;
			int j = 0;

			if (c[0] == '(') {
				j = 1;
				level++;
			}

			for (int i = j; i < c.length; i++) {
				if (c[i] == '(') {
					level++;
					if (level > 1)
						b.append(c[i]);
					else if (c[i - 1] == 'n' || c[i - 1] == 's') {
						b.append(c[i]);
						take = true;
					}
				} else if (c[i] == ')') {
					level--;
					if (take) {
						b.append(c[i]);
						take = false;
					}
					if (level == 0) {
						temp.add(b.toString());
						b = new StringBuilder();
					} else
						b.append(c[i]);
				} else
					b.append(c[i]);
			}

			if (b.length() > 0)
				temp.add(b.toString());
			poly.add(temp);
		}
	}

	public void makeFn() throws Exception {
		LinkedList<Function> tempProd = new LinkedList<Function>();
		int match;
		Function fn = null;
		String arg;
		Parser pars;
		String cof;
		double coef = 1;
		for (int i = 0; i < poly.size(); i++) {
			for (String p : poly.get(i)) {
				match = match(p);
				// System.out.println(match);
				switch (match) {
				case 1:
					
					cof = p.substring(0, p.indexOf('x'));
					if (cof.equals("-"))
						cof+=1;
					if (!cof.isEmpty())
						coef = Double.parseDouble(cof);
					if (p.contains("^")) {
						arg = p.substring(p.indexOf("{") + 1, p.indexOf("}"));
						double pow = Double.parseDouble(arg);
						fn = new XFun(coef, pow);
					} else {
						fn = new XFun(coef, 1);

					}
					break;

				case 2:
					arg = p.substring(p.indexOf("(") + 1, p.indexOf(")"));
					pars = new Parser(arg);
					fn = new SinFun(pars.getPoly());
					break;

				case 3:
					arg = p.substring(p.indexOf("(") + 1, p.indexOf(")"));
					pars = new Parser(arg);
					fn = new CosFun(pars.getPoly());
					break;

				case 4:
					arg = p.substring(p.indexOf("{")+1, p.indexOf("}"));
					pars = new Parser(arg);
					fn = new ExpFun(pars.getPoly());
					break;

				default:
					System.err.println("can't parse input");
					break;

				}
				tempProd.add(fn);
				// System.out.println(tempProd);
			}

			Product pro = new Product(tempProd);
			// System.out.println(pro);
			function.add(pro);
			tempProd = new LinkedList<Function>();
		}
	}

	public int match(String s) {
		if (s.matches(x))
			return 1;
		else if (s.matches(sin))
			return 2;
		else if (s.matches(cos))
			return 3;
		else if (s.matches(e))
			return 4;
		else
			return -1;
	}

	public Polynomial getPoly() {
		return function;
	}

	public static void main(String[] args) throws Exception {
//		 String test = "(3x^{5})(sin(x))-(sin(x)) -cos(x) + (3x^{1})";
//		String test = "e^{-x}";
//		Parser pars = new Parser(test);

		// System.out.println(pars.match("sin(x)"));
		// System.out.println(pars.match("-sin(x)"));
		// System.out.println(pars.match("-cos(x)"));
		// System.out.println(pars.match("+3x^{5}"));
	}
}
