public class Piece {

    public static int i = 6;
    public  int row;
    public  int col;
    public  int id;
    public static boolean share = false;
    public static int board[][]  = new int[10][10];
    public static Piece pieces[] = new Piece[10];
    public static int cord[][] = new int[pieces.length][2];
    public static int v[]= new int [10];


    Piece() {
        row = -1;
        col = -1;
        id++;
    };
    public  int getRow() {
        return row;
    }
    public  int getCol() {
        return col;
    }
    public  void setRow(int newRow) {
        row = newRow;
    }
    public  void setCol(int newCol) {
        col = newCol;
    }
    public int getId(){
    return id;
}
    public static int getNext(){
    int next=-1;
        for( int a =0 ; a<i ; a++){
        if(v[a]==0){
//           pieces[a].getCol()==-1 && pieces[a].getRow()==-1
            next=a;
            break;
            }
        }
        return next;
    }
    public static int getElement(int i, int j) {
        return board[j][i];
    }
     public static void setElementsToStart() {
        for(int k =0 ; k<10 ; k++){
            for(int j=0 ; j<10 ; j++)
         board[j][k]=-1;
        }
    }public static int getPiecesCol(int j){
        return pieces[j].getCol();
    }
    public static int getPiecesRow(int j){
        return pieces[j].getRow();
    }

    public static void putPiece(int col1, int row1) {
        int id1 = getNext();
        if(id1==-1)
            System.out.println("no more pieces to put");
        else{
        v[id1]=1;
            board[row1][col1] = id1;
        pieces[id1].setCol(col1);
        pieces[id1].setRow(row1);
         }}
    public static void removePiece(int col1, int row1) {
        int id1=getElement(col1, row1);
        v[id1]=0;
        board[row1][col1] = -1;
        pieces[id1].setCol(-1);
        pieces[id1].setRow(-1);

    }
    public static void seti(int p){
        i = p ;
    }
    public static int geti(){
        return i;
    }
    public static void state() {
        for (int k = 0; k < pieces.length; k++) {
                    cord[k][0] = pieces[k].getCol();
                    cord[k][1] = pieces[k].getRow();
            }
//    for (int k = 0; k < cord.length; k++) {
//        for (int t = 0; t < cord[0].length; t++) {
//         System.out.print(cord[k][t]+" ");
//        }
//        System.out.println();
//    }
    }
    //check if there is a repetetion in the cord array
    public static boolean isRepeated() {
       boolean lose = false;
        for (int k = 0; k < pieces.length; k++) {
                    cord[k][0] = pieces[k].getCol();
                    cord[k][1] = pieces[k].getRow();
            }
        for (int x = 0; x < cord.length ; x++) {
            int temp0 = cord[x][0];
            int temp1 = cord[x][1];
            int temp2 = temp0 - temp1;
            int temp3 = temp0 + temp1;
            for (int y = x+1; y < cord.length-(10-i)  ; y++) {
                if (temp0 == cord[y][0]) {
                    lose = true;
                } else if (temp1 == cord[y][1]) {
                    lose = true;
                } else if (temp2 == cord[y][0] - cord[y][1]) {
                    lose = true;
                }else if (temp0 - cord[y][0] == temp1- cord[y][1] ) {
                    lose = true;
                }
            }
        }
       return lose;
    }

    }
