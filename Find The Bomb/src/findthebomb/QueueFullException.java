package findthebomb;


public class QueueFullException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QueueFullException() {
		super("Queue is Full");
	}
}
