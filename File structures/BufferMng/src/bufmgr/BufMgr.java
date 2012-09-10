package bufmgr;

import global.GlobalConst;
import global.PageId;
import global.SystemDefs;
import java.io.IOException;
import chainexception.ChainException;
import diskmgr.Page;

public class BufMgr {
	private byte[][] bufPool;
	private int bufSize;
	private int used;
	private BufDescr[] bufDescr; 
	private HashTable<Integer, Integer> hash; // bufmgr.HashTable 
	private PlacementPolicy placementPolicy;

	/**
	 * Create the BufMgr object. Allocate pages (frames) for the buffer pool in
	 * main memory and make the buffer manage aware that the replacement policy
	 * is specified by replacerArg (i.e. Clock, LRU, MRU etc.).
	 * 
	 * @param numbufs
	 *            number of buffers in the buffer pool.
	 * @param replacerArg
	 *            name of the buffer replacement policy.
	 */

	public BufMgr(int numbufs, String replacementArg) {
		bufSize = numbufs;
		used = 0;		
		PlacementFactory factory = new PlacementFactory();
		placementPolicy = factory.createPolicy(replacementArg);
		bufPool = new byte[bufSize][GlobalConst.MINIBASE_PAGESIZE];
		bufDescr = new BufDescr[bufSize];
		hash = new HashTable<Integer, Integer>(bufSize);
	}

	/**
	 * Pin a page. First check if this page is already in the buffer pool. If it
	 * is, increment the pin_count and return a pointer to this page. If the
	 * pin_count was 0 before the call, the page was a replacement candidate,
	 * but is no longer a candidate. If the page is not in the pool, choose a
	 * frame (from the set of replacement candidates) to hold this page, read
	 * the page (using the appropriate method from {\em diskmgr} package) and
	 * pin it. Also, must write out the old page in chosen frame if it is dirty
	 * before reading new page. (You can assume that emptyPage==false for this
	 * assignment.)
	 * 
	 * @param Page_Id_in_a_DB
	 *            page number in the minibase.
	 * @param page
	 *            the pointer poit to the page.
	 * @param emptyPage
	 *            true (empty page); false (non-empty page)
	 */

	public void pinPage(PageId pin_pgid, Page page, boolean empty)
			throws ChainException {

		Integer index = hash.get(pin_pgid.pid);
		if (index != null) {
			// increment pincount
			boolean wasZero = bufDescr[index].increamentPinCount();

			if (wasZero)
				placementPolicy.removeCandidate(index);
			page.setpage(bufPool[index]);

		} else

		{
			int insertPos;
			if (used < bufSize) {
				insertPos = used++;
			} else {
				insertPos = placementPolicy.getFrame();

				// Free old page from frame , because it may be dirty and have
				// to be written to disk
				if (bufDescr[insertPos] != null) {
					PageId id = bufDescr[insertPos].getId();
					freePage(id);
					placementPolicy.removeCandidate(insertPos);
				}
			}
			page.setpage(bufPool[insertPos]);
			try {
				SystemDefs.JavabaseDB.read_page(pin_pgid, page);
			} catch (IOException e) {
				throw new ChainException(e, "DB.java: read_page() failed");
			}
			PageId id = new PageId(pin_pgid.pid);
			bufDescr[insertPos] = new BufDescr(id, 1);
			hash.put(id.pid, insertPos);
		}

	}

	/**
	 * Unpin a page specified by a pageId. This method should be called with
	 * dirty==true if the client has modified the page. If so, this call should
	 * set the dirty bit for this frame. Further, if pin_count>0, this method
	 * should decrement it. If pin_count=0 before this call, throw an exception
	 * to report error. (For testing purposes, we ask you to throw an exception
	 * named PageUnpinnedException in case of error.)
	 * 
	 * @param globalPageId_in_a_DB
	 *            page number in the minibase.
	 * @param dirty
	 *            the dirty bit of the frame
	 */

