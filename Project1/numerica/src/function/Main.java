package function;

public class Main {

	/**
	 * @param args
	 * @throws CloneNotSupportedException 
	 */
	public static void main(String[] args) throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		XFun f1 = new XFun(3, 2);
		XFun f2 = new XFun(20, 15);
		Product prod = new Product(f1, f2);
		Polynomial poly = new Polynomial(prod, new ExpFun(f2));
		SinFun sin = new SinFun(poly);
		CosFun cos = new CosFun(sin);
		Function derivative = cos.getDerivative();
		System.out.println(derivative);
	}

}
