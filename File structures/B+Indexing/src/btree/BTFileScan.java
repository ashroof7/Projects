package btree;

import java.io.IOException;

import chainexception.ChainException;
import global.Convert;
import global.PageId;
import global.RID;
import global.SystemDefs;
import heap.HFPage;
import heap.Tuple;

public class BTFileScan extends IndexFileScan {

	private RID rid;// current record
	private KeyDataEntry dEntry = null;
	private PageId pageId = null; // current pageId
	private Tuple t;
	BTreeFile bTreeFile;
	private BTLeafPage leaf;// current leaf page 
	HFPage header ;
	private KeyClass upperKey ;
	private KeyDataEntry justScanned ;
	
	public BTFileScan(HFPage header, KeyClass lower, KeyClass upper)
			throws IOException, ChainException {
		
		
		this.header = header ;
		pageId = header.getNextPage(); // root page
		t = header.getRecord(header.firstRecord()) ;
		int keyType  = Convert.getIntValue(0,t.getTupleByteArray()); 
		BTSortedPage page = new BTSortedPage(header.getNextPage(),keyType);
		BTIndexPage indexPage ;

		boolean found = false ;
		PageId left, right = null;

		if (lower == null){
			PageId id2 = new PageId();
			while (true){
				if(page.getType() == NodeType.LEAF)
					break;
				id2 = page.getPrevPage();
				SystemDefs.JavabaseBM.unpinPage(pageId, false);
				pageId = id2;
				SystemDefs.JavabaseBM.pinPage(pageId, page, false);
			}
			leaf = new BTLeafPage(page,keyType);
			dEntry = leaf.getFirst(rid = new RID());
			upperKey = upper;
			return ;
		}
		
		if (upper != null && BT.keyCompare(lower, upper) > 0){
			upperKey = lower;
			lower = upper;
			upper = upperKey ;
		}
		upperKey = upper;
		
		if(page.getType() == NodeType.INDEX){
			indexPage = new BTIndexPage(header.getNextPage(), keyType);
		while (true){
		found = false ;
		dEntry = indexPage.getFirst(rid = new RID());
		left = indexPage.getLeftLink();
		right = ((IndexData)dEntry.data).getData(); 
		
		//inner while
		while (!found) {
			// temp key > lower
			if (BT.keyCompare(dEntry.key, lower) >= 0)
				found = true ; // req key is the right key  
			
			else {
				left = right ;
				dEntry = indexPage.getNext(rid);
				if (dEntry ==null)//no more entries
					break ;
				right = ((IndexData)dEntry.data).getData();
			}
			
			}//while !found
		
		SystemDefs.JavabaseBM.unpinPage(pageId, false);
		pageId = left;
		SystemDefs.JavabaseBM.pinPage(pageId, page, false);
		
		if (page.getType() == NodeType.LEAF)
			break ;
		}//while true
	
	}
		// working node is a leaf node 
		leaf = new BTLeafPage(page, keyType);
		found = false ;
		dEntry = leaf.getFirst(rid = new RID());
		while (!found){
			if (BT.keyCompare(dEntry.key,lower) >= 0)
				break ;
			dEntry = leaf.getNext(rid);
		}
		justScanned = new KeyDataEntry(keyType, rid) ;
	}

	public void delete_current() {
		try {
			leaf.delEntry(dEntry);
		} catch (ChainException | IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public KeyDataEntry get_next() {
		try {
			justScanned = dEntry ;
			if (leaf==null)//out of range 
				return null;
			
			PageId temp = new PageId();
			if (rid == null) {
				// no more records in the page
				temp = leaf.getNextPage();
				SystemDefs.JavabaseBM.unpinPage(pageId, false);
				pageId = temp ;
				SystemDefs.JavabaseBM.pinPage(pageId, leaf, false);
				dEntry = leaf.getFirst(rid = new RID());
			}
			else {
			dEntry = leaf.getNext(rid);
			if (upperKey==null){
					if (rid == null)
						return null;
			}
			else if( BT.keyCompare(dEntry.key, upperKey) > 0){
				leaf = null; // to indicate out of range 
				return null;
			}
			}
		} catch (ChainException | IOException e) {
			e.printStackTrace();
		}

		return justScanned;
	}

	@Override
	public int keysize() {
		try {
			rid = header.firstRecord();
			rid = header.nextRecord(rid);
			t = header.getRecord(rid);
			return  Convert.getIntValue(0,t.getTupleByteArray());
			
		}catch(ChainException  | IOException e){
			e.printStackTrace();
		}
		return -1 ;
	}

	public void DestroyBTreeFileScan() {
		try {
			SystemDefs.JavabaseBM.unpinPage(pageId, false);
			pageId = null;
			this.finalize();
		} catch (Throwable e) {
			System.err
					.println("nothing to unpin...everything is goin pretty well");
		}
	}

}
