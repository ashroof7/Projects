import java.util.ArrayList;

public class MinHeap<E extends Comparable<? super E>> {

	private ArrayList<E> heap;
	private int top;

	/**
	 * create a MinHeap with the default size of ArrayList
	 */
	public MinHeap() {
		heap = new ArrayList<E>();
	}

	/**
	 * @param max
	 *            create a MinHeap with the max size
	 */
	public MinHeap(int max) {
		heap = new ArrayList<E>(max);
	}

	/**
	 * @param element
	 *            insert a new node then adjust the heap
	 */
	public void add(E element) {
		heap.add(element);
		int parent = (top - 1) / 2;
		int current = top;
		E temp;

		while (parent >= 0 && element.compareTo(heap.get(parent)) < 0) {
			temp = heap.get(current);
			heap.set(current, heap.get(parent));
			heap.set(parent, temp);
			current = parent;
			parent = (parent - 1) / 2;
		}
		top++;
	};

	/**
	 * @param element
	 *            deletes the all the nodes containing the given element then
	 *            reheapify the heap
	 */
	public void remove(E element) {
		int i;

		while ((i = search(element)) >= 0) {
			heap.set(i, heap.get(--top));
			heap.remove(top);
			heapify(i);
		}

	}

	/**
	 * @return the root of the tree (minimum element)
	 */
	public E getMin() {
		E temp = heap.get(0);
		heap.set(0, heap.get(--top));
		heap.remove(top);
		heapify(0);
		return temp;
	}

	/**
	 * @return true if the heap is empty false otherwise
	 */
	public boolean isEmpty(){
		return top==0;
	}
	
	/**
	 * @return the root of the heap without removing it from the heap
	 */
	public E peek(){
		return heap.get(0);
	}
	
	/**
	 * @return the size of the heap
	 */
	public int size() {
		return top;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return heap.toString();
	}

	
	/**
	 * @param i
	 *            heapify the sub tree whose root is at index i in the heap
	 */
	private void heapify(int i) {
		int minIndex;
		E temp;

		while ((i + 1) * 2 - 1 < top) {

			if ((i + 1) * 2 < top) {
				minIndex = heap.get((i + 1) * 2 - 1).compareTo(
						heap.get((i + 1) * 2)) < 0 ? (i + 1) * 2 - 1
						: (i + 1) * 2;

			} else if ((i + 1) * 2 - 1 < top) {
				minIndex = (i + 1) * 2 - 1;
			} else
				break;
			temp = heap.get(minIndex);
			heap.set(minIndex, heap.get(i));
			heap.set(i, temp);
			i = minIndex;
		}
	}

	/**
	 * @param element
	 * @return the index of the node -1 otherwise
	 */
	private int search(E element) {
		for (int i = 0; i < heap.size(); i++) {
			if (heap.get(i).equals(element))
				return i;
		}
		return -1;
	}

	

}
