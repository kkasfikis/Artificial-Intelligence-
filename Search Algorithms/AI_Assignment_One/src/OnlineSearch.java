import java.util.LinkedList;

public class OnlineSearch extends Search{
	private boolean goal;
	private String attempts = "";
	public OnlineSearch(boolean withTraffic, Graph graph, int day, Vertex destination, int Heuristics) {
		super(withTraffic, graph, day, destination, Heuristics);
		// TODO Auto-generated constructor stub
	}

	
	
	//http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html#S7
	public int LRTAstar(Graph mGraph,LinkedList<Vertex> visited,Vertex destination,int new_frontier,int curr_cost,double ret) {
		Edge t_edge;
        LinkedList<Vertex> nodes = new LinkedList<Vertex>();
        for(int i=0;i<visited.getLast().getNeighborCount();i++){
        	if((visited.getLast().getNeighbor(i).getTwo()).equals(visited.getLast())){
        		if(!nodes.contains(visited.getLast().getNeighbor(i).getOne())){
        			nodes.add(visited.getLast().getNeighbor(i).getOne());
        		}
        	}
        	else if((visited.getLast().getNeighbor(i).getOne()).equals(visited.getLast())){
        		if(!nodes.contains(visited.getLast().getNeighbor(i).getTwo())){
        			nodes.add(visited.getLast().getNeighbor(i).getTwo());
        		}
        	}
        }
        int i=0;
        for (Vertex node : nodes){
            if (visited.contains(node)){
                continue;
            }
            if (node.equals(destination)){
                visited.add(node);
                results.add(new LinkedList<Vertex>(visited));
                goal = true;
                visited.removeLast();
                continue;
            }
        }
        i=0;
        for (Vertex node : nodes) {
        	t_edge = visited.getLast().getNeighbor(i);
        	i++;
        	curr_cost += calculateCost(day,t_edge,withTraffic);
        	if(node.equals(destination) || visited.contains(destination)){
        		curr_cost -= calculateCost(day,t_edge,withTraffic);
        		continue;
        	}
            if (visited.contains(node) || node.equals(destination) || curr_cost > new_frontier) {

            	if(curr_cost>new_frontier){
            		if(curr_cost<ret){
            			ret=curr_cost;
            		}
            	}
            	curr_cost -= calculateCost(day,t_edge,withTraffic);
            	continue;
            }
            visited.addLast(node);
            ret = LRTAstar(mGraph,visited,destination,new_frontier,curr_cost,ret);
            visited.removeLast();
            curr_cost -= calculateCost(day,t_edge,withTraffic);//t_edge.getWeight();
        }
        return (int)ret;
    }
	
	
	public int calculateCost(int day,Edge t_edge,boolean withTraffic){
		int cost =0;
		String  traffic="normal";
		int count=0,count1=0;
		
		for(int i=0;i<=day;i++){
			if(t_edge.dayActual(i).equals("heavy")){
				count++;
			}
			else if(t_edge.dayActual(i).equals("low")){
				count1++;
			}
		}
		
		
		for(int j=day;j<MainClass.WorkingDays;j++){
			if(t_edge.dayPredicted(j).equals("heavy")){
				count++;
			}
			else if(t_edge.dayPredicted(j).equals("low")){
				count1++;
			}
		}
		
		
		if(count<count1){
			traffic = "heavy";
		}
		else if(count>count1){
			traffic = "low";
		}
		else{
			traffic = "normal";
		}
		
		if(traffic.equals("low") && withTraffic){
			cost = t_edge.getWeight() - ((t_edge.getWeight()*10)/100);
		}
		else if(traffic.equals("heavy") && withTraffic){
			cost = t_edge.getWeight() + ((t_edge.getWeight()*10)/100);
		}
		else{
			cost = t_edge.getWeight();
		}
		return cost;
	}
	
	
	public LinkedList<Vertex> startSearch(Graph mGraph,Vertex source){
		int new_frontier = Heuristics;
		Edge t_edge;
		results.clear();
		attempts ="";
		LinkedList<Vertex> myPath = null; 
		double lowest = Double.POSITIVE_INFINITY;
		LinkedList<Vertex> visited = new LinkedList<Vertex>();
		int i=0;
		goal = false;
		boolean check = true;
		while(check){
			i++;
			attempts += "\nInformed Search :ATTEMPT "+ i +" using frontier :" + (new_frontier - 3);
			visited.clear();
			visited.add(source);
			new_frontier += LRTAstar(mGraph,visited,destination,new_frontier,0,Double.POSITIVE_INFINITY) +3;
			if(goal){check = false;}
		}
		int sum =0;
		for(int j=0;j<results.size();j++){
			sum =0;
			for(int x=0 ; x<results.get(j).size()-1;x++){
				t_edge = graph.getEdgeFromVertices(results.get(j).get(x),results.get(j).get(x+1));
				if(t_edge ==null){
					t_edge = graph.getEdgeFromVertices(results.get(j).get(x+1),results.get(j).get(x));
				}
				sum += calculateCost(day,t_edge,withTraffic);
			}
			if(sum<lowest){
				lowest = sum;
				myPath = results.get(j);
			}
		}
		return myPath;
	}



	public String getAttempts() {
		return attempts;
	}



	public void setAttempts(String attempts) {
		this.attempts = attempts;
	}
	
}
