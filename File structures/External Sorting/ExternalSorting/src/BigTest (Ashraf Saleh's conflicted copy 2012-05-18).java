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
import heap.Heapfile;
import heap.InvalidTypeException;
import heap.Tuple;

public class BigTest {

	public static void main(String[] args) throws InvalidTypeException,
			PageNotReadException, JoinsException, IndexException,
			TupleUtilsException, PredEvalException, LowMemException,
			UnknowAttrType, UnknownKeyTypeException, Exception {
		SystemDefs sysdef = new SystemDefs(System.getProperty("user.name")
				+ ".minibase-db", 1000, 1000, "Clock");

		System.out.println("Start External Sorting Test");
		// test1();
		// test2();
		// test3();
		test4();
	}

	private static void test4() throws InvalidTypeException,
			PageNotReadException, JoinsException, IndexException,
			TupleUtilsException, PredEvalException, LowMemException,
			UnknowAttrType, UnknownKeyTypeException, Exception {
		System.out.println("-----------------Test 4-----------------");
		System.out
				.println("Insert 1000 [int-String-int-String] records Random");
		System.out.println("File test4 is created");
		Heapfile heapfile = new Heapfile("test4");
		byte[] record = new byte[20];
		for (int i = 0; i < 1000; i++) {
			record = new byte[20];
			Convert.setIntValue(i, 0, record);
			Convert.setStrValue(getRandStr(), 4, record);
			Convert.setIntValue(i + 1, 10, record);
			String s = getRandStr();
			Convert.setStrValue(s, 14, record);

			heapfile.insertRecord(record);
		}
		System.out.println("------------sorting the File");
		Iterator am = new HFIterator("test4");
		AttrType[] att = new AttrType[4];
		att[0] = new AttrType(1);
		att[1] = new AttrType(0);
		att[2] = new AttrType(1);
		att[3] = new AttrType(0);

		short len_in = 4;
		short[] str_sizes = {6,6};
		int sort_fld = 1;
		TupleOrder sort_order = new TupleOrder(0);
		int srt_fld_len = 6;
		ExternalSorting sort = new ExternalSorting(att, len_in, str_sizes, am,
				sort_fld, sort_order, srt_fld_len, 3);
		System.out.println("-------print the sorted file");
		Tuple t = sort.get_next();
		while (t != null) {
			
			System.out.print(Convert.getIntValue(0, t.getTupleByteArray()));
			System.out.print(" "
					+ Convert.getStrValue(4, t.getTupleByteArray(), 6));
			System.out.print(" "
					+ Convert.getIntValue(10, t.getTupleByteArray()));
			System.out.println(" "
					+ Convert.getStrValue(14, t.getTupleByteArray(), 6));
		
			t = sort.get_next();
		}
		System.out.println("-------------test4 finished------------");

	}

	private static String getRandStr() {
		char a = (char) ('a'+Math.random() * 26);
		char b = (char) ('a'+Math.random() * 26);
		char c = (char) ('a'+Math.random() * 26);
		char d = (char) ('a'+Math.random() * 26);

		return a + "" + b + "" + c + "" + d;
	}

	private static void test1() throws InvalidTypeException,
			PageNotReadException, JoinsException, IndexException,
			TupleUtilsException, PredEvalException, LowMemException,
			UnknowAttrType, UnknownKeyTypeException, Exception {
		System.out.println("-----------------Test 1-----------------");
		System.out
				.println("Insert 1000 [int-int-int] records sorted acsendingly, sort them descendingly");
		System.out.println("File test1 is created");
		Heapfile heapfile = new Heapfile("test1");
		byte[] record = new byte[12];
		for (int i = 0; i < 1000; i++) {
			record = new byte[12];
			Convert.setIntValue(i, 0, record);
			Convert.setIntValue(i + 1, 4, record);
			Convert.setIntValue(i + 2, 8, record);
			heapfile.insertRecord(record);
		}
		System.out.println("------------sorting the File");
		Iterator am = new HFIterator("test1");
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
				sort_fld, sort_order, srt_fld_len, 3);
		System.out.println("-------print the sorted file");
		Tuple t = sort.get_next();
		while (t != null) {
			System.out.print(Convert.getIntValue(0, t.getTupleByteArray()));
			System.out.print(" "
					+ Convert.getIntValue(4, t.getTupleByteArray()));
			System.out.println(" "
					+ Convert.getIntValue(8, t.getTupleByteArray()));
			t = sort.get_next();
		}
		System.out.println("-------------test1 finished------------");

	}

	private static void test2() throws InvalidTypeException,
			PageNotReadException, JoinsException, IndexException,
			TupleUtilsException, PredEvalException, LowMemException,
			UnknowAttrType, UnknownKeyTypeException, Exception {
		System.out.println("-----------------Test 2-----------------");
		System.out
				.println("Insert 1000 [int-int-int] records sorted decsendingly, sort them ascendingly");
		System.out.println("File test2 is created");
		Heapfile heapfile = new Heapfile("test2");
		byte[] record = new byte[12];
		for (int i = 1000 - 1; i >= 0; i--) {
			record = new byte[12];
			Convert.setIntValue(i, 0, record);
			Convert.setIntValue(i - 1, 4, record);
			Convert.setIntValue(i - 2, 8, record);
			heapfile.insertRecord(record);
		}
		System.out.println("------------sorting the File");
		Iterator am = new HFIterator("test2");
		AttrType[] att = new AttrType[3];
		att[0] = new AttrType(1);
		att[1] = new AttrType(1);
		att[2] = new AttrType(1);

		short len_in = 3;
		short[] str_sizes = new short[0];
		int sort_fld = 0;
		TupleOrder sort_order = new TupleOrder(1);
		int srt_fld_len = 4;
		ExternalSorting sort = new ExternalSorting(att, len_in, str_sizes, am,
				sort_fld, sort_order, srt_fld_len, 3);
		System.out.println("-------print the sorted file");
		Tuple t = sort.get_next();

		while (t != null) {
			System.out.print(Convert.getIntValue(0, t.getTupleByteArray()));
			System.out.print(" "
					+ Convert.getIntValue(4, t.getTupleByteArray()));
			System.out.println(" "
					+ Convert.getIntValue(8, t.getTupleByteArray()));
			t = sort.get_next();
		}
		System.out.println("-------------test2 finished------------");

	}

	private static void test3() throws InvalidTypeException,
			PageNotReadException, JoinsException, IndexException,
			TupleUtilsException, PredEvalException, LowMemException,
			UnknowAttrType, UnknownKeyTypeException, Exception {
		System.out.println("-----------------Test 3-----------------");
		System.out
				.println("Insert 1000 [int-int-int] records randomly generated , sort them ascendingly");
		System.out.println("File test2 is created");
		Heapfile heapfile = new Heapfile("test3");
		byte[] record = new byte[12];
		for (int i = 0; i < 1000; i++) {
			record = new byte[12];
			Convert.setIntValue((int) (Math.random() * 1000), 0, record);
			Convert.setIntValue((int) (Math.random() * 1000), 4, record);
			Convert.setIntValue((int) (Math.random() * 1000), 8, record);
			heapfile.insertRecord(record);
		}
		System.out.println("------------sorting the File");
		Iterator am = new HFIterator("test3");
		AttrType[] att = new AttrType[3];
		att[0] = new AttrType(1);
		att[1] = new AttrType(1);
		att[2] = new AttrType(1);

		short len_in = 3;
		short[] str_sizes = new short[0];
		int sort_fld = 1;
		TupleOrder sort_order = new TupleOrder(0);
		int srt_fld_len = 4;
		ExternalSorting sort = new ExternalSorting(att, len_in, str_sizes, am,
				sort_fld, sort_order, srt_fld_len, 3);
		System.out.println("-------print the sorted file");
		Tuple t = sort.get_next();

		while (t != null) {
			System.out.print(Convert.getIntValue(0, t.getTupleByteArray()));
			System.out.print(" "
					+ Convert.getIntValue(4, t.getTupleByteArray()));
			System.out.println(" "
					+ Convert.getIntValue(8, t.getTupleByteArray()));
			t = sort.get_next();
		}
		System.out.println("-------------test3 finished------------");

	}

}
