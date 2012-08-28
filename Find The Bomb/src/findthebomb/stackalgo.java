package findthebomb;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class stackalgo {

	char[][][] map;
	boolean[][][] visited;
	   Stack<point> S = new Stack<point>();
	point t;
	int k;
	boolean[] levels;
	public static Stack<point> final1 = new Stack<point>();
	public static Stack<Character> symbols = new Stack<Character>();

	public  stackalgo(char[][][] c, boolean[] k, int f, int i, int j) {
		map = c;
		levels = k;
		t = new point(f, i, j);
	}

	public point getindex(char c, int x) {
		boolean found = false;
		int i = 0;
		int j = 0;
		int f = x;
		point v = new point();
		while (i < map[f].length && !found) {
			j = 0;
			while (j < map[f][i].length && !found) {
				if (map[f][i][j] == c) {
					v.setdata(f, i, j);
					found = true;
				}
				j++;
			}
			i++;
		}
		return v;
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

	public void print() throws IOException
	{
		
		BufferedWriter br=new BufferedWriter(new FileWriter("try1.txt"));
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

	public boolean valid(int f, int i, int j) {
		int x1 = visited[f][i].length - 1;
		boolean valid = false;
		if (j != x1 && !visited[f][i][j + 1]) // check right
		{
			valid = true;
			t = new point(f, i, (j + 1));
		} else if (i != 0 && !visited[f][i - 1][j]) // check up
		{
			valid = true;
			t = new point(f, (i - 1), j);
		} else if (j != 0 && !visited[f][i][j - 1]) // check left
		{
			valid = true;
			t = new point(f, i, (j - 1));
		} else if (i != x1 && !visited[f][i + 1][j]) // check down
		{
			valid = true;
			t = new point(f, (i + 1), j);
		} else if (j != x1 && i != 0 && !visited[f][i - 1][j + 1]) // check
																	// right up
		{
			valid = true;
			t = new point(f, (i - 1), (j + 1));
		} else if (j != 0 && i != 0 && !visited[f][i - 1][j - 1]) // check left
																	// up
		{
			valid = true;
			t = new point(f, (i - 1), (j - 1));
		} else if (j != 0 && i != x1 && !visited[f][i + 1][j - 1]) // check left
																	// down
		{
			valid = true;
			t = new point(f, (i + 1), (j - 1));
		} else if (j != x1 && i != x1 && !visited[f][i + 1][j + 1]) // check
																	// right
																	// down
		{
			valid = true;
			t = new point(f, (i + 1), (j + 1));
		} else if (map[f][i][j] < 57 && map[f][i][j] >= 48
				&& levels[map[f][i][j] - 48]) {
			valid = true;
			int x = 0;
			int count = 0;
			int Currentlevel = 0;
			while (count != (f + 1)) {
				if (levels[Currentlevel]) {
					count++;
				}
				Currentlevel++;
			}
			count = 0;
			while (x != (map[f][i][j] - 48)) {
				if (levels[x]) {
					count++;
				}
				x++;
			}

			point z = getindex((char) (Currentlevel + 47), count);
			t = new point(z.getlevel(), z.getindex1(), z.getindex2());
		}
		return valid;
	}

	public void path() throws Exception {

		int f, f1, i, i1, j, j1;
		while (S.size() > 1) {

			point a2 = (point) S.pop();
			point a1 = (point) S.peek();
			final1.push(a2);
			f = a1.getlevel();
			f1 = a2.getlevel();
			i = a1.getindex1();
			i1 = a2.getindex1();
			j = a1.getindex2();
			j1 = a2.getindex2();
			if (map[f1][i1][j1] < 57 && map[f1][i1][j1] > 48) {

			} else if (f == f1 && i - i1 == 1 && j1 - j == 1) {
				map[f1][i1][j1] = 'k';
			} else if (f == f1 && i - i1 == 1 && j - j1 == 1) {
				map[f1][i1][j1] = 'h';
			} else if (f == f1 && i1 - i == 1 && j - j1 == 1) {
				map[f1][i1][j1] = 'j';
			} else if (f == f1 && i1 - i == 1 && j1 - j == 1) {
				map[f1][i1][j1] = 'l';
			} else if (f == f1 && i == i1 && j1 - j == 1) {
				map[f1][i1][j1] = 'e';
			} else if (f == f1 && i - i1 == 1 && j == j1) {
				map[f1][i1][j1] = 'n';
			} else if (f == f1 && i == i1 && j - j1 == 1) {
				map[f1][i1][j1] = 'w';
			} else if (f == f1 && i1 - i == 1 && j == j1) {
				map[f1][i1][j1] = 's';
			}
			symbols.push((char) map[a2.getlevel()][a2.getindex1()][a2
					.getindex2()]);
		}
		point a2 = (point) S.pop();
		final1.push(a2);
		symbols.push((char) map[a2.getlevel()][a2.getindex1()][a2.getindex2()]);

	}

	public void algo() throws Exception {
		copy();
		S.push(t);
		visited[t.getlevel()][t.getindex1()][t.getindex2()] = true;

		while (!S.isEmpty()
				&& map[t.getlevel()][t.getindex1()][t.getindex2()] != 'B'
				&& map[t.getlevel()][t.getindex1()][t.getindex2()] != 'V') {
			if (!valid(t.getlevel(), t.getindex1(), t.getindex2())) {
				S.pop();
				if (!S.isEmpty()) {
					t = (point) S.peek();
				}
			} else {
				S.push(t);
				visited[t.getlevel()][t.getindex1()][t.getindex2()] = true;
			}

		}
		if (map[t.getlevel()][t.getindex1()][t.getindex2()] == 'B'
				|| map[t.getlevel()][t.getindex1()][t.getindex2()] == 'V') {
			path();
			print();
		} else {
			System.out.println("no path found");
		}

	}

}
