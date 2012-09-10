//package iterator;

import java.io.*;
import global.*;
import bufmgr.*;
import diskmgr.*;
import exceptions.JoinsException;
import exceptions.LowMemException;
import exceptions.SortException;
import exceptions.UnknowAttrType;
import heap.*;
//import index.*;
import chainexception.*;

/**
 * The Sort class sorts a file. All necessary information are passed as
 * arguments to the constructor. After the constructor call, the user can
 * repeatly call <code>get_next()</code> to get tuples in sorted order. After
 * the sorting is done, the user should call <code>close()</code> to clean up.
 */
public class Sort extends Iterator implements GlobalConst {
	private static final int ARBIT_RUNS = 10;

	protected AttrType[] _in;
	protected short n_cols;
	protected short[] str_lens;
	protected Iterator _am;
	protected int _sort_fld;
	protected int _n_pages;
	protected byte[][] bufs;
	protected boolean first_time;
	protected int Nruns;
	protected int max_elems_in_heap;
	protected int sortFldLen;
	protected int tuple_size;
	protected short _len_in;
	protected short[] _str_sizes;
	protected TupleOrder _sort_order; // ascending or descending
	protected int _sort_fld_len;

	// private pnodeSplayPQ Q;
	private Heapfile[] temp_files;
	private int n_tempfiles;
	private Tuple output_tuple;
	private int[] n_tuples;
	private int n_runs;
	private Tuple op_buf;
	// private OBuf o_buf;
	// private SpoofIbuf[] i_buf;
	private PageId[] bufs_pids;
	private boolean useBM = true; // flag for whether to use buffer manager

	/**
	 * Class constructor, take information about the tuples, and set up the
	 * sorting
	 * 
	 * @param in
	 *            array containing attribute types of the relation
	 * @param len_in
	 *            number of columns in the relation
	 * @param str_sizes
	 *            array of sizes of string attributes
	 * @param am
	 *            an iterator for accessing the tuples
	 * @param sort_fld
	 *            the field number of the field to sort on
	 * @param sort_order
	 *            the sorting order (ASCENDING, DESCENDING)
	 * @param sort_field_len
	 *            the length of the sort field
	 * @param n_pages
	 *            amount of memory (in pages) available for sorting
	 * @exception IOException
	 *                from lower layers
	 * @exception SortException
	 *                something went wrong in the lower layer.
	 */
	public Sort(AttrType[] in, short len_in, short[] str_sizes, Iterator am,
			int sort_fld, TupleOrder sort_order, // ascending or descending
			int sort_fld_len, int n_pages) throws IOException, SortException {

		_in = in;
		_am = am;
		_n_pages = n_pages;
		_sort_fld = sort_fld;
		_len_in = len_in;
		_str_sizes = str_sizes;
		_sort_fld_len = sort_fld_len;
		_sort_order = sort_order;

	}

	/**
	 * Returns the next tuple in sorted order. Note: You need to copy out the
	 * content of the tuple, otherwise it will be overwritten by the next
	 * <code>get_next()</code> call.
	 * 
	 * @return the next tuple, null if all tuples exhausted
	 * @exception IOException
	 *                from lower layers
	 * @exception SortException
	 *                something went wrong in the lower layer.
	 * @exception JoinsException
	 *                from <code>generate_runs()</code>.
	 * @exception UnknowAttrType
	 *                attribute type unknown
	 * @exception LowMemException
	 *                memory low exception
	 * @exception Exception
	 *                other exceptions
	 */
	public Tuple get_next() throws IOException, SortException, UnknowAttrType,
			LowMemException, JoinsException, Exception {
		return null;
	}

	
	/**
	 * Cleaning up, including releasing buffer pages from the buffer pool and
	 * removing temporary files from the database.
	 * 
	 * @exception IOException
	 *                from lower layers
	 * @exception SortException
	 *                something went wrong in the lower layer.
	 */
	public void close() throws SortException, IOException {
	}

}
