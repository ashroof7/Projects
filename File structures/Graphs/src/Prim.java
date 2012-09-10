import java.util.ArrayList;
import java.util.PriorityQueue;

public class Prim {

	
	
	public static int prim(int n, ArrayList<Edge> edgeList){
		PriorityQueue<Edge> q = new PriorityQueue<Edge>();
		boolean []v = new boolean[n];
		int mspCost = 0 ;
		
		// insert the first node
		for (Edge edge : edgeList) {
			if (edge.from == 0)
				q.add(edge);
		}
		v[0] = true ;

		
		
		while(!q.isEmpty()){
			Edge e = q.poll();
			int currentNode = e.to;

			if (!v[currentNode]){
				mspCost+=e.cost;
				System.out.println((e.from)+"--"+(e.to));
				for (Edge edge : edgeList) {
					if (edge.from == currentNode)
						q.add(edge);
				}
				v[currentNode] = true ;
			}
		}
		return mspCost;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Edge> edges = new ArrayList<Edge>();
		edges.add(new Edge(0, 1, 4));
		edges.add(new Edge(0, 4, 6));
		edges.add(new Edge(0, 3, 6));
		edges.add(new Edge(0, 2, 4));
		edges.add(new Edge(1, 2, 2));
		edges.add(new Edge(2, 3, 8));
		edges.add(new Edge(3, 4, 9));
		
		System.out.println(prim(5, edges));
	}

}
