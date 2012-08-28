import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.LinkedList;

public class LUCompositionFloat {
	private float[][] equations; // the array of elimination
	private float[][] B;
	private float[] X;
	private float[] R;
	private float[] Y;
	private LinkedList<float[][]> history;
	private LinkedList<float[]> history1;
	private boolean pivoting;
	private LinkedList<float[]> result;
	private double time;

	// ABLU
	// BZX
	public void write() {
		try {
			// Create file
			FileWriter fstream = new FileWriter("LUDetailedSoln.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("The system A= \n");
			// A
			for (int j = 0; j < history.get(0).length; j++) {
				for (int k = 0; k < history.get(0)[j].length; k++) {
					out.write(history.get(0)[j][k] + " ");
				}
				out.write("\n");
			}
			out.write("---------------\n");
			out.write("LU= \n");
			// LU
			for (int j = 0; j < history.get(2).length; j++) {
				for (int k = 0; k < history.get(2)[j].length; k++) {
					out.write(history.get(2)[j][k] + " ");
				}
				out.write("\n");
			}
			out.write("==========================================\n");
			for (int k = 0; k < ((history1.size() + 1) / 3); k++) {
				out.write("B | Z | X\n");
				for (int i = 0; i < history1.get(k).length; i++) {
					out.write(history1.get(k * 3)[i] + " | "
							+ history1.get((k * 3) + 1)[i] + " | "
							+ history1.get((k * 3) + 2)[i] + "\n");
				}
				out.write("------------------------\n");
			}

			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	// constructor
	public LUCompositionFloat(float[][] e, float[][] b, boolean doPivoting)
			throws Exception {
		equations = e;
		R = new float[e.length];
		for (int i = 0; i < e.length; i++) {
			R[i] = i;
		}
		if (doPivoting)
			pivoting = true;
		else
			pivoting = false;
		B = b;
		X = new float[equations[0].length];
		Y = new float[equations[0].length];

		history = new LinkedList<float[][]>();
		history1 = new LinkedList<float[]>();
		if (equations[0].length > equations.length)
			throw new Exception("LUF : can't be solved");
		float[][] t = clone2(equations);
		history.add(t);
		history.add(b);
		result = new LinkedList<float[]>();
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
			if (candi != i)
				swap(i, candi);
		}

	}

	// swap 2 rows
	private void swap(int i, int candi) {
		for (int j = 0; j < equations[i].length; j++) {
			float temp = equations[i][j];
			equations[i][j] = equations[candi][j];
			equations[candi][j] = temp;
		}
		float temp = R[i];
		R[i] = R[candi];
		R[candi] = temp;
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

				for (int l = i; l < equations[k].length; l++) {
					equations[k][l] = equations[k][l] - (equations[i][l] * m);
				}
				equations[k][i] = m;
			}
		}
		float[][] t = clone2(equations);
		history.add(t);
	}

	public static float matMultiplication(float[] a, float[] b) {
		float c = 0;
		for (int i = 0; i < a.length; i++) {
			c += a[i] * b[i];

		}
		return c;
	}

	private float[] clone1(float[] b2) {
		float[] temp = new float[b2.length];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = b2[i];
		}
		return temp;
	}

	public void solution(float[] b) throws Exception {
		float[] t5 = clone1(b);
		history1.add(t5);
		Y[0] = b[(int) R[0]];
		for (int i = 1; i < Y.length; i++) {
			float[] A = new float[i];
			for (int j = 0; j < i; j++) {
				A[j] = equations[i][j];
			}
			float temp = matMultiplication(A, Y);
			Y[i] = b[(int) R[i]] - temp;

		}
		t5 = clone1(Y);
		history1.add(t5);
		for (int i = equations.length - 1; i >= 0; i--) {
			float temp = 0;
			for (int k = i + 1; k < equations.length; k++) {
				temp += X[k] * equations[i][k];
			}
			float t1 = equations[i][i];
			float t2 = Y[i] - temp;
			X[i] = t2 / t1;
			if (t1 == 0 || (t1 == 0 && t2 == 0))
				throw new Exception("Numbers are joking here :P ");
		}
		t5 = clone1(X);
		history1.add(t5);
	}

	public void solve() throws Exception {
		long time1;
		long time2;
		if (pivoting) {
			time1 = System.nanoTime();
			eliminate(true);
			for (int i = 0; i < B[0].length; i++) {
				float[] temp = new float[B.length];
				for (int j = 0; j < temp.length; j++) {
					temp[j] = B[j][i];// take the column that is the turn on
				}
				solution(temp);
				float[] temp12 = clone1(X);
				result.add(temp12);

			}
			time2 = System.nanoTime();
		} else {
			if (needPivoting())
				throw new Exception("Need Pivoting to be solved ");
			else {
				time1 = System.nanoTime();
				eliminate(false);
				for (int i = 0; i < B[0].length; i++) {
					float[] temp = new float[B.length];
					for (int j = 0; j < temp.length; j++) {
						temp[j] = B[j][i];// take the column that is the turn on
					}
					solution(temp);
					float[] temp12 = clone1(X);
					result.add(temp12);

				}
				time2 = System.nanoTime();
			}

		}
		time = (time2 - time1) / Math.pow(10, 6);
		for (int i = 0; i < result.size(); i++) {
			System.out.println(Arrays.toString(result.get(i)));
		}
	}

	public static void main(String[] args) throws Exception {
		float[][] e = { { 25, 5, 1 }, { 64, 8, 1 }, { 144, 12, 1 } };
		float[][] b = { { (float) 106.8, (float) 106.8, (float) 106.8 },
				{ (float) 177.2, (float) 177.2, (float) 177.2 },
				{ (float) 279.2, (float) 29.2, (float) 27.2 } };
		LUCompositionFloat ee = new LUCompositionFloat(e, b, true);
		ee.solve();
		ee.write();
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

	public LinkedList<float[]> getResult() {
		return result;
	}

	public void setResult(LinkedList<float[]> result) {
		this.result = result;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

}
