import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class InitFile {
	private String filePath = "C:\\Users\\BSN\\Desktop\\sampleGraph3.txt";
	private String fullFileText;
	private String roadInfo;
	private String trafficPredictionInfo;
	private String trafficActualInfo;
	private String source;
	private String outputName = "output.txt";
	private String destination;
	private Graph myGraph;
	private int MAX_COST =0;
	private double MIN_COST = Double.POSITIVE_INFINITY;
	int counter =0;
	int lastIndex = 0;
	public InitFile(Graph myGraph){
		this.myGraph = myGraph;
	}
	
	public void initialization() throws IOException{
		readFile();
		roadInfo = fullFileText.substring(fullFileText.indexOf("<Roads>")+7, fullFileText.lastIndexOf("</Roads>"));
		source = fullFileText.substring(fullFileText.indexOf("<Source>")+8, fullFileText.lastIndexOf("</Source>"));
		destination = fullFileText.substring(fullFileText.indexOf("<Destination>")+13, fullFileText.lastIndexOf("</Destination>"));
		trafficPredictionInfo = fullFileText.substring(fullFileText.indexOf("<Predictions>"), fullFileText.lastIndexOf("</Predictions>"));
		trafficActualInfo = fullFileText.substring(fullFileText.indexOf("<ActualTrafficPerDay>"), fullFileText.lastIndexOf("</ActualTrafficPerDay>"));
	}
	
	public void fillGraph(){
		
		//ADD VERTICES AND EDGES == ADD ROADS
		String t_name;
		String t_day;
		String t_traffic;
		Vertex [] vertices = new Vertex[2];
		String t_vertexA;
		String t_vertexB;
		int t_weight;
		String[] parts;
		String[] lines; 
		Edge t_edge;
		lines = cleanDublicates(roadInfo.split(System.getProperty("line.separator")));
		System.out.println("================INITIALISING ROADS==================");
		for (int i = 0; i < lines.length; i++){
			if(lines[i]!=null && !lines[i].isEmpty()){
				parts = lines[i].split("; ");
				t_name = parts[0];
				t_vertexA = parts[1];
				t_vertexB = parts[2];
				t_weight = Integer.parseInt(parts[3]);
				MAX_COST += t_weight;
				if(MIN_COST>t_weight){
					MIN_COST = t_weight;
				}
				if(myGraph.getVertex(t_vertexA)==null){
					vertices[0] = new Vertex(t_vertexA);		
					myGraph.addVertex(vertices[0], true);
				}
				else{
					vertices[0] = myGraph.getVertex(t_vertexA);
				}
				
				if(myGraph.getVertex(t_vertexB)==null){
					vertices[1] = new Vertex(t_vertexB);
					myGraph.addVertex(vertices[1], true);
				}
				else{
					vertices[1] = myGraph.getVertex(t_vertexB);
				}
				
				myGraph.addEdge(vertices[0],vertices[1],t_weight,t_name);
				System.out.println("Added edge with name : "+ t_name + " consisting of vertices: " + t_vertexA +" and " + t_vertexB);
				
				
				
			}
		}
		System.out.println("======================================================");
		System.out.println("=============INITIALISING ACTUAL TRAFFIC===========");
		lastIndex = trafficActualInfo.indexOf("</Day>");
		for (int index = trafficActualInfo.indexOf("<Day>"); index >= 0; index = trafficActualInfo.indexOf("<Day>", index + 1)) {
			t_day = trafficActualInfo.substring(index+5, lastIndex);
			lines = t_day.split(System.getProperty("line.separator"));
			for(int j =0 ; j<lines.length;j++){
				if(!lines[j].isEmpty()){
					
					//System.out.println(lines[j]);
					parts = lines[j].split("; ");
					t_name = parts[0];
					t_traffic = parts[1];
					t_edge = myGraph.getEdge(t_name);
					if(t_edge!=null){
						t_edge.setDayActual(counter, t_traffic);
					}
				}
			}
			lastIndex = trafficActualInfo.indexOf("</Day>",lastIndex+1);
			System.out.println("Added actual traffic for day : " + counter);
			counter++;
		}
		System.out.println("======================================================");
		System.out.println("=============INITIALISING PREDICTED TRAFFIC===========");
		lastIndex = trafficPredictionInfo.indexOf("</Day>");
		counter = 0;
		for (int index = trafficPredictionInfo.indexOf("<Day>"); index >= 0; index = trafficPredictionInfo.indexOf("<Day>", index + 1)) {
			t_day = trafficPredictionInfo.substring(index+5, lastIndex);
			lines = t_day.split(System.getProperty("line.separator"));
			for(int j =0 ; j<lines.length;j++){
				if(!lines[j].isEmpty()){		
					parts = lines[j].split("; ");
					t_name = parts[0];
					t_traffic = parts[1];
					t_edge = myGraph.getEdge(t_name);
					if(t_edge!=null){
						t_edge.setDayPredicted(counter, t_traffic);
					}
				}
			}
			lastIndex = trafficPredictionInfo.indexOf("</Day>",lastIndex+1);
			System.out.println("Added predicted traffic for day : " + counter);
			counter++;
		}
		System.out.println("======================================================");

	}
	
	public String [] cleanDublicates(String [] lines){
		String [] newTable;
		int size= lines.length;
		int value = 0;
		String [] parts1,parts2;
		
		for (int i=0;i<lines.length;i++){
			if(lines[i].isEmpty()){
				lines[i]=null;
				size--;
				continue;
			}
			if(lines[i]!=null){
				parts1=lines[i].split("; ");
				value = Integer.parseInt(parts1[3]);
			}
			else{
				continue;
			}
			for(int j=0;j<lines.length;j++){
				if(lines[j]!=null){
					parts2=lines[j].split("; ");
					if((parts1[1].equals(parts2[1]) && parts1[2].equals(parts2[2])) || (parts1[1].equals(parts2[2]) && parts1[2].equals(parts2[1]))){
						if(Integer.parseInt(parts2[3])<value){
							value = Integer.parseInt(parts2[3]);
						}
					}
				}
				else{
					continue;
				}
			}
			for(int j=0 ; j<lines.length;j++){
				if(lines[j]!= null){
					parts2=lines[j].split("; ");
					if((parts1[1].equals(parts2[1]) && parts1[2].equals(parts2[2])) || (parts1[1].equals(parts2[2]) && parts1[2].equals(parts2[1]))){
						if(Integer.parseInt(parts2[3])<value){
							lines[j]=null;
							size --;
						}
					}
				}
				else{
					continue;
				}
			}
		}
		int counter =0;
		newTable = new String[size];
		for(int i=0;i<size;i++){
			if(lines[i]!=null){
				newTable[counter]=lines[i];
				counter++;
			}
		}
		return newTable;
	}
	
	public void writeOutputFile(String text){
		String [] lines = text.split(System.getProperty("line.separator"));
		File file=new File(outputName);	
		if (file.exists()==true) {file.delete();}
		try{
		    PrintWriter writer = new PrintWriter(outputName, "UTF-8");
		    for(int i=0;i<lines.length;i++){
		    	writer.print(text);
		    	writer.println("");
		    }
		    writer.close();
		} catch (IOException e) {
		   System.out.println("AN ERROR OCCURED ! THE OUTPUT FILE IS PROPABLY CORRUPTED ");
		}
		
	}
	
	public void readFile() throws IOException{
		File file = new File(filePath);
	    FileInputStream fis = new FileInputStream(file);
	    byte[] bytes = new byte[(int) file.length()];
	    fis.read(bytes);
	    fis.close();
	    fullFileText = new String(bytes, "UTF-8");
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getMAX_COST() {
		return MAX_COST;
	}

	public void setMAX_COST(int mAX_COST) {
		MAX_COST = mAX_COST;
	}

	public double getMIN_COST() {
		return MIN_COST;
	}

	public void setMIN_COST(double mIN_COST) {
		MIN_COST = mIN_COST;
	}
	
}
