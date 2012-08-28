package findthebomb;


public class QueueEmptyException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	QueueEmptyException(){
		super("Queue is Empty");
	}

}
