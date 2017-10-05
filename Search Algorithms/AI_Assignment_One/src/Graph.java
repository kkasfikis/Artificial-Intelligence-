import java.util.*;

public class Graph {

	private HashMap<String, Vertex> vertices;
    private HashMap<String, Edge> edges;
    
    
    
    
    public Graph(){
        this.vertices = new HashMap<String, Vertex>();
        this.edges = new HashMap<String, Edge>();
    }
    
    public Edge getEdgeFromVertices(Vertex one,Vertex two){
    	for(Map.Entry<String, Edge> e : edges.entrySet()) {
    	    Edge value = e.getValue();
    		if (value.getOne().equals(one) && value.getTwo().equals(two)){
    			return value;
    		}
    	}
    	return null;
    }
    
    public Graph(ArrayList<Vertex> vertices){
        this.vertices = new HashMap<String, Vertex>();
        this.edges = new HashMap<String, Edge>();
        for(Vertex v: vertices){
            this.vertices.put(v.getLabel(), v);
        }
    }
    
    public boolean addEdge(Vertex one, Vertex two){
        return addEdge(one, two, 1,"");
    }
    
    public boolean addEdge(Vertex one, Vertex two, int weight,String name){
        if(one.equals(two)){
            return false;  
        }
        //ensures the Edge is not in the Graph
        Edge e = new Edge(one, two, weight,name);
        if(edges.containsKey(e.hashCode())){
            return false;
        }
        else if(one.containsNeighbor(e) || two.containsNeighbor(e)){
            return false;
        }
        edges.put(name, e);
        one.addNeighbor(e);
        two.addNeighbor(e);
        return true;
    }
    
    public boolean containsEdge(Edge e){
        if(e.getOne() == null || e.getTwo() == null){
            return false;
        }
        return this.edges.containsKey(e.hashCode());
    }
    
    public Edge removeEdge(Edge e){
       e.getOne().removeNeighbor(e);
       e.getTwo().removeNeighbor(e);
       return this.edges.remove(e.hashCode());
    }
    
    public boolean containsVertex(Vertex vertex){
        return this.vertices.get(vertex.getLabel()) != null;
    }
    public Edge getEdge(String name){
    	return edges.get(name);
    }
    public Vertex getVertex(String label){
        return vertices.get(label);
    }
    
    public boolean addVertex(Vertex vertex, boolean overwriteExisting){
        Vertex current = this.vertices.get(vertex.getLabel());
        if(current != null){
            if(!overwriteExisting){
                return false;
            }
            while(current.getNeighborCount() > 0){
                this.removeEdge(current.getNeighbor(0));
            }
        }
        vertices.put(vertex.getLabel(), vertex);
        return true;
    }
    
    public Vertex removeVertex(String label){
        Vertex v = vertices.remove(label);
        while(v.getNeighborCount() > 0){
            this.removeEdge(v.getNeighbor((0)));
        }
        return v;
    }
    
    public Set<String> vertexKeys(){
        return this.vertices.keySet();
	}
    
    public HashSet<Edge> getEdges(){
        return new HashSet<Edge>(this.edges.values());
    }
    public HashSet<Vertex> getVertices(){
    	return new HashSet<Vertex>(this.vertices.values());
    }
}
