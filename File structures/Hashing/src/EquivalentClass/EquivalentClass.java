package EquivalentClass;

import java.util.Hashtable;

public class EquivalentClass {

	int parent [];
	public EquivalentClass(int n) {
		parent = new int[n];
		for (int i = 0; i < n; i++) {
			parent[i] = i;
		}
	}

	
	public int getParent(int node){
		if (node == parent[node])
			return node;
		else
			return parent[node] = getParent(parent[node]);
	}
	
	public void union(int first, int second){
		parent[getParent(first)] = getParent(second);
	}
	
	public boolean isSameSet(int first, int second){
		return getParent(first)==getParent(second);
	}
	 
	
	public static void main(String[] args) {
		Hashtable<Character, Integer> h = new Hashtable<Character, Integer>();
		int i = 0;
//		a b c x t f n
//		t f
//		a f
//		? t a
//		f x
//		x n
//		? a n
//		? a a
//		? c a

		
		h.put('a', i++);//0
		h.put('b', i++);//1
		h.put('c', i++);//2
		h.put('x', i++);//3
		h.put('t', i++);//4
		h.put('f', i++);//5
		h.put('n', i++);//6
		
		EquivalentClass eq = new EquivalentClass(7);
		
		eq.union(h.get('t'), h.get('f'));
		eq.union(h.get('a'), h.get('f'));
		System.out.println(eq.isSameSet(h.get('t'), h.get('a')));
		eq.union(h.get('f'), h.get('x'));
		eq.union(h.get('x'), h.get('n'));
		System.out.println(eq.isSameSet(h.get('a'), h.get('n')));
		System.out.println(eq.isSameSet(h.get('a'), h.get('a')));
		System.out.println(eq.isSameSet(h.get('c'), h.get('a')));
		

	}

}