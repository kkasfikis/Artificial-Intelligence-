public class Edge implements Comparable<Edge> {
    private Vertex one, two;
    private int weight;
    private String  name ;
    private String [] ActualTrafficPerDay=null;
    private String [] PredictedTrafficPerDay=null;
    public Edge(Vertex one, Vertex two){
        this(one, two, 1,"");
    }
    public Edge(Vertex one, Vertex two, int weight,String name){
        this.one = (one.getLabel().compareTo(two.getLabel()) <= 0) ? one : two;
        this.two = (this.one == one) ? two : one;
        this.weight = weight;
        this.name = name;
    }
    
    public String [] getActualTrafficPerDay(){
    	 return this.ActualTrafficPerDay;
    }
    
    public void setActualTrafficPerDay(String [] ActualTrafficPerDay){
    	this.ActualTrafficPerDay = ActualTrafficPerDay;
    }

    public String dayActual(int index){
    	return ActualTrafficPerDay[index];
    }
    
    public void setDayActual(int index,String value){
    	if(ActualTrafficPerDay == null){
    		ActualTrafficPerDay = new String[MainClass.WorkingDays];
    		System.out.println("DO YOU WORK ?");
    	}
    	ActualTrafficPerDay[index]=value;
    }
    
    public String [] getPredictedTrafficPerDay(){
   	 return this.PredictedTrafficPerDay;
   }
   

   public String dayPredicted(int index){
   	return PredictedTrafficPerDay[index];
   }
   
   public void setDayPredicted(int index,String value){
	   if(PredictedTrafficPerDay == null){
		   PredictedTrafficPerDay = new String[MainClass.WorkingDays];
		   System.out.println("DO YOU WORK ?");
	   }
	   PredictedTrafficPerDay[index]= value;
   }
    
    
    public Vertex getNeighbor(Vertex current){
        if(!(current.equals(one) || current.equals(two))){
            return null;
        }
        return (current.equals(one)) ? two : one;
    }
    public Vertex getOne(){
        return this.one;
    }
    public Vertex getTwo(){
        return this.two;
    }
    public String getName(){
    	return this.name;
    }
    public void setName(String name){
    	this.name = name;
    }
    public int getWeight(){
        return this.weight;
    }
    public void setWeight(int weight){
        this.weight = weight;
    }
    public int compareTo(Edge other){
        return this.weight - other.weight;
    }
    public String toString(){
        return "({" + one + ", " + two + "}, " + weight + ")";
    }
    public int hashCode(){
        return (one.getLabel() + two.getLabel()).hashCode();
    }
    public boolean equals(Object other){
        if(!(other instanceof Edge)){
            return false;
        }
        Edge e = (Edge)other;
        return e.one.equals(this.one) && e.two.equals(this.two);
    }  
}
