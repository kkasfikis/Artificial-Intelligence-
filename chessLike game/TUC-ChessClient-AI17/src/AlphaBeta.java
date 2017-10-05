import java.util.Vector;


public class AlphaBeta extends MovementAlgo{
	 
	public AlphaBeta(Piece[][] board) {
		super(board);
	}

	public Move getReply(boolean white) {
		System.out.println("\n"+(white?"White":"Black") + " replies with "+toString());
	    alfaBeta(white, -INFINITY, INFINITY, dd);
	    return mm;
	}
	  
	  
	private double alfaBeta(boolean white, double alpha, double beta, int depth) {
		Piece [][] b = board;
		if(depth==0)
			return estimate();
	    double best = -INFINITY;
	    Vector v = successors(white);
	    if(v!=null) {
	    	if(v.size()==1){
	  			mm  = (Move)v.get(0);
	  		}
	    	while(v.size()>0 && best<beta) {
	    		Move mv = (Move)v.remove(0);
	    		mv.setBoard(board);
	  			if(mv.y2 >0 && mv.y2<7 && mv.x2>0 && mv.x2<5){
	  				mv.perform();
	  				board =mv.getBoard();
	  				if(best>alpha)
	  					alpha = best;
	  				double est = -alfaBeta(!white, -beta, -alpha, depth-1);
	  				if(est>best) {
	  					best = est;
	  					if(depth==dd) {
	  						mm = mv; // Current choice of move
	  						val = estimate();
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
