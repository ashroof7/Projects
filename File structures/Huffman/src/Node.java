
public class Node <K extends Comparable<K>> extends AbstNode<K>  {

	private AbstNode<K> right;
	private AbstNode<K> left;
	
	
	public Node(K key){
		this.key = key;
		right = left = null;
	}  
	
		
	public void setChildren(AbstNode<K> left, AbstNode<K> right){
		this.left = left;
		this.right = right;
	} 
	
	public AbstNode<K> getRight() {
		return right;
	}


	public void setRight(AbstNode<K> right) {
		this.right = right;
	}


	public AbstNode<K> getLeft() {
		return left;
	}


	public void setLeft(AbstNode<K> left) {
		this.left = left;
	}

	
}
