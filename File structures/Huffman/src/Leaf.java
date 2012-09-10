public class Leaf <K extends Comparable<K>, E extends Comparable<? super E>> extends AbstNode<K>{

	private E element;
	
	public Leaf (K key, E element){
		this.key = key;
		this.element = element;
		isleaf = true ;
	}
	
	public E getElement() {
		return element;
	}

	public void setElement(E element) {
		this.element = element;
	}

	public int compareTo(AbstNode<K> ob) {
		return key.compareTo(ob.getKey());
	}



	
}
