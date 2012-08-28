package methods.interpolation;

import function.Function;
import function.NumberFun;
import function.Point;
import function.Polynomial;
import function.Product;
import function.XFun;

public class Lagrange {
	Point[] points;
	int size;
	double maxDerivative;	
	
	public Lagrange(Point[] p){
		points = p;
		size = p.length;		
	}
	
	public Function solve() throws CloneNotSupportedException{
		Polynomial result = new Polynomial();
		Product numerator;
		Product denominator;
		NumberFun currentX;
		NumberFun currentY;
		NumberFun tempX;
		XFun x = new XFun(1,1);
		for(int i=0; i<size; i++){
			numerator = new Product();
			denominator = new Product();
			currentX = new NumberFun(points[i].x);
			currentY = new NumberFun(points[i].y);
			for(int j=0; j<i; j++){
//				add factor to the numerator
				tempX = new NumberFun(-1*points[j].x);
				performIteration(tempX, x, numerator, denominator, currentX);
			}
			for(int j=i+1; j<size; j++){
//				add factor to the numerator
				tempX = new NumberFun(-1*points[j].x);				
//				numerator factor j = X - Xj				
				performIteration(tempX, x, numerator, denominator, currentX);
			}
			numerator.multiply(currentY);
			numerator.divide((Product)denominator.clone());									
			result.add((Product)numerator.clone());
		}
		return result;
	}
	
	private void performIteration(NumberFun tempX, XFun x, Product numerator, Product denominator, NumberFun currentX) throws CloneNotSupportedException{
		Polynomial tempFactor = new Polynomial();
//		numerator factor j = X - Xj				
		tempFactor.add((XFun)(x.clone()));
		tempFactor.add((NumberFun)tempX.clone());
//		add factor to the numerator
		numerator.multiply((Polynomial)(tempFactor.clone()));
//		denominator factor j = Xo - Xj
		tempFactor = new Polynomial();
		tempFactor.add((NumberFun)currentX.clone());
		tempFactor.add((NumberFun)tempX.clone());
		denominator.multiply((Polynomial)tempFactor.clone());
	}
	
//	public Function getErrorBound(Function f, double startInterval, double endInterval){
//		double increment = 0.001;
//		Function derivative = f;
//		double maxDerivative = -1;
//		Product result = new Product();
//		XFun x = new XFun(1, 1);
//		for(int i=0; i<size; i++){
//			Polynomial temp = new Polynomial();
//			temp.add(x);
//			temp.add(new NumberFun(-1*points[i].x));
//			result.multiply(temp);
//			derivative = derivative.getDerivative();
//		}
//		
//		for(double current = startInterval; current<endInterval; current += increment){
//			double temp = Math.abs(derivative.getValue(current));
//			maxDerivative = temp>maxDerivative ? temp:maxDerivative;	
//		}		
//		return result.multiply(new NumberFun(maxDerivative/factorial(size)));		
//	}
	
	public Product getError(Function f) throws CloneNotSupportedException{
		double startInterval = Integer.MAX_VALUE;
		double endInterval = Integer.MIN_VALUE;		
		double increment = 0.001;
		XFun x = new XFun(1,1);
		Product result = new Product();
		Function derivative = f;
		double maxDerivative = -1;			
		for(int i=0; i<size; i++){
			double tempX = points[i].x;
			startInterval = startInterval<tempX?startInterval: tempX;
			endInterval = startInterval>tempX?endInterval: tempX;
			result.multiply(new Polynomial(x, new NumberFun(-1*tempX)));			
			derivative = derivative.getDerivative();
		}
		
		for(double current = startInterval; current<endInterval; current += increment){
			double temp = Math.abs(derivative.getValue(current));
			maxDerivative = temp>maxDerivative ? temp:maxDerivative;	
		}		
		return result.multiply(new NumberFun(maxDerivative/(factorial(size))));		
	}
	
	private double factorial(int i){
		if(i==0 || i==1)
			return 1;
		return i*factorial(i-1);
	}
	
//	public double getMaxError(Function f, double startInterval, double endInterval) throws CloneNotSupportedException{
//		double m1 = getError(f, startInterval, startInterval, endInterval).;
//		double m2 = getError(f, endInterval, startInterval, endInterval);
//		if(m1>m2)
//			return m1;
//		else
//			return m2;
//	}
}
