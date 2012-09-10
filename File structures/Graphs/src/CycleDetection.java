import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

public class CycleDetection {

	static int[][] adjMat
	= 
//		{ { 1, 1, 1, 1 },
//		  { 1, 1, 1, 1 }, 
//		  { 1, 1, 1, 1 },
//		  { 1, 1, 1, 1 }};
	{
	{1,1,1,1,1,1,1,1},
	{1,1,1,1,1,1,1,1},
	{1,1,1,1,1,1,1,1},
	{1,1,1,1,1,1,1,1},
	{1,1,1,1,1,1,1,1},
	{1,1,1,1,1,1,1,1},
	{1,1,1,1,1,1,1,1},
	{1,1,1,1,1,1,1,1}};
//	
// {{0, 1, 0, 0, 0, 0, 0, 0},
//  {1, 0, 1, 1, 0, 0, 0, 0},
//  {0, 1, 0, 1, 0, 0, 0, 0},
//  {0, 1, 1, 0, 1, 0, 0, 0},
//  {0, 0, 0, 1, 0, 1, 1, 0},
//  {0, 0, 0, 0, 1, 0, 0, 1},
//  {0, 0, 0, 0, 1, 0, 0, 1},
//  {0, 0, 0, 0, 0, 1, 1, 0}};

	static BitSet v = new BitSet();
	static ArrayList<String> cycles = new ArrayList<String>();
	static HashSet<Integer> masks = new HashSet<Integer>();
	static ArrayList<ArrayList<Integer>> memo = new ArrayList<ArrayList<Integer>>();
	
	public static void getCyclesDirected(int node, ArrayList<Integer> v,
			int mask) {
			if ((mask & (1 << node)) != 0 ) {
			
				int cycle = 0;
				ArrayList<Integer> loop = new ArrayList<Integer>();//carry the current cycles 

				// reconstruct cycle
				for (int i = v.indexOf(node); i < v.size(); i++) {
					cycle |= (1 << v.get(i));
					loop.add(v.get(i));
				}
				
				if (Integer.bitCount(cycle) == 2)//if loop is of 2 vertices ignore
					return;
				if (masks.contains(cycle)) {
					return;
//					 if (Integer.bitCount(cycle)==3)// if # vertices = 3 then loop must be a duplicate
//					 return;
//					for (ArrayList<Integer> oldCycle : memo)//loop over old cycles 
//						if (oldCycle.size() == loop.size() && areSimilar(oldCycle, loop))
//								return;
				}
				
				
				masks.add(cycle);
				memo.add(loop);

			} else {
				mask |= (1 << node);
				v.add(node);
				for (int i = 0; i < adjMat.length; i++) 
					if (adjMat[node][i] != 0 && node != i) 
						getCyclesUndirected(i, v, mask);
				v.remove(new Integer(node));
			}
		}

		
	
	public static void getCyclesUndirected(int node, ArrayList<Integer> v,
		int mask) {
		if ((mask & (1 << node)) != 0 ) {
		
			int cycle = 0;
			ArrayList<Integer> loop = new ArrayList<Integer>();//carry the current cycles 

			// reconstruct cycle
			for (int i = v.indexOf(node); i < v.size(); i++) {
				cycle |= (1 << v.get(i));
				loop.add(v.get(i));
			}
			
			if (Integer.bitCount(cycle) == 2)//if loop is of 2 vertices ignore
				return;
			if (masks.contains(cycle)) {
//				 if (Integer.bitCount(cycle)==3)// if # vertices = 3 then loop must be a duplicate
//				 return;
				for (ArrayList<Integer> oldCycle : memo)//loop over old cycles 
					if (oldCycle.size() == loop.size() && areSimilar(oldCycle, loop))
							return;
			}
			
			
			masks.add(cycle);
			memo.add(loop);

		} else {
			mask |= (1 << node);
			v.add(node);
			for (int i = 0; i < adjMat.length; i++) 
				if (adjMat[node][i] != 0 && node != i) 
					getCyclesUndirected(i, v, mask);
			v.remove(new Integer(node));
		}
	}

	
	
