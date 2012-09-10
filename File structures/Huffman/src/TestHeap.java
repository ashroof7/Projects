
public class TestHeap {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MinHeap<Integer> h = new MinHeap<Integer>();
		h.add(10);
		h.add(20);
		h.add(30);
		h.add(22);
		h.add(39);
		h.add(31);
		h.add(32);
		h.add(25);
		h.add(100);
		h.add(50);
		h.add(60);
					
		System.out.println(h.toString());
		h.remove(10);
		System.out.println(h.toString());
				
	}

}
