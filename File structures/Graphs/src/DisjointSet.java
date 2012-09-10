
public class DisjointSet {
	int pset[];
	
	public DisjointSet(int n) {
		pset = new int[n];
		for (int i = 0; i < n; i++) 
			pset[i] = i;
	}
	int findSet(int i){	return pset[i]==i?i:(pset[i] = findSet(pset[i]));}
	
	void unionSet(int i, int j){pset[findSet(i)] = findSet(j);}
	
	boolean isSameSet(int i, int j){return findSet(i)==findSet(j);}
}
