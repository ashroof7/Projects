import java.util.BitSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;



public class BFS_DFS {
	
	public static String bfs(boolean [][] adjMat){
		Queue<Integer> q = new LinkedList<Integer>();
		BitSet v = new BitSet(adjMat.length);
		q.add(0);
		String s = "";
		while (!q.isEmpty()){
			int current = q.poll();
			s+=current +", ";
			for (int i = 0; i < adjMat.length; i++) {
				if (adjMat[current][i] && !v.get(i)){
					
					q.add(i);
					v.set(i);
				}
			}
					
		}
		return s;
	}
	
	
	public static String dfs(boolean [][] adjMat){
		Stack<Integer> q = new Stack<Integer>();
		BitSet v = new BitSet(adjMat.length);
		q.push(0);
		
		String s = "";
		while (!q.isEmpty()){
			int current = q.pop();
			s+=current +", ";
			for (int i = 0; i < adjMat.length; i++) {
				if (adjMat[current][i] && !v.get(i)){
					
					q.push(i);
					v.set(i);
				}
			}
					
		}
		return s;
	}
	
	static BitSet v = new BitSet();
	public static String dfsRecursive(int node,String s, boolean[][] adjMat){
		
		for (int i = 0; i < adjMat.length; i++) {
			if (adjMat[node][i]){
				v.set(i);
				s = dfsRecursive(i, s+i+", ", adjMat);
				v.clear(i);
			}
		}
		return s;
	}

 	public static void main(String[] args) {
//		int map[][] = {{0,1,0,0,0,0,0,0},
//				{0,0,1,1,0,0,0,0},
//				{0,0,0,1,0,1,0,0},
//				{0,0,0,0,1,1,0,0},
//				{0,0,0,0,0,0,1,0},
//				{0,0,0,0,0,0,1,1},
//				{0,0,0,0,0,0,0,0},
//				{0,0,0,0,0,0,1,0}};

int map [][] = {{0,1,1,0,0,0,0,0,0},
		{0,0,0,1,1,0,0,0,0},
		{0,0,0,0,0,1,1,1,0},
		{0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,1},
		{0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0},
		{0,0,0,0,0,0,0,0,0}};		
		
		
		boolean mat[][] = new boolean[map.length][map.length];
		for (int i = 0; i < mat.length; i++) 
			for (int j = 0; j < mat.length; j++) 
				mat[i][j]= map[i][j]==1?true:false;
		
		System.out.println("BFS : ["+bfs(mat)+"]");
		System.out.println("DFS : ["+dfs(mat)+"]");
		System.out.println("DFS Rec : ["+dfsRecursive(0, "0, ", mat)+"]");
	}
	
	
	
}
