package findthebomb;

public class StackFullException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	StackFullException(){
		super("Stack Is Full");
	}
}