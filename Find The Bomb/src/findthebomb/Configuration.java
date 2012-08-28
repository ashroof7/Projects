package findthebomb;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Configuration {

	/**
	 * @param args
	 * @throws Exception
	 */

	public static int stairs[][] = new int [100][4];
	public static boolean levelIsFound[] = new boolean [10]; 
	public static int levelMatch[] = new int [10];
        public static int [][]bvs = new int [3][3];
	public static boolean bvsFound[]=new boolean [3];
        public static char[][][] map;
	private static int count = 0 ;
        public static String fileName  = "map.config";

        public static void browse() throws FileNotFoundException, IOException {
FileDialog f = new FileDialog(new Frame(),"Load File" , FileDialog.LOAD);
f.show();
	File t = new File(f.getFile());
//	 while(f.isActive()){System.out.println("java is active"); }
	fileName = f.getDirectory()+t.getName() ;
}
	public static void configure() throws Exception{
		Scanner sc = new Scanner(new File(fileName));
		String s = sc.nextLine();
		int l = 0;
		int d = 0;
                stairs = new int [100][4];
                count = 0 ;
		boolean bombFound = false;
		boolean startFound = false ;
		boolean victimFound = false ;
		s.replaceAll(" ", "");
		int n = Integer.parseInt(s.substring(8, s.length()));
		if (n>10) throw new Exception("levels must be less than 10 :D");
                System.out.println("n="+n);
                map = new char[n][][];
		for (int i = 0; i < n; i++) {
                    int t = 0;
			while (sc.hasNextLine() ) {
				s = sc.nextLine();
				s = s.replaceAll(" ", "");

				if (s.length()>6 && s.substring(0, 6).equals("level=")) {
					l = Integer.parseInt(s.substring(6, 7));
					levelIsFound[l] = true ;
                                        levelMatch[l] = i;
					t++;
				} else if (s.length()>10 && s.substring(0, 10).equals("dimension=")) {
					d = Integer.parseInt(s.substring(10, s.length()));
					t++;
				}
				else if (s.length()>3 && s.substring(0,4).equals("map=")){
					map[i] = new char[d][d];
					for (int j = 0; j < d; j++) {
                                            String temp = sc.nextLine();
                                       if (temp.length()<d){
                                        System.out.println("temp before = "+temp);
                                           for(int a = temp.length() ; a < d ; a++)
                                        {
                                        temp+=" ";
                                        }
                                        System.out.println("temp after = "+temp);
                                       }
                                            for (int k = 0 ; k < temp.length() ; k++){
							char c = temp.charAt(k);
//                                                        System.out.println(c);
							if (c >= 48 && c <= 57) {
								stairs[count][0]= l;
								stairs[count][1]= j;
								stairs[count][2]= k;
								stairs[count][3]=Integer.parseInt(temp.substring(k, k+1)) ;
							count++;
						}
						else if (c=='B'){
							if (!bombFound){ bombFound = true;
							bvs[0] =new int[] {l,j,k};
                                                        bvsFound[0]=true;
                                                        }
							else throw new Exception("more than one bomb found");
						}
						else if(c=='S'){
							if (!startFound){ startFound = true;
							bvs[2] =new int[] {l,j,k};
                                                        bvsFound[2]=true;
}
							else throw new Exception("more than one start found");
						}
						else if(c=='V'){
							if (!victimFound){ victimFound = true;
							bvs[1] =new int[] {l,j,k};
                                                        bvsFound[1]=true ; }
							else throw new Exception("more than one victim found");
						}
						map[i][j][k] = c;
						}
					}
				i++;
				}
				else if (s.length()==0 || s.charAt(0) == '#') {
					continue;
				}
			}
if(!bvsFound[0] && !bvsFound[1] && !bvsFound[2]) throw new Exception("NO B or V or S found");
		}
for (int i =0 ; i< map.length ; i++){
		System.out.println();
		for(int j =0 ; j<map[i].length ; j++){
			for(int k =0 ; k<map[i][j].length ; k++){
			System.out.print(map[i][j][k]);
		}
		System.out.println();
	}}
	}
	public static void checkStairs(){
		boolean visited[] = new boolean [count];
		int result[][] = new int[count][4];
		int next = 0;
		for (int i = 0 ; i < count ; i++){
			for (int j = i ; j < count ; j++){
				if (stairs[i][0] == stairs[j][3] && stairs[i][3] == stairs[j][0]){
					visited[i]=true;
					visited[j]=true;
					for (int t = 0 ; t < 4;t++ ){
						result[next][t] = stairs[i][t];
						result[next+1][t] = stairs[j][t];
					}
					next+=2;
				}
			}
		}
	stairs = new int[next][4];
	System.arraycopy(result, 0, stairs, 0, next);
	}
        public static int[][] inlevelStairs(int level){
        int result[][] = new int [map.length][3];
        int count =0 ;
        System.out.println(stairs.length);

        for (int i =0 ; i<stairs.length ; i++){
                    if(level == stairs [i][0]){
                      result[count][0] = stairs[i][1];
                      result[count][1] = stairs[i][2];
                      result[count][2] = stairs[i][3];
                      count ++ ;
                }
            }
        
        int temp [][] =new int[count][3];
        System.out.println(temp.length);
        for(int i =0 ;i<count ; i++){
            for(int j =0 ;j<3 ; j++){
//        System.arraycopy(result, 0, temp, 0, count-1);
temp[i][j]=result[i][j];
            }
            }
        result = temp;
        for(int i =0 ; i < result.length ; i++){
        for(int j =0 ; j < result[0].length ; j++){
//            System.out.print(result[i][j]+" ");
            }
        System.out.println();
        }
//        System.out.println(temp.length);
        return result ;
        }
        public static int[] stairCoord(int inlevel , int leadsTo){
int[] result = new int[2];
for (int i =0 ; i<stairs.length ; i++){
                    if(inlevel == stairs [i][0] && leadsTo == stairs [i][3]){
                    result[0] = stairs[i][1];
                    result[1] = stairs[i][2];
                    }}
return result;
}
}
        