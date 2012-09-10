import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class Decoder {

	private long[] freq = new long[256];
	private PriorityQueue<AbstNode<Long>> pq = new PriorityQueue<AbstNode<Long>>();
	public HashMap<Byte, Byte> codelen = new HashMap<Byte, Byte>(257);
	public HashMap<Long, Byte> codes = new HashMap<Long, Byte>(257);
	Node<Long> tree;

	@SuppressWarnings("unchecked")
	public void read() throws IOException, ClassNotFoundException {

		BufferedInputStream br = new BufferedInputStream(new FileInputStream(
				Main.compressed));
		BufferedOutputStream bw = new BufferedOutputStream(
				new FileOutputStream(Main.dest + Main.ext));
		ObjectInputStream ob = new ObjectInputStream(br);
		long bytesNo = ob.readLong(); 
		
		freq = (long[]) ob.readObject() ;
		calcFreq();

		byte buffer[] = new byte[Main.bufferSize];
		byte outBuffer[] = new byte[Main.bufferSize];
		int n = 0;
		int i = 0;
		int j = 0;
		int count = 0;
		byte current = 0;
		AbstNode<Long> node = tree;

		n = br.read(buffer);
		current = buffer[i++];
		
		while (true) {

			if (node.isLeaf()) {
				if(--bytesNo<0)break;
				outBuffer[count++] = ((Leaf<Long, Byte>) node).getElement();
				node = tree;
				if (count >= outBuffer.length) {
					count = 0;
					bw.write(outBuffer);
				}

			} else {
				if ((current & (1 << (7 - j))) == 0) {
					node = ((Node<Long>) node).getLeft();
					j++;
				} else {
					node = ((Node<Long>) node).getRight();
					j++;
				}
			}

			if (j >= 8) {
				current = buffer[i++];
				j = 0;

			}
			
			if (i >= buffer.length) {
				i = 0;
				n = br.read(buffer);
				if (n == -1)
					break;
			}

		}
		//should be 0 but i decrement in the if statement 
		if (bytesNo !=-1)
			count+=bytesNo+1;
		
		bw.write(outBuffer, 0, (int)(count));
		
		bw.close();
		ob.close();
		br.close();

	}

	public void calcFreq() {
		for (int i = 0; i < freq.length; i++) {
			if (freq[i] == 0)
				continue;
			pq.add(new Leaf<Long, Byte>(freq[i], (byte) i));
		}

		AbstNode<Long> temp1;
		AbstNode<Long> temp2;

		while (pq.size() != 1) {
			temp1 = pq.poll();
			temp2 = pq.poll();

			tree = new Node<Long>(temp1.getKey() + temp2.getKey());
			tree.setChildren(temp1, temp2);
			pq.add(tree);
		}
		decode(0, pq.poll(), 0);
	}

	@SuppressWarnings("unchecked")
	public void decode(long code, AbstNode<Long> node, int len) {
		if (node.isLeaf()) {
			codes.put(code, ((Leaf<Long, Byte>) node).getElement());
			codelen.put(((Leaf<Long, Byte>) node).getElement(), (byte) len);
		} else {
			Node<Long> temp = (Node<Long>) node;
			decode(((code << 1) | 1), temp.getRight(), len + 1);
			decode((code << 1), temp.getLeft(), len + 1);
		}

	};

	
	String bin(byte b){
 		String s = "";
 		for (int i = 0; i < 8; i++) {
			if((b&(1<<7-i))==0)
 			s+=0;
			else 
				s+=1;
		} 
 		return s;
 	}
 	
}
