import java.io.IOException;


public class Main {
	public static int bufferSize = 1048576;
	public static String compressed = "res.leo";
//	public static String source = "/home/leonardo/Desktop/embers.mkv";
//	public static String source = "res";
//	public static String source = "/home/leonardo/Desktop/PACMAN.EXE";
//	public static String source = "/home/leonardo/Desktop/fruitNinja.exe";
	public static String source = "a.txt";
	public static String dest = "result";
	public static String ext = ".txt";
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Encoder e = new Encoder();
		
		long a = System.nanoTime();
		
		e.read();
		e.calcFreq();
		e.write();

		System.out.println("encoding finished in "+(System.nanoTime()-a)/1000000+" ms");
		a = System.nanoTime();
		
		Decoder d = new Decoder();
		d.read();
		System.out.println("decoding finished in "+(System.nanoTime()-a)/1000000+" ms");
	}
	
}
