public class Move implements Constants {
  private double moveScore = 0;
  private String eat = "";
  private boolean pawnc = false;
  private Move rook;
  public Piece p = null; // piece taken
  public int type;
  public boolean white;
  private Piece [][] board ; 
  
  
  //private boolean enpassant = false;
  
  public int x1;
  public int y1;
  public int x2;
  public int y2;

  
  public Move(Piece [][] board) {
	  this.board = board;

  }

  public void perform() {
	  if(board[y2][x2]!=null && board[y1][x1].type!=3 && board[y1][x1].type!=4){
			p = board[y2][x2];
		    board[y2][x2] = board[y1][x1];
		    board[y1][x1] = new Piece(4,false);
	  }
  }

  public Piece[][] getBoard(){
	  return board;
  }
  public void undo() {
	  if(p!=null){
		  if(board[y2][x2].type == 0 && (y2==6 || y2==0) ){
			  moveScore =moveScore - 1;
		  }
		  board[y1][x1] = board[y2][x2];
		  board[y2][x2] = p;  
		  p = null;
	  }
	  else{
		  board[y1][x1] = board[y2][x2];
		  board[y2][x2] = new Piece(4,false); 
	  }
  }
	  
  public void setBoard(Piece[][] p){
	  this.board=p;
  }
  
  /* Save move values */
  public Move(int a, int b, int c, int d,Piece [][] board) {
    x1 = a;
    y1 = b;
    x2 = c;
    y2 = d;
    this.board = board;
	type = board[y2][x2].type;
	white = board[y2][x2].white;
    rook = null;
  }

  
  public String getStringMov(){
	  return String.valueOf(y1)+String.valueOf(x1)+String.valueOf(y2)+String.valueOf(x2);
  }
}