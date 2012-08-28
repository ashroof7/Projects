package methods.interpolation;

import function.Function;
import function.Point;
import function.Polynomial;
import function.SinFun;
import function.XFun;

public class TestLagrange {

	/**
	 * @param args
	 * @throws CloneNotSupportedException 
	 */
	public static void main(String[] args) throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Point[] points = new Point[3];
		points[0] = new Point(-1,2);
		points[1] = new Point(2,3);
		points[2] = new Point(3,4);
		Lagrange l1 = new Lagrange(points);
		System.out.println(l1.getError((Function)(new SinFun(new XFun(1,1)))));
		Polynomial solution = (Polynomial) l1.solve();		
		System.out.println(solution);		
	}
}
