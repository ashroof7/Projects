import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class FReader {

	BufferedReader br ;
	
	ArrayList<Record> records = new ArrayList<Record>();
	
	
//	@SuppressWarnings("deprecation")
	public FReader() throws IOException {
		// TODO Auto-generated constructor stub
//		FileDialog fc = new FileDialog(new Frame());
//		fc.show();
		
//		br = new BufferedReader(new FileReader(fc.getFile()+fc.getName()));
		br = new BufferedReader(new FileReader("Radwan.txt"));
		String s ;
		while (true){
			s = br.readLine();
			if (s==null || s.isEmpty())
				break;
			readRecord(s);
		}
	}
	
	private void readRecord(String s){
		StringTokenizer tk = new StringTokenizer(s);
		Record rec = new Record(tk.nextToken(),
				tk.nextToken(), tk.nextToken(), tk.nextToken(),
				Integer.parseInt(tk.nextToken()));
		records.add(rec);
	}
	
	public ArrayList<Record> getRecords() {
		return records;
	}


}
