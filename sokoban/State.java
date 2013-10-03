package sokoban;

import java.awt.Point;
import java.util.HashSet;

/**
 * NEO-SOKOBAN STATE CLASS.
 * 
 * Class to store game states.
 * 
 */

public class State implements Cloneable{
	
	// Parent state
	private State parentState;
	
	// Boxes and player
	private HashSet<Point> boxes = new HashSet<Point>();;
	private Player player;

    /** Empty constructor. */
   	public State() {
   	}
   	
   	/** Not so empty constructor. */
   	public State(State parentState){
   		this.parentState = parentState;
   	}
   	
   	/** USE ONLY FOR INITIAL STATE. */
   	public void initialState(Board board){
   		this.player = Factory.createPlayer(board.getPlayer());
   		for(Point p : board.getBoxes()){
   			this.boxes.add(Factory.createBox(p));
   		}
   	}
   	
   	/** Create children. */
   	public void createChildren(){
   		
   		// For each box in the box array we call the method boxMoves().
   		for(Point box : boxes){
   			boxMoves(box);
   		}
   	}
   	
   	/** Examine how a specific box can be moved. */
   	public void boxMoves(Point box){
   		
   		// We start by retrieving the box we want to examine 
   		// from the boxes in current state.
   		Box examine = (Box) box;
   		
   		if(Sokoban.debug)System.out.println(box.toString());
   		
   		// We examine if we can move the box UP on the map. 
   		// To move the box up both the cell above and below it need to be free.
   		if(Factory.getCellUp(examine) != null &&
   		   Factory.getCellDown(examine) != null &&
   		   Factory.getCellUp(examine).boxAllowed){
   			
   			// We then have to make sure that there is no box in the desired direction of push
   			// or where the player needs to be to do the push.
   			if(boxes.contains(Factory.getCellUp(examine)) == false &&
   				boxes.contains(Factory.getCellDown(examine))== false){
	   			
   				// If we can move the box in this direction we also have to check if the player
	   			// can move to the cell to push the box.
	   			if(Search.aStar(this, player, Factory.getCellDown(examine)) != 0){
	   				
	   				// Now we have determined that it is a valid move, time to act on it.
	   				if(Sokoban.debug) System.out.println("UP MOTHERFUCKER!");
	   			}
   			}
   		}
   		
   		// We examine if we can move the box DOWN on the map.
   		if(Factory.getCellDown(examine) != null &&
   		   Factory.getCellUp(examine) != null &&
   		   Factory.getCellDown(examine).boxAllowed){

   			// We then have to make sure that there is no box in the desired direction of push
   			// or where the player needs to be to do the push.
   			if(boxes.contains(Factory.getCellDown(examine)) == false &&
   	   			boxes.contains(Factory.getCellUp(examine))== false){
   				
   				// If we can move the box in this direction we also have to check if the player
	   			// can move to the cell to push the box.
	   			if(Search.aStar(this, player, Factory.getCellUp(examine)) != 0){
	   				
	   				// Now we have determined that it is a valid move, time to act on it.
	   				if(Sokoban.debug) System.out.println("DOWN MOTHERFUCKER!");
	   			}
   			}
   		}	
   		
   		// We examine if we can move the box to the LEFT on the map.
   		if(Factory.getCellLeft(examine) != null &&
   		   Factory.getCellRight(examine) != null &&
   		   Factory.getCellLeft(examine).boxAllowed){
   			
   			// We then have to make sure that there is no box in the desired direction of push
   			// or where the player needs to be to do the push.
   			if(boxes.contains(Factory.getCellLeft(examine)) == false &&
   	   				boxes.contains(Factory.getCellRight(examine))== false){
	   			
	   			// If we can move the box in this direction we also have to check if the player
	   			// can move to the cell to push the box.
	   			if(Search.aStar(this, player, Factory.getCellRight(examine)) != 0){
	   				
	   				// Now we have determined that it is a valid move, time to act on it.
	   				if(Sokoban.debug) System.out.println("LEFT MOTHERFUCKER!");
	   			}
   			}
   		}
   		
   		// We examine if we can move the box to the RIGHT on the map.
   		if(Factory.getCellRight(examine) != null &&
   		   Factory.getCellLeft(examine) != null &&
   		   Factory.getCellRight(examine).boxAllowed){
   			
   			// We then have to make sure that there is no box in the desired direction of push
   			// or where the player needs to be to do the push.
   			if(boxes.contains(Factory.getCellRight(examine)) == false &&
   	   				boxes.contains(Factory.getCellLeft(examine))== false){
	   			
	   			// If we can move the box in this direction we also have to check if the player
	   			// can move to the cell to push the box.
	   			if(Search.aStar(this, player, Factory.getCellLeft(examine)) != 0){
	   				
	   				// Now we have determined that it is a valid move, time to act on it.
	   				if(Sokoban.debug) System.out.println("RIGHT MOTHERFUCKER!");
	   			}
   			}
   		}
   	}
   	
   	public boolean gotBoxAt(Point p){
   		return boxes.contains(p);
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
