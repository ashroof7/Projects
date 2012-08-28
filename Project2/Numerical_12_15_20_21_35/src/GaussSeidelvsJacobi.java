import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.LinkedList;

public class GaussSeidelvsJacobi {
	final int maxNumItr = 10;
	final double defultPres = 0.0001;
	int numItr;
	double pres;
	boolean isJacobi;
	double system[][];
	double output[];
	double currentX[];
	LinkedList<double[]> allX;
	long execuTime = 0;

	public LinkedList<double[]> getXs()
	{
		return this.allX;
	}

	public double getTime()
	{
		return execuTime / 1000000.0;
	}

	public void write(LinkedList<double[]> allXs)
	{
		try
		{
			// Create file
			FileWriter fstream = new FileWriter("DetailedSoln.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			for (int i = 0; i < allXs.size(); i++)
			{
				out.write("Num of Iteration: " + (i + 1) + "\n");
				System.out.println("Num of Iteration: " + (i + 1));
				for (int j = 0; j < allXs.get(i).length; j++)
				{
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
		} catch (Exception e)
		{// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	public double[] getFinalX(LinkedList<double[]> allXs)
	{
		return allXs.get(allXs.size() - 1);
	}

	public GaussSeidelvsJacobi(int numItr, double pres, boolean isJacobi,
			double system[][], double[] output, double currentX[])
	{
		this.numItr = numItr;
		this.pres = pres;
		this.isJacobi = isJacobi;
		this.system = system;
		this.output = output;
		this.currentX = currentX;
		this.allX = new LinkedList<double[]>();
	}

	public double[] GaussvsJacobi(double[][] system, double[] output,
			double[] initialX, boolean isJacobi)
	{
		double x[] = new double[output.length];
		for (int i = 0; i < x.length; i++)
		{
			double temp = 0;
			for (int j = 0; j < x.length; j++)
			{
				if (j != i)
				{
					temp = temp + (system[i][j] * initialX[j]);
				}
			}
			if (!isJacobi)
			{
				initialX[i] = (output[i] - temp) / system[i][i];
			} else
			{
				x[i] = (output[i] - temp) / system[i][i];
			}
		}
		if (!isJacobi)
			for (int i = 0; i < initialX.length; i++)
				x[i] = initialX[i];
		return x;
	}

	public boolean rearrange(double[][] a, double[] b)
	{
		double[] sum = new double[a.length];

		// summation of rows
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[i].length; j++)
				sum[i] += Math.abs(a[i][j]);

		boolean strictly = false;
		int[] dominant = new int[a[0].length];
		int countDom = 0;
		Arrays.fill(dominant, -1);
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[i].length; j++)
				if (Math.abs(a[i][j]) >= sum[i] - Math.abs(a[i][j]))
				{
					if (Math.abs(a[i][j]) > sum[i] - Math.abs(a[i][j]))
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

		double[][] aa = new double[a.length][a[0].length];
		double[] bb = new double[b.length];
		for (int i = 0; i < a.length; i++)
		{
			aa[i] = a[i];
			bb[i] = b[i];
		}

		for (int i = 0; i < dominant.length; i++)
		{
			a[i] = aa[dominant[i]];
			b[i] = bb[dominant[i]];
		}

		return true;
	}

	public boolean maxError(double[] a, double[] b)
	{
		double error[] = new double[a.length];
		for (int i = 0; i < a.length; i++)
		{
			error[i] = (a[i] - b[i]) / a[i];
		}
		double max = 0;
		for (int i = 0; i < error.length; i++)
		{
			if (error[i] > max)
			{
				max = error[i];
			}
		}
		if (max > this.pres)
			return false;
		else
			return true;
	}

	public LinkedList<double[]> run()
	{
		rearrange(this.system , currentX);
		long startTime = System.nanoTime();
		double temp[] = new double[currentX.length];
		for (int i = 0; i < currentX.length; i++)
		{
			temp[i] = currentX[i];
		}
		allX.add(temp);
		for (int i = 1; i < this.numItr; i++)
		{
			currentX = this.GaussvsJacobi(system, output, currentX,
					this.isJacobi);
			double temp1[] = new double[currentX.length];
			for (int k = 0; k < currentX.length; k++)
			{
				temp1[k] = currentX[k];
			}
			allX.add(temp1);
			if (maxError(allX.get(i), allX.get(i - 1)))
			{
				break;
			}
		}
		long finalTime = System.nanoTime();
		execuTime = finalTime - startTime;
		return allX;
	}

	public static void main(String[] args)
	{
		double[] output = { 1, 28, 76, 1, 28, 76 };
		double currentX[] = { 1, 0, 1, 1, 0, 1 };
		double[][] system = { { 12, 3, -5, 12, 3, -5 }, { 1, 5, 3, 1, 5, 3 },
				{ 3, 7, 13, 3, 7, 13 }, { 12, 3, -5, 12, 3, -5 },
				{ 1, 5, 3, 1, 5, 3 }, { 3, 7, 13, 3, 7, 13 } };
		GaussSeidelvsJacobi gsj = new GaussSeidelvsJacobi(100, .0000001, false,
				system, output, currentX);
		LinkedList<double[]> allX = gsj.run();
		gsj.getFinalX(allX);
		gsj.write(allX);
		System.out.println("The execution time= "
				+ (gsj.getTime() * Math.pow(10, -9)) + " sec");
	}
}
