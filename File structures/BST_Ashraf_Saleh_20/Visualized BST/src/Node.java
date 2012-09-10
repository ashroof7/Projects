	/**
	 * Node Class that is used to construct the tree
	 * 
	 * @param <T>
	 */
//	@SuppressWarnings("hiding")
	public  class Node<T extends Comparable<? super T>> {
		T data;
		Node<T> right;
		Node<T> left;

		Node(T data) {
			this.data = data;
			right = left = null;
		}

		public Node<T> getParent(Node<T> start) {
			if (start == this )return null;
			
			
			while (start != null) {
				if (data.compareTo(start.data) >= 0) {
					if (start.right == this)
						return start;
					else
						start = start.right;
				} else {

					if (start.left == this)
						return start;
					else
						start = start.left;
				}

			}
			return null;
		}

	}
	