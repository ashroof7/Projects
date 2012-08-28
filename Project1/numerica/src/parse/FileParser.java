package parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import function.Function;

public class FileParser {

	int maxIt ;
	double prec,startPt,endPt;
	Function fn;
	PointParser pp ;
	String points;
	
	public FileParser(File f) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(f)); 
		Parser p = new Parser(br.readLine());
		fn = p.getPoly();
		solveInfo(br.readLine());
		
		String s;
		StringBuilder sb = new StringBuilder();
		while (true){
			s = br.readLine();
			if (s.isEmpty() || s==null)
				break;
			sb.append(s.trim());
			sb.append(",\n");
		}
		points = sb.toString();
		pp = new PointParser(points);
	}
	
		
	private void solveInfo(String s){
		String[] ss = s.split(",");
		double[] d = new double[4];
		
		for (int i = 0; i < ss.length; i++) {
			if (ss[i].trim().isEmpty())continue;
			d[i] = Double.parseDouble(ss[i]);
		}
		maxIt = (int) d[0];
		prec = d[1];
		startPt = d[2];
		endPt = d[3];
	}

	public int getMaxIt() {
		return maxIt;
	}

	public void setMaxIt(int maxIt) {
		this.maxIt = maxIt;
	}

	public double getPrec() {
		return prec;
	}

	public void setPrec(double prec) {
		this.prec = prec;
	}

	public double getStartPt() {
		return startPt;
	}

	public void setStartPt(double startPt) {
		this.startPt = startPt;
	}

	public double getEndPt() {
		return endPt;
	}

	public void setEndPt(double endPt) {
		this.endPt = endPt;
	}

	public Function getFn() {
		return fn;
	}

	public void setFn(Function fn) {
		this.fn = fn;
	}


	public PointParser getPp() {
		return pp;
	}

	public void setPp(PointParser pp) {
		this.pp = pp;
	}


	public String getPoints() {
		return points;
	}


	public void setPoints(String points) {
		this.points = points;
	}

	
}
