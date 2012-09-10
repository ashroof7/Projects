
public class HashByPseudoHashing extends OpenAddressing{

	public HashByPseudoHashing(int n) {
		super(n);
		// TODO Auto-generated constructor stub
	}
	
	int seed = 93077; //prime number
	
	@Override
	public int probe(int h, int j) {
		// TODO Auto-generated method stub
		return (seed*(h+j) + j*7549 )%(1<<r);
	}

}
