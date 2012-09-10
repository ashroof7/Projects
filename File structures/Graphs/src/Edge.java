
public class Edge implements Comparable<Edge>{
		int from,to,cost;
		
		public Edge(int from, int to, int cost) {
			this.from = from;
			this.to = to;
			this.cost = cost;
		}
		
		@Override
		public int compareTo(Edge o) {
			if (cost > o.cost)return 1;
			else if (cost< o.cost)return -1;
			else return 0;
		}
	}