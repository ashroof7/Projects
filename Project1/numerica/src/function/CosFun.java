package function;

public class CosFun extends Function {

	private Function angle;

	public CosFun(Function a) {
		angle = a;
		power = 1;
	}

	public CosFun(Function a, double p) {
		angle =a;
		power = p;
	}

	@Override
	public double getValue(double x) {
		return Math.cos(angle.getValue(x));
	}

	@Override
	public Function differentiateFunction() throws CloneNotSupportedException {
	if (power == 0) {
			return new ZeroFunction();
		}
		Product result = new Product();
		result.multiply(new NumberFun(-1));
		result.multiply(new SinFun(angle));
		result.multiply(angle.getDerivative());
		return result;
	}
public String toString() {
	if(power == 1)
		return "cos(" + angle.toString() + ")";
	else
		return "cos^{"+power+"}("+ angle.toString()+ ")";
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return new CosFun(angle, power);
	}
}
