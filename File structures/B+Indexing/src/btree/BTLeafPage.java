package btree;

import java.io.IOException;

import diskmgr.Page;
import global.PageId;
import global.RID;
import heap.InvalidSlotNumberException;
import heap.Tuple;

public class BTLeafPage extends BTSortedPage {
	
	public BTLeafPage(PageId pageno,int keyType) throws ConstructPageException, IOException
	{
		super(pageno, keyType);
		this.setType(NodeType.LEAF);
	}
	
	public BTLeafPage(Page page,int keyType) throws IOException
	{
		super(page, keyType);
		this.setType(NodeType.LEAF);
	}
	
	 public BTLeafPage(int keyType) throws ConstructPageException, IOException
	 {
		 super(keyType);
		 this.setType(NodeType.LEAF);
	 }
	 
	 public RID insertRecord(KeyClass key , RID dataRid) throws InsertRecException
	 {
		return super.insertRecord(new KeyDataEntry(key,dataRid));
//		 LeafData leafData = new LeafData(dataRid);
//
//		    KeyDataEntry keyDataEntry = new KeyDataEntry(key, leafData);
//		    RID newRid = null;
//		  
//		  newRid = insertRecord(keyDataEntry);
//
//		  return newRid;
		 
	 }
	 
	 public KeyDataEntry getFirst(RID rid) throws KeyNotMatchException, NodeNotMatchException, ConvertException, IOException, InvalidSlotNumberException
	 {
			RID rid1 = super.firstRecord();
			if(rid1==null)
				return null;
			rid.pageNo = rid1.pageNo;
			rid.slotNo = rid1.slotNo;
			Tuple t = super.getRecord(rid1);
			return BT.getEntryFromBytes(t.getTupleByteArray(), t.getOffset(),t.getLength(), this.keyType,NodeType.LEAF);
	 }
	 
	 public KeyDataEntry getNext(RID rid) throws IOException, InvalidSlotNumberException, KeyNotMatchException, NodeNotMatchException, ConvertException
	 {
		 	RID rid1 = super.nextRecord(rid);
		 	if(rid1==null)
				return null;
		 	rid.pageNo = rid1.pageNo;
			rid.slotNo = rid1.slotNo;
			Tuple t = super.getRecord(rid1);
			return BT.getEntryFromBytes(t.getTupleByteArray(), t.getOffset(),t.getLength(), this.keyType,NodeType.LEAF);
	 }
	 
	 public KeyDataEntry getCurrent(RID rid) throws InvalidSlotNumberException, IOException, KeyNotMatchException, NodeNotMatchException, ConvertException
	 {
		 	
		 	Tuple t = super.getRecord(rid);
		 	if(t == null)
		 		return null;
			return BT.getEntryFromBytes(t.getTupleByteArray(), t.getOffset(),t.getLength(), this.keyType,NodeType.LEAF);		 
	 }
	 
	 public boolean delEntry(KeyDataEntry dEntry) throws IOException, InvalidSlotNumberException, KeyNotMatchException, NodeNotMatchException, ConvertException, DeleteRecException
	 {
		 RID rid = super.firstRecord();
		 Tuple t;
		 KeyDataEntry test ;
		 int comp = 0;
		 boolean check = false ;
		 
		 while(true)
		 {
			 if(rid == null)
				 return false;
			 t = super.getRecord(rid);
			 test = BT.getEntryFromBytes(t.getTupleByteArray(), t.getOffset(), t.getLength(), this.keyType, NodeType.LEAF);
			 comp = BT.keyCompare(test.key, dEntry.key);
//			 if(comp<0)
//				 return false;
			 if(comp == 0)
				 break;
			 rid = super.nextRecord(rid);
		 }
		 check = super.deleteSortedRecord(rid);
		 if(check)
			 return true;
		 else
		     return false;
		 
	 }
}



