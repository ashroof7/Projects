import java.math.BigInteger;


public class HashByDoubleHashing extends OpenAddressing {

	private int B;
	
	public HashByDoubleHashing(int n) {
		super(n);
		BigInteger temp = new BigInteger(Integer.MAX_VALUE/10+"");
		A = temp.nextProbablePrime().intValue();
		temp = new BigInteger(Integer.MAX_VALUE/500+"");
		B = temp.nextProbablePrime().intValue();
		// TODO Auto-generated constructor stub
	}
	
	private int h2(Record rec){
		int k = rec.getName().hashCode();
		h2 = (A*k + B)% (1<<r);
		if (h2<0)return -h2;
		return h2;
	}
	
	@Override
	protected int h(Record rec){
		h2(rec);
		return super.h(rec);
	}
	
	int h2 ; //second hash fn
	@Override
	public int probe(int h, int j) {
		// TODO Auto-generated method stub
		int ret = (h+j*h2)%(1<<r);
		if (ret < 0)return -ret;
		return ret;
	}

	

}
