import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class GraphReader {

	private BufferedReader br;
	boolean directed = false;
	int[][] adjMat;

	public GraphReader() throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		
		String type  = br.readLine(); 
		if (type.equals("DIRECTED"))
			directed = true;
		else if (type.equals("UNDIRECTED"))
			directed = false ;
		else 
			throw new IOException("invalid graph type");
		
		ArrayList<String> buffer  = new ArrayList<String>();

		while(br.ready())
			buffer.add(br.readLine());
		buffer.add(br.readLine());

		int n = buffer.size();
		adjMat = new int[n][n];
		StringTokenizer tk;
		for (int i = 0; i < n; i++) {
			tk = new StringTokenizer(buffer.get(i));
			for (int j = 0; j < n; j++) {
				adjMat[i][j] = Integer.parseInt(tk.nextToken());
			}
		}
	}

	public static void main(String[] args) throws IOException {
		// DIRECTED
		// 0 100 200 50
		// 0 0 75 0
		// 0 0 0 0
		// 0 45 85 0

		GraphReader r = new GraphReader();
		for (int i = 0; i < r.adjMat.length; i++)
			System.out.println(Arrays.toString(r.adjMat[i]));

	}

}
