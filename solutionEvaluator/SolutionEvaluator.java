package solutionEvaluator;

import sokoban.Sokoban;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;

/**
 * 
 * Class to simulate Kattis and evaluate our sokoban solver.
 * 
 * @author Ariel Ekgren
 *
 */

public class SolutionEvaluator {
	private Sokoban path;
	private String filePath;
    // Insert Level number
    private MapReader map = new MapReader(31);

	public SolutionEvaluator() throws IOException{
		System.out.println("Initializing evaluation... \n");
		long startTime = System.currentTimeMillis();
		
		//String filePath = "./sokoban/res/00_sample.in";
		//String filePath = "./sokoban/res/TestTrack.in";

		// Initializing sokoban solver object with map from file.
		path = new Sokoban(new StringReader(map.getMap()), false);
		
		System.out.println("\nTime to execute: " + Double.toString((System.currentTimeMillis() - startTime)/1000.0) + " seconds.\n");
		animateSolution();
	}
	
	public void animateSolution() throws FileNotFoundException, IOException{
		Vector<StringBuilder> board = new Vector<StringBuilder>();

		BufferedReader fileBr = new BufferedReader(new StringReader(map.getMap()));
		
		StringBuilder line = null;
		
		while(fileBr.ready()) {
			line = new StringBuilder(fileBr.readLine());
			board.add(line);
		} // End while
		fileBr.close();

		
		
	}
	
    /*public void printMap(){
        for(StringBuilder s : board){
                System.out.println(s);
        }
        try {
            Thread.sleep(50);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }*/
	
	public static void main(String[] args) throws IOException{
		SolutionEvaluator MapEval = new SolutionEvaluator();
	}
}
