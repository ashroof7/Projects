package findthebomb;


public class StackEmptyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	StackEmptyException(){
		super("Stack Is Empty");
	}
}

