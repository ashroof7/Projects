import java.io.IOException;
import java.util.ArrayList;

public class test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FReader fr = new FReader();
		ArrayList<Record> ar = fr.getRecords();
		System.out.println(ar.size());
		
		HashByChaining hh = new HashByChaining(ar.size());
//		HashByDoubleHashing hh = new HashByDoubleHashing(ar.size());
//		HashByLinearProbing hh = new HashByLinearProbing(ar.size());
//		HashByQuadProbing hh = new HashByQuadProbing(ar.size());
//		HashByPseudoHashing hh  = new HashByPseudoHashing(ar.size());
		
		for (int i = 0; i < ar.size(); i++) {
			hh.put(ar.get(i));
		}
		System.out.println("# of probes = "+hh.getComparisons());
	}

}
