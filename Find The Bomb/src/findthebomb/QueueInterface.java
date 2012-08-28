package findthebomb;


public interface QueueInterface<type> {
	public int size(); //return the number of elements stored in the queue
	
	public boolean isEmpty(); // test whether the queue is empty
	
	public type front() // return element at front of Q
	throws QueueEmptyException; // thrown if called on an empty queue
	
	// update methods
	public void enqueue(type element) //insert an element onto the rear of the queue
	throws QueueFullException; //thrown if called on a full queue
	
	public type dequeue() // return and remove the element at the front of the queue
	throws QueueEmptyException; //thrown if called on an empty queue
}
