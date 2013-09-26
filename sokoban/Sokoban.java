package sokoban;
/*
 * Main
 * 
 * Version 0.1
 * 
 * 
 * Gnerall conventions:
 * - All class constants desricptiveName
 * - All input parameters pDesricptiveName
 * - All local variables lDescriptiveName
 * - "GameState" is used equivalent to "Node"; one GameState is expanded, has parent and successors... 
 * 
 */

import java.io.*;
import java.util.Vector;

public class Sokoban {

	/**
	 * To test with our own maps we use the SolutionEvaluator class 
	 * to run the program.
	 * 
	 * @param args
	 * 
	 */
	
	public static boolean debugMode = true;
	public String solution;
	
	public Sokoban(Reader r, boolean debugMode) throws IOException{
		
		//final Client client = new Client();
		this.debugMode = debugMode;
		final Board board = getBoardFromFile(r);

		if (debugMode){
			Visualizer v = new Visualizer();
			Visualizer.printState(board.getInitialState(), "INITIAL STATE");
		}
		 
		Solver solver = new Solver(); //Reaches info incl. initial state from Map staticaly.
		State solution = solver.greedyBFS();
		 
	    if(solution.isFinalState()) this.solution = solver.getStrToGoal(solution);
        else this.solution = "no path";

	}
	
	public Board getBoardFromFile(Reader r) throws IOException{
		
		Vector<String> board = new Vector<String>();

		BufferedReader fileBr = new BufferedReader(r);
		
		String line;
		
		while((line = fileBr.readLine()) != null) {
			board.add(line);
        } // End while
		fileBr.close();
		
		return new Board(board);
	}
	
	public static void main(String[] args) throws IOException {
	    Sokoban soko = new Sokoban(new InputStreamReader(System.in), false);
        // Prints the solution to Kattis
        System.out.println(soko.solution.toUpperCase());
    } // main
} // End Main