package btree;

import global.Convert;
import global.GlobalConst;
import global.PageId;
import global.RID;
import global.SystemDefs;
import heap.HFPage;
import heap.InvalidSlotNumberException;
import heap.Tuple;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

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

import diskmgr.Page;

public class BT implements GlobalConst {
	private static IOException localIOException;

	public static final int keyCompare(KeyClass paramKeyClass1,
			KeyClass paramKeyClass2) throws KeyNotMatchException {
		if (((paramKeyClass1 instanceof IntegerKey))
				&& ((paramKeyClass2 instanceof IntegerKey))) {
			return ((IntegerKey) paramKeyClass1).getKey().intValue()
					- ((IntegerKey) paramKeyClass2).getKey().intValue();
		}
		if (((paramKeyClass1 instanceof StringKey))
				&& ((paramKeyClass2 instanceof StringKey))) {
			return ((StringKey) paramKeyClass1).getKey().compareTo(
					((StringKey) paramKeyClass2).getKey());
		}

		throw new KeyNotMatchException(null, "key types do not match");
	}

	public static final int getKeyLength(KeyClass paramKeyClass)
			throws KeyNotMatchException, IOException {
		if ((paramKeyClass instanceof StringKey)) {
			ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
			DataOutputStream localDataOutputStream = new DataOutputStream(
					localByteArrayOutputStream);
			localDataOutputStream
					.writeUTF(((StringKey) paramKeyClass).getKey());
			return localDataOutputStream.size();
		}
		if ((paramKeyClass instanceof IntegerKey))
			return 4;
		throw new KeyNotMatchException(null, "key types do not match");
	}

	public static final int getDataLength(short paramShort)
			throws NodeNotMatchException {
		if (paramShort == 12)
			return 8;
		if (paramShort == 11)
			return 4;
		throw new NodeNotMatchException(null, "key types do not match");
	}

	public static final int getKeyDataLength(KeyClass paramKeyClass,
			short paramShort) throws KeyNotMatchException,
			NodeNotMatchException, IOException {
		return getKeyLength(paramKeyClass) + getDataLength(paramShort);
	}

	public static final KeyDataEntry getEntryFromBytes(byte[] paramArrayOfByte,
			int paramInt1, int paramInt2, int paramInt3, short paramShort)
			throws KeyNotMatchException, NodeNotMatchException,
			ConvertException {
		try {
			int i;
			Object localObject2;
			if (paramShort == 11) {
				i = 4;
				localObject2 = new IndexData(Convert.getIntValue(paramInt1
						+ paramInt2 - 4, paramArrayOfByte));
			} else if (paramShort == 12) {
				i = 8;
				RID localRID = new RID();
				localRID.slotNo = Convert.getIntValue(
						paramInt1 + paramInt2 - 8, paramArrayOfByte);
				localRID.pageNo = new PageId();
				localRID.pageNo.pid = Convert.getIntValue(paramInt1 + paramInt2
						- 4, paramArrayOfByte);
				localObject2 = new LeafData(localRID);
			} else {
				throw new NodeNotMatchException(null, "node types do not match");
			}
			Object localObject1;
			if (paramInt3 == 1) {
				localObject1 = new IntegerKey(new Integer(Convert.getIntValue(
						paramInt1, paramArrayOfByte)));
			} else if (paramInt3 == 0) {
				localObject1 = new StringKey(Convert.getStrValue(paramInt1,
						paramArrayOfByte, paramInt2 - i));
			} else {
				throw new KeyNotMatchException(null, "key types do not match");
			}
			return new KeyDataEntry((KeyClass) localObject1,
					(DataClass) localObject2);
		} catch (IOException localIOException) {
		}
		throw new ConvertException(localIOException, "convert faile");
	}

