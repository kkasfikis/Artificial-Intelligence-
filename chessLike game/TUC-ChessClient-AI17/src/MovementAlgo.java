import java.util.Vector;


public class MovementAlgo {
	private boolean xaxa = true;
	protected int dd = 5;       // Depth
	protected final int INFINITY = 100000; 
	protected int stepcounter;  // Total node count
	protected Move mm;          // Current move
	protected double val = 0;      // Current estimation value
	  int lalala = 0;
	private double estnow;         // base estimation
    private double est_cost;       // Cost estimation value
	private double est_attackCost; // Attack cost estimation value
	protected Piece[][] board;
	public MovementAlgo(Piece[][] board){
		this.board = board;
	}
	
	
	public String getRandomMove(){
		return null;
	}
	
	private Vector sumVectors(Vector a, Vector b) {
		if(a==null && b==null)
			return null;
		Vector v=new Vector();
		if(a!=null)
			for(int i=0;i<a.size();i++)
				v.add(a.elementAt(i));
		if(b!=null)
		  	for(int i=0;i<b.size();i++)
		  		v.add(b.elementAt(i));
		return v;
	}
	private int getCost() {
		int out = 0;
		for(int c=0;c<7;c++)
			for(int r=0;r<5;r++)
				if(board[c][r].type!=4 && board[c][r].type!=3)
					out+=board[c][r].getCost(board[c][r].white);
		return out;
	}
		  
	private double getAttackCost() {
		double out = 0;
		for(byte c=0;c<7;c++)
			for(byte r=0;r<5;r++)
				if(board[c][r]!=null) {
					Vector v = getAttacks(new Coord(r,c));
					if(v!=null){
						for(int k=0;k<v.size();k++) {
							Move mm = (Move)v.elementAt(k);
							if(xaxa){
								//System.out.println("Attack for "+board[mm.y1][mm.x1].white+" "+board[mm.y1][mm.x1].type+":"+mm.y1 + " " + mm.x1 + " || and target is:"+mm.y2+ " " +mm.x2);
								xaxa =false;
							}
						}
						for(int k=0;k<v.size();k++) {
							Move m = (Move)v.elementAt(k);
							//System.out.println(m.x2);
							if(m.y2 >0 && m.y2<7 && m.x2>0 && m.x2<5)
								out += board[m.y2][m.x2].getCost(board[m.y1][m.x1].white);
						}
					}
				}
		return -out;
	}

	protected double estimate() {
		stepcounter++;
		double dc = getCost()-est_cost;
		double dac = getAttackCost()-est_attackCost;
		//System.out.println("getCost => " + getCost());
		//System.out.println("getAttackCost => " + getAttackCost());
		return dc*10+dac;
	}
		  
	protected double estimateBase() {
	   //cc.progress.setValue(0);
		mm = null;
	   	est_cost = 0;
	   	est_attackCost = 0;
	   	double out = estimate();
	   	est_cost = getCost();
	   	est_attackCost = getAttackCost();
	   	return out;
	}
   public Vector getRealAll(Coord c) {
	   Piece p = board[c.y][c.x];
	   Vector x;
	   int z;
	   if (p==null) 
			return null;
		Vector v = null;
		switch(p.type) {
			case 0: //PAWN
				v=sumVectors(getPawnMoves(c),getPawnAttacks(c));
				break;
		    case 1: //ROOK
				v=getRookMoves(c);
				break;
		    case 2: //KING
		        v = getKingMoves(c);
		        break;
		    default:
		    	return null;
		 }

		 if(v!=null) {
			 for(int k=0;k<v.size();k++) {
				 Move mmm = (Move)v.elementAt(k);
		         //illegal coordinates
				 if(mmm.y2<0 || mmm.y2 >6 || mmm.x2<0 || mmm.x2>4){
		        	 v.remove(k);
		        	 k--;
		        	 continue;
		         }
				// Make sure piece can not take one of it's own
		         if(board[mmm.y2][mmm.x2].type!=3 && board[mmm.y2][mmm.x2].type!=4 && board[mmm.y2][mmm.x2].white==p.white) {
		        	 v.remove(k);
		        	 k--;
		         }
			 }
			 if(v.size()==0)
				 v = null;
		 }
		 return v;
	}
	

   
	
