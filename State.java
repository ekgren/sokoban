/*
 * State
 * 
 * Version 0.1
 * 
 * Represents the Sokoban State.
 * 
 * Should not be confused with the class Map. 
 */

import java.util.Vector;

public class State implements Cloneable {
	
	private Vector<Box> boxes;
	private BoxMove mLastMove;
	private int playerRow;
	private int playerCol;
	private Map map;
	
	/**
	 * Constructs the internal representation of the State
	 *
	 * @param 
	 */
	public State(Vector<Box> pBoxes, int pPlayerRow, int pPlayerCol) {
		boxes = pBoxes;
		playerRow = pPlayerRow;
		playerCol= pPlayerCol;
	
	} // End constructor State

	/**
	 * Constructs a state which is the result of applying one move.
	 * "final" in arguments to avoid applying changes to parrent state
	 */
	public State(final State pParrentState, final BoxMove pMove) {
		/*
		 * TODO
		 */
		this.boxes = (Vector<Box>) pParrentState.boxes.clone();
		
		
	} // End constructor State

	
	/**
	 * Operates on an (empty) Vector with States
	 * and "fills" with all successor states
	 * @param pStates
	 */
	void allSuccessors(Vector<State> pStates) {
	    /*
	     * TODO
	     * 
	     * Idea taken from HW1
	     * I guess that the reason for sending in an empty vector that this function
	     * "operates" on is to avoid incorrect references...
	     * Since the game information is splited up between constants in Map and dynamical in State
	     * one must also send in a reference to pState...
	     */
		
	    pStates.clear();

		Vector<BoxMove> lMoves = new Vector<BoxMove>();

		//FILL IT WITH POSSIBLE MOVES...
		for( Box box : boxes){
			
		}
		
	    // Convert moves to GameStates
	    for (int i = 0; i < lMoves.size(); i++) {
			try{
				pStates.add(new State((State) this.clone(), lMoves.elementAt(i))); 
				//is cloning here really needed?? since I clone the information in the constructor?
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    } // End for
	    
	} // End allSuccesors
	
	/**
	 * Tries to make a move from a certain position
	 *
	 * @param pMoves vector where the valid moves will be inserted
	 * @param pBox the box that is tried to move

	 */
	void tryMove(Vector<BoxMove> pMoves, Box pBox) {
		int lR = pBox.getRow();
		int lC = pBox.getCol();
		
		if(Map.isFree(lR-1, lC)){
			//pMoves.add(new BoxMove(MoveType.MOVE_UPMOVE_UP, 1));
			
		}
		
	} // End TryMove
	
} // End Class State
