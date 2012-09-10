import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Stack;

public class ConnectedComponents {

	public static class DisjointSet {
		int pset[];

		public DisjointSet(int n) {
			pset = new int[n];
			for (int i = 0; i < n; i++)
				pset[i] = i;
		}

		int findSet(int i) {
			return pset[i] == i ? i : (pset[i] = findSet(pset[i]));
		}

		void unionSet(int i, int j) {
			pset[findSet(i)] = findSet(j);
		}

		boolean isSameSet(int i, int j) {
			return findSet(i) == findSet(j);
		}
	}

	static ArrayList<String> components = new ArrayList<String>();

	public static int getComponents(boolean[][] matrix) {
		BitSet v = new BitSet();
		DisjointSet set = new DisjointSet(matrix.length);
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix.length; j++)
				if (matrix[i][j])
					set.unionSet(i, j);

		String s;
		boolean isolated = true;
		for (int i = 0; i < matrix.length; i++) {
			if (v.get(i))
				continue;
			s = i + "";
			isolated = true;
			for (int j = 0; j < matrix.length; j++) {
				if (i != j && set.isSameSet(i, j)) {
					s += "-" + j;
					v.set(j);
					v.set(i);
					isolated = false;
					continue;
				}
			}
			if (isolated)
				components.add(i + "");

			if (s.length() > 1)
				components.add(s);
		}
		return components.size();
	}

	static Stack<Integer> stack;
	static int v;
	static int adjMat[][];

	public static ArrayList<String> Kosaraju(int matrix[][]) {
		adjMat = matrix;
		stack = new Stack<Integer>();
		for (int i = 0; i < matrix.length; i++)
			if ((v & (1 << i)) == 0)
				DFS_forward(i);
		v = 0;
		int res, pop;
		String s = "";
		ArrayList<String> SCCs = new ArrayList<String>();

		while (!stack.empty()) {
			pop = stack.pop();
			s = "-" + pop;
			res = DFS_transpose(pop, 0);
			
			for (Iterator<Integer> it = stack.iterator(); it.hasNext();) {
				int k = it.next();
				if ((res & (1 << k)) != 0) {
					s += "-" + k;
					it.remove();
					//use V to mark node already in component
					v|=(1<<k);
				}
			}
			SCCs.add(s.substring(1));
		}
		return SCCs;
	}

	private static void DFS_forward(int i) {
		if ((v & (1 << i)) != 0)
			return ;
		else {
			v |= (1 << i);
			stack.push(i);
			for (int j = 0; j < adjMat.length; j++)
				if (adjMat[i][j] != 0)
					DFS_forward(j);
		}
	}
	
	private static int DFS_transpose(int i, int SCC) {
		SCC |= (1 << i);
		for (int j = 0; j < adjMat.length; j++)
			// the condition on V is to check if the node i'm going to is taken in another 
			// component ---> don't continue 
			if (adjMat[i][j] != 0 && (SCC & (1 << j)) == 0 &&  (v & (1 << j)) == 0 )
				SCC |= DFS_transpose(j, SCC);
		 v|=SCC;
		return SCC;
	}

	public static void main(String[] args) throws IOException {
		// UNDIRECTED
		// 0 1 0 0 0 0 0 0 0
		// 1 0 1 1 0 0 0 0 0
		// 0 1 0 1 0 0 0 0 0
		// 0 1 1 0 1 0 0 0 0
		// 0 0 0 1 0 0 0 0 0
		// 0 0 0 0 0 0 0 0 0
		// 0 0 0 0 0 0 0 1 1
		// 0 0 0 0 0 0 1 0 0
		// 0 0 0 0 0 0 1 0 0
		GraphReader reader = new GraphReader();
		int[][] adjMat = reader.adjMat;

		// boolean[][] mat = new boolean[adjMat.length][adjMat.length];

		// for (int i = 0; i < adjMat.length; i++)
		// for (int j = 0; j < adjMat.length; j++)
		// if(adjMat[i][j]!=0)
		// mat[i][j]=adjMat[i][j]==0?false:true;

		// System.out.println(getComponents(mat));
		// System.out.println(components);

		System.out.println(Kosaraju(adjMat));

	}

}
