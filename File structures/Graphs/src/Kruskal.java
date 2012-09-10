import java.util.ArrayList;
import java.util.Collections;


public class Kruskal {

	public static class DisjointSet {
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

	
	public static int kruskal(int n, ArrayList<Edge> edgeList){
		int mspCost = 0 ;
		int count = 1;
		//sort the egdes 
		Collections.sort(edgeList);
		//construct the msp
		DisjointSet set = new DisjointSet(n);
		for (int i = 0; i < edgeList.size(); i++) {
			if (!set.isSameSet(edgeList.get(i).from, edgeList.get(i).to)){
				mspCost+=edgeList.get(i).cost;
				set.unionSet(edgeList.get(i).from, edgeList.get(i).to);
				System.out.println((edgeList.get(i).from)+"--"+(edgeList.get(i).to));
				count++;
				if (count==n)break;
			}
		}
		
		return mspCost;
	}
	
	public static void main(String[] args) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		edges.add(new Edge(0, 1, 4));
		edges.add(new Edge(0, 4, 6));
		edges.add(new Edge(0, 3, 6));
		edges.add(new Edge(0, 2, 4));
		edges.add(new Edge(1, 2, 2));
		edges.add(new Edge(2, 3, 8));
		edges.add(new Edge(3, 4, 9));
		
		System.out.println(kruskal(5, edges));

	}

}
