package DBSystem;

/**
 * @author Database Project CSED 2014<address @ example.com>
 * @version 1.0 (the version of the package this class was first added to)
 * @since 2011-12-4 (a date or the version number of this program)
 */
public interface Entry {
	/**
	 * Change the value of a certain variable, the certain variable that will be
	 * changed is the one with index index
	 * 
	 * @param String
	 *            value , int index
	 * @return void
	 */
	public void changeValue(String value, int index);

	/**
	 * Check if this entry contains these values
	 * 
	 * @param LinekdList
	 *            String[] value , String[] index
	 * @return boolean , true if these values are equal to that of the entry ,
	 *         else false
	 */
	public boolean equal(String[] value, int[] index);

	/**
	 * will return the entry values
	 * 
	 * @param
	 * @return void
	 */
	public String[] getValues();
}
