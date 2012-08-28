package findthebomb;

public class QueueArray<type> implements QueueInterface<type> {
	private final static int defaultCapacity = 1000;
	private int capacity;
	private type array[];
	int front;
	int rear;
	int n=0;

	QueueArray() {
		this(defaultCapacity);
	}

	@SuppressWarnings("unchecked")
	QueueArray(int x) {
		capacity = x;
		array = (type[]) new Object[capacity];
		n=array.length;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return ((n - front + rear) % n);
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return (front == rear);
	}

	@Override
	public type front() throws QueueEmptyException {
		// TODO Auto-generated method stub
		return array[front];
	}

	@Override
	public void enqueue(type element) throws QueueFullException {
		// TODO Auto-generated method stub
		if (size()==n-1)//array is full
			throw new QueueFullException();
		array[rear]=element;
		rear=(rear+1)%n;
	}

	@Override
	public type dequeue() throws QueueEmptyException {
		// TODO Auto-generated method stub
		if (isEmpty())
			throw new QueueEmptyException();
		type temp = array[front];
		array[front] = null;
		front = (front + 1) % n;
		return temp;
	}

}
