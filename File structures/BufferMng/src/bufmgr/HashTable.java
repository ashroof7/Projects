package bufmgr;

import java.math.BigInteger;
import java.util.LinkedList;

public class HashTable<K, E> {
	private int size, n = 0;
	private static final int A = 1, B = 1, DEFAULT_HASH_SIZE = 10;
	LinkedList<Entry<K, E>>[] table; // Used array instead of Array list

	/**
	 * creates a new HashTable with the given size
	 * 
	 * @param size
	 */
	@SuppressWarnings("unchecked")
	public HashTable(int size) {
		// Loading factor
		BigInteger nextPrime = BigInteger.valueOf((size * 10) / 7)
				.nextProbablePrime();
		this.size = nextPrime.intValue();

		table = new LinkedList[this.size]; // Generic issue
		for (int i = 0; i < this.size; i++) {
			table[i] = new LinkedList<Entry<K, E>>();
		}
	}

	/**
	 * creates a new HashTable with the default size
	 */
	public HashTable() {
		this(DEFAULT_HASH_SIZE);
	}

	/**
	 * puts a new value in the hash table with the given Key 
	 * @param key
	 * @param value
	 */
	public void put(K key, E value) {
		Entry<K, E> temp = search(key);
		if (temp == null) {
			int index = hashF(key);
			insert(value, key, index);
			n++;
		} else {
			temp.setValue(value);
		}
	}

	/**
	 * @param key
	 * @return the value of the Entry corresponding to the given key
	 */
	public E get(K key) {
		Entry<K, E> temp = search(key);
		if (temp == null)
			return null;
		return temp.getValue();
	}

	/**
	 * removes the Entry with the given key from the hash table and returns its value
	 * @param key
	 * @return the value of the Entry corresponding to the given key
	 */
	public E remove(K key) {
		Entry<K, E> temp = search(key);
		if (temp != null) {
			int index = hashF(key);
			table[index].remove(temp);
			return temp.getValue();
		}
		return null;
	}

	
	/**
	 * @param key
	 * @return true if the hash table contains the given key, false otherwise
	 */
	public boolean containsKey(K key) {
		int index = hashF(key);
		if (table[index] == null)
			return false;

		for (Entry<K, E> entry : table[index])
			if (entry.key.equals(key))
				return true;

		return false;
	}

	/**
	 * @param value
	 * @return true if the hash table contains the given value, false otherwise
	 */
	public boolean containsValue(E value) {
		for (int i = 0; i < table.length; i++) {
			for (Entry<K, E> element : table[i]) {
				if (element.getValue().equals(value))
					return true;
			}
		}
		return false;
	}

	/**
	 * @return the max size (array length) in the hash table
	 */
	public int size() {
		return size;
	}

	
	/**
	 * @return the number of entries in the hash table
	 */
	public int numberOfElements() {
		return n;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String str = "";
		if (size != 0)
			str += table[0].toString();
		int i = 0;
		for (LinkedList<Entry<K, E>> list : table)
			if (i++ != 0)
				str += "\n" + list.toString();
		return str;
	}

	
	/*
	 Helping methods
	 */
	
	private void insert(E value, K key, int index) {
		LinkedList<Entry<K, E>> list = table[index];
		list.add(new Entry<K, E>(key, value));
	}

	private Entry<K, E> search(K key) {
		Entry<K, E> entry = null;
		int index = hashF(key);
		for (Entry<K, E> temp : table[index]) {
			boolean compare = temp.key.equals(key);
			if (compare) {
				entry = temp;
				break;
			}
		}
		return entry;
	}

	private int hashF(K key) {
		return (A * key.hashCode() + B) % size;
	}
}
