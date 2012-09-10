public class HashByLinearProbing extends OpenAddressing{

	public HashByLinearProbing(int n) {
		super(n);
	}
	
	@Override
	public int probe(int i, int j){
		return (i+j)%(1<<r);
	}
}
