package bufmgr;

public class Entry<K, E> {
	public K key;
	public E value;

	/**
	 * creates a new Entry with key k and Element e
	 * @param k
	 * @param e
	 */
	public Entry(K k, E e) {
		key = k;
		value = e;
	}

	/**
	 * @return the key
	 */
	public K getKey() {
		return key;
	}

	/**
	 * sets the key
	 * 
	 * @param key
	 */
	public void setKey(K key) {
		this.key = key;
	}

	/**
	 * @return the value of the element
	 */
	public E getValue() {
		return value;
	}

	/**
	 * sets the value of the element
	 * 
	 * @param value
	 */
	public void setValue(E value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + key.toString() + "," + value.toString() + ")";
	}

}