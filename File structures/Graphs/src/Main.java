import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Main {
	
	public static void main(String[] args)throws IOException {
		GraphReader reader = new GraphReader();
		int [][] adjMat = reader.adjMat;
		ArrayList<Edge> edgeList = new ArrayList<Edge>();
		boolean[][] mat = new boolean[adjMat.length][adjMat.length];
		
		
		for (int i = 0; i < adjMat.length; i++) {
			for (int j = 0; j < adjMat.length; j++){
				if(adjMat[i][j]!=0){
				edgeList.add(new Edge(i,j,adjMat[i][j]));
				mat[i][j]=adjMat[i][j]==0?false:true;
				}
			}
		}
		
		
		
		if (!reader.directed){
			System.out.println("Cycle Detection");
			CycleDetection.adjMat = adjMat ;
			int v[] = new int[adjMat.length];
			Arrays.fill(v, -1);
			CycleDetection.getCyclesUndirected(0,  new ArrayList<Integer>(),0);
			System.out.println("  cycles");
			System.out.println("  "+CycleDetection.memo);
			System.out.println();
			
			
			System.out.println("Kruskal");
			System.out.println("  msp minCost = "+  Kruskal.kruskal(adjMat.length, edgeList));
			System.out.println();
			
			System.out.println("Prim");
			System.out.println("  msp minCost = "+  Prim.prim(adjMat.length, edgeList));
			System.out.println();
			
			

			System.out.println("Connected Components (using disjoint sets)");
			System.out.println("  # of components = "+ConnectedComponents.getComponents(mat));
			System.out.println("  components : "+ConnectedComponents.components);
			
		}
		else {
//			System.out.println("Topological Sort");
//			System.out.println("  "+TopologicalSort.topologicalSort(mat));
//			System.out.println();
			
			System.out.println("Cycle Detection");
			CycleDetection.adjMat = adjMat ;
			int v[] = new int[adjMat.length];
			Arrays.fill(v, -1);
			CycleDetection.getCyclesDirected(0, new ArrayList<Integer>(),0 );
			System.out.println("  cycles");
			System.out.println("  "+CycleDetection.memo);
			System.out.println();
			
			
			System.out.println("Connected Components (Kosaraju)");
			ArrayList<String> SCCs = ConnectedComponents.Kosaraju(adjMat);
			System.out.println("  # of components = "+SCCs.size());
			System.out.println("  components : "+SCCs);
		}
		
		
		System.out.println("BFS");
		System.out.println("  "+BFS_DFS.bfs(mat));
		System.out.println();
		
		System.out.println("DFS");
		System.out.println("  "+BFS_DFS.dfs(mat));
		System.out.println();
		
		System.out.println("SSSP : Dijkstra ");
		System.out.println("  using adjacency matrix");
		System.out.println("    cost = "+Dijkstra.dijkstra(0, adjMat.length-1, adjMat));
		System.out.println("    path = "+Dijkstra.getPath(0, adjMat.length-1));
		System.out.println();
		System.out.println("  using edge list");
		System.out.println("    cost = "+Dijkstra.dijkstra(0, adjMat.length-1, adjMat.length, edgeList));
		System.out.println("    path = "+Dijkstra.getPath(0, adjMat.length-1));
		System.out.println();
		
		
	}

}
