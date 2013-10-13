package sokoban;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * NEO-SOKOBAN SOKOBAN CLASS.
 * 
 */
public class Sokoban {
	
	public static boolean debug = false;
	public static boolean debugTime = true;
	public String solution;
	public static Board board;
	
	/**
	 * You can say that the magic happens in this constructor.
	 * @param r
	 * @param debug
	 * @throws IOException
	 */
	public Sokoban (Reader r, boolean debug, boolean debugTime) throws IOException{
		
		//Apply debug setting.
		Sokoban.debug = debug;
		Sokoban.debugTime = debugTime;
		
		/** "Fun" debug message to signal start of program. */
		if(debug) System.out.println("NEO-SOKOBAN IS ONLINE.");
		
		// Create an initial batch of empty states, players and boxes.
		Factory.initializeBoxes();
		Factory.initializePlayers();
		Factory.initializeStates();
		
		// Create board.
		board = new Board(r);
		
		// Initialize solver.
		@SuppressWarnings("unused")
		Solver solver = new Solver(board);
		
		// Set the solution string to desired answer.
		solution = Solver.getSolution();
		//solution = "ddrRRRRRRRRRRRRRRRRRRRRRRRRRRRurDD";
		/** "Fun" debug message to signal end of program. */
		if(debug) System.out.println(Factory.getStateCount());
		if(debug) System.out.println("NEO-SOKOBAN HAS TERMINATED.");
		if(debugTime) TimeIt.print();
	}
	
	public static void main(String[] args) throws IOException {
		// Initiate and run Sokoban solver.
	    Sokoban soko = new Sokoban(new InputStreamReader(System.in), false, false);
        // Prints the solution to Kattis
        System.out.println(soko.solution.toUpperCase());
    } 
}
