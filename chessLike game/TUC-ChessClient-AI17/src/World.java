import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;


public class World
{
	private boolean abPruning = true;
	private Piece[][] board = null;
	private int rows = 7;
	private int columns = 5;
	private int myColor = 0;
	private ArrayList<String> availableMoves = null;
	private int rookBlocks = 3;		// rook can move towards <rookBlocks> blocks in any vertical or horizontal direction
	private int nTurns = 0;
	private int nBranches = 0;
	private int noPrize = 9;
	
	public World()
	{
		board = new Piece[rows][columns];
		
		/* represent the board
		
		BP|BR|BK|BR|BP
		BP|BP|BP|BP|BP
		--|--|--|--|--
		P |P |P |P |P 
		--|--|--|--|--
		WP|WP|WP|WP|WP
		WP|WR|WK|WR|WP
		*/
		
		// initialization of the board
		for(int i=0; i<rows; i++)
			for(int j=0; j<columns; j++)
				board[i][j] = new Piece(4,false);
		
		// setting the black player's chess parts
		
		// black pawns
		for(int j=0; j<columns; j++){
			board[1][j]=new Piece(0,false);
		}
		
		
		board[0][0] =new Piece(0,false);
		board[0][columns-1] =new Piece(0,false);
		
		// black rooks
		board[0][1] =new Piece(1,false);
		board[0][columns-2] =new Piece(1,false);
		
		// black king
		board[0][2] =new Piece(2,false);
		
		// setting the white player's chess parts
		
		// white pawns
		for(int j=0; j<columns; j++){
			board[rows-2][j]=new Piece(0,true);
		}
		
		board[rows-1][0]=new Piece(0,true);
		board[rows-1][columns-1]=new Piece(0,true);
		
		// white rooks
		board[rows-1][1]=new Piece(1,true);
		board[rows-1][columns-2]=new Piece(1,true);
		
		// white king
		board[rows-1][2]=new Piece(2,true);
		
		// setting the prizes
		for(int j=0; j<columns; j++){
			board[3][j]=new Piece(3,false);
		}
		availableMoves = new ArrayList<String>();
	}
	
	public void setMyColor(int myColor)
	{
		this.myColor = myColor;
	}
	
	public String selectAction()
	{
		boolean color = false;
		MiniMax minmax = new MiniMax(board);
		AlphaBeta ab = new AlphaBeta(board);
		availableMoves = new ArrayList<String>();
		Move myMove = null;	
		Move myMoveAB = null;
		if(myColor == 0){		// I am the white player
			myMove = minmax.getReply(true);
			if(abPruning){
				myMoveAB = ab.getReply(true);
			}
			color = true;
		}
		else{					// I am the black player
			myMove = minmax.getReply(false);
			if(abPruning){
				myMoveAB = ab.getReply(false);
			}
			color = false;
		}
		// keeping track of the branch factor
		nTurns++;
		nBranches += availableMoves.size();
		if( MovementValidator(myMove,color)){
			if(abPruning){
				if(myMove == null){
					if(myMoveAB !=null){
						System.out.println("[*VS*]AB seems to know what its doing :/   y1:" + myMove.y1 + " x1:"+ myMove.x1 + " y2:"+ myMove.y2 + " x2:"+ myMove.x2);
						return myMoveAB.getStringMov();
					}
					else{
						String a = ab.getRandomMove();
						System.out.println("[*VS*]I have no idea what im doing :'(  " + a);
						return a;
					}
				}
				else{
					if(myMove.x1 == myMoveAB.x1 && myMove.x2 == myMoveAB.x2 && myMove.y1 == myMoveAB.y1 && myMove.y2 == myMoveAB.y2){
						System.out.println("[*VS*]I am pretty confident about this move    :) :D :)  y1:" + myMove.y1 + " x1:"+ myMove.x1 + " y2:"+ myMove.y2 + " x2:"+ myMove.x2);
					}
					else{
						System.out.println("[*VS*]I surely have a plan    ;)  y1:" + myMove.y1 + " x1:"+ myMove.x1 + " y2:"+ myMove.y2 + " x2:"+ myMove.x2);
						System.out.println("MyMoveAB  y1:" + myMoveAB.y1 + " x1:"+ myMoveAB.x1 + " y2:"+ myMoveAB.y2 + " x2:"+ myMoveAB.x2);
					}
					return myMove.getStringMov();
				}
			}
			else{
				if(myMove == null){
					String a = ab.getRandomMove();
					System.out.println("[*VS*]I have no idea what im doing :'(  " + a);
					return a;
				}
				else{
					return myMove.getStringMov();
				}
			}
		}
		else{
			String a = ab.getRandomMove();
			System.out.println("[*VF*]I have no idea what im doing :'(  " + a);
			return a;
		}

		
	}
	
	public boolean MovementValidator(Move fin,boolean white){
		MovementAlgo ma = new MovementAlgo(board);
		Vector v = ma.successors(white);
		if(v!=null){
			boolean x = false;
			for(int i=0;i<v.size();i++){
				Move m = (Move)v.elementAt(i);
				if(fin.x1 == m.x1 && fin.y1 == m.y1 && fin.x2 == m.x2 && fin.y2 == m.y2 ){
					return true;
				}
			}
		}
		return false;
	}
	
	public double getAvgBFactor()
	{
		return nBranches / (double) nTurns;
	}
	
	public void makeMove(int y1, int x1, int y2, int x2, int prizeX, int prizeY)
	{
		int chesspart = (board[y1][x1].type);
		
		boolean pawnLastRow = false;
		
		// check if it is a move that has made a move to the last line
		if(chesspart==0)
			if( (x1==rows-2 && x2==rows-1) || (x1==1 && x2==0) )
			{
				board[y2][x2] = new Piece(4,false); 	// in a case an opponent's chess part has just been captured
				board[y1][x1] = new Piece(4,false);
				pawnLastRow = true;
			}
		
		// otherwise
		if(!pawnLastRow)
		{
			//System.out.println(x1 + " " + y1 + " " + x2 + " " + y2);
			board[y2][x2] = board[y1][x1];
			board[y1][x1] = new Piece(4,false);
					
		}
		
		// check if a prize has been added in the game
		if(prizeX != noPrize){
			board[prizeX][prizeY] = new Piece (3,false);
		}
	}
	
}
