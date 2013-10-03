package sokoban;

import java.util.HashSet;

/**
 * NEO-SOKOBAN STATE CLASS.
 * 
 * Class to store game states.
 * 
 */

public class State implements Cloneable{
	
	private State parentState;
	
	private Box boxes[];
	private Player player;

    /** Empty constructor. */
   	public State() {}
   	
   	/** Not so empty constructor. */
   	public State(State parentState){
   		this.parentState = parentState;
   	}
   	
   	/** Create children. */
   	public void createChildren(){
   		
   		// For each box in the box array we call the method boxMoves().
   		for(int i = 0 ;  i < boxes.length ; i++){
   			boxMoves(i);
   		}
   	}
   	
   	/** Examine how a specific box can be moved. */
   	public void boxMoves(int boxIndex){
   		// We start by retrieving the box we want to examine 
   		// from the box array in current state.
   		Box examine = boxes[boxIndex];
   		
   		// We examine if we can move the box up. To move the box up both
   		// the cell above and below it need to be free.
   		if(Factory.getCellUp(examine) != null && //Check above
   		   Factory.getCellDown(examine) != null && //Check below
   		   Factory.getCellUp(examine).boxAllowed){
   			// If we can move the box in this direction we also have to check if the player
   			// can move to the cell to push the box.
   			if(Search.aStar(this, player, Factory.getCellDown(examine)) != 0);
   		}
   		
   		// We examine if we can move the box down.
   		if(Factory.getCellDown(examine) != null && Factory.getCellDown(examine).boxAllowed){
   			
   		}
   		
   		// We examine if we can move the box to the left.
   		if(Factory.getCellLeft(examine) != null && Factory.getCellLeft(examine).boxAllowed){
   			
   		}
   		
   		// We examine if we can move the box to the right.
   		if(Factory.getCellRight(examine) != null && Factory.getCellRight(examine).boxAllowed){
   			
   		}
   	}
   	
   	@Override
	public int hashCode() {
		return 0;
	}
   	
   	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (!(obj instanceof State))return false;
	    State otherState = (State) obj;
	    return false;
   	}

}
