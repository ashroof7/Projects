package findthebomb;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Queuealgo {
	point t;
	char[][][] map;
	boolean[][][] visited;
	boolean found;
	point target;
	QueueArray<point> q= new QueueArray<point>(300); 
	int[] a = new int[3];
	boolean [] levels;
	 public static Stack<point> final1=new Stack<point>();
	 public static Stack<Character>  symbols=new Stack<Character>();

	public Queuealgo(char[][][] c,boolean [] k, int f,int i,int j) {
		found = false;
		levels=k;
		map = c;
		t = new point(f, i, j);
	}

	public void copy() {
		visited = new boolean[map.length][][];
                for (int f = 0; f < map.length; f++) {
			visited[f] = new boolean[map[f].length][map[f].length];
			for (int i = 0; i < map[f].length; i++) {
				for (int j = 0; j < map[f][i].length; j++) {
					if (map[f][i][j] == 'x') {
						visited[f][i][j] = true;
					}
				}
			}
		}
	}

	public point getindex(char c, int x) {
		boolean found = false;
		int i = 0;
		int j = 0;
		int f = x;
		point v=new point();
		while (i < map[f].length && !found) {
			j = 0;
			while (j < map[f][i].length && !found) {
				if (map[f][i][j] == c) {
					v.setdata(f,i,j);
				}
				j++;
			}
			i++;
		}
		return v;
	}

	public void path () throws Exception
	{
		
		point a2=target;
		int f,f1,i,i1,j,j1;
		while (a2.getnext()!=null)
		{
			point a1=a2.getnext();
			final1.push(a2);
			f=a1.getlevel();
			f1=a2.getlevel();
			i=a1.getindex1();
			i1=a2.getindex1();
			j=a1.getindex2();
			j1=a2.getindex2();
			if (map[f1][i1][j1]<57 && map[f1][i1][j1]>48)
			{
				
			}else if (f==f1 && i-i1==1 && j1-j==1)
			{
				map[f1][i1][j1]='k';
			}else if (f==f1 && i-i1==1 && j-j1==1)
			{
				map[f1][i1][j1]='h';
			}else if (f==f1 && i1-i==1 && j-j1==1)
			{
				map[f1][i1][j1]='j';
			}else if (f==f1 && i1-i==1 && j1-j==1)
			{
				map[f1][i1][j1]='l';
			}else if(f==f1 && i==i1 && j1-j==1)
			{
				map[f1][i1][j1]='e';
			}else if (f==f1 && i-i1==1 && j==j1)
			{
				map[f1][i1][j1]='n';
			}else if (f==f1 && i==i1 && j-j1==1)
			{
				map[f1][i1][j1]='w';
			}else if (f==f1 && i1-i==1 && j==j1)
			{
				map[f1][i1][j1]='s';
			}
			a2=a1;
			symbols.push((char)map[a2.getlevel()][a2.getindex1()][a2.getindex2()]);
		}
		final1.push(a2);
		symbols.push((char)map[a2.getlevel()][a2.getindex1()][a2.getindex2()]);
		
	}
	
	public void print() throws IOException
	{
		/*for (int f = 0; f < map.length; f++) {
			for (int i = 0; i < map[f].length; i++) {
				for (int j = 0; j < map[f][i].length; j++) {
					System.out.print(map[f][i][j]+"  ");
				}
				System.out.println();
			}
			System.out.println("\n\n");
		}
		*/
		BufferedWriter br=new BufferedWriter(new FileWriter("try.txt"));
		for (int f = 0; f < map.length; f++) {
			for (int i = 0; i < map[f].length; i++) {
				for (int j = 0; j < map[f][i].length; j++) {
					br.write(map[f][i][j]);
				}
				br.newLine();
			}
			br.newLine();
			br.newLine();
			br.newLine();
		}
		br.close();
	}
	
	public boolean valid(int f, int i, int j, point n) throws QueueFullException {
		int x = visited[f][i].length - 1;
		boolean valid = false;
		if (j != x && !visited[f][i][j + 1]) // check right
		{
			point n1 = new point(f,i,(j + 1));
			n1.setnext(n);
			q.enqueue(n1);
			valid = true;
			visited[f][i][j + 1] = true;
			if (map[f][i][j + 1] == 'V' || map[f][i][j + 1] == 'B') {
				found = true;
				target = n1;
				return true;
			}
		}
		if (i != 0 && !visited[f][i - 1][j]) // check up
		{
			point n1 = new point(f,(i - 1),j);
			n1.setnext(n);
			q.enqueue(n1);
			valid = true;
			visited[f][i - 1][j] = true;
			if (map[f][i - 1][j] == 'V' || map[f][i - 1][j] == 'B') {
				found = true;
				target = n1;
				return true;
			}

		}
		if (j != 0 && !visited[f][i][j - 1]) // check left
		{
			point n1 = new point(f,i,(j - 1));
			n1.setnext(n);
			q.enqueue(n1);
			valid = true;
			visited[f][i][j - 1] = true;
			if (map[f][i][j - 1] == 'V' || map[f][i][j - 1] == 'B') {
				found = true;
				target = n1;
				return true;
			}

		}
		if (i != x && !visited[f][i + 1][j]) // check down
		{
			point n1 = new point(f,(i + 1),j);
			n1.setnext(n);
			q.enqueue(n1);
			valid = true;
			visited[f][i + 1][j] = true;
			if (map[f][i + 1][j] == 'V' || map[f][i + 1][j] == 'B') {
				found = true;
				target = n1;
				return true;
			}
		}
		if (j != x && i != 0 && !visited[f][i - 1][j + 1]) // check right up
		{
			point n1 = new point(f,(i - 1),(j + 1));
			n1.setnext(n);
			q.enqueue(n1);
			valid = true;
			visited[f][i - 1][j + 1] = true;
			if (map[f][i - 1][j + 1] == 'V' || map[f][i - 1][j + 1] == 'B') {
				found = true;
				target = n1;
				return true;
			}
		}
		if (j != 0 && i != 0 && !visited[f][i - 1][j - 1]) // check left up
		{
			point n1 = new point(f,(i - 1),(j - 1));
			n1.setnext(n);
			q.enqueue(n1);
			valid = true;
			visited[f][i - 1][j - 1] = true;
			if (map[f][i - 1][j - 1] == 'V' || map[f][i - 1][j - 1] == 'B') {
				found = true;
				target = n1;
				return true;
			}
		}
		if (j != 0 && i != x && !visited[f][i + 1][j - 1]) // check left down
		{
			point n1 = new point(f,(i + 1),(j - 1));
			n1.setnext(n);
			q.enqueue(n1);
			valid = true;
			visited[f][i + 1][j - 1] = true;
			if (map[f][i + 1][j - 1] == 'V' || map[f][i + 1][j - 1] == 'B') {
				found = true;
				target = n1;
				return true;
			}
		}
		if (j != x && i != x && !visited[f][i + 1][j + 1]) // check right down
		{
			point n1 = new point(f,(i + 1),(j + 1));
			n1.setnext(n);
			q.enqueue(n1);
			valid = true;
			visited[f][i + 1][j + 1] = true;
			if (map[f][i + 1][j + 1] == 'V' || map[f][i + 1][j + 1] == 'B') {
				found = true;
				target = n1;
				return true;
			}
		}
		if (map[f][i][j] < 57 && map[f][i][j] >= 48 && levels[map[f][i][j] - 48]) {
			valid = true;
			int x1=0;
			int count=0;
			int Currentlevel=0;
			while (count!=(f+1))
			{
				if (levels[Currentlevel])
				{
					count++;
				}
				Currentlevel++;
			}
			count=0;
			while (x1!=(map[f][i][j] - 48))
			{
				if (levels[x1])
				{
					count++;
				}
				x1++;
			}
			
			point z=getindex((char) (Currentlevel + 47), count);
			point n1 = new point(z.getlevel(), z.getindex1(), z.getindex2());
			n1.setnext(n);
			q.enqueue(n1);
			visited[z.getlevel()][z.getindex1()][z.getindex2()] = true;
		}
		return valid;
	}

	public void algorithm() throws Exception {
		copy();
		q.enqueue(t);
		visited[t.getlevel()][t.getindex1()][t.getindex2()] = true;
		while (!q.isEmpty() && !found) {
			t = (point) q.dequeue();
			valid(t.getlevel(), t.getindex1(), t.getindex2(), t);
		}
		if (found) {
			/*while (target != null) {
				System.out.println("" + target.getdata());
				target = target.getnext();
			}
			*/
			path();
			print();
		} else {
			System.out.println("no path found");
		}

	}

}
