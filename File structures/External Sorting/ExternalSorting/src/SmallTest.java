import java.io.IOException;

import bufmgr.PageNotReadException;
import exceptions.IndexException;
import exceptions.JoinsException;
import exceptions.LowMemException;
import exceptions.PredEvalException;
import exceptions.TupleUtilsException;
import exceptions.UnknowAttrType;
import exceptions.UnknownKeyTypeException;

import global.AttrType;
import global.Convert;
import global.SystemDefs;
import global.TupleOrder;
import heap.HFBufMgrException;
import heap.HFDiskMgrException;
import heap.HFException;
import heap.Heapfile;
import heap.InvalidSlotNumberException;
import heap.InvalidTupleSizeException;
import heap.InvalidTypeException;
import heap.Tuple;

public class SmallTest {

	public static void main(String[] args) throws InvalidTypeException,
			PageNotReadException, JoinsException, IndexException,
			TupleUtilsException, PredEvalException, LowMemException,
			UnknowAttrType, UnknownKeyTypeException, Exception {
		// SystemDefs sysdef = new SystemDefs("/tmp/" + "lol"
		// + System.getProperty("user.name") + ".minibase-db", 100, 100,
		// "Clock");
		SystemDefs sysdef = new SystemDefs(System.getProperty("user.name")
				+ ".minibase-db", 100, 100, "Clock");
		System.out.println(SystemDefs.JavabaseDB);
		Heapfile file = new Heapfile("testfile");
		byte[] record = new byte[12];
		Convert.setIntValue(1, 0, record);
		Convert.setIntValue(6, 4, record);
		Convert.setIntValue(8, 8, record);
		file.insertRecord(record);
		record = new byte[12];
		Convert.setIntValue(2, 0, record);
		Convert.setIntValue(6, 4, record);
		Convert.setIntValue(8, 8, record);
		file.insertRecord(record);
		record = new byte[12];
		Convert.setIntValue(3, 0, record);
		Convert.setIntValue(6, 4, record);
		Convert.setIntValue(8, 8, record);
		file.insertRecord(record);
		Iterator am = new HFIterator("testfile");
		Iterator am2 = new HFIterator("testfile");
		AttrType[] att = new AttrType[3];
		att[0] = new AttrType(1);
		att[1] = new AttrType(1);
		att[2] = new AttrType(1);

		short len_in = 3;
		short[] str_sizes = new short[0];
		int sort_fld = 0;
		TupleOrder sort_order = new TupleOrder(0);
		int srt_fld_len = 4;
		ExternalSorting sort = new ExternalSorting(att, len_in, str_sizes, am,
				sort_fld, sort_order, srt_fld_len, 2);
		Tuple t = sort.get_next();
		Tuple t2 = am2.get_next();
		while (t != null) {
			System.out.print(Convert.getIntValue(0, t.getTupleByteArray()));
			System.out.print(" "
					+ Convert.getIntValue(4, t.getTupleByteArray()));
			System.out.println(" "
					+ Convert.getIntValue(8, t.getTupleByteArray()));
			// System.out.print(Convert.getIntValue(0, t2.getTupleByteArray()));
			// System.out.print(" "
			// + Convert.getIntValue(4, t2.getTupleByteArray()));
			// System.out.println(" "
			// + Convert.getIntValue(8, t2.getTupleByteArray()));

			t = sort.get_next();
//			t2 = am2.get_next();
		}
	}
}
