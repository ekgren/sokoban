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
	 * @param args
	 * 
	 */
	
	public static boolean debugMode = true;
	
	public Sokoban(Reader r) throws IOException{
		 //final Client client = new Client();
		 final Board board = getBoardFromFile(r);
		 final Visualizer visual = new Visualizer();
		 
		 Solver solver = new Solver(); //Reaches info incl. initial state from Map staticaly.
		 State finalState = solver.getFinalState();
		 
		 //System.out.println(solver.solutionPath());
		 
		 //Board.printGoalGrad(1);

		 Visualizer.printOriginalMap(0);
		 Visualizer.printCleanMap(0);
		 Visualizer.printGoalGrad(1);
		 Visualizer.printState(board.getInitialState());
	}
	
	public Board getBoardFromFile(Reader r) throws IOException{
		
		Vector<String> board = new Vector<String>();

		BufferedReader fileBr = new BufferedReader(r);
		
		String line = null;
		
		while(fileBr.ready()) {
			line = fileBr.readLine();
			board.add(line);
		} // End while
		fileBr.close();
		
		return new Board(board);
	}
	
	public static void main(String[] args) throws IOException {
		 Sokoban soko = new Sokoban(new InputStreamReader(System.in));
	} // main
} // End Main