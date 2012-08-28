package function;

public class XFun extends Function{
	double coefficient;
	
	public XFun(double coef, double pow){
		coefficient = coef;
		power = pow;
	}
	
	@Override
	public double getValue(double x) {
		return coefficient * (Math.pow(x, power));
	}
	
	@Override
	public Function differentiateFunction() {		
		return new NumberFun(1);		
	}

	@Override
	public String toString(){
		if(power == 1)
			return coefficient + "x";
		return coefficient + " (x^{" + power + "}";
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new XFun(coefficient, power);
	}
}
