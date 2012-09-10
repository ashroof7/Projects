
public abstract class AbstNode <K extends Comparable<K>> implements Comparable<AbstNode<K>> {
	
	protected K key ;
	protected boolean isleaf;
	
	
	public boolean isLeaf(){
		return isleaf;
	}
	
	public K getKey(){
		return key;
	};
	
	public void setKey(K key){
		this.key = key;
	};
		
	@Override
	public int compareTo(AbstNode<K> o) {
		return key.compareTo(o.getKey());
	}
	
}
