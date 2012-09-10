package bufmgr;

public interface PlacementPolicy {
	
	/**
	 * @return the frame that will be replaced
	 * @throws BufferPoolExceededException
	 */
	public int getFrame() throws BufferPoolExceededException;
	
	/**
	 * adds a new frame and sets it with normal priority to be a replacement candidate 
	 * @param frame
	 */
	public void addFrame(int frame);


	/**
	 * adds a new frame and sets it with highest priority to be a replacement candidate
	 * @param freeFrame
	 */
	public void addFreeFrame(int freeFrame);

	/**
	 * @return number of replacement candidates
	 */
	public int getNumCandidates();

	/**
	 * removes the referenced frame from replacement candidates
	 * @param page
	 * page index in the pool
	 */
	public void removeCandidate(int page);
}
