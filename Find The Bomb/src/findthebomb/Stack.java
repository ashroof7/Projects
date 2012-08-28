package findthebomb;

public class Stack<type> implements StackInterface<type> {
	SNode<type> top;
	private int size;

	Stack() {
		top = null;
		size = 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return (top == null);
	}

	@Override
	public type peek() throws StackEmptyException {
		if (isEmpty())
			throw new StackEmptyException();
		return top.getContent();
	}

	@Override
	public void push(type obj)  {
		SNode<type> temp = new SNode<type>(obj, top);
		top = temp;
		size++;
	}

	@Override
	public type pop() throws StackEmptyException {
		if (isEmpty())
			throw new StackEmptyException();
		SNode<type> temp = top;
		top = top.getNext();
		temp.setNext(null);
		size--;
		return temp.getContent();
	}

}
