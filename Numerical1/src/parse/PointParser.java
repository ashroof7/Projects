package parse;

import java.util.Arrays;
import java.util.StringTokenizer;

import function.Point;

public class PointParser {

	
	Point [] points;
	public PointParser(String s) {
		s =s.replace("\n", "");
		s=s.replace("\\s", "");
		System.out.println(s);
		
		StringTokenizer tk = new StringTokenizer(s,",");
		int n = tk.countTokens();
		points = new Point[n/2];
		for (int i = 0; i < n/2; i++) {
//			System.out.println(tk.nextToken()+" "+tk.nextToken());
			points[i] = new Point(Double.parseDouble(tk.nextToken()), Double.parseDouble(tk.nextToken()));
		}
	}
	
	public Point[] getPoints(){
		return points;
	}
	
	public double[] getXArray(){
		double [] x = new double[points.length];
		for (int i = 0; i < points.length; i++) {
			x[i] = points[i].x;
		}
		return x;
	}
	
	public double[] getYArray(){
		double [] y = new double[points.length];
		for (int i = 0; i < points.length; i++) {
			y[i] = points[i].y;
		}
		return y;
	}
	public static void main(String[] args) {
		String p = "2,3,4,5\n,3,4";
		PointParser pp = new PointParser(p);
		System.out.println(Arrays.toString(pp.getPoints()));
		
	}

}
