package solutionEvaluator;

import sokoban.Sokoban;

import java.io.IOException;
import java.io.StringReader;

/**
 * 
 * Class to simulate Kattis and evaluate our sokoban solver.
 * 
 * @author Ariel Ekgren
 *
 */

public class SolutionEvaluator {
	private Sokoban path;

	public SolutionEvaluator() throws IOException{
		System.out.println("Initializing evaluation... \n");
		long startTime = System.currentTimeMillis();
		
		//String filePath = "./sokoban/res/00_sample.in";
		//String filePath = "./sokoban/res/TestTrack.in";
        // Insert Level number
        MapReader map = new MapReader(31);

		// Initializing sokoban solver object with map from file.
		path = new Sokoban(new StringReader(map.getMap()), false);
		
		System.out.println("Time to execute: " + Double.toString((System.currentTimeMillis() - startTime)/1000.0) + " seconds.");
		animateSolution();
	}
	
	public void animateSolution(){
		System.out.println(path.solution);
	}
	
	public static void main(String[] args) throws IOException{
		SolutionEvaluator MapEval = new SolutionEvaluator();
	}
}
