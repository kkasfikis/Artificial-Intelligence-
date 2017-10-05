import java.util.ArrayList;
import java.util.LinkedList;

public class Search {
	protected Graph graph;
	protected int day;
	protected Vertex destination;
	protected int Heuristics;
	protected boolean withTraffic;
	protected long startTime;
	protected long endTime;
	protected ArrayList<LinkedList<Vertex>> results;

	public Search(boolean withTraffic,Graph graph,int day,Vertex destination,int Heuristics){
		this.graph = graph;
		this.destination = destination;
		this.Heuristics = Heuristics;
		this.day=day;
		this.withTraffic = withTraffic;
		results = new ArrayList<LinkedList<Vertex>>();
	}
	
	 public LinkedList<Edge> getRoads(LinkedList<Vertex> path){
	    	LinkedList<Edge> roads = new LinkedList<Edge>();
	    	Edge t_edge;
	    	for(int i=0;i<path.size()-1;i++){
	    		 t_edge =graph.getEdgeFromVertices(path.get(i),path.get(i+1));
	    		 if(t_edge!=null){
	    			 roads.add(t_edge);
	    		 }
	    		 else{
	    			 t_edge = graph.getEdgeFromVertices(path.get(i+1),path.get(i));
	    			 roads.add(t_edge);
	    		 }
	    	}
	    	return roads;
	    }
	    
		public int calculateCost(int day,Edge t_edge,boolean withTraffic){
			int cost =0;
			if(t_edge.dayActual(day).equals("low") && withTraffic){
				cost = t_edge.getWeight() - ((t_edge.getWeight()*10)/100);
			}
			else if(t_edge.dayActual(day).equals("heavy") && withTraffic){
				cost = t_edge.getWeight() + ((t_edge.getWeight()*10)/100);
			}
			else{
				cost = t_edge.getWeight();
			}
			return cost;
		}
	 
	    public String printPath(LinkedList<Vertex> nodes) {
	    	String text ="";
	    	text += "\nExtended "+ nodes.size() + " nodes in order to reach goal";
	    	text += "\nThe nodes of the optimal path are:";
	    	for (Vertex node : nodes) {
	            text += node.getLabel();
	            if(!node.equals(nodes.getLast())){
	            	text+= " ==> ";
	            }
	            else{
	            	text+= " " ;
	            }
	        }
	        return text;
	    }
	    
	    public String printRoads(LinkedList<Edge> roads) {
	    	String text ="";
	    	text = "\nThe roads of the optimal path are:";
	        for (Edge edge : roads) {
	            text += edge.getName();
	            if(!edge.equals(roads.getLast())){
	            	text+= " ==> ";
	            }
	            else{
	            	text+= " " ;
	            }
	        }
	        text += "\nwith cost :" + getCost(roads) ;
	        return text;
	    }
	    
	    
	    public int getCost(LinkedList<Edge> roads){
	    	int sum =0;
	    	for(Edge edge :roads){
	    		sum += calculateCost(day,edge,withTraffic);
	    	}
	    	return sum;
	    }
	    
	    
	    public int getDay() {
			return day;
		}

		public void setDay(int day) {
			this.day = day;
		}

		public void setTimerStart(){
	    	startTime = System.nanoTime();
	    }
	    
	    public void setTimerEnd(){
	    	endTime = System.nanoTime();
	    }
	    
	    
	    public int getTime(){
	    	long duration = (endTime - startTime);
	    	endTime = startTime = 0;
	    	return (int) (duration);
	    }

		public boolean isWithTraffic() {
			return withTraffic;
		}

		public void setWithTraffic(boolean withTraffic) {
			this.withTraffic = withTraffic;
		}
	    
}
