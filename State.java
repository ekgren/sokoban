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
	private Vector<Cell> reachableCells;
	private Vector<Cell> unReachableCells;

	private int playerRow;
	private int playerCol;
	private int parentKey; //reference to make it possible to find parent state - for final path building.
	
	/**
	 * Constructs the internal representation of the State
	 *
	 * USED ONLY WHEN CREATING INITIAL STATE
	 * @param 
	 */
	public State(Vector<Box> pBoxes, int pPlayerRow, int pPlayerCol) {
		boxes = pBoxes;
		playerRow = pPlayerRow;
		playerCol= pPlayerCol;
			
		for(Box box : pBoxes){
			if(isConnected(playerRow, playerCol, box.getRow(),box.getCol())){
				if(isConnected(playerRow, playerCol, box.getRow(),box.getCol()))
			}
		}
		
	} // End constructor State

	/**
	 * Constructs a state which is the result of applying one move.
	 * "final" in arguments to avoid applying changes to parrent state
	 */
	public State(final State pParentState, final BoxMove pMove) {
		/*
		 * TODO
		 */
		this.boxes = (Vector<Box>) pParentState.boxes.clone();
		
		
	} // End constructor State

	
	/**
	 * Operates on an (empty) Vector with States
	 * and "fills" with all successor states
	 * @param pStates
	 */
	void allSuccessors(Vector<State> pStates, int pBox) {
	    /*
	     * TODO
	     * 
	     * Idea taken from HW1
	     * I guess that the reason for sending in an empty vector that this function
	     * "operates" on is to avoid incorrect references...
	     */
		
	    pStates.clear();

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
		
	}// End TryMove
	
	
	/**
	 * Checks if there is a box on this position (in this state)
	 * 
	 * @param pRow
	 * @param pCol
	 * @return
	 */
	public boolean isBox( int pRow, int pCol){
		/*
		 * TODO
		 * 
		 * Very inefficient way of checking must be rewritten later.
		 */
		
		for(Box box : boxes){
			if (box.getRow() == pRow && box.getCol() == pCol){
			 return true;
			}
		}
		return false;
	}
	
	private boolean isConnected(Cell pCell1, Cell pCell){
		
		//skrivs av Adam...
		
		return true;
	}
	
	private boolean isConnected(int pRow1, int pCol1,  int pRow2 , int pCol2){
		
		//skrivs av Adam...
		
		return true;
	}
	/**
	 * Returnerar den cell som expanderades senast innan
	 * 
	 * @param pRow1
	 * @param pCol1
	 * @param pRow2
	 * @param pCol2
	 * @return
	 */
	private cell cellNeighbour(int pRow1, int pCol1,  int pRTarget , int pCTarget){
		
		//skrivs av Adam...
		
		return new Cell(0, 0);
	}
	
	
} // End Class State