 	private static boolean areSimilar(ArrayList<Integer> a, ArrayList<Integer> b) {
		int bPivot = b.indexOf(a.get(0));
		if (bPivot == -1 ) return false ;
		if (a.size() <= 3 || b.size() <= 3)
			return true;
	
		if (a.equals(b))return true;

		for (int i = 0; i < a.size(); i++) {
			if (a.get(i) != b.get(bPivot))
				return false;
			bPivot = bPivot - 1 < 0 ? a.size() - 1 : bPivot - 1;
		}
		return true;
	}

	public static void main(String[] args) throws IOException {
		 GraphReader r = new GraphReader();
		 adjMat = r.adjMat;
//		 getCyclesDirected(0, "");
//		int v[] = new int[8];
//		Arrays.fill(v, -1);
//		long a = System.nanoTime();
//		getCyclesUndirected(0, new ArrayList<Integer>(), 0);
//		System.out.println((System.nanoTime()-a)/1000000000D);
		// System.out.println(cycles);
//		System.out.println(memo);

		 getCyclesUndirected(0,new ArrayList<Integer>(),0);
		 System.out.println(memo);
		// DIRECTED
		// 0 1 0 0 0 0 0 0
		// 0 0 0 1 0 0 0 0
		// 0 1 0 0 0 0 0 0
		// 0 0 1 0 1 0 0 0
		// 0 0 0 0 0 1 0 0
		// 0 0 0 0 0 0 0 1
		// 0 0 0 0 1 0 0 0
		// 0 0 0 0 0 0 1 0

		// UNDIRECTED
		// 0 1 0 0 0 0 0 0
		// 1 0 1 1 0 0 0 0
		// 0 1 0 1 0 0 0 0
		// 0 1 1 0 1 0 0 0
		// 0 0 0 1 0 1 1 0
		// 0 0 0 0 1 0 0 1
		// 0 0 0 0 1 0 0 1
		// 0 0 0 0 0 1 1 1

	}

	
	
	
	
	//these methods are not running :: trying to reduce order
	public static void test(int node, ArrayList<Integer> v,int mask) {
		System.out.println(node+"  "+v);
		
		mask|=(1<<node);
		for (int i = 0; i < adjMat.length; i++) {
			if (adjMat[node][i]!=0){
				if ((mask&(1<<i))==0){//not visited
					if (v.isEmpty() && v.get(0)!=node)
						continue ;
					v.add(i);
					test(i,v,mask);
					v.remove(new Integer(i));
				}else {
					if ( v.size()!=2 && v.get(0) == i )
						System.out.println("cycle");
				}
			}
		}
	}

	public static void test2(int node, ArrayList<Integer> v,
			int mask) {
//			mask |= (1 << node);
			v.add(node);
			l:for (int i = 0; i < adjMat.length; i++) 
				if (adjMat[node][i] != 0 )
					if ((mask&(1<<i))==0){
						test2(i, v, mask|=(1<<i));
					}
					else {//cycle detected
						 if (v.contains(i)	&& v.get(v.size()-2)!=i){
//							System.out.println(i + "   " +v);

							int cycle = 0;
							ArrayList<Integer> loop = new ArrayList<Integer>();//carry the current cycles 

							// reconstruct cycle
							for (int k = v.indexOf(i); k < v.size(); k++) {
								cycle |= (1 << v.get(k));
								loop.add(v.get(k));
							}
							
							if (masks.contains(cycle)) {
								 if (Integer.bitCount(cycle)==3)// if # vertices = 3 then loop must be a duplicate
									 continue l;
								for (ArrayList<Integer> oldCycle : memo){//loop over old cycles
									if (oldCycle.size() == loop.size() &&	isEqual(oldCycle, loop))
											continue l;
								}
							}
							
							masks.add(cycle);
							memo.add(loop);
							
						}
					}
//			mask|=~(1<<node);
			v.remove(new Integer(node));
		}
	
	private static boolean isEqual(ArrayList<Integer> a, ArrayList<Integer> b) {
		int bPivot = b.indexOf(a.get(0));
		if (bPivot == -1 ) return false ;
		if (a.equals(b))return true;
		for (int i = 0; i < a.size(); i++) {
			if (a.get(i) != b.get(bPivot))
				return false;
			bPivot = bPivot - 1 < 0 ? a.size() - 1 : bPivot - 1;
		}
		return true;
	}

}
