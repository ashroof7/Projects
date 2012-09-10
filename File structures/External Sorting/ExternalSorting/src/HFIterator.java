import global.RID;
import heap.FileAlreadyDeletedException;
import heap.HFBufMgrException;
import heap.HFDiskMgrException;
import heap.HFException;
import heap.Heapfile;
import heap.InvalidSlotNumberException;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Scan;
import heap.Tuple;

import java.io.IOException;

import bufmgr.PageNotReadException;
import exceptions.IndexException;
import exceptions.JoinsException;
import exceptions.LowMemException;
import exceptions.PredEvalException;
import exceptions.SortException;
import exceptions.TupleUtilsException;
import exceptions.UnknowAttrType;
import exceptions.UnknownKeyTypeException;

public class HFIterator extends Iterator {
	private String fileName;
	private Scan scan;
	private RID CurRID;
	private Heapfile hf;

	public HFIterator(String fileName) throws InvalidTupleSizeException,
			HFException, HFBufMgrException, HFDiskMgrException, IOException {
		this.fileName = fileName;
		scan = new Scan(hf = new Heapfile(fileName));
		CurRID = new RID();
	}

	@Override
	public void close() throws IOException, JoinsException, SortException,
			IndexException {
		scan.closescan();
		try {
			hf.deleteFile();
		} catch (InvalidSlotNumberException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileAlreadyDeletedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTupleSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HFBufMgrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HFDiskMgrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Tuple get_next() throws IOException, JoinsException, IndexException,
			InvalidTupleSizeException, InvalidTypeException,
			PageNotReadException, TupleUtilsException, PredEvalException,
			SortException, LowMemException, UnknowAttrType,
			UnknownKeyTypeException, Exception {
		return scan.getNext(CurRID);
	}

}
