package methods.interpolation;

import java.util.Arrays;
import java.util.Vector;

import function.Function;
import function.NumberFun;
import function.Polynomial;
import function.Product;
import function.SinFun;
import function.XFun;

public class Divide {

	private double[] X;
	private double[][] table;
	private double[] Y;
	private Function F;
	private Vector<Double> C;
	private Polynomial[] terms;

	public Divide(Function f, double[] x) throws CloneNotSupportedException {
		X = x;
		F = (Function) f.clone();
		table = new double[x.length][x.length];
		Y = new double[X.length];
		for (int i = 0; i < x.length; i++) {
			Y[i] = f.getValue(x[i]);
			table[i][0] = Y[i];
		}
		C = new Vector<Double>();
	}

	public Divide(double[] x, double[] y) {
		X = x;
		Y = y;
		table = new double[x.length][x.length];
		for (int i = 0; i < y.length; i++) {
			table[i][0] = y[i];
		}
		C = new Vector<Double>();
	}

	private void buildTerms() {
		terms = new Polynomial[X.length];
		for (int i = 0; i < X.length; i++) {
			terms[i] = new Polynomial();
			terms[i].add(new XFun(1, 1));
			terms[i].add(new NumberFun(X[i]));

		}
	}

	public Polynomial solve() {
		for (int i = 1; i < X.length; i++) {
			for (int j = i; j < X.length; j++) {
				double n1 = table[j][i - 1];
				double n2 = table[j - 1][i - 1];
				double d1 = X[j];
				double d2 = X[j - (1 * i)];
				table[j][i] = (n1 - n2) / (d1 - d2);
			}
		}
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				if (table[j][i] != 0) {
					C.add(table[j][i]);
					break;
				}
			}
		}
		buildTerms();
		return buildPol();
	}

	public Polynomial errorBound() throws CloneNotSupportedException {
		if (F == null)
			return null;
		Function temp = (Function) F.clone();

		for (int i = 0; i < X.length + 1; i++) {
			temp = temp.getDerivative();
		}
		Product p = new Product();
		p.multiply(temp);
		for (int i = 0; i < terms.length; i++) {
			p.multiply(terms[i]);
		}
		Arrays.sort(X);

		double factorial = fact(X.length + 1);
		p.divide(new NumberFun(factorial));
		Polynomial last = new Polynomial();
		last.add(p);

		return last;
	}

	private double fact(int i) {
		double t = 1;
		for (int j = i; j > 0; j--) {
			t *= j;
		}
		return t;
	}

	private Polynomial buildPol() {

		Polynomial p = new Polynomial();
		for (int i = 0; i < C.size(); i++) {
			Product p1 = new Product();
			// Polynomial res =new Polynomial();
			NumberFun t = new NumberFun(C.get(i));
			p1.multiply(t);
			for (int j = terms.length - i; j < terms.length; j++) {
				p1.multiply(terms[terms.length - j]);
			}
			p.add(p1);
		}
		return p;
	}

	public static void main(String[] args) throws CloneNotSupportedException {
		double[] x = { 0, 1, 2, 3, 4 };
		XFun term2 = new XFun(1, 1);
		Product p1 = new Product();
		p1.multiply(term2);
		Polynomial poly1 = new Polynomial();
		poly1.add(p1);
		// double[] y = { 1, 0.5403, -0.41614, -0.9899, -.6356 };
		Function f = new SinFun(poly1);
		Divide d = new Divide(f, x);
		System.out.println(d.solve());
		System.out.println(d.errorBound());
	}
}
