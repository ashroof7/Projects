import java.util.Iterator;
import java.util.LinkedList;


public class HashByChaining extends MyHashTable{
	LinkedList<Record>[] table;
	
	@SuppressWarnings("unchecked")
	public HashByChaining(int capacity) {
		table = new LinkedList[generateLength(capacity)];
		for (int i = 0; i < table.length; i++) {
			table[i] = new LinkedList<Record>();
		}
	}
	
	public void put(Record rec){
		int index = h(rec);
		table[index].add(rec);
		comparisons = 0; 
		loadFactor = ++n / (1<<r) ;
		
		if (loadFactor > DEFAULT_LOAD_FACTOR)
			reHash() ;
		
	}
	
	@SuppressWarnings("unchecked")
	public void reHash(){
		LinkedList<Record> oldTable[] = table ;
		
		LinkedList<Record> newTable[] = new LinkedList[n<<++r];
		table = newTable ;
		n = 0;
		for (int i = 0; i < oldTable.length; i++) 
			for (Record r : newTable[i]) 
				put(r);
	}
	
	/**
	 * takes a record as the user doesn't know the instance of the record class used as a key
	 * @param rec
	 * @return
	 */
	
	public int get(Record rec){
		comparisons = 0;
		int index = h(rec);
		for (Iterator<Record> iterator = table[index].iterator(); iterator.hasNext();){ 
			comparisons++;
			if (rec.equals(iterator.next()))return index;
		}
		return comparisons =-1 ;
	} 

	public boolean remove(Record rec){
		int index = h(rec);
		comparisons = 0;
		
		for (Iterator<Record> iterator = table[index].iterator(); iterator.hasNext();){ 
			if (rec.equals(iterator.next()))
				comparisons++;
				iterator.remove();
				return true;
		}
		comparisons = -1;
		return false ;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[ ");
		for (int i = 0; i < table.length; i++) {
			for (Record r : table[i]) {
				sb.append(r.getName()+" = "+r+"\n");
			}
		}
		sb.append(" ]");
		return sb.toString();
	}	

	public static void main(String[] args) {
	
//		Hashtable<Integer, Integer> h = new Hashtable<Integer, Integer>();
		
	}



}
