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
import heap.SpaceNotAvailableException;
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
		 test3();
		// test4();
		//test5();
		//test6();
	}

	private static void test5() throws InvalidTypeException,
			PageNotReadException, JoinsException, IndexException,
			TupleUtilsException, PredEvalException, LowMemException,
			UnknowAttrType, UnknownKeyTypeException, Exception {
		String data1[] = { "raghu", "xbao", "cychan", "leela", "ketola",
				"soma", "ulloa", "dhanoa", "dsilva", "kurniawa", "dissoswa",
				"waic", "susanc", "kinc", "marc", "scottc", "yuc", "ireland",
				"rathgebe", "joyce", "daode", "yuvadee", "he", "huxtable",
				"muerle", "flechtne", "thiodore", "jhowe", "frankief",
				"yiching", "xiaoming", "jsong", "yung", "muthiah", "bloch",
				"binh", "dai", "hai", "handi", "shi", "sonthi", "evgueni",
				"chung-pi", "chui", "siddiqui", "mak", "tak", "sungk",
				"randal", "barthel", "newell", "schiesl", "neuman", "heitzman",
				"wan", "gunawan", "djensen", "juei-wen", "josephin", "harimin",
				"xin", "zmudzin", "feldmann", "joon", "wawrzon", "yi-chun",
				"wenchao", "seo", "karsono", "dwiyono", "ginther", "keeler",
				"peter", "lukas", "edwards", "mirwais", "schleis", "haris",
				"meyers", "azat", "shun-kit", "robert", "markert", "wlau",
				"honghu", "guangshu", "chingju", "bradw", "andyw", "gray",
				"vharvey", "awny", "savoy", "meltz" };

		String data2[] = { "andyw", "awny", "azat", "barthel", "binh", "bloch",
				"bradw", "chingju", "chui", "chung-pi", "cychan", "dai",
				"daode", "dhanoa", "dissoswa", "djensen", "dsilva", "dwiyono",
				"edwards", "evgueni", "feldmann", "flechtne", "frankief",
				"ginther", "gray", "guangshu", "gunawan", "hai", "handi",
				"harimin", "haris", "he", "heitzman", "honghu", "huxtable",
				"ireland", "jhowe", "joon", "josephin", "joyce", "jsong",
				"juei-wen", "karsono", "keeler", "ketola", "kinc", "kurniawa",
				"leela", "lukas", "mak", "marc", "markert", "meltz", "meyers",
				"mirwais", "muerle", "muthiah", "neuman", "newell", "peter",
				"raghu", "randal", "rathgebe", "robert", "savoy", "schiesl",
				"schleis", "scottc", "seo", "shi", "shun-kit", "siddiqui",
				"soma", "sonthi", "sungk", "susanc", "tak", "thiodore",
				"ulloa", "vharvey", "waic", "wan", "wawrzon", "wenchao",
				"wlau", "xbao", "xiaoming", "xin", "yi-chun", "yiching", "yuc",
				"yung", "yuvadee", "zmudzin" };

		Heapfile heapfile = new Heapfile("test4");
		byte[] record = new byte[6];
		for (int i = 0; i < data1.length; i++)
			if (data1[i].length() <= 3)
				data1[i] += "aaaaaaaaaa";

		for (int i = 0; i < data2.length; i++)
			if (data2[i].length() <= 3)
				data2[i] += "aaaaaaaaa";

		for (int i = 0; i < data1.length; i++) {
			record = new byte[6];
			String s = data1[i].substring(0, 4);
			Convert.setStrValue(s, 0, record);
			heapfile.insertRecord(record);
		}
		Iterator am = new HFIterator("test4");
		AttrType[] att = new AttrType[1];
		att[0] = new AttrType(0);

		short len_in = 6;
		short[] str_sizes = { 6 };
		int sort_fld = 0;
		TupleOrder sort_order = new TupleOrder(0);
		int srt_fld_len = 6;
		ExternalSorting sort = new ExternalSorting(att, len_in, str_sizes, am,
				sort_fld, sort_order, srt_fld_len, 3);
		System.out.println("-------print the sorted file");
		Tuple t = sort.get_next();
		while (t != null) {

			System.out
					.println(Convert.getStrValue(0, t.getTupleByteArray(), 6));

			t = sort.get_next();
		}

	}

	private static void test6() throws InvalidTypeException,
			PageNotReadException, JoinsException, IndexException,
			TupleUtilsException, PredEvalException, LowMemException,
			UnknowAttrType, UnknownKeyTypeException, Exception {

		String data2[] = { "andyw", "awny", "azat", "barthel", "binh", "bloch",
				"bradw", "chingju", "chui", "chung-pi", "cychan", "dai",
				"daode", "dhanoa", "dissoswa", "djensen", "dsilva", "dwiyono",
				"edwards", "evgueni", "feldmann", "flechtne", "frankief",
				"ginther", "gray", "guangshu", "gunawan", "hai", "handi",
				"harimin", "haris", "he", "heitzman", "honghu", "huxtable",
				"ireland", "jhowe", "joon", "josephin", "joyce", "jsong",
				"juei-wen", "karsono", "keeler", "ketola", "kinc", "kurniawa",
				"leela", "lukas", "mak", "marc", "markert", "meltz", "meyers",
				"mirwais", "muerle", "muthiah", "neuman", "newell", "peter",
				"raghu", "randal", "rathgebe", "robert", "savoy", "schiesl",
				"schleis", "scottc", "seo", "shi", "shun-kit", "siddiqui",
				"soma", "sonthi", "sungk", "susanc", "tak", "thiodore",
				"ulloa", "vharvey", "waic", "wan", "wawrzon", "wenchao",
				"wlau", "xbao", "xiaoming", "xin", "yi-chun", "yiching", "yuc",
				"yung", "yuvadee", "zmudzin" };

		Heapfile heapfile = new Heapfile("test4");
		byte[] record = new byte[6];

		for (int i = 0; i < data2.length; i++)
			if (data2[i].length() <= 3)
				data2[i] += "aaaaaaaaa";

		for (int i = 0; i < data2.length; i++) {
			record = new byte[6];
			String s = data2[i].substring(0, 4);
			Convert.setStrValue(s, 0, record);
			heapfile.insertRecord(record);
		}
		Iterator am = new HFIterator("test4");
		AttrType[] att = new AttrType[1];
		att[0] = new AttrType(0);

		short len_in = 6;
		short[] str_sizes = { 6 };
		int sort_fld = 0;
		TupleOrder sort_order = new TupleOrder(1);
		int srt_fld_len = 6;
		ExternalSorting sort = new ExternalSorting(att, len_in, str_sizes, am,
				sort_fld, sort_order, srt_fld_len, 3);
		System.out.println("-------print the sorted file");
		Tuple t = sort.get_next();
		while (t != null) {

			System.out
					.println(Convert.getStrValue(0, t.getTupleByteArray(), 6));

			t = sort.get_next();
		}

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
		short[] str_sizes = { 6, 6 };
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
		char a = (char) ('a' + Math.random() * 26);
		char b = (char) ('a' + Math.random() * 26);
		char c = (char) ('a' + Math.random() * 26);
		char d = (char) ('a' + Math.random() * 26);

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