	protected Vector successors(boolean white) {
		Vector v = new Vector();
		Vector tmp = new Vector();
		for(int i=0;i<7;i++){
			for(int j=0;j<5;j++){
				//System.out.println(i+" ########## "+j);
				if(board[i][j]!=null){
					//System.out.print(board[i][j].white+" !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!| "+board[i][j].type+" | " + "x:"+j+"||y:" +i+" | ");
					if(board[i][j].type!=4 && board[i][j].type!=3 && board[i][j].white==white){ 
						tmp = getRealAll(new Coord(j,i));
						if(tmp!=null){
							v = sumVectors(v, tmp);
						}
					}
				}
			}
			//System.out.println("\n");
		}
		
		return v;
		
	}
		  
	public Vector getAttacks(Coord c) {
		Piece p = board[c.y][c.x];
			if(p==null)
				return null;
		    switch(p.type) {
		    	case 0:
		    		return getPawnAttacks(c);
		    	case 1:
		    		return getRookAttacks(c);
		    	case 2:
		    		return getKingAttacks(c);
		    }
		    return null;
	}
	private Vector getMoves(Coord c) {
		Piece p ;
		if(c.x >4 || c.x<0 || c.y >6 || c.y<0 || board[c.y][c.x]==null )
			return null;
		else
			p= board[c.y][c.x];
		switch(p.type) {
			case 0: //pawn
				return getPawnMoves(c);
		    case 1: //rook
		        return getRookMoves(c);
		    case 2: //king
		        return getKingMoves(c);
		}
		return null;
	}	
	

//====================================================================================================
//==============================================PAWN MOVES/ATTACKS====================================
	private Vector getPawnMoves(Coord c){
		Vector v= new Vector();
		//System.out.println(c.x + "!!!" + c.y);
		if(board[c.y][c.x].white){
			//last row
			if(c.y-1 == 0 && board[c.y-1][c.x].type==4)
			{
				v.add(new Move(c.x,c.y,c.x,c.y-1,board));
			}
			// check if it can move one vertical position ahead
			if(c.y-1>0 && board[c.y-1][c.x].type ==4){
				v.add(new Move(c.x,c.y,c.x,c.y-1,board));
			}
		}
		else{
			//last row
			if(c.y+1 == 6 && board[c.y+1][c.x].type==4)
			{
				v.add(new Move(c.x,c.y,c.x,c.y+1,board));
			}
			// check if it can move one vertical position ahead
			if(c.y+1<7 && board[c.y+1][c.x].type ==4){
				v.add(new Move(c.x,c.y,c.x,c.y+1,board));
			}
		}
		return (v.size()==0)?null:v;
	}
	
