package btree;

import java.io.IOException;

import diskmgr.Page;
import global.PageId;
import global.RID;
import heap.InvalidSlotNumberException;
import heap.Tuple;

public class BTIndexPage extends BTSortedPage {

	 public BTIndexPage(PageId pageno,int keyType) throws ConstructPageException, IOException
	 {
		 super(pageno, keyType);
		 setType(NodeType.INDEX);
	 }
	 
	 public BTIndexPage(Page page,int keyType) throws IOException
	 {
		 super(page, keyType);
		 setType(NodeType.INDEX);
	 }
	 
	 public BTIndexPage(int keyType) throws ConstructPageException, IOException
	 {
		 super(keyType);
		 setType(NodeType.INDEX);
	 }
	 
	 
	 
	 public RID insertKey(KeyClass key,  PageId pageNo) throws InsertRecException{
		return super.insertRecord(new KeyDataEntry(key, pageNo));
	 }

	 
	 public PageId getPageNoByKey(KeyClass key)
	 {
		 System.err.println("Ashraf: What page ?");
		 return null;
	 }
	 
	 public KeyDataEntry getFirst(RID rid) throws IOException, KeyNotMatchException, NodeNotMatchException, ConvertException, InvalidSlotNumberException
	 {
		RID r = super.firstRecord();
		if (r == null)
			return null;
		rid.slotNo = r.slotNo;
		rid.pageNo.pid = r.pageNo.pid;
		
			Tuple t = super.getRecord(r);
			return BT.getEntryFromBytes(t.getTupleByteArray(), t.getOffset(), t.getLength(), keyType, NodeType.INDEX);
	 }
	 
	 public KeyDataEntry getNext(RID rid) throws IOException, KeyNotMatchException, NodeNotMatchException, ConvertException, InvalidSlotNumberException
	 {
		//try rid = super.nextRecord(rid);
		RID rid1 = super.nextRecord(rid);
		if (rid1 == null)
			return null;
		rid.pageNo = rid1.pageNo;
		rid.slotNo = rid1.slotNo;
		Tuple t = super.getRecord(rid);
		return BT.getEntryFromBytes(t.getTupleByteArray(), t.getOffset(), t.getLength(), keyType, NodeType.INDEX);
	 }
	
	 public PageId getLeftLink() throws IOException
	 {
		return getPrevPage();  
	 }
	 
	 public void setLeftLink(PageId left) throws IOException
	 {
		 setPrevPage(left);
	 }

	
}