	public static final byte[] getBytesFromEntry(KeyDataEntry paramKeyDataEntry)
			throws KeyNotMatchException, NodeNotMatchException,
			ConvertException {
		try {
			int i = getKeyLength(paramKeyDataEntry.key);
			int j = i;
			if ((paramKeyDataEntry.data instanceof IndexData))
				i += 4;
			else if ((paramKeyDataEntry.data instanceof LeafData)) {
				i += 8;
			}
			byte[] arrayOfByte = new byte[i];

			if ((paramKeyDataEntry.key instanceof IntegerKey)) {
				Convert.setIntValue(((IntegerKey) paramKeyDataEntry.key)
						.getKey().intValue(), 0, arrayOfByte);
			} else if ((paramKeyDataEntry.key instanceof StringKey))
				Convert.setStrValue(
						((StringKey) paramKeyDataEntry.key).getKey(), 0,
						arrayOfByte);
			else {
				throw new KeyNotMatchException(null, "key types do not match");
			}
			if ((paramKeyDataEntry.data instanceof IndexData)) {
				Convert.setIntValue(
						((IndexData) paramKeyDataEntry.data).getData().pid, j,
						arrayOfByte);
			} else if ((paramKeyDataEntry.data instanceof LeafData)) {
				Convert.setIntValue(
						((LeafData) paramKeyDataEntry.data).getData().slotNo,
						j, arrayOfByte);
				Convert.setIntValue(
						((LeafData) paramKeyDataEntry.data).getData().pageNo.pid,
						j + 4, arrayOfByte);
			} else {
				throw new NodeNotMatchException(null, "node types do not match");
			}
			return arrayOfByte;
		} catch (IOException localIOException) {
		}
		throw new ConvertException(localIOException, "convert failed");
	}

//	public static void printBTree(HFPage headerPage) throws ChainException, IOException {
//		
//		// u can another q keeping the level of each node to know the proper indentation
//		// of each level 
//		
//		
//		RID rid1 = headerPage.firstRecord();
//		System.out.println(rid1);
//		Tuple t = headerPage.getRecord(rid1);
//		int ktype = Convert.getIntValue(0,t.getTupleByteArray());
//		// should be set to root page (read from header)
//		BTSortedPage page = new BTSortedPage(ktype);
//		PageId pageId = headerPage.getNextPage();
//		KeyDataEntry dEntry = null;
//		Stack<PageId> q = new Stack<PageId>();
//		q.add(pageId);
//		
//
//		while (!q.isEmpty()) {
//			pageId = q.pop();
//			SystemDefs.JavabaseBM.pinPage(pageId, page, false);
//			RID rid = page.firstRecord();
//			
//			System.out.print(" + |");
//			while (rid != null) {
//				t = page.getRecord(rid);
//				dEntry = BT.getEntryFromBytes(t.getTupleByteArray(), t.getOffset(),
//						t.getLength(), page.keyType, NodeType.LEAF);
//				System.out.print(dEntry.key );
//				System.out.print("("+rid.slotNo+")"+"|");
//
//				if (page.keyType == NodeType.INDEX) {
//					IndexData id = (IndexData) dEntry.data;
//					q.add(id.getData());
//					
//				}
//
//				rid = page.nextRecord(rid);
//			}
//			System.out.println();
//
//			SystemDefs.JavabaseBM.unpinPage(pageId, false);
//		}
//	}
//
	public static void printBTree(HFPage headerPage) throws ChainException, IOException {
		PageId pageId = headerPage.getNextPage(); // = root		

		RID rid = headerPage.firstRecord();
		Tuple t = headerPage.getRecord(rid);
		int ktype = Convert.getIntValue(0,t.getTupleByteArray());
		BTSortedPage page = new BTSortedPage(ktype);
		
		KeyDataEntry dEntry = null;
	Queue<PageId> q = new LinkedList<PageId>();
		Queue<Integer> indent = new LinkedList<Integer>();
		q.add(pageId);
		int level = 0;
		indent.add(level++);
		

		while (!q.isEmpty()) {
			pageId = q.remove();
			SystemDefs.JavabaseBM.pinPage(pageId, page, false);
			rid = page.firstRecord();
			
			String spaces = "";
			int n = indent.remove();
			for (int i = 0; i < n; i++) {
				spaces += " -----";
			}
			System.out.print(spaces+"+ |");
			
			if (page.getType() == NodeType.INDEX  ) {
				BTIndexPage indexPage = new BTIndexPage(page,ktype);
				q.add(indexPage.getLeftLink());
				indent.add(level);
			}
			while (rid != null) {
				t = page.getRecord(rid);
				dEntry = BT.getEntryFromBytes(t.getTupleByteArray(), t.getOffset(),
						t.getLength(), page.keyType, page.getType());
				System.out.print(dEntry.key );
				if(page.getType() == NodeType.INDEX)
				System.out.print("("+((IndexData)dEntry.data).getData().pid+")"+"|");
				else
				{
					RID r = ((LeafData)dEntry.data).getData();
					
					System.out.print("("+r.slotNo+","+r.pageNo.pid+")"+"|");
					
				}
					
				if (page.getType() == NodeType.INDEX) {
					IndexData id = (IndexData) dEntry.data;
					q.add(id.getData());
					indent.add(level);
				}

				rid = page.nextRecord(rid);
			}
			System.out.println();
			level ++;
			SystemDefs.JavabaseBM.unpinPage(pageId, false);
		}
	}