	private Vector getPawnAttacks(Coord c){
		Vector v = new Vector();
		if(board[c.y][c.x].white){
			// check if it can move crosswise to the left
			if(c.x>0 && c.y>0 && board[c.y-1][c.x-1].white!=board[c.y][c.x].white && board[c.y-1][c.x-1].type !=3 && board[c.y-1][c.x-1].type !=4){
				v.add(new Move(c.x,c.y,c.x-1,c.y-1,board));
			}
			if(c.x<4 && c.y>0 && board[c.y-1][c.x+1].white!=board[c.y][c.x].white && board[c.y-1][c.x+1].type !=3 && board[c.y-1][c.x+1].type !=4){
				v.add(new Move(c.x,c.y,c.x+1,c.y-1,board));
			}	
			if(c.y>0 && board[c.y-1][c.x].type==3){
				//System.out.println("GOT IN !!!!");
				v.add(new Move(c.x,c.y,c.x,c.y-1,board));
			}
		}
		else{	
			// check if it can move crosswise to the left
			if(c.x>0 && c.y<6 && board[c.y+1][c.x-1].white!=board[c.y][c.x].white && board[c.y+1][c.x-1].type !=3 && board[c.y+1][c.x-1].type !=4){
					v.add(new Move(c.x,c.y,c.x-1,c.y+1,board));
			}
			if(c.x<4 && c.y<6 && board[c.y+1][c.x+1].white!=board[c.y][c.x].white && board[c.y+1][c.x+1].type !=3 && board[c.y+1][c.x+1].type !=4){
				v.add(new Move(c.x,c.y,c.x+1,c.y+1,board));
			}
			if(c.y<6 && board[c.y+1][c.x].type==3){
				v.add(new Move(c.x,c.y,c.x,c.y+1,board));
			}
		}
		return v;
	}
//====================================================================================================
//==============================================ROOK MOVES/ATTACKS====================================
	private Vector getRookMoves(Coord c) {
		Vector v = new Vector();
		for(int k=0; k<3; k++)
		{
			if((c.y-(k+1)) < 0)
				break;		
			if(board[c.y-(k+1)][c.x].type!=4)
				break;
			v.add(new Move(c.x,c.y,c.x,c.y-(k+1),board));
		}
			// check if it can move downwards
		for(int k=0; k<3; k++)
		{
			if((c.y+(k+1)) > 6)
				break;
			if(board[c.y+(k+1)][c.x].type!=4)
				break;
			v.add(new Move(c.x,c.y,c.x,c.y+(k+1),board));
		}
		
		// check if it can move on the left
		for(int k=0; k<3; k++)
		{
			if((c.x-(k+1)) < 0)
				break;
			if(board[c.y][c.x-(k+1)].type!=4)
				break;
			v.add(new Move(c.x,c.y,c.x-(k+1),c.y,board));
		}
			
		// check of it can move on the right
		for(int k=0; k<3; k++)
		{
			if((c.x+(k+1)) >4)
				break;
			if(board[c.y][c.x+(k+1)].type!=4)
							break;
			v.add(new Move(c.x,c.y,c.x+(k+1),c.y,board));
		}
		return (v.size()==0)?null:v;
	}	
	public Vector getRookAttacks(Coord c){
		return getRookMoves(c);
	}
//==============================================================================================================================	
//==============================================KING MOVES/ATTACKS ==============================================================	

	
	private Vector getKingMoves(Coord c) {
		Vector v= new Vector();
		if((c.y-1) >= 0)
		{
			if(board[c.y-1][c.x].white!=board[c.y][c.x].white || board[c.y-1][c.x].type==4 || board[c.y-1][c.x].type==3)
			{
				v.add(new Move(c.x,c.y,c.x,c.y-1,board));	
			}
		}
		
		// check if it can move downwards
		if((c.y+1) < 7)
		{
			if(board[c.y+1][c.x].white!=board[c.y][c.x].white || board[c.y+1][c.x].type==4 || board[c.y+1][c.x].type==3)
			{
				v.add(new Move(c.x,c.y,c.x,c.y+1,board));	
			}
		}
		
		// check if it can move on the left
		if((c.x-1) >= 0)
		{
			if(board[c.y][c.x-1].white!=board[c.y][c.x].white || board[c.y][c.x-1].type==4 || board[c.y][c.x-1].type==3)
			{
				v.add(new Move(c.x,c.y,c.x-1,c.y,board));	
			}
		}

		
		// check if it can move on the right
		if((c.x+1) < 5)
		{
			if(board[c.y][c.x+1].white!=board[c.y][c.x].white || board[c.y][c.x+1].type==4 || board[c.y][c.x+1].type==3)
			{
				v.add(new Move(c.x,c.y,c.x+1,c.y,board));	
			}
		}
		return (v.size()==0)?null:v;
	}			

	
	private Vector getKingAttacks(Coord c) {
		return getKingMoves(c);
    }
//====================================================================================================================================================
}
