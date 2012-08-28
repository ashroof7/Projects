package findthebomb;


public interface  StackInterface<type> {

	// accessor methods
		public int size(); // return the number of elements stored in the stack
		
		public boolean isEmpty(); // test whether the stack is empty
		
		public type peek() // return the top element
		throws StackEmptyException; // thrown if called on an empty stack
		// update methods
		
		public void push (type obj) // insert an element onto the stack
		throws StackFullException; // thrown if overflow
		
		public type pop() // return and remove the top element of the stack
		throws StackEmptyException; // thrown if called on an empty stack
}
