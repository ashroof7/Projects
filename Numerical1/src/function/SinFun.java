package function;

//ok
public class SinFun extends Function {
	Function angle;

	public SinFun(Function a) {
		angle = a;
		power = 1;
	}

	public SinFun(Function a, double power) {
		angle = a;
		this.power = power;
	}

	@Override
	public double getValue(double x) {
		return Math.sin(angle.getValue(x));
	}

	@Override
	public Function differentiateFunction() throws CloneNotSupportedException {
		if (power == 0) {
			return new ZeroFunction();
		}
		Product res = new Product();
		res.multiply(new CosFun(angle));
		res.multiply(angle.getDerivative());
		return res;
	}

	@Override
	public String toString() {
		if(power != 1)
			return "sin^{" + power + "}(" + angle.toString() + ")";
		else
			return "sin(" + angle.toString() + ")";
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new SinFun(angle, power);
	}
}
