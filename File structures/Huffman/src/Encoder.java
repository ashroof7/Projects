import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class Encoder {

	private long[] freq = new long[256];
	private PriorityQueue<AbstNode<Long>> pq = new PriorityQueue<AbstNode<Long>>();

	public HashMap<Byte, Long> codes = new HashMap<Byte, Long>(257);
	public HashMap<Byte, Byte> codelen = new HashMap<Byte, Byte>(257);
	
	public BufferedInputStream br;
	private byte buffer[] = new byte[Main.bufferSize];
	long bytesNo = 0 ;

	public Encoder() throws FileNotFoundException {
		br = new BufferedInputStream(new FileInputStream(Main.source));
	}

	@SuppressWarnings("unchecked")
	public void encode(long code, AbstNode<Long> node, int len) {
		if (node.isLeaf()) {
			codes.put(((Leaf<Long, Byte>) node).getElement(), code);
			codelen.put(((Leaf<Long, Byte>) node).getElement(), (byte) len);
		} else {
			Node<Long> temp = (Node<Long>) node;
			encode(((code << 1) | 1), temp.getRight(), len + 1);
			encode((code << 1), temp.getLeft(), len + 1);
		}

	};

	public void read() throws IOException {
		int readBytes;

		while (true) {
			readBytes = br.read(buffer);
			if (readBytes == -1)
				break;
			bytesNo += readBytes;
			for (int i = 0; i < readBytes; i++){
				if(buffer[i]>=0)
					freq[buffer[i]]++;
				else 
					freq[buffer[i]+256]++;
			}
			
		}
	}

	public void calcFreq() {
		for (int i = 0; i < freq.length; i++) {
			if (freq[i] == 0)
				continue;
			pq.add(new Leaf<Long, Byte>(freq[i], (byte) i));
		}

		Node<Long> tree;
		AbstNode<Long> temp1;
		AbstNode<Long> temp2;

		while (pq.size() != 1) {
			temp1 = pq.poll();
			temp2 = pq.poll();
			
			tree = new Node<Long>(temp1.getKey() + temp2.getKey());
			tree.setChildren(temp1, temp2);
			pq.add(tree);
		}
		encode(0, pq.poll(), 0);
	}
	
 	public void write() throws IOException {
		BufferedOutputStream bw = new BufferedOutputStream(
				new FileOutputStream(Main.compressed));
		br = new BufferedInputStream(new FileInputStream(Main.source));
		
		
		ObjectOutputStream ob = new ObjectOutputStream(bw);
		ob.writeLong(bytesNo);
		ob.writeObject(freq);
		
		int readBytes;
		long code;
		int len;
		byte[] outBuffer = new byte[Main.bufferSize];
		byte currentByte = 0;
		int leftLen = 8;
		int count = 0;
		int i = 0;
		
		readBytes = br.read(buffer);
		code = codes.get(buffer[i]);
		len = codelen.get(buffer[i]);
		while (true) {
				if (len <= leftLen) {
					
					code = code << (64 - len) ;
					code = code >>> (64 - len) ; 
					
					currentByte = (byte) (currentByte | code<<(leftLen - len));
					leftLen = leftLen - len;
					
					
					i++;
					if (i >= readBytes)	{
						readBytes = br.read(buffer);
						if (readBytes == -1)
							break;
						i = 0; 
					}
					code = codes.get(buffer[i]);
					len = codelen.get(buffer[i]);
					
					
					if (leftLen == 0) {
	
						if (count >= buffer.length) {
							bw.write(outBuffer);
							count = 0;
						}
	
						outBuffer[count++] = currentByte;
						currentByte = 0;
						leftLen = 8;
						
					
					}
					
	
				} else {
					currentByte = (byte) ((currentByte ) | (code >> (len - leftLen)));
					code = code << 64 - len + leftLen;
					code = code >>> 64 - len + leftLen;
					len = len - leftLen;
					leftLen = 0;
					
					
					if (count >= buffer.length) {
						bw.write(outBuffer);
						count = 0;
					}
					outBuffer[count++] = currentByte;
					
					currentByte = 0;
					leftLen = 8;
				}
			
		}
	
		outBuffer[count++] = (byte)(currentByte<<(leftLen));
		bw.write(outBuffer, 0, count);

		
		ob.close();
		bw.close();
	}
 	
 	
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
