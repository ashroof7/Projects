/**
 * @author Ashraf Saleh
 * @date 11-Feb-2012
 * @param <T>
 * 
 */
public class BST<T extends Comparable<? super T>> {

	private Node<T> root = null;

	

//	/**
//	 * Node Class that is used to construct the tree
//	 * 
//	 * @param <T>
//	 */
//	@SuppressWarnings("hiding")
//	private class Node<T extends Comparable<? super T>> {
//		T data;
//		Node<T> right;
//		Node<T> left;
//
//		Node(T data) {
//			this.data = data;
//			right = left = null;
//		}
//
//		private Node<T> getParent(Node<T> start) {
//			if (start == this )return null;
//			
//			
//			while (start != null) {
//				if (data.compareTo(start.data) >= 0) {
//					if (start.right == this)
//						return start;
//					else
//						start = start.right;
//				} else {
//
//					if (start.left == this)
//						return start;
//					else
//						start = start.left;
//				}
//
//			}
//			return null;
//		}
//
//	}
//	
	/**
	 * create new tree (default constructor)
	 */
	public BST() {
		root = new Node<T>(null);
	}

	/**
	 * @param data
	 *            create new tree with a root containing given data
	 */
	public BST(T data) {
		root = new Node<T>(data);
	}

	/**
	 * @return the root
	 */
	public Node<T> getRoot() {
		return root;
	}

	/**
	 * @param root
	 *            the root to set
	 */
	public void setRoot(Node<T> root) {
		this.root = root;
	}

	/**
	 * @param target
	 * @return true if the tree contains the target value, false otherwise
	 */
	public boolean contains(T target) {
		return search(target, root) != null;
	}

	/**
	 * @param data
	 *            add new node to the tree
	 * @throws Exception
	 *             if the node is already in the tree
	 */
	public void add(T data) throws Exception {
		insert(new Node<T>(data), root);
	}

	/**
	 * @param data
	 * @return the node containing the target data
	 */
	public Node<T> get(T data){
		return search(data, root);
	}
	
	/**
	 * @param node
	 * @throws Exception
	 *             removes a given node from the tree
	 */
	public void remove(Node<T> node) throws Exception {
		// leaf node or doesn't have a right node
		if (node.right == null) {
			Node<T> parent = node.getParent(root);
			// if the node is a right child
			if (parent.right.equals(node))
				parent.right = node.left;
			// the node is a left child
			else
				parent.left = node.left;
		}

		else {
			Node<T> newRoot = getMin(node.right);
			
			node.data = newRoot.data;
			
			Node<T> parent = newRoot.getParent(root);
			// the node is a left child
			if (parent.left != null && parent.left.equals(newRoot)) 
				parent.left = newRoot.right;
				// the node is a right child
			 else if (parent.right.equals(newRoot)) 
				parent.right = newRoot.right;
			
			newRoot.right = null;
			
			
			
			
			
		}
	}

	/**
	 * @return depth of the tree
	 */
	public int depth(){
		return countLevels(root, 0);
	}
	
	//Traversal methods
	/**
	 * @return a a postOrder traversal of the tree
	 */
	public String postOrder() {
		StringBuilder ret = new StringBuilder("[");
		postOrder(root, ret);
		ret = ret.replace(ret.length() - 2, ret.length(), "]");
		return ret.toString();
	}

	/**
	 * @return a a inOrder traversal of the tree
	 */
	public String inOrder() {
		StringBuilder ret = new StringBuilder("[");
		inOrder(root, ret);
		ret = ret.replace(ret.length() - 2, ret.length(), "]");
		return ret.toString();
	}

	/**
	 * @return a a preOrder traversal of the tree
	 */
	public String preOrder() {
		StringBuilder ret = new StringBuilder("[");
		preOrder(root, ret);
		ret = ret.replace(ret.length() - 2, ret.length(), "]");
		return ret.toString();
	}

	
	
	//helping methods
	
	/**
	 * @param target
	 * @param start
	 * @return the node containing the target value or null if the tree doesn't
	 *         contain the target value
	 */
	private Node<T> search(T target, Node<T> start) {
		while (start != null) {
			if (start.data.compareTo(target) < 0)
				start = start.right;
			else if (start.data.compareTo(target) > 0)
				start = start.left;
			else
				return start;
			
		
		}
		return null;
	}

	/**
	 * @param node
	 * @param start
	 *            insert the given node in the right place
	 * @throws Exception
	 *             if the given node is already in tree
	 */
	private void insert(Node<T> node, Node<T> start) throws Exception {
		while (start != null) {
			if (start.data.equals(node.data))
				throw new Exception("Duplicate node " + start.toString());
			else if (start.data.compareTo(node.data) < 0) {
				if (start.right == null) {
					start.right = node;
					return;
				} else
					start = start.right;
			}

			else {

				if (start.left == null) {
					start.left = node;
					return;
				} else
					start = start.left;
			}
		}
	}

	/**
	 * @param start
	 * @return the minimum node in a subtree whose root = start
	 * @throws Exception
	 *             if the given node == null
	 */
	private Node<T> getMin(Node<T> start) throws Exception {
		if (start == null)
			throw new IllegalArgumentException();
		while (start.left != null)
			start = start.left;
		return start;
	}

	/**
	 * @param start
	 * @param s
	 *            make a postOrder traversal of a subtree starting from the node
	 *            given
	 */
	private void postOrder(Node<T> start, StringBuilder s) {
		if (start == null)
			return;
		postOrder(start.left, s);
		postOrder(start.right, s);
		s.append(start.data.toString());
		s.append(", ");
	}

	/**
	 * @param start
	 * @param s
	 *            make a inOrder traversal of a subtree starting from the node
	 *            given
	 */
	private void inOrder(Node<T> start, StringBuilder s) {
		if (start == null)
			return;
		inOrder(start.left, s);
		s.append(start.data.toString());
		s.append(", ");
		inOrder(start.right, s);

	}

	/**
	 * @param start
	 * @param s
	 *            make a preOrder traversal of a subtree starting from the node
	 *            given
	 */
	private void preOrder(Node<T> start, StringBuilder s) {
		if (start == null)
			return;
		s.append(start.data.toString());
		s.append(", ");
		preOrder(start.left, s);
		preOrder(start.right, s);

	}

	
	/**
	 * @param start
	 * @param n
	 * @return no of levels in a subtree whose root is given 
	 */
	private int countLevels(Node<T> start, int n){
		if (start==null)return n;
		else return Math.max(countLevels(start.right, n+1), countLevels(start.left, n+1));
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		BST<Integer> bt = new BST<Integer>(5);		System.out.println(bt.preOrder());
		bt.add(3);		System.out.println(bt.preOrder());
		bt.add(7);		System.out.println(bt.preOrder());
		bt.add(9);		System.out.println(bt.preOrder());
		bt.add(2);		System.out.println(bt.preOrder());
		bt.add(4);		System.out.println(bt.preOrder());
		
		System.out.println(bt.preOrder());
//		System.out.println(bt.inOrder());
//		System.out.println(bt.postOrder());
		System.out.println();
//		System.out.println(bt.contains(3));
//		System.out.println(bt.preOrder());
//		System.out.println(bt.get(7).getParent(bt.getRoot()).right.data);
		System.out.println(bt.depth());
		
		bt.remove(bt.get(2));
		System.out.println(bt.preOrder());
		
		
	}
	
	
}