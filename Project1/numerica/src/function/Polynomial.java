package function;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Polynomial extends Function implements Iterable<Function> {
	private List<Function> functions;

	public Polynomial() {
		// initialize power to 0 or 1 ?
		power = 1;
		functions= new LinkedList<Function>();
	}

	public Polynomial(double power, List<Function> f) {
		this();
		this.power = power;
		functions.addAll(f);
	}

	public Polynomial(LinkedList<Function> f) {
		power = 1;
		functions = f;
	}

	public Polynomial(Function... functions) {
		this();
		for (Function function : functions)
			this.functions.add(function);
	}

	public List<Function> getProducts() {
		return functions;
	}

	@Override
	public double getValue(double x) {
		double sum = 0;
		Iterator<Function> it = functions.iterator();
		while (it.hasNext()) {
			sum += it.next().getValue(x);
		}
		return sum;
	}

	@Override
	public Function differentiateFunction() throws CloneNotSupportedException {
		Polynomial result = new Polynomial();
		for (Function function : functions)
			result.add(function.getDerivative());
		return result;
	}

	public void add(Function function) {
//		if (function instanceof Polynomial)
//			for (Function f : (Polynomial) function)
//				this.functions.add(f);
//
//		else if (function instanceof Product)
//			this.functions.add((Product) function);
//
//		else
			this.functions.add(function);
	}

	@Override
	public Iterator<Function> iterator() {
		return functions.iterator();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		LinkedList<Function> cl = new LinkedList<Function>();
		Iterator<Function> it = functions.iterator();
		while (it.hasNext()) {
			cl.add(it.next());
		}
		return new Polynomial(power, cl);
	}

	@Override
	public String toString() {
		if(functions.size() == 1){
			return functions.get(0).toString();
		}
		String s = "(";
		Iterator<Function> it = functions.iterator();
		if (it.hasNext())
			s += it.next();
		while (it.hasNext()) {
			s += " + " + it.next().toString();
		}
		s += ")";
		return s;
	}
}
