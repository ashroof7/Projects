import java.awt.FileDialog;
import java.io.File;
import java.util.Scanner;

import javax.swing.JFrame;


public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		FileDialog fd = new FileDialog(new JFrame(), "BST input file ",FileDialog.LOAD);
		fd.show();
		
		Scanner sc = new Scanner(new File(fd.getFile()));
		BST<Integer> bt = new BST<Integer>(sc.nextInt());
		while (sc.hasNext()) {
			bt.add(sc.nextInt());
		}
		
		System.out.println(bt.preOrder());
		System.out.println(bt.inOrder());
		System.out.println(bt.postOrder());
		System.exit(0);
	}
	
}
