public abstract class MyHashTable {


	protected int w = 32 ;//system word length = 32;
	protected int A =    1073676287 ;// big prime number ;
	protected int r;  //the length of the table is 2^r
	protected int n;//no of elements in the table
	protected int comparisons ;
	protected double loadFactor ;
	protected final double DEFAULT_LOAD_FACTOR = 0.7;
	
	public abstract void put(Record rec);
	
	/**
	 * @param rec
	 * @return the index of the record in the table if found, -1 otherwise
	 */
	public abstract int get(Record rec);

	/**
	 * @param rec
	 * @return true if rec id found in the table (then remove it), false otherwise 
	 */
	public abstract boolean remove(Record rec);	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();	

	protected int h(Record rec){
	/*	 A*k mod 2^w >> (w-r)
	 *	taking the mod to  2^w = taking the w bits from the number  
	 */
		 int k = rec.getName().hashCode();
//		 System.out.println(k);
		 int ret = A*k >> (w-r) ;
		 
		 if (ret < 0) return -ret;
		 
		 else return ret ;
	}

	/**
	 * constructs a new table with double size -as to keep the load factor <0.7-
	 * then reinserts all the records in the new table 
	 */
	protected abstract void reHash();
	
	/**
	 * @param n
	 * @return returns the first power of 2 that is > n
	 */
	protected int generateLength(int n){
		int ret = 1;
		r = 0;
		while (ret<n){
			ret<<=1;
			r++;
		}
		return ret ;
	}
	
	
	/**
	 * @return the number of comparisons in the last successful search
	 * 	if the last search was not successful return -1
	 */
	public int getComparisons(){
		return comparisons;
	}
}
