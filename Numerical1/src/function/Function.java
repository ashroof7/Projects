package function;

public abstract class Function {
	protected double power;

	public abstract double getValue(double x);

	protected abstract Function differentiateFunction() throws CloneNotSupportedException;

	public Function getDerivative() throws CloneNotSupportedException {

		Function functionDerivative = differentiateFunction();

		if (Math.abs(power) < 0.001)
			return new ZeroFunction();

		if (Math.abs(power - 1) < 0.001) // Power ~= 1
			return functionDerivative;

		Product result = new Product(new NumberFun(power));
		result.multiply(functionDerivative);
		result.multiply(getFunctionWithDecreasedPower());
		return result;
	}

	protected Function getFunctionWithDecreasedPower() {
		Function cloned = null;
		try {
			cloned = (Function) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
		cloned.power--;
		return cloned;
	}

	@Override
	public abstract String toString();

	@Override
	public
	abstract Object clone() throws CloneNotSupportedException;
}
