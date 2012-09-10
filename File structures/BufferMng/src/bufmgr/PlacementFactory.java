package bufmgr;

public class PlacementFactory {

	/**
	 * @param policy
	 * @return a placementPolicy 
	 */
	public PlacementPolicy createPolicy(String policy) {
		if (policy.equals("MRU"))
			return new MostRecentPolicy();
		return new LeastRecentPolicy();
	}
}