package btree;

import java.io.IOException;
import java.util.Arrays;

import chainexception.ChainException;

import bufmgr.BufMgrException;
import bufmgr.BufferPoolExceededException;
import bufmgr.HashEntryNotFoundException;
import bufmgr.HashOperationException;
import bufmgr.InvalidFrameNumberException;
import bufmgr.PageNotReadException;
import bufmgr.PagePinnedException;
import bufmgr.PageUnpinnedException;
import bufmgr.ReplacerException;

import global.Convert;
import global.GlobalConst;
import global.PageId;
import global.RID;
import global.SystemDefs;
import heap.HFPage;
import heap.InvalidSlotNumberException;
import heap.Tuple;
import diskmgr.DiskMgrException;
import diskmgr.DuplicateEntryException;
import diskmgr.FileEntryNotFoundException;
import diskmgr.FileIOException;
import diskmgr.FileNameTooLongException;
import diskmgr.InvalidPageNumberException;
import diskmgr.InvalidRunSizeException;
import diskmgr.OutOfSpaceException;
import diskmgr.Page;

public class BTreeFile extends IndexFile implements GlobalConst{

	private	HFPage header;
	private String fileName;
	private int keyType;
	private int keySize;
	private int delete_fashion;
	
	 public BTreeFile(String filename,int keytype,int keysize,int delete_fashion) throws FileIOException, InvalidPageNumberException, DiskMgrException, IOException, ReplacerException, HashOperationException, PageUnpinnedException, InvalidFrameNumberException, PageNotReadException, BufferPoolExceededException, PagePinnedException, BufMgrException, HashEntryNotFoundException, FileNameTooLongException, InvalidRunSizeException, DuplicateEntryException, OutOfSpaceException, InvalidSlotNumberException
	 {
		 	this.fileName = filename;
			PageId headerId = SystemDefs.JavabaseDB.get_file_entry(filename);
			Page headData = new Page();
			if(headerId == null)
			{
				headerId = SystemDefs.JavabaseBM.newPage(headData, 1);
				SystemDefs.JavabaseDB.add_file_entry(filename, headerId);
				header = new HFPage();
				header.init(headerId, headData);
				// adding info to the header page
				
				
//				Page page = new Page();
				BTLeafPage root = null;
				try {
					root = new BTLeafPage(this.keyType);
				} catch (ConstructPageException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				SystemDefs.JavabaseBM.pinPage(rootId, root, false);
//				BTLeafPage root = new BTLeafPage(page,this.keyType);
//				root.setCurPage(rootId);
				header.setNextPage(root.getCurPage());
				
				System.err.println(root.getType());
				
				header.setType(NodeType.BTHEAD);
				
				this.keySize = keysize;
				this.keyType = keytype;
				this.delete_fashion = delete_fashion;
				
				byte [] rec1 = new byte[4];
				Convert.setIntValue(keytype, 0, rec1);
				byte [] rec2 = new byte[4];
				Convert.setIntValue(keysize, 0, rec2);
				byte [] rec3 = new byte[4];
				Convert.setIntValue(delete_fashion, 0, rec3);
				header.insertRecord(rec1);
				header.insertRecord(rec2);
				header.insertRecord(rec3);
				// insert keytype & keysize.
			}
			else
			{
			header = new HFPage ();
			SystemDefs.JavabaseBM.pinPage(headerId, header, false);
			// complete root and rest of info extraction
			RID rid = header.firstRecord();
			Tuple t = header.getRecord(rid);
			this.keyType = Convert.getIntValue(0,t.getTupleByteArray());
			rid = header.nextRecord(rid);
			t = header.getRecord(rid);
			this.keySize = Convert.getIntValue(0,t.getTupleByteArray());
			rid = header.nextRecord(rid);
			t = header.getRecord(rid);
			this.delete_fashion = Convert.getIntValue(0,t.getTupleByteArray());
			}
			header.setCurPage(headerId);
		 
	 }

	public BTreeFile(String filename) throws FileIOException, InvalidPageNumberException, DiskMgrException, IOException, ReplacerException, HashOperationException, PageUnpinnedException, InvalidFrameNumberException, PageNotReadException, BufferPoolExceededException, PagePinnedException, BufMgrException, InvalidSlotNumberException {
		this.fileName = filename;
		PageId headerId = SystemDefs.JavabaseDB.get_file_entry(filename);
		if(headerId == null)
			// error not found in database;
			return;
		else
		{
		header = new HFPage ();
		SystemDefs.JavabaseBM.pinPage(headerId, header, false);
//		header.init(headerId, headData);
		// complete root and rest of info extraction
		RID rid = header.firstRecord();
		Tuple t = header.getRecord(rid);
		this.keyType = Convert.getIntValue(0,t.getTupleByteArray());
		rid = header.nextRecord(rid);
		t = header.getRecord(rid);
		this.keySize = Convert.getIntValue(0,t.getTupleByteArray());
		rid = header.nextRecord(rid);
		t = header.getRecord(rid);
		this.delete_fashion = Convert.getIntValue(0,t.getTupleByteArray());

		}
		}

	public HFPage getHeaderPage() {
		return header;
	}
	
	private PageId getLeafthatContains(PageId nodeId,KeyClass key)
	{
		BTSortedPage currs = null;
		try {
			currs = new BTSortedPage(nodeId,this.keyType);
		} catch (ConstructPageException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			if(currs.getType()==NodeType.LEAF)
			{
				SystemDefs.JavabaseBM.unpinPage(nodeId, false);
				return nodeId;
			}
			else
			{
				BTIndexPage n = new BTIndexPage(currs,this.keyType);
				PageId left = n.getLeftLink();
				RID rid1 = new RID();
				KeyDataEntry curre = n.getFirst(rid1);
				KeyClass currk = curre.key;
				PageId right = ((IndexData)curre.data).getData();
				boolean found = false;
				
				// search for the suitable page in the tree through the directing keys
				while (!found)
				{
					
					if(BT.keyCompare(key, currk)<0)
					{
						found = true;
					}
					else
					{
						left = right;
						curre = n.getNext(rid1);
						if(curre!=null)
						{
						currk = curre.key;
						right = ((IndexData)curre.data).getData();
						} 
						else 
						break;
					}
				}
				SystemDefs.JavabaseBM.unpinPage(nodeId, false);
				return getLeafthatContains(left, key);
			}
		} catch (IOException | ReplacerException | PageUnpinnedException | HashEntryNotFoundException | InvalidFrameNumberException | KeyNotMatchException | NodeNotMatchException | ConvertException | InvalidSlotNumberException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public boolean Delete(KeyClass key, RID rid) {
		
		try {
			PageId leafId = getLeafthatContains(header.getNextPage(), key);
			BTLeafPage data = null;
			try {
				data = new BTLeafPage(leafId,this.keyType);
			} catch (ConstructPageException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			boolean check = false;
			try {
				check = data.delEntry(new KeyDataEntry(key,rid));
			} catch (InvalidSlotNumberException | KeyNotMatchException
					| NodeNotMatchException | ConvertException
					| DeleteRecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SystemDefs.JavabaseBM.unpinPage(leafId, check);
			return check;
		} catch (IOException | ReplacerException |PageUnpinnedException | InvalidFrameNumberException |   HashEntryNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
	}

	private void insertRec(PageId nodeId , KeyClass key , RID rid , KeyDataEntry newChildEntry) throws ChainException, IOException
	{		
		
		
	
		
		BTSortedPage sp = new BTSortedPage(nodeId,keyType);
		//		sp.init(nodeId,p);

			// check for being leaf node
			if(sp.getType()==NodeType.LEAF)
			{
					BTLeafPage l = new BTLeafPage(sp,sp.keyType);
				
				KeyDataEntry ent = new KeyDataEntry(key,rid);
				byte [] entry= BT.getBytesFromEntry(ent);
				// if it has space
//				System.out.println(l.insertRecord(key, rid)); 3'alta ostoreya 
				if(l.insertRecord(key, rid)!=null)
				{					
					SystemDefs.JavabaseBM.unpinPage(nodeId, true);
					newChildEntry.data = null;
					newChildEntry.key = null;
					return;
				}
				// doesn't have space
				else
				{
					// split
					int numRec = l.numberOfRecords();
					
					// getting the middle record
					RID rid2 = l.firstRecord();
					int i = 0;
					for(i = 0;i<numRec/2;i++)
					{
						rid2 = l.nextRecord(rid2);						
					}
					
					Tuple t = l.getRecord(rid2);
					KeyDataEntry ch = BT.getEntryFromBytes(t.getTupleByteArray(), t.getOffset(), t.getLength(), this.keyType, NodeType.LEAF);
					
					boolean check = BT.keyCompare(key, ch.key) < 0 ;
					
					// new page to add splited data
					
					BTLeafPage sibling = new BTLeafPage(keyType);
					
					
					// linking pages
					sibling.setPrevPage(nodeId);
					l.setNextPage(sibling.getCurPage());
					
					RID rid3 = new RID ();
					rid3.pageNo.pid = rid2.pageNo.pid;
					rid3.slotNo = rid2.slotNo;
					
					// adding data to the new page
					while(rid2!=null)
					{						
						t = l.getRecord(rid2);
						ch = BT.getEntryFromBytes(t.getTupleByteArray(), t.getOffset(), t.getLength(),this.keyType,NodeType.LEAF);
//						l.deleteSortedRecord(rid2);
						sibling.insertRecord(ch);
						rid2 = l.nextRecord(rid2);
					}
					
					while(t!=null)
					{						
						
						ch = BT.getEntryFromBytes(t.getTupleByteArray(), t.getOffset(), t.getLength(),this.keyType,NodeType.LEAF);
						l.delEntry(ch);
//						l.deleteRecord(rid3);
//						sibling.insertRecord(ch);
//						rid3 = l.nextRecord(rid3);
						try{
						t = l.getRecord(rid3);
						}catch(Exception e)
						{
							break;
						}
					}	

					
					// where to put the given record
					if(check)
					l.insertRecord(key, rid);
					else
					sibling.insertRecord(key, rid);
					
					sibling.setNextPage(new PageId(-1));
				// copying up the first record
					rid2 = new RID();
				ch = sibling.getFirst(rid2);
				SystemDefs.JavabaseBM.unpinPage(sibling.getCurPage(), true);
				SystemDefs.JavabaseBM.unpinPage(nodeId, true);
				newChildEntry .key = ch.key;
				newChildEntry.data =  new IndexData(sibling.getCurPage());
					
				System.out.println("number of records in orig : "+l.numberOfRecords());
				System.out.println("number of records in new : "+sibling.numberOfRecords());
				
				// if the root is the page splitted
				if (nodeId.pid==header.getNextPage().pid)
				{
					
					BTIndexPage f = new BTIndexPage(this.keyType);
					f.setLeftLink(nodeId);
					f.insertRecord(newChildEntry);
//					System.out.println("slotno : "+f.firstRecord().slotNo+" pid : "+f.firstRecord().pageNo.pid);
//					RID rid4 = new RID(); 
//					f.getFirst(rid4);
					header.setNextPage(f.getCurPage());
					SystemDefs.JavabaseBM.unpinPage(f.getCurPage(), true);
					newChildEntry.data = null;
					newChildEntry.key = null;
				}
				BT.printBTree(header);
				return;
				}
					
			}
				// if index page
				else
			{
				BTIndexPage n = new BTIndexPage(sp,sp.keyType);

				PageId left = n.getLeftLink();
				RID rid1 = new RID();
				KeyDataEntry curr = n.getFirst(rid1);
				KeyClass currk = curr.key;
				PageId right = ((IndexData)curr.data).getData();
				boolean found = false;
				
				// search for the suitable page in the tree through the directing keys
				while (!found)
				{
					
					if(BT.keyCompare(key, currk)<0)
					{
						found = true;
					}
					else
					{
						left = right;
						curr = n.getNext(rid1);
						if(curr!=null)
						{
						currk = curr.key;
						right = ((IndexData)curr.data).getData();
						}
						else
						{
							break;
						}
					}
				}
					// insert recursively
				insertRec(left, key, rid1, newChildEntry);
					
				if(newChildEntry.key != null)
				{
						// add new index
						
						byte [] rec = BT.getBytesFromEntry(newChildEntry);
						// check space
						if(n.insertRecord(newChildEntry)!=null)
						{
							SystemDefs.JavabaseBM.unpinPage(nodeId, true);
							newChildEntry.data = null;
							newChildEntry.key = null;
							return;
						}
							else
						{
								//	split index
								int numRec = n.numberOfRecords();
								
								// get middle record
								RID rid2 = n.firstRecord();
								int i = 0;
								for(i = 0;i<numRec/2;i++)
								{
									rid2 = n.nextRecord(rid2);						
								}
								
								Tuple t = n.getRecord(rid2);
								KeyDataEntry ch = BT.getEntryFromBytes(t.getTupleByteArray(), t.getOffset(), t.getLength(), this.keyType, NodeType.LEAF);
								
								boolean check = BT.keyCompare(key, ch.key) < 0 ;
								// bring new page
								
								BTIndexPage sibling = new BTIndexPage(keyType);
								
								// insert entries in newpage and remove from n
								RID rid3 = new RID();
								rid3.pageNo = rid2.pageNo;
								rid3.slotNo = rid2.slotNo;
								
								
								while(rid2!=null)
								{
									t = n.getRecord(rid2);
									rid2 = n.nextRecord(rid2);
									ch = BT.getEntryFromBytes(t.getTupleByteArray(), t.getOffset(), t.getLength(),this.keyType,NodeType.LEAF);
//									n.deleteSortedRecord(rid2);
									sibling.insertRecord(ch);
								}
								while(true)
								{
//									rid3.pageNo = rid2.pageNo;
//									rid3.slotNo = rid2.slotNo;
//									rid2 = n.nextRecord(rid3);
//									ch = BT.getEntryFromBytes(t.getTupleByteArray(), t.getOffset(), t.getLength(),this.keyType,NodeType.LEAF);
									n.deleteSortedRecord(rid3);
//									sibling.insertRecord(ch);
									try{
									t = n.getRecord(rid3);
									}catch(Exception e){
										break;
									}
									
								}
								
								if(check)
									n.insertRecord(newChildEntry);
									else
									sibling.insertRecord(newChildEntry);
									
									n.setNextPage(sibling.getCurPage());
									
									
								
							// push up and fix left link	
							rid3 = new RID ();
							ch = sibling.getFirst(rid3);
							sibling.setLeftLink(((IndexData)ch.data).getData());
							sibling.deleteSortedRecord(rid3);
							SystemDefs.JavabaseBM.unpinPage(sibling.getCurPage(), true);
							SystemDefs.JavabaseBM.unpinPage(nodeId, true);

							newChildEntry .key = ch.key;
							newChildEntry.data = new IndexData(sibling.getCurPage());
							
							// root case
							if (nodeId.pid==header.getNextPage().pid)
							{
								BTIndexPage f = new BTIndexPage(this.keyType);
								f.setLeftLink(nodeId);
								f.insertRecord(newChildEntry);
								System.out.println(f.firstRecord().slotNo);
								header.setNextPage(f.getCurPage());
								SystemDefs.JavabaseBM.unpinPage(f.getCurPage(), true);
								newChildEntry.data = null;
								newChildEntry.key = null;
								return;
							}
							
						}
								
					}
						
				}
	}
	public void insert(KeyClass key, RID rid){
		
		PageId currRootId = null;
		try {
			currRootId = new PageId(header.getNextPage().pid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		KeyClass k = null;
		DataClass d = null;
		KeyDataEntry en = new KeyDataEntry(k,d);
		try {
		insertRec(currRootId,key,rid,en);
		}catch(ChainException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void traceFilename(String string) {
		// TODO Auto-generated method stub
		
	}

	public void destroyFile() throws ReplacerException, PageUnpinnedException, HashEntryNotFoundException, InvalidFrameNumberException, IOException, FileEntryNotFoundException, FileIOException, InvalidPageNumberException, DiskMgrException 
	{
//		this.close();
		// clean all pages.
		SystemDefs.JavabaseDB.delete_file_entry(fileName);
	}

	public void close() throws ReplacerException, PageUnpinnedException, HashEntryNotFoundException, InvalidFrameNumberException, IOException 
	{		
		SystemDefs.JavabaseBM.unpinPage(header.getCurPage(), true);	
	}

	public BTFileScan new_scan(KeyClass lowkey, KeyClass hikey) {
		// TODO Auto-generated method stub
		try {
			return new BTFileScan(header ,lowkey, hikey);
		} catch (IOException | ChainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
