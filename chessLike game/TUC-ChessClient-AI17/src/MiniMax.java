import java.util.Vector;


public class MiniMax extends MovementAlgo {
	private boolean white; // White player ?

	public MiniMax(Piece[][] board) {
		super(board);
		estimateBase();
		// TODO Auto-generated constructor stub
	}

	public Move getReply(boolean white) {
	    minimax(white, dd);
	    return mm;
	}

	public void minimax(boolean white, int depth){
	    this.white = white;
	  	double val = 0;
	  	//System.out.println("My color is : "+ white);
	  	if(this.white)
	  	  val = Max(depth); // white moves first
	  	else
	  	  val = Min(depth); // black moves first
	}

	private double Max(int depth) {
		Piece [][] b = board;
		if (depth == 0)
			return estimate();
	  	double best = -INFINITY;
	  	Vector v = successors(true);
	  	if (v!=null) { 
	  		while(v.size()>0) {
	  			Move mv = (Move)v.remove(0);
	  			mv.setBoard(board);
	  			if(mv.y2 >0 && mv.y2<7 && mv.x2>0 && mv.x2<5){
	  				mv.perform();
	  				board = mv.getBoard();
	  				double val = -Min(depth-1);
	  				if (val>best) {
	  					best = val;
	  					if (this.white && depth == dd) {
	  						mm = mv; // Current choice of move
	  					}
	  				}
	  				
		  			mv.undo();
		  			board =b;
	  			}
	  		}
	  	}
	  	return best;
	}

	private double Min(int depth) {
		Piece [][] b = board;
	    if (depth == 0)
	      return estimate();
	    double best = -INFINITY;
	    Vector v = successors(false);
	    
	  	if (v!=null) { 
	  		while(v.size()>0) {
	  			Move mv = (Move)v.remove(0);
	  			mv.setBoard(board);
	  			if(mv.y2 >0 && mv.y2<7 && mv.x2>0 && mv.x2<5){
	  				mv.perform();
		  			board =mv.getBoard();
		  			double val = -Max(depth-1);
		  			if (val>best) {
		  				best = val;
		  				if (!this.white && depth == dd) {
		  					mm = mv; // Current choice of move
		  				}
		  			}
		  			
	  				mv.undo();
	  				board =b;
	  			}
	  		}	
	  	}
	  	return best;
	}
}
