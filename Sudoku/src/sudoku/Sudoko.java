package sudoku;

import java.util.LinkedList;

/**
 *
 * @author Ashraf Saleh
 * @version 1.0
 */
public class Sudoko {

    public static int map[] = new int[81];
    public static LinkedList<int[]> sols = new LinkedList<int[]>();
    static int foundSols = 0;
    static int hintCell = -1;
    static int validSol = -1;
    static boolean terminate = false;

    public static void deleteResources() {
        // a method to clean the gsmse resources before starting a new game
        map = new int[81];
        sols = new LinkedList<int[]>();
        foundSols = 0;
        hintCell = -1;
        validSol = -1;
        terminate = false;
        System.gc();
    }

    public static void solveOnce(int n) {
       // to solve the board once to generate a hint
        if (n >= 81) {
            terminate = true;
            validSol = map[hintCell];
            return;
        } else {
            if (map[n] != 0) {
                solveOnce(n + 1);
            } else {
                for (int k = 1; k <= 9 && !terminate; k++) {
                    if (valid(n, k)) {
                        map[n] = k;
                        solveOnce(n + 1);
                        if (terminate)return;
                        map[n] = 0;
                    }
                }
            }
        }

    }

    public static int hint(int cell) {
        // return a hint
        hintCell = cell;
        validSol = -1;
        terminate = false;
        solveOnce(0);
        return validSol;
    }

    public static boolean valid(int cell, int n) {
       // returns true if the number n is assigned to the cell 'cell' is valid, false otherwise
        int y = cell / 9;
        int x = cell % 9;
//check horizontal and vertical
        for (int i = 0; i < 9; i++) {
            if (map[i * 9 + x] == n || map[y * 9 + i] == n) {
                return false;
            }
        }
//        check within block
        int dx = (x / 3) * 3;
        int dy = (y / 3) * 3;
        for (int k = 0; k < 3; k++) {
            for (int k2 = 0; k2 < 3; k2++) {
                if (map[(k + dy) * 9 + k2 + dx] == n) {
                    return false;
                }
            }
        }

        return true;
    }

    private static void putRandom(int cell) {
        // put about 20 random non-conflicting cell in an empty puzzle, used to generate a new puzzle
        if (cell == 81) {
            solve(0);
        } else {
            int temp = 0;
            do {
                temp = (int) (Math.round(Math.random()) * Math.random() * 9);
            } while (!valid(cell, temp) && temp != 0);
            map[cell] = temp;
            putRandom(cell + 1);
            map[cell] = 0;

        }
    }

    public static void solve(int n) {
     // solves the puzzles and put the solutions in the list 'sols'
        if (foundSols >= 50) {
            return;
        }
        if (n >= 81) {
            sols.addLast(map.clone());
            foundSols++;
            return;
        } else {
            if (map[n] != 0) {
                solve(n + 1);
            } else {
                for (int k = 1; k <= 9; k++) {
                    if (valid(n, k)) {
                        map[n] = k;
                        solve(n + 1);
                        if (foundSols >= 50) return;
                        map[n] = 0;
                    }
                }
            }
        }
    }

    public static int[] generate() {
// to generate a new puzzle
        int s = 0;
        do {
            putRandom(0);
            s = sols.size();
        } while (s == 0);
        s = (int) (s * Math.random());
        int[] temp = sols.get(s);
        int readyMap[] = new int[81];
        for (int i = 0; i < 81; i++) {
            if (i * Math.random() / 1.1 > i * 1.7 / 3) {
                readyMap[i] = temp[i];
            }
        }
        return readyMap;
    }

}
