
public class HashByQuadProbing extends OpenAddressing{

	public HashByQuadProbing(int n) {
		super(n);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int probe(int i, int j){
		return (i + j*j)%(1<<r);
	}

}
