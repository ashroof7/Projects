import java.util.BitSet;


public abstract class OpenAddressing extends MyHashTable {
	
	protected Record[] table ;
	protected BitSet isDeleted;
    public int j; // intermediate # of probing
    private int h; // intermediate hash function
	
	public OpenAddressing(int n) {
		// TODO Auto-generated constructor stub
		table = new Record[n = generateLength(n)];
		isDeleted = new BitSet(n);
	}

	/**
	 * @param i
	 * @param j step;
	 * @return the next probe according to the addressing technique
	 */
	public abstract int probe(int h, int j);
	
	/* (non-Javadoc)
	 * @see MyHashTable#put(Record)
	 */
	@Override
	public void put(Record rec) {
		// TODO Auto-generated method stub
		h = h(rec);
		int index = h;
		j = comparisons = 0 ;
		while(table[index]!=null && !isDeleted.get(index)){
			index=probe(h,j++);
			comparisons++;
		}
		table[index] = rec ; 	
		
		loadFactor = ++n / (1<<r) ;
		if (loadFactor > DEFAULT_LOAD_FACTOR)
			reHash() ;
	}

	/* (non-Javadoc)
	 * @see MyHashTable#get(Record)
	 * 
	 * To improve, when an element is searched and found in the table, 
	 * the element is relocated to the first location marked for deletion 
	 * that was probed during the search.
	 */
	@Override
	public int get(Record rec) {
		// TODO Auto-generated method stub
		h = h(rec);
		int i = h; 
		j = comparisons = 0 ;
		int firstDeleted = -1;
		boolean found = false ;
		/* don't have to check that #of probes < length
		 * as i rehash when the load factor > 0.7
		 * for sure i'll find an empty slot
		 */		
		while(table[i] != null){
				comparisons++;
				if (isDeleted.get(i)){
					if (firstDeleted == -1)
						firstDeleted = i;
					i = probe(h,j++);
				}
				else {
					found = rec.equals(table[i]);
					if (found && firstDeleted != -1)
						table[firstDeleted] = rec ; 
					break;
				}
		}
		
		if (found)
			return i;
		else
			return comparisons = -1;
	}

	/* (non-Javadoc)
	 * @see MyHashTable#remove(Record)
	 */
	@Override
	public boolean remove(Record rec) {
		int index = get(rec);
		if (index==-1)
			return false;
		
		isDeleted.set(index);
		n--;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see MyHashTable#reHash()
	 */
	public void reHash(){
		Record[] oldTable = table ;
		Record[] newTable = new Record[n<<++r];
		table = newTable ;
		n = 0;
		for (int i = 0; i < oldTable.length; i++) 
			put(oldTable[i]);
	}
	
	/* (non-Javadoc)
	 * @see MyHashTable#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[ ");
		for (int i = 0; i < table.length; i++) 
				sb.append(table[i].getName()+" = "+table[i]+"\n");
		sb.append(" ]");
		return sb.toString();
	}	
	
}
