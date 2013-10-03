package sokoban;

import java.awt.Point;
import java.util.HashSet;
import java.util.PriorityQueue;

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
   	public void createChildren(PriorityQueue<State> open, HashSet<State> closed){
   		
   		// For each box in the box array we call the method boxMoves().
   		for(Point box : boxes){
   			boxMoves(box, open, closed);
   		}
   	}
   	
   	/** Examine how a specific box can be moved. */
   	public void boxMoves(Point box, PriorityQueue<State> open, HashSet<State> closed){
   		
   		// We start by retrieving the box we want to examine 
   		// from the boxes in current state.
   		Box examine = (Box) box;
   		State stateHolder;
   		
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
	   			if(Search.Astar(this, player, Factory.getCellDown(examine), true)){
	   				
	   				// Now we have determined that it is a valid move, time to act on it.
	   				if(Sokoban.debug) System.out.println("UP MOTHERFUCKER!");
	   				
	   				// Create new state and put it in stateHolder until finished.
	   				stateHolder = Factory.createState();
	   				// Add new player to state.
	   				stateHolder.player = Factory.createPlayer(examine);
	   				// Create copies of boxes in this state.
	   		   		for(Point p : boxes){
	   		   			stateHolder.boxes.add(Factory.createBox(p));
	   		   		}
	   		   		// Remove the box we moved.
	   		   		stateHolder.boxes.remove(examine);
	   		   		// Add new moved box.
	   		   		stateHolder.boxes.add(Factory.createBox(Factory.getCellUp(examine)));
	   		   		// Add state to PriorityQueue.
	   		   		if(open.contains(stateHolder) == false && 
	   		   				closed.contains(stateHolder) == false) open.add(stateHolder);
	   			}
   			}
   		}
   		
   		// We examine if we can move the box DOWN on the map.
   		// To move the box down both the cell above and below it need to be free.
   		if(Factory.getCellDown(examine) != null &&
   		   Factory.getCellUp(examine) != null &&
   		   Factory.getCellDown(examine).boxAllowed){

   			// We then have to make sure that there is no box in the desired direction of push
   			// or where the player needs to be to do the push.
   			if(boxes.contains(Factory.getCellDown(examine)) == false &&
   	   			boxes.contains(Factory.getCellUp(examine))== false){
   				
   				// If we can move the box in this direction we also have to check if the player
	   			// can move to the cell to push the box.
	   			if(Search.Astar(this, player, Factory.getCellUp(examine), true)){
	   				
	   				// Now we have determined that it is a valid move, time to act on it.
	   				if(Sokoban.debug) System.out.println("DOWN MOTHERFUCKER!");
	   			
	   				// Create new state and put it in stateHolder until finished.
	   				stateHolder = Factory.createState();
	   				// Add new player to state.
	   				stateHolder.player = Factory.createPlayer(examine);
	   				// Create copies of boxes in this state.
	   		   		for(Point p : boxes){
	   		   			stateHolder.boxes.add(Factory.createBox(p));
	   		   		}
	   		   		// Remove the box we moved.
	   		   		stateHolder.boxes.remove(examine);
	   		   		// Add new moved box.
	   		   		stateHolder.boxes.add(Factory.createBox(Factory.getCellDown(examine)));
	   		   		// Add state to PriorityQueue.
	   		   		if(open.contains(stateHolder) == false && 
	   		   				closed.contains(stateHolder) == false) open.add(stateHolder);
	   			}
   			}
   		}	
   		
   		// We examine if we can move the box to the LEFT on the map.
   		// To move the box left both the cell to the left and to the right of it need to be free.
   		if(Factory.getCellLeft(examine) != null &&
   		   Factory.getCellRight(examine) != null &&
   		   Factory.getCellLeft(examine).boxAllowed){
   			
   			// We then have to make sure that there is no box in the desired direction of push
   			// or where the player needs to be to do the push.
   			if(boxes.contains(Factory.getCellLeft(examine)) == false &&
   	   				boxes.contains(Factory.getCellRight(examine))== false){
	   			
	   			// If we can move the box in this direction we also have to check if the player
	   			// can move to the cell to push the box.
	   			if(Search.Astar(this, player, Factory.getCellRight(examine), true)){
	   				
	   				// Now we have determined that it is a valid move, time to act on it.
	   				if(Sokoban.debug) System.out.println("LEFT MOTHERFUCKER!");
	   				
	   				// Create new state and put it in stateHolder until finished.
	   				stateHolder = Factory.createState();
	   				// Add new player to state.
	   				stateHolder.player = Factory.createPlayer(examine);
	   				// Create copies of boxes in this state.
	   		   		for(Point p : boxes){
	   		   			stateHolder.boxes.add(Factory.createBox(p));
	   		   		}
	   		   		// Remove the box we moved.
	   		   		stateHolder.boxes.remove(examine);
	   		   		// Add new moved box.
	   		   		stateHolder.boxes.add(Factory.createBox(Factory.getCellLeft(examine)));
	   		   		// Add state to PriorityQueue.
	   		   		if(open.contains(stateHolder) == false && 
	   		   				closed.contains(stateHolder) == false) open.add(stateHolder);
	   			}
   			}
   		}
   		
   		// We examine if we can move the box to the RIGHT on the map.
   		// To move the box right both the cell to the left and to the right of it need to be free.
   		if(Factory.getCellRight(examine) != null &&
   		   Factory.getCellLeft(examine) != null &&
   		   Factory.getCellRight(examine).boxAllowed){
   			
   			// We then have to make sure that there is no box in the desired direction of push
   			// or where the player needs to be to do the push.
   			if(boxes.contains(Factory.getCellRight(examine)) == false &&
   	   				boxes.contains(Factory.getCellLeft(examine))== false){
	   			
	   			// If we can move the box in this direction we also have to check if the player
	   			// can move to the cell to push the box.
	   			if(Search.Astar(this, player, Factory.getCellLeft(examine), true)){
	   				
	   				// Now we have determined that it is a valid move, time to act on it.
	   				if(Sokoban.debug) System.out.println("RIGHT MOTHERFUCKER!");
	   				
	   				// Create new state and put it in stateHolder until finished.
	   				stateHolder = Factory.createState();
	   				// Add new player to state.
	   				stateHolder.player = Factory.createPlayer(examine);
	   				// Create copies of boxes in this state.
	   		   		for(Point p : boxes){
	   		   			stateHolder.boxes.add(Factory.createBox(p));
	   		   		}
	   		   		// Remove the box we moved.
	   		   		stateHolder.boxes.remove(examine);
	   		   		// Add new moved box.
	   		   		stateHolder.boxes.add(Factory.createBox(Factory.getCellRight(examine)));
	   		   		// Add state to PriorityQueue.
	   		   		if(open.contains(stateHolder) == false &&
	   		   			closed.contains(stateHolder) == false) open.add(stateHolder);
	   			}
   			}
   		}
   	}
   	
   	/** Returns true if box at point p. */
   	public boolean gotBoxAt(Point p){
   		return boxes.contains(p);
   	}
   	
   	/** Adds box at point p. */
   	public void addBoxAt(Point p){
   		boxes.add(Factory.createBox(p));
   	}
   	
   	/** Adds player at point p. */
   	public void addPlayerAt(int x, int y){
   		player = Factory.createPlayer(x, y);
   	}
   	
   	/** State hashCode method. Returns the product of all the boxes hashcodes. */
   	@Override
	public int hashCode() {
   		int multiHash = 1;
   		for(Point box : boxes){
   			multiHash = multiHash*box.hashCode() + box.hashCode();
   		}
		return multiHash;
	}
   	
   	/** State equals method. */
   	@Override
	public boolean equals(Object obj) {
		// If same object... well.
   		if (this == obj) return true;
   		if (obj == null) return false;
   		
   		// If object does not belong to State class.
		if (!(obj instanceof State)) return false;
	    
		// Cast object as State object.
		State otherState = (State) obj;
	    
	    // If the states have boxes at different positions return false.
	    for(Point box : otherState.boxes){
	    	if(boxes.contains(box) == false) return false;
	    }
	    
	    // This should only react to solved state.
	    if(otherState.player.x == -666 && otherState.player.y == -666 ) return true;
	    
	    // Returns true if players can walk to each other.
	    if(Search.Astar(this, player, otherState.player, true)) return true;
	    
	    return false;
   	}

}
