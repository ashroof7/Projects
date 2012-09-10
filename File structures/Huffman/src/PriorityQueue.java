public class PriorityQueue<T extends Comparable<? super T>>  {

	private MinHeap<T> heap;
	
	public PriorityQueue() {
		heap = new MinHeap<T>();
	}
	
	public void add(T node){
		heap.add(node);
	}
	
	public T poll(){
		return heap.getMin();
	}
	
	public boolean isEmpty(){
		return heap.isEmpty();
	}
	
	public T peek(){
		return heap.peek();
	}
	
	public int size(){
		return heap.size();
	}


	public static void main(String[] args) {

	}

}
