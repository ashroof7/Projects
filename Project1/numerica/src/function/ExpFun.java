package function;

public class ExpFun extends Function{

	private Function pow;
	
	public ExpFun(Function p){
		pow = p;
		power =1;
	}
	@Override
	public double getValue(double x) {
		return  Math.pow(Math.E, pow.getValue(x));
	}

	@Override
	public Function differentiateFunction() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Product res = new Product();
		res.multiply(new ExpFun(pow));
		return res.multiply(pow.getDerivative());
	}
	public String toString() {		
		return "e ^ {" + pow.toString() + "}";
	}
	@Override
	public Object clone() throws CloneNotSupportedException {		
		return new ExpFun(pow);
	}

}
