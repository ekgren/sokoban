package solutionEvaluator;

import sokoban.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
		String filePath = "./sokoban/res/TestTrack.in";

		
		// Initializing sokoban solver object with map from file.
		path = new Sokoban(new FileReader(filePath), false);
		
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