	public static void printAllLeafPages(HFPage headerPage) throws IOException, ChainException {
		PageId pageId = headerPage.getNextPage(); // = root		

		RID rid = headerPage.firstRecord();
		Tuple t = headerPage.getRecord(rid);
		int ktype = Convert.getIntValue(0,t.getTupleByteArray());
		BTSortedPage page = new BTSortedPage(ktype);
		
		KeyDataEntry dEntry = null;

		PageId id2 = new PageId();
		
		
	while (true){
			SystemDefs.JavabaseBM.pinPage(pageId, page, false);
			if(page.getType() == NodeType.LEAF)
				break;
			id2 = page.getPrevPage();
			SystemDefs.JavabaseBM.unpinPage(pageId, false);
			pageId = id2;
		}
	// now i have the pageID of the first leaf page
	
	while(true){
			SystemDefs.JavabaseBM.pinPage(pageId, page, false);
			rid = page.firstRecord();
			System.out.println(page.numberOfRecords());
			System.out.print("| ");
			while (rid != null) {
				t = page.getRecord(rid);
				dEntry = BT.getEntryFromBytes(t.getTupleByteArray(), t.getOffset(),
						t.getLength(), page.keyType, NodeType.LEAF);
				System.out.print(dEntry.key + " | ");

				rid = page.nextRecord(rid);
			}
			System.out.println();
			if (page.getNextPage().pid < 0) //no more leaf pages to print
				break;
			SystemDefs.JavabaseBM.unpinPage(pageId, false);
			pageId = page.getNextPage();
		}
	// unpin the last page
//		SystemDefs.JavabaseBM.unpinPage(pageId, false);
	}
	
//	public static void printAllLeafPages(HFPage headerPage) throws IOException, ReplacerException, HashOperationException, PageUnpinnedException, InvalidFrameNumberException, PageNotReadException, BufferPoolExceededException, PagePinnedException, BufMgrException, HashEntryNotFoundException, InvalidSlotNumberException, KeyNotMatchException, NodeNotMatchException, ConvertException {
//		//el madrood tegab men el header page
//		RID rid1 = headerPage.firstRecord();
//		System.out.println(rid1);
//		Tuple t = headerPage.getRecord(rid1);
//		int ktype = Convert.getIntValue(0,t.getTupleByteArray());
//		// should be set to root page (read from header)
//		BTSortedPage page = null;
//		try {
//			page = new BTSortedPage(ktype);
//		} catch (ConstructPageException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		PageId pageId = headerPage.getNextPage();		
//		KeyDataEntry dEntry = null;
////		PageId pageId = page.getCurPage(); 
//		RID rid  ;
////		Tuple t ;
//		
//		while(page.getPrevPage() != null){
//				SystemDefs.JavabaseBM.unpinPage(pageId, false);
//			pageId = page.getPrevPage();
//			SystemDefs.JavabaseBM.pinPage(pageId, page, false);
//		}
//
//		// now i have the pageID of the first leaf page  
//		
//		do{
//			SystemDefs.JavabaseBM.pinPage(pageId, page, false);
//			rid = page.firstRecord();
//			System.out.print("[");
//			while (rid != null) {
//				t = page.getRecord(rid);
//				dEntry = BT.getEntryFromBytes(t.getTupleByteArray(), t.getOffset(),
//						t.getLength(), page.keyType, NodeType.LEAF);
//				System.out.print(dEntry.key + "|");
//
//				rid = page.nextRecord(rid);
//			}
//			System.out.println("]");
//			SystemDefs.JavabaseBM.unpinPage(pageId, false);
//			pageId = page.getNextPage();
//			
//		}while (page.getNextPage()!=null);
//	}
//
	@SuppressWarnings("null")
	public static void printPage(PageId pageId, int keyType)
			throws ReplacerException, HashOperationException,
			PageUnpinnedException, InvalidFrameNumberException,
			PageNotReadException, BufferPoolExceededException,
			PagePinnedException, BufMgrException, IOException,
			ConstructPageException, KeyNotMatchException,
			NodeNotMatchException, ConvertException,
			InvalidSlotNumberException, HashEntryNotFoundException {
		// TODO Auto-generated method stub
		BTSortedPage page = new BTSortedPage(keyType);
		SystemDefs.JavabaseBM.pinPage(pageId, page, false);

		RID rid = page.firstRecord();
		KeyDataEntry dEntry = null;
		Tuple t = null;
		if (page.getType() == NodeType.LEAF) {
			while (rid != null) {
				t = page.getRecord(rid);
				BT.getEntryFromBytes(t.getTupleByteArray(), t.getOffset(),
						t.getLength(), keyType, NodeType.LEAF);
				System.out.print(dEntry.key + ":" + dEntry.data + "  ");
				rid = page.nextRecord(rid);
			}

		} else if (page.getType() == NodeType.INDEX) {
			while (rid != null) {
				t = page.getRecord(rid);
				BT.getEntryFromBytes(t.getTupleByteArray(), t.getOffset(),
						t.getLength(), keyType, NodeType.INDEX);
				System.out.print(dEntry.key + " : " + dEntry.data);
				rid = page.nextRecord(rid);
			}
		}
		SystemDefs.JavabaseBM.unpinPage(pageId, false);
	}
}