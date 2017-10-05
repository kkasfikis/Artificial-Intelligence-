public class Piece implements Constants {
	public int type;
	public boolean white;
	
	
	public Piece(int type,boolean white){
		this.type=type;
		this.white=white;
	}
	
	public void setType(int type) {
		if(this.type!=type) {
			this.type = type;
		}
	}
	public void setColor(boolean color){
		white = color;
	}
	
	public double getCost(boolean c) {
		if(type == 3){
			return c?(-1):1*cost[type];
		}
		else{
			return ((white?1:-1)*cost[type]);
		}
	}
	
}

