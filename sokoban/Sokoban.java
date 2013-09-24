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

public class Sokoban {

	/**
	 * @param args
	 * 
	 */
	
	public static boolean debugMode = true;
	
	public Sokoban(Reader r) throws IOException{
		 final Client client = new Client();
		 final Board board = client.getBoardFromFile();
		 final Visualizer visual = new Visualizer();
		 
		 Solver solver = new Solver(); //Reaches info incl. initial state from Map staticaly.
		 State finalState = solver.getFinalState();
		 
		 //System.out.println(solver.solutionPath());
		 
		 //Board.printGoalGrad(1);

		 Visualizer.printOriginalMap(0);
		 Visualizer.printCleanMap(0);
		 Visualizer.printGoalGrad(1);
	}
	
	public static void main(String[] args) throws IOException {
		 Sokoban soko = new Sokoban(new InputStreamReader(System.in));
	} // main
} // End Main