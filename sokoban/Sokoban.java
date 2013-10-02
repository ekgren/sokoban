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
	
	public static boolean debugMode;
    public static boolean profilingMode;
    // Start constructorTime for the profiling
    private long startTime;

	public String solution;
	
	public Sokoban(Reader r, boolean debugMode, boolean profilingMode) throws IOException{
		
		//final Client client = new Client();
		this.debugMode = debugMode;
        this.profilingMode = profilingMode;

        // Start constructorTime for board init
        if (profilingMode) startTime = System.currentTimeMillis();

        // Set the board
        final Board board = getBoardFromFile(r);

        // End constructorTime for board init
        if (profilingMode) {
            System.err.println("Initialized board in: " +
                    (System.currentTimeMillis() - startTime) + " ms");
        }

		if (debugMode){
			Visualizer v = new Visualizer();
			Visualizer.printState(board.getInitialState(), "INITIAL STATE");
		}

        //Reaches info incl. initial state from Map statically.
		Solver solver = new Solver();

        // Start constructorTime search after solution
        if (profilingMode) startTime = System.currentTimeMillis();

        // Search after solution
        State solution = solver.greedyBFS();

        // End constructorTime searching for solution
        if (profilingMode)
            System.err.println("Searched for a solution in: " +
                    (System.currentTimeMillis() - startTime) + " ms");

	    if(solution.isFinalState()) this.solution = solver.getStrToGoal(solution);
        else this.solution = "no path";

        if(profilingMode) {
            System.err.println("\n--- Accumulated Time in State Methods ---");
            System.err.println("Time spent constructing states: " + State.constructorTime + " ms");
            System.err.println("Time spent in ''allSuccessors'': " + State.allSuccessorsTime + " ms");
        }
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
	    Sokoban soko = new Sokoban(new InputStreamReader(System.in), false, true);
        // Prints the solution to Kattis
        System.out.println(soko.solution.toUpperCase());
    } // main
} // End Main