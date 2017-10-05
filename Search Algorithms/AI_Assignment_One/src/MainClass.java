import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;

public class MainClass {
	public static final int WorkingDays=80;
	private Graph myGraph = new Graph();
	int counter =0;
	int lastIndex = 0;
	private int day = 0;
	private InitFile myFile;

	public static void main(String [] args) throws IOException{
		
		MainClass myProcess = new MainClass();		
		myProcess.myFile= new InitFile(myProcess.myGraph);
		myProcess.myFile.initialization();
		myProcess.myFile.fillGraph();
		myProcess.performSearches();
	}
	
	
	public void performSearches() throws FileNotFoundException{
		LinkedList<Vertex> myList = new LinkedList<Vertex>();
		int timeToMillis = 0;
		UninformedSearch us = new UninformedSearch(true,myGraph,day,myGraph.getVertex(myFile.getDestination()),0);
		InformedSearch is = new InformedSearch(true,myGraph,day,myGraph.getVertex(myFile.getDestination()),(int)myFile.getMIN_COST());
		OnlineSearch os = new OnlineSearch(true,myGraph,day,myGraph.getVertex(myFile.getDestination()),(int)myFile.getMIN_COST());
		int Search1Cost=0,Search2Cost=0,Search3Cost=0;
		int AlgoSearch1Cost=0,AlgoSearch2Cost=0,AlgoSearch3Cost=0;
		String Text ="";
		String curr_Text="";
		
		System.out.println("Search for optimal path between nodes " + myFile.getSource() +  " and " +myFile.getDestination() + " :");
		
		for(int i=0;i<WorkingDays;i++){
			us.setDay(i);
			is.setDay(i);
			os.setDay(i);
			curr_Text = "";
			curr_Text += "\n##################### SEARCH FOR DAY :" + i+ " ###############################";
			myList.clear();
			curr_Text += "\n============ PERFORMING UNINFORMED SEARCH =============================";
			curr_Text += "\n ---------TAKING IN ACCCOUNT PREDICTED/ACTUAL TRAFFIC------------------";
			us.setWithTraffic(true);
			myList.add(myGraph.getVertex(myFile.getSource()));
	        us.setTimerStart();
			us.execute(myGraph.getVertex(myFile.getSource()));
	        us.setTimerEnd();
	        timeToMillis = us.getTime();
	        curr_Text += "\nThe search was completed in : " + timeToMillis + " nano secs";
			myList = us.getPath(myGraph.getVertex(myFile.getDestination()));
	        if(myList!=null && !myList.isEmpty()){
				curr_Text += us.printPath(myList);
				curr_Text += us.printRoads(us.getRoads(myList));
			}
			else{
				curr_Text += "\nAN ERROR OCCURED!";
			}
	        AlgoSearch1Cost += us.getCost(us.getRoads(myList));
			curr_Text += "\n --------------WITHOUT PREDICTED/ACTUAL TRAFFIC------------------------";
			us.setWithTraffic(false);
			myList.add(myGraph.getVertex(myFile.getSource()));
	        us.setTimerStart();
			us.execute(myGraph.getVertex(myFile.getSource()));
	        us.setTimerEnd();
	        timeToMillis = us.getTime();
	        curr_Text += "\nThe search was completed in : " + timeToMillis + " nano secs";
			myList = us.getPath(myGraph.getVertex(myFile.getDestination()));
	        if(myList!=null && !myList.isEmpty()){
				curr_Text += us.printPath(myList);
				curr_Text += us.printRoads(us.getRoads(myList));
			}
			else{
				curr_Text += "\nAN ERROR OCCURED!";
			}
	        Search1Cost += us.getCost(us.getRoads(myList));
			curr_Text += "\n=======================================================================";
			
			myList.clear();
			curr_Text += "\n========== PERFORMING INFORMED SEARCH =================================";
			curr_Text += "\n ---------TAKING IN ACCCOUNT PREDICTED/ACTUAL TRAFFIC------------------";
			is.setWithTraffic(true);
			myList.add(myGraph.getVertex(myFile.getSource()));
	        is.setTimerStart();		
			myList=is.startSearch(myGraph,myGraph.getVertex(myFile.getSource()));
	        is.setTimerEnd();
	        timeToMillis = is.getTime();	
	        curr_Text += "\nThe search was completed in : " + timeToMillis + " nano secs";
	        curr_Text += is.getAttempts();
	        curr_Text += is.printPath(myList);
	        curr_Text += is.printRoads(is.getRoads(myList));
	        AlgoSearch2Cost += is.getCost(is.getRoads(myList)); 
			curr_Text += "\n --------------WITHOUT PREDICTED/ACTUAL TRAFFIC------------------------";
			is.setWithTraffic(false);
			myList.add(myGraph.getVertex(myFile.getSource()));
	        is.setTimerStart();		
			myList=is.startSearch(myGraph,myGraph.getVertex(myFile.getSource()));
	        is.setTimerEnd();
	        timeToMillis = is.getTime();	
	        curr_Text += "\nThe search was completed in : " + timeToMillis + " nano secs";
	        curr_Text += is.getAttempts();
	        curr_Text += is.printPath(myList);
	        curr_Text += is.printRoads(is.getRoads(myList));
	        Search2Cost += is.getCost(is.getRoads(myList)); 
	        curr_Text += "\n=======================================================================";
			
			myList.clear();
			curr_Text += "\n========== PERFORMING ONLINE SEARCH ===================================";
			curr_Text += "\n ---------TAKING IN ACCCOUNT PREDICTED/ACTUAL TRAFFIC------------------";
			os.setWithTraffic(true);
			myList.addLast(myGraph.getVertex(myFile.getSource()));
			os.setTimerStart();			
			myList=os.startSearch(myGraph, myGraph.getVertex(myFile.getSource()));
	        os.setTimerEnd();
	        timeToMillis = os.getTime();	
	        curr_Text += "\nThe search was completed in : " + timeToMillis + " nano secs";
	        curr_Text += os.getAttempts();
	        curr_Text += os.printPath(myList);
			curr_Text += os.printRoads(os.getRoads(myList));
			AlgoSearch3Cost += os.getCost(os.getRoads(myList)); 
			curr_Text += "\n --------------WITHOUT PREDICTED/ACTUAL TRAFFIC------------------------";
			os.setWithTraffic(false);
			myList.addLast(myGraph.getVertex(myFile.getSource()));
			os.setTimerStart();			
			myList=os.startSearch(myGraph, myGraph.getVertex(myFile.getSource()));
	        os.setTimerEnd();
	        timeToMillis = os.getTime();	
	        curr_Text += "\nThe search was completed in : " + timeToMillis + " nano secs";
	        curr_Text += os.getAttempts();
	        curr_Text += os.printPath(myList);
			curr_Text += os.printRoads(os.getRoads(myList));
			Search3Cost += os.getCost(os.getRoads(myList)); 
			curr_Text += "\n=======================================================================";
		
			System.out.println(curr_Text);
			Text  += curr_Text;
		}
		
		curr_Text = "\n######################################################################\n" +
					"AFTER " + WorkingDays + " REPEATS THE AVERAGE COSTS FOR EACH ALGORITHM WITHOUT TRAFFIC ARE: \n" +
					"UNINFORMED SEARCH: " + (Search1Cost/WorkingDays) + "\n" +
					"INFORMED SEARCH  : " + (Search2Cost/WorkingDays) + "\n" +
					"ONLINE SEARCH    : " + (Search3Cost/WorkingDays) + "\n" +
					"######################################################################\n" + 
					"\n######################################################################\n" +
					"AFTER " + WorkingDays + " REPEATS THE AVERAGE COSTS FOR EACH ALGORITHM WITH TRAFFIC ARE: \n" +
					"UNINFORMED SEARCH: " + (AlgoSearch1Cost/WorkingDays) + "\n" +
					"INFORMED SEARCH  : " + (AlgoSearch2Cost/WorkingDays) + "\n" +
					"ONLINE SEARCH    : " + (AlgoSearch3Cost/WorkingDays) + "\n" +
					"######################################################################\n"
					;
				 	
		System.out.println(curr_Text);
		Text += curr_Text;
		myFile.writeOutputFile(Text);
	}

}
