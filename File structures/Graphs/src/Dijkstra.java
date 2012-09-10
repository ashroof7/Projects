import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra {

	
	static int d[] ;
	static boolean v[];
	static int p[];
	
	public  static Comparator<Integer> comp = new Comparator<Integer>() {
		@Override
		public int compare(Integer a , Integer b) {
			return d[a]-d[b];
		}
	};

	
	public static int dijkstra(int source,  int target, int nodes, ArrayList<Edge> edgeList){
		d = new int[nodes];
		p = new int[nodes];
		v = new boolean[nodes];
		Arrays.fill(d, 1<<25);
		PriorityQueue<Integer> q = new PriorityQueue<Integer>(nodes,comp);
		q.add(source);
		d[source] = 0;
	
		while (!q.isEmpty()){
			int current = q.poll();
			if (v[current])continue;

			v[current] = true ;
			if (target == current ) 
				return d[current];
			
			else for (Edge edge : edgeList) {
				if (!v[edge.to]){
					if (edge.cost + d[edge.from] < d[edge.to]){
						d[edge.to] = edge.cost + d[edge.from];
						q.add(edge.to);
						p[edge.to] = edge.from;
					}
				}
			}
		}
		
		return -1;
	}

	public static int dijkstra(int source,  int target, int[][] adjMat){
		d = new int[adjMat.length];
		p = new int[adjMat.length];
		v = new boolean[adjMat.length];
		Arrays.fill(d, 1<<25);
		PriorityQueue<Integer> q = new PriorityQueue<Integer>(adjMat.length,comp);
		q.add(source);
		d[source] = 0;
		
		while (!q.isEmpty()){
			
			int current = q.poll();
			if (v[current])continue;
			v[current] = true ;
			
			if (target == current ) 
				return d[current];
			
			else for (int i =0; i<adjMat.length ; i++){
				if (!v[i] && adjMat[current][i]!=0){
					if (d[current]+adjMat[current][i] < d[i])
						d[i] = d[current]+adjMat[current][i];//Relaxation
						q.add(i);
						p[i] =current;
				}
			}
			
		}
		return -1;
	}
	
	public static String getPath(int source, int target){
		String s= "";
		int i = target;
		int c = 0;
		while (i!=source && c<=p.length){
			s=i+"-"+s;
			i = p[i];
		c++;
		}
		if (c >= p.length)return "No Path";
		return source+"-"+s.substring(0,s.length()-1);
	}
	
	public static void main(String[] args) {
//		int map[][] = {{ 0,  7,  9,  0 ,  0, 14},
//			 { 7,  0, 10, 15 ,  0,  0},
//			 { 9, 10,  0, 11 ,  0,  2},
//			 { 0, 15, 11,  0 ,  6,  0},
//			 { 0,  0,  0,  6 ,  0,  9},
//			 {14,  0,  2,  0 ,  9,  0}};
		
		int map[][] = {{0,0,0,0,1},
						{0,0,0,3,6},
						{6,2,0,7,0},
						{0,0,0,0,5},
						{0,0,0,0,0}};
		
		ArrayList<Edge> edgeList = new ArrayList<Edge>();
		for (int i = 0; i < map.length; i++) 
			for (int j = 0; j < map.length; j++)
				if(map[i][j]!=0)
				edgeList.add(new Edge(i,j,map[i][j]));

//				int t = dijkstra(2, 4,map);
		int t = dijkstra(2,4,map.length,edgeList);
		System.out.println(t);
		System.out.println(getPath(2, 4));
	}
	


}
