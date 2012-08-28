import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.LinkedList;

public class EliminationFloat {
	private float[][] equations; // the array of elimination
	private float[] B;
	private float[] X;
	private LinkedList<float[][]> history;
	private LinkedList<float[]> history1;
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
	public EliminationFloat(float[][] e, float[] b, boolean doPivot)
			throws Exception {
		equations = e;
		B = b;
		X = new float[equations[0].length];
		if (doPivot) {
			Pivoting = true;
		} else
			Pivoting = false;
		if (equations[0].length > equations.length)
			throw new Exception("ElF : cannot be solved ");
		history = new LinkedList<float[][]>();
		history1 = new LinkedList<float[]>();
		float[][] t = clone2(equations);
		history.add(t);
		float[] t1 = clone1(B);
		history1.add(t1);

	}

	private float[] clone1(float[] b2) {
		float[] temp = new float[b2.length];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = b2[i];
		}
		return temp;
	}

	private float[][] clone2(float[][] equations2) {
		float[][] temp = new float[equations2.length][equations2[0].length];
		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[i].length; j++) {
				temp[i][j] = equations2[i][j];
			}
		}
		return temp;
	}

	public float[] getB() {
		return B;
	}

	public void setB(float[] b) {
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
		float max = 0;// holder for max value
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
				float[][] t = clone2(equations);
				history.add(t);
				float[] t1 = clone1(B);
				history1.add(t1);
			}
		}

	}

	// swap 2 rows
	private void swap(int i, int candi) {
		for (int j = 0; j < equations[i].length; j++) {
			float temp = equations[i][j];
			equations[i][j] = equations[candi][j];
			equations[candi][j] = temp;
		}
		float temp = B[i];
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
				float m = equations[k][i] / equations[i][i];// the factor

				for (int l = 0; l < equations[k].length; l++) {
					equations[k][l] = equations[k][l] - (equations[i][l] * m);
				}

				B[k] = B[k] - (B[i] * m);

			}
			float[][] t = clone2(equations);
			history.add(t);
			float[] t1 = clone1(B);
			history1.add(t1);
		}

	}

	public static float[] matMultiplication(float[][] a, float[] b) {
		if (a[0].length != b.length)
			throw new IllegalArgumentException("Matrix Dimension Error");
		float c[] = new float[b.length];

		for (int i = 0; i < c.length; i++) {
			for (int k = 0; k < c.length; k++) {
				c[i] += a[i][k] * b[k];
			}

		}
		return c;
	}

	public void solution() throws Exception {
		float temp = 0;

		for (int i = equations.length - 1; i >= 0; i--) {
			temp = 0;
			for (int k = i + 1; k < equations.length; k++) {
				temp += X[k] * equations[i][k];
			}
			float t1 = equations[i][i];
			float t2 = B[i] - temp;
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
		float[][] e = { { 25, 5, 1 }, { 64, 8, 1 }, { 144, 12, 1 } };
		float[] b = { (float) 106.8, (float) 177.2, (float) 279.2 };
		EliminationFloat ee = new EliminationFloat(e, b, true);
		ee.eliminate(true);
		ee.solution();
		float[] u = ee.X;
		ee.write();
		System.out.println(Arrays.toString(u));

	}

	public float[][] getEquations() {
		return equations;
	}

	public void setEquations(float[][] equations) {
		this.equations = equations;
	}

	public float[] getX() {
		return X;
	}

	public void setX(float[] x) {
		X = x;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

}
