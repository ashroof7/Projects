import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.LinkedList;

public class GaussSeidelvsJacobiF {
	final int maxNumItr = 10;
	final float defultPres = 0.0001f;
	int numItr;
	float pres;
	boolean isJacobi;
	float system[][];
	float output[];
	float currentX[];
	LinkedList<float[]> allX;
	long execuTime = 0;

	public LinkedList<float[]> getXs() {
		return this.allX;
	}

	public double getTime() {
		// return time in ms
		return execuTime/1000000.0; 
	}

	public GaussSeidelvsJacobiF(int numItr, float pres, boolean isJacobi,
			float system[][], float[] output, float currentX[]) {
		this.numItr = numItr;
		this.pres = pres;
		this.isJacobi = isJacobi;
		this.system = system;
		this.output = output;
		this.currentX = currentX;
		this.allX = new LinkedList<float[]>();
	}

	public void write(LinkedList<float[]> allXs) {
		try {
			// Create file
			FileWriter fstream = new FileWriter("DetailedSoln.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			for (int i = 0; i < allXs.size(); i++) {
				out.write("Num of Iteration: " + (i + 1) + "\n");
				System.out.println("Num of Iteration: " + (i + 1));
				for (int j = 0; j < allXs.get(i).length; j++) {
					out.write("x(" + (j + 1) + ") = " + allXs.get(i)[j] + "\n");
					System.out.println("x(" + (j + 1) + ") = "
							+ allXs.get(i)[j]);
				}
				out.write("=================\n");
				System.out.println("================");
			}
			System.out.println();
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	public float[] getFinalX(LinkedList<float[]> allXs) {
		return allXs.get(allXs.size() - 1);
	}

	public float[] GaussvsJacobi(float[][] system, float[] output,
			float[] initialX, boolean isJacobi) {
		float x[] = new float[output.length];
		for (int i = 0; i < x.length; i++) {
			float temp = 0;
			for (int j = 0; j < x.length; j++) {
				if (j != i) {
					temp = temp + (system[i][j] * initialX[j]);
				}
			}
			if (!isJacobi) {
				initialX[i] = (output[i] - temp) / system[i][i];
			} else {
				x[i] = (output[i] - temp) / system[i][i];
			}
		}
		if (!isJacobi)
			for (int i = 0; i < initialX.length; i++)
				x[i] = initialX[i];
		return x;
	}

	public boolean rearrange(float[][] a) {
		float[] sum = new float[a.length];

		// summation of rows
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[i].length; j++)
				sum[i] += a[i][j];

		boolean strictly = false;
		int[] dominant = new int[a[0].length];
		int countDom = 0;
		Arrays.fill(dominant, -1);
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[i].length; j++)
				if (a[i][j] >= sum[i] - a[i][j]) {
					if (a[i][j] > sum[i] - a[i][j])
						strictly = true;

					if (dominant[j] >= 0)
						return false;

					dominant[j] = i;
					countDom++;

					break;
				}

		// not all rows have a dominant term or it's strictly dominant
		if (countDom < a.length || !strictly)
			return false;

		float[][] aa = new float[a.length][a[0].length];
		for (int i = 0; i < a.length; i++)
			aa[i] = a[i];

		for (int i = 0; i < dominant.length; i++)
			a[i] = aa[dominant[i]];

		return true;
	}

	public boolean maxError(float[] a, float[] b) {
		float error[] = new float[a.length];
		for (int i = 0; i < a.length; i++) {
			error[i] = (a[i] - b[i]) / a[i];
		}
		float max = 0;
		for (int i = 0; i < error.length; i++) {
			if (error[i] > max) {
				max = error[i];
			}
		}
		if (max > this.pres)
			return false;
		else
			return true;
	}

	public LinkedList<float[]> run() {
		rearrange(this.system);
		long startTime = System.nanoTime();
		float temp[] = new float[currentX.length];
		for (int i = 0; i < currentX.length; i++) {
			temp[i] = currentX[i];
		}
		allX.add(temp);
		for (int i = 1; i < this.numItr; i++) {
			currentX = this.GaussvsJacobi(system, output, currentX,
					this.isJacobi);
			float temp1[] = new float[currentX.length];
			for (int k = 0; k < currentX.length; k++) {
				temp1[k] = currentX[k];
			}
			allX.add(temp1);
			if (maxError(allX.get(i), allX.get(i - 1))) {
				break;
			}
		}
		long finalTime = System.nanoTime();
		execuTime = finalTime - startTime;
		return allX;
	}

	public static void main(String[] args) {
		float[] output = { 1, 28, 76 };
		float currentX[] = { 1, 0, 1 };
		float[][] system = { { 12, 3, -5 }, { 1, 5, 3 }, { 3, 7, 13 } };
		GaussSeidelvsJacobiF gsj = new GaussSeidelvsJacobiF(100, .0001f, true,
				system, output, currentX);
		LinkedList<float[]> allX = gsj.run();
		gsj.getFinalX(allX);
		gsj.write(allX);
		System.out.println("The execution time= "
				+ (gsj.getTime() * Math.pow(10, -9)) + " sec");

	}
}
