package bufmgr;

import java.util.Iterator;
import java.util.LinkedList;

public class LeastRecentPolicy implements PlacementPolicy {
	private LinkedList<Integer> list;

	/**
	 *	creates a new MostRecentlyUsed Placement policy 
	 */
	public LeastRecentPolicy()
	{
		list = new LinkedList<Integer>();
	}

	/**
	 * @return the frame that will be replaced
	 * @throws BufferPoolExceededException
	 */
	public int getFrame() throws BufferPoolExceededException
	{
		if (list.isEmpty())
			throw new BufferPoolExceededException();
		return list.removeFirst();
	}

	/**
	 * adds a new frame and sets it with normal priority to be a replacement candidate 
	 * @param frame
	 */
	public void addFrame(int frame)
	{
		list.addLast(frame);
	}

	/**
	 * adds a new frame and sets it with highest priority to be a replacement candidate
	 * @param freeFrame
	 */
	public void addFreeFrame(int freeFrame)
	{
		removeCandidate(freeFrame);
		list.addFirst(freeFrame);
	}

	/**
	 * removes the referenced frame from replacement candidates
	 * @param page
	 * page index in the pool
	 */
	public void removeCandidate(int page)
	{
		Iterator<Integer> itr = list.iterator();
		while (itr.hasNext())
		{
			if (itr.next() == page)
			{
				itr.remove();
				break;
			}
		}
	}

	/**
	 * @return number of replacement candidates
	 */
	public int getNumCandidates()
	{
		return list.size();
	}
}
