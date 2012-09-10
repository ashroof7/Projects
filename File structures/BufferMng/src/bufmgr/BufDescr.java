package bufmgr;

import global.PageId;

public class BufDescr {
	private int pinCount;
	private boolean dirtyBit;
	private PageId number;

	/**
	 * creates new buffer Descriptor with pageId = null and pinCount = 0
	 * @param number
	 * @param pin
	 */
	
	public BufDescr(PageId number)
	{
		 this(number , 0);
	}
	
	/**
	 * creates new buffer Descriptor with pageId = number and pinCount = pin
	 * @param number
	 * @param pin
	 */
	public BufDescr(PageId number , int pin)
	{
		 pinCount = pin;
		 dirtyBit = false;
		this.number = number;
	}

	/**
	 * @return pinCount 
	 */
	public int getPinCount()
	{
		return pinCount;
	}

	/**
	 * sets pinCount with the given value
	 * @param pinCount
	 */
	public void setPinCount(int pinCount)
	{
		this.pinCount = pinCount;
	}

	
	/**
	 * @return true if page is dirty false otherwise
	 */
	public boolean isDirty()
	{
		return dirtyBit;
	}

	/**
	 * sets the dirtyBit
	 * @param dirtyBit
	 */
	public void setDirtyBit(boolean dirtyBit)
	{
		this.dirtyBit = dirtyBit;
	}

	/**
	 * @return pageId
	 */
	public PageId getId()
	{
		return number;
	}

	/**
	 * sets the pageId
	 * @param number
	 */
	public void setId(PageId number)
	{
		this.number = number;
	}
	
	/**
	 *  decrements the pin count and return the pin count became a zero 
	 * @return pin_count == 0
	 */
	public boolean decreamentPinCount()
	{
		return --pinCount == 0;
	}
	/**
	 * increments the pin count and return if it was a zero or not
	 * to remove it from Placement Policy
	 */
	public boolean increamentPinCount()
	{
		pinCount++;
		return pinCount == 1;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return number+" "+pinCount+" "+dirtyBit;
	}
}
