import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;


public class TopologicalSort {

	
	public static String topologicalSort(boolean[][] adjMat){
		String sort ="[";
		int [] commingEdges = new int[adjMat.length] ;
		
		for (int i = 0; i < commingEdges.length; i++) {
			for (int j = 0; j < commingEdges.length; j++) {
				if (adjMat[i][j]) 
				commingEdges[j]++;
			}
		}
		
		
		Queue<Integer> q = new LinkedList<Integer>();
		
		for (int i = 0; i < commingEdges.length; i++) 
			if (commingEdges[i]==0)q.add(i);
		
		int current ;

		while (!q.isEmpty()){
			current = q.poll();
			 sort+=current+", ";
			 
			 for (int i = 0; i < adjMat.length; i++) {
				if (adjMat[current][i]){
					commingEdges[i]--;
					if (commingEdges[i]==0)
						q.add(i);
				}
				
			}
			
		}
		sort = sort.substring(0,sort.length()-2)+"]";
		return sort ;
	}
	
	 public static void main(String[] args) throws IOException {
			
		 GraphReader r = new GraphReader();
		 int [][] adjMat = r.adjMat;
		 boolean[][] mat = new boolean[adjMat.length][adjMat.length];
			
			
			for (int i = 0; i < adjMat.length; i++) {
				for (int j = 0; j < adjMat.length; j++){
					if(adjMat[i][j]!=0){
					mat[i][j]=adjMat[i][j]==0?false:true;
					}
				}
			}
		 
			System.out.println(topologicalSort(mat));	 
	 }

}
