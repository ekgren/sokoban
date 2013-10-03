package sokoban;
/**
 * NEO-SOKOBAN SOLVER CLASS.
 * 
 * @author Ariel
 *
 */

public class Solver {
	
	// Initial state of the game.
	private State initState;
	
	public Solver(){
		initState = Factory.createState();
	}
}