	public void unpinPage(PageId PageID_in_a_DB, boolean dirty)
			throws ChainException {
		Integer index = hash.get(PageID_in_a_DB.pid);

		if (index == null)// Checking valid pageId
			throw new HashEntryNotFoundException();

		if (bufDescr[index].getPinCount() == 0)
			throw new PageUnpinnedException();

		if (dirty)
			bufDescr[index].setDirtyBit(dirty);

		boolean isZero = bufDescr[index].decreamentPinCount();
		if (isZero) {
			placementPolicy.addFrame(index);
		}
	}

	/**
	 * Allocate new pages. Call DB object to allocate a run of new pages and
	 * find a frame in the buffer pool for the first page and pin it. (This call
	 * allows a client of the Buffer Manager to allocate pages on disk.) If
	 * buffer is full, i.e., you can’t find a frame for the first page, ask DB
	 * to deallocate all these pages, and return null.
	 * 
	 * @param firstpage
	 *            the address of the first page.
	 * @param howmany
	 *            total number of allocated new pages.
	 * 
	 * @return the first page id of the new pages. null, if error.
	 */

	public PageId newPage(Page firstpage, int howmany) throws IOException,
			ChainException {

		PageId pid = new PageId();
		SystemDefs.JavabaseDB.allocate_page(pid, howmany);

		pinPage(pid, firstpage, false);
		return pid;
		/*
		 * keep for further development 
		 * 
		 * Integer index = hash.get(pid.pid); if (index != null) {
		 * firstpage.setpage(bufpool[index]);
		 * bufDescr[index].increamentPinCount();
		 * placement_policy.removeCandidate(index); return pid; }
		 * System.err.println(
		 * "####NO empty frame was found will deallocate them form DB and return null####"
		 * ); // NO empty frame was found will deallocate them form DB and
		 * return null SystemDefs.JavabaseDB.deallocate_page(pid, howmany);
		 * return null;
		 */
	}

	/**
	 * This method should be called to delete a page that is on disk. This
	 * routine must call the method in diskmgr package to deallocate the page.
	 * 
	 * @param globalPageId
	 *            the page number in the data base.
	 */

	public void freePage(PageId globalPageId) throws ChainException {
		Integer index = hash.get(globalPageId.pid);
		if (index != null) {
			if (bufDescr[index].getPinCount() > 1)
				throw new PagePinnedException();
			try {
				flushPage(globalPageId);
				placementPolicy.addFreeFrame(index);
				bufDescr[index] = null;
				hash.remove(globalPageId.pid);
				SystemDefs.JavabaseDB.deallocate_page(globalPageId);
				hash.remove(globalPageId.pid);
			} catch (IOException e) {
				throw new ChainException(e, "DB.java: deallocate_page() failed");
			}
		}
	}

	/**
	 * Used to flush a particular page of the buffer pool to disk. This method
	 * calls the write_page method of the diskmgr package.
	 * 
	 * @param pageid
	 *            the page number in the database.
	 */

	public void flushPage(PageId pageid) throws ChainException {
		Integer index = hash.get(pageid.pid);
		if (index != null) {
			if (bufDescr[index].isDirty())
				try {
					SystemDefs.JavabaseDB.write_page(pageid, new Page(
							bufPool[index]));
				} catch (Exception e) {
					throw new ChainException(e, "DB.java: write_page() failed");
				}
		} else
			throw new HashEntryNotFoundException();
	}

	/**
	 * used to get the number of the unpinned frames
	 */

	public int getNumUnpinnedBuffers() {
		return placementPolicy.getNumCandidates() + bufSize - used;
	}

	/**
	 * Used to flush all pages in the buffer pool to disk. This method calls
	 * flushPage for each page in the bufferPool
	 */

	public void flushAllPages() throws ChainException {
		for (BufDescr desc : bufDescr) {
			if (desc != null)
				flushPage(desc.getId());
		}
	}

}
