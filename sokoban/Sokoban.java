package sokoban;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Vector;

/**
 * NEO-SOKOBAN SOKOBAN CLASS.
 * 
 * @author Ariel
 */
public class Sokoban {
	
	public static boolean debug = false;
	public static boolean debugTime = true;
	public String solution;
	
	/**
	 * You can say that the magic happens in this constructor.
	 * @param r
	 * @param debug
	 * @throws IOException
	 */
	public Sokoban (Reader r, boolean debug) throws IOException{
		
		//Apply debug setting.
		this.debug = debug;
		
		/** "Fun" debug message to signal start of program. */
		if(debug) System.out.println("NEO-SOKOBAN IS ONLINE.");
		
		// Create board.
		final Board board = new Board(r);
		
		// Initialize solver.
		Solver solver = new Solver(board);
		
		// Set the solution string to desired answer.
		solution = "";
		
		/** "Fun" debug message to signal end of program. */
		if(debug) System.out.println("NEO-SOKOBAN HAS TERMINATED.");
	}
	
	public static void main(String[] args) throws IOException {
		// Initiate and run Sokoban solver.
	    Sokoban soko = new Sokoban(new InputStreamReader(System.in), false);
        // Prints the solution to Kattis
        System.out.println(soko.solution.toUpperCase());
    } 
}
