import exceptions.IndexException;
import exceptions.JoinsException;
import exceptions.LowMemException;
import exceptions.PredEvalException;
import exceptions.SortException;
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
import java.io.IOException;
import java.util.PriorityQueue;

import bufmgr.PageNotReadException;

public class ExternalSorting extends Sort {

	private int recordSize;
	private int lastRun;
	private HFIterator it;
	private String fileName;

	public ExternalSorting(AttrType[] in, short len_in, short[] str_sizes,
			Iterator am, int sort_fld, TupleOrder sort_order, int sort_fld_len,
			int n_pages) throws InvalidTypeException, PageNotReadException,
			JoinsException, IndexException, TupleUtilsException,
			PredEvalException, LowMemException, UnknowAttrType,
			UnknownKeyTypeException, Exception {
		super(in, len_in, str_sizes, am, sort_fld, sort_order, sort_fld_len,
				n_pages);

		recordSize = 4 * (_in.length - _str_sizes.length);
		for (int i = 0; i < _str_sizes.length; i++)
			recordSize += _str_sizes[i];

		pass_0();
		Merge(lastRun, 0);
		it = new HFIterator(fileName);
	}

	/**
	 * the first step take B pages in buffer and sort the records by quick sort
	 * 
	 * @throws InvalidTypeException
	 * @throws PageNotReadException
	 * @throws JoinsException
	 * @throws IndexException
	 * @throws TupleUtilsException
	 * @throws PredEvalException
	 * @throws SortException
	 * @throws LowMemException
	 * @throws UnknowAttrType
	 * @throws UnknownKeyTypeException
	 * @throws Exception
	 */
	public void pass_0() throws InvalidTypeException, PageNotReadException,
			JoinsException, IndexException, TupleUtilsException,
			PredEvalException, SortException, LowMemException, UnknowAttrType,
			UnknownKeyTypeException, Exception {

		// get record size
		@SuppressWarnings("static-access")
		int size = (int) (_n_pages * (Math.ceil(1.0
				* SystemDefs.JavabaseDB.MINIBASE_PAGESIZE / recordSize)));

		lastRun = 0;
		// used in looping
		boolean loop = true;
		while (loop) {
			// the array size because the last records in the file may fit in
			// smaller place than size
			int arraySize = size;
			Tuple[] array = new Tuple[size];
			for (int i = 0; i < array.length && loop; i++) {
				array[i] = _am.get_next();
				// if null it means that i have reached the end of the file
				if (array[i] == null) {
					arraySize = i;
					loop = false;
				}
			}

			if (arraySize != 0) {

				// sort the array of tuples by quick sort
				quicksort(0, arraySize - 1, array);

				// make a new run and insert the sorted records into it
				Heapfile tempRun = new Heapfile("pass_0_" + (lastRun));
				lastRun++;
				for (int i = 0; i < arraySize; i++)
					tempRun.insertRecord(array[i].getTupleByteArray());
			}
		}

	}

	// this class is used as an entry in the priorityQueue to determine the
	// runNo of the popped Tuple
	class Pair implements Comparable<Pair> {
		Tuple data;
		int runNo;

		public Pair(Tuple data, int runNo) {
			this.data = data;
			this.runNo = runNo;
		}

