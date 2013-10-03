package sokoban;

import java.util.HashSet;

/**
 * NEO-SOKOBAN STATE CLASS.
 * 
 * Class to store game states.
 * 
 * @author Ariel
 *
 */

public class State {
	
	State parentState;
	HashSet<Box> boxes = new HashSet<Box>();
	
	public State(){}
	
	public State(State state){}
	
	/** Method to reset state for reuse. */
	public void resetState(){}

}
