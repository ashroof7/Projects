import java.util.Arrays;
import java.util.Collection;


public class HeapSort <E extends Comparable<? super E>> {

	/**
	 * @param args
	 */
	
	E[] input ; 
	MinHeap<E> heap ;
	Collection<E> collect;
	
	
	
	/**
	 * @param indata
	 * create new sorter to sort an Array
	 */
	public HeapSort(E[] indata){
		input = indata;
		heap = new MinHeap<E>(indata.length);
	}
	
	/**
	 * @param indata
	 * create new sorter to sort a Collection
	 */
	public HeapSort(Collection<E> indata){
		collect = indata;
		heap = new MinHeap<E>(indata.size());
	}
	
	/**
	 * @return sorted Collection
	 * sorts a Collection 
	 */
	public Collection<E> CollectionSort(){
		for (E element : collect) 
			heap.add(element);
		collect.clear();
				for (E element : collect) 
					collect.add(element);
		return collect;
	}
	
	/**
	 * @return sorted Array
	 * sorts an Array
	 */
	public E[] ArraySort(){
		for (int i = 0; i < input.length; i++) 
			heap.add(input[i]);
		for (int i = 0; i < input.length; i++) 
			input[i] = heap.getMin();
	return input;
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Integer [] a = {4,5,8,2,4,7,9};
		HeapSort<Integer> sorter = new HeapSort<Integer>(a);
		Integer[] res = sorter.ArraySort();
		System.out.println(Arrays.toString(res));
	}

}
