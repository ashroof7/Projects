import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.LinkedList;

public class EliminationDouble {
	private double[][] equations; // the array of elimination
	private double[] B;
	private double[] X;
	private LinkedList<double[][]> history;
	private LinkedList<double[]> history1;
	private boolean Pivoting;
	private double time;

	public void write() {
		try {
			// Create file
			FileWriter fstream = new FileWriter("ElimDetailedSoln.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			for (int i = 0; i < history.size(); i++) {
				out.write("The system A= \n");
				for (int j = 0; j < history.get(i).length; j++) {
					for (int k = 0; k < history.get(i)[j].length; k++) {
						out.write(history.get(i)[j][k] + " ");
					}
					out.write("|" + history1.get(i)[j]);
					out.write("\n");
				}
				out.write("==========================================\n");
			}
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	// constructor
	public EliminationDouble(double[][] e, double[] b, boolean doPivot)
			throws Exception {
		equations = e;
		B = b;
		X = new double[equations[0].length];
		if (doPivot) {
			Pivoting = true;
		} else
			Pivoting = false;

		if (equations[0].length > equations.length)
			throw new Exception("ELD : cannot be solved");
		history = new LinkedList<double[][]>();
		history1 = new LinkedList<double[]>();
		double[][] t = clone2(equations);
		history.add(t);
		double[] t1 = clone1(B);
		history1.add(t1);

	}

	private double[] clone1(double[] b2) {
		double[] temp = new double[b2.length];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = b2[i];
		}
		return temp;
	}

	private double[][] clone2(double[][] equations2) {
		double[][] temp = new double[equations2.length][equations2[0].length];
		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[i].length; j++) {
				temp[i][j] = equations2[i][j];
			}
		}
		return temp;
	}

	public double[] getB() {
		return B;
	}

	public void setB(double[] b) {
		B = b;
	}

	// check the need for pivoting
	public boolean needPivoting() {
		int y = 0;
		int x = 0;
		while (y < equations[0].length && x < equations.length && x >= 0
				&& y >= 0) {
			if (equations[x][y] == 0) {
				return true;
				// break;
			}
			y++;
			x++;
		}
		return false;
	}

	// pivoting based on the partial pivoting strategy
	public void pivoting(int k) {
		int candi;// candidate for substitution
		double max = 0;// holder for max value
		for (int i = k; i < equations.length; i++) {
			candi = i;
			max = equations[i][i];
			for (int j = i + 1; j < equations.length; j++) {
				if (max < equations[j][i]) {
					max = equations[j][i];
					candi = j;
				}
			}
			if (candi != i) {
				swap(i, candi);
				double[][] t = clone2(equations);
				history.add(t);
				double[] t1 = clone1(B);
				history1.add(t1);
			}
		}

	}

	// swap 2 rows
	private void swap(int i, int candi) {
		for (int j = 0; j < equations[i].length; j++) {
			double temp = equations[i][j];
			equations[i][j] = equations[candi][j];
			equations[candi][j] = temp;
		}
		double temp = B[i];
		B[i] = B[candi];
		B[candi] = temp;
	}

	// forward elimination
	public void eliminate(boolean p) throws Exception {

		for (int i = 0; i < equations.length; i++) { // loop on diagonal
			if (p)
				pivoting(i);

			for (int k = i + 1; k < equations.length; k++) {// loop horizontal
															// to eliminate
				if (equations[i][i] == 0)
					throw new Exception("Numbers are joking here really :P ");
				double m = equations[k][i] / equations[i][i];// the factor

				for (int l = 0; l < equations[k].length; l++) {
					equations[k][l] = equations[k][l] - (equations[i][l] * m);
				}

				B[k] = B[k] - (B[i] * m);

			}
			double[][] t = clone2(equations);
			history.add(t);
			double[] t1 = clone1(B);
			history1.add(t1);
		}

	}

	public static double[] matMultiplication(double[][] a, double[] b) {
		if (a[0].length != b.length)
			throw new IllegalArgumentException("Matrix Dimension Error");
		double c[] = new double[b.length];

		for (int i = 0; i < c.length; i++) {
			for (int k = 0; k < c.length; k++) {
				c[i] += a[i][k] * b[k];
			}

		}
		return c;
	}

	public void solution() throws Exception {
		double temp = 0;

		for (int i = equations.length - 1; i >= 0; i--) {
			temp = 0;
			for (int k = i + 1; k < equations.length; k++) {
				temp += X[k] * equations[i][k];
			}
			double t1 = equations[i][i];
			double t2 = B[i] - temp;
			X[i] = t2 / t1;
			if (t1 == 0 || (t1 == 0 && t2 == 0))
				throw new Exception("Numbers are joking here :P ");
			// System.out.println(Arrays.toString(X));
		}
	}

	public void solve() throws Exception {
		long time1;
		long time2;
		if (Pivoting) {
			time1 = System.nanoTime();
			eliminate(true);
			solution();
			time2 = System.nanoTime();
		} else {
			if (needPivoting()) {
				throw new Exception("need pivoting to be solve");
			} else {
				time1 = System.nanoTime();
				eliminate(false);
				solution();
				time2 = System.nanoTime();
			}
		}
		time = (time2 - time1) / Math.pow(10, 6);
	}

	public static void main(String[] args) throws Exception {
		double[][] e = { { 25, 5, 1 }, { 64, 8, 1 }, { 144, 12, 1 } };
		double[] b = { 106.8, 177.2, 279.2 };
		EliminationDouble ee = new EliminationDouble(e, b, true);
		ee.eliminate(true);
		ee.solution();
		double[] u = ee.X;
		ee.write();
		System.out.println(Arrays.toString(u));

	}

	public double[][] getEquations() {
		return equations;
	}

	public void setEquations(double[][] equations) {
		this.equations = equations;
	}

	public double[] getX() {
		return X;
	}

	public void setX(double[] x) {
		X = x;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

}