		@Override
		public int compareTo(Pair o) {
			try {
				return compareTuple(data, o.data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return 0;
		}
	}

	// Recursively call the merge method to merge runs in each level
	public void Merge(int leftRuns, int depth) throws InvalidTypeException,
			PageNotReadException, JoinsException, IndexException,
			TupleUtilsException, PredEvalException, SortException,
			LowMemException, UnknowAttrType, UnknownKeyTypeException, Exception {

		int currentRun = 0;
		int merged;
		int itr = 0;
		while (leftRuns != 0) {
			String runName;
			merged = Math.min(_n_pages - 1, leftRuns);
			leftRuns -= merged;
			PriorityQueue<Pair> prQueue = new PriorityQueue<Pair>();
			Iterator[] iterators = new Iterator[merged];
			Tuple tmp;
			// TODO if the first tuple is null i think it won't happen
			for (int i = 0; i < iterators.length; i++) {
				runName = "pass_" + depth + "_" + (itr++);
				iterators[i] = new HFIterator(runName);
				if ((tmp = iterators[i].get_next()) != null)
					prQueue.add(new Pair(tmp, i));
			}

			// create a new file and loop until the queue is empty
			runName = "pass_" + (depth + 1) + "_" + (currentRun);
			Heapfile heapfile = new Heapfile(runName);

			while (!prQueue.isEmpty()) {
				// poll the next min or max record
				Pair pair = prQueue.poll();
				heapfile.insertRecord(pair.data.getTupleByteArray());

				// enqueue the next record after the just polled one

				Tuple next = iterators[pair.runNo].get_next();
				if (next != null)
					prQueue.add(new Pair(next, pair.runNo));
			}
			for (int i = 0; i < iterators.length; i++) {
				iterators[i].close();
			}
			currentRun++;
		}

		if (currentRun > 1) {
			Merge(currentRun, depth + 1);
		} else
			fileName = "pass_" + (depth + 1) + "_0";

	}

	/**
	 * 
	 * @param low
	 * @param high
	 * @param array
	 * @throws IOException
	 */
	private void quicksort(int low, int high, Tuple[] array) throws IOException {
		int i = low, j = high;
		// Get the pivot element from the middle of the list
		Tuple pivot = array[low + (high - low) / 2];

		// Divide into two lists
		while (i <= j) {
			// If the current value from the left list is smaller then the pivot
			// element then get the next element from the left list
			while (compareTuple(array[i], pivot) < 0) {
				i++;
			}
			// If the current value from the right list is larger then the pivot
			// element then get the next element from the right list
			while (compareTuple(array[j], pivot) > 0) {
				j--;
			}

			// If we have found a values in the left list which is larger then
			// the pivot element and if we have found a value in the right list
			// which is smaller then the pivot element then we exchange the
			// values.
			// As we are done we can increase i and j
			if (i <= j) {
				exchange(i, j, array);
				i++;
				j--;
			}
		}
		// Recursion
		if (low < j)
			quicksort(low, j, array);
		if (i < high)
			quicksort(i, high, array);
	}

	private void exchange(int i, int j, Tuple[] array) {
		Tuple temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	/**
	 * 
	 * @param one
	 * @param two
	 * @return
	 * @throws IOException
	 */
	public int compareTuple(Tuple one, Tuple two) throws IOException {
		int start = 0;
		int index = 0;
		// get the start of the compare field in the byte array
		for (int i = 0; i < _sort_fld; i++) {
			if (_in[i].attrType == AttrType.attrInteger)
				start += 4;
			else
				start += _str_sizes[index++];
		}
		// convert the bytes to string or integer
		if (_in[_sort_fld].attrType == AttrType.attrInteger) {
			int valOne = Convert.getIntValue(start, one.getTupleByteArray());
			int valTwo = Convert.getIntValue(start, two.getTupleByteArray());

			if (_sort_order.tupleOrder == 0) {
				// Ascending
				return valOne - valTwo;
			} else {
				return valTwo - valOne;
			}

		} else {
			String sOne = Convert.getStrValue(start, one.getTupleByteArray(),
					_sort_fld_len);
			String sTwo = Convert.getStrValue(start, two.getTupleByteArray(),
					_sort_fld_len);
			if (_sort_order.tupleOrder == 0) {
				// Ascending
				return sOne.compareTo(sTwo);
			} else {
				return sTwo.compareTo(sOne);
			}

		}

	}

	@Override
	public Tuple get_next() throws IOException, SortException, UnknowAttrType,
			LowMemException, JoinsException, Exception {
		return it.get_next();
	}

}
