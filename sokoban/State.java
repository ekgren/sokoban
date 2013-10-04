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
	
	// MAGIC.
	public boolean isSolved = false;
	
	// Heuristic.
	private int h;
	
	// Parent state
	private State parentState;
	
	// Move to get to this state here.
	// Where 0 = up, 1 = down, 2 = left and 3 = right.
	private int previousMove;
	
	// Boxes and player
	private HashSet<Point> boxes = new HashSet<Point>();
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
   		
   		//if(Sokoban.debug)System.out.println(box.toString());
   		
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
	   			if(Search.Astar(this, player, Factory.getCellDown(examine), false) != null){
	   				   				
	   				// Create new state and put it in stateHolder until finished.
	   				stateHolder = Factory.createState();
	   				stateHolder.setParent(this);
	   				// Add new player to state.
	   				stateHolder.player = Factory.createPlayer(examine);
	   				// Create copies of boxes in this state.
	   		   		for(Point p : boxes){
	   		   			Box b = Factory.createBox(p);
	   		   			if(Board.goals.contains(p)) b.onGoal = true;
	   		   			stateHolder.boxes.add(b);
	   		   		}
	   		   		// Remove the box we moved.
	   		   		stateHolder.boxes.remove(examine);
	   		   		// Add new moved box.
	   		   		Box b = Factory.createBox(Factory.getCellUp(examine));
	   		   		if(Board.goals.contains(Factory.getCellUp(examine))) b.onGoal = true;
	   		   		stateHolder.boxes.add(b);
	   		   		// Add state to PriorityQueue.
	   		   		if(open.contains(stateHolder) == false && 
	   		   				closed.contains(stateHolder) == false){
	   		   			
	   		   			//stateHolder.heuristic();
	   		   			stateHolder.setPreviousMove(0);
	   		   			open.add(stateHolder);
	   		   			
	   		   		} else {
	   		   			// Here we should add the state to a queue for reuse.
	   		   		}
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
	   			if(Search.Astar(this, player, Factory.getCellUp(examine), false) != null){
	   			
	   				// Create new state and put it in stateHolder until finished.
	   				stateHolder = Factory.createState();
	   				stateHolder.setParent(this);
	   				// Add new player to state.
	   				stateHolder.player = Factory.createPlayer(examine);
	   				// Create copies of boxes in this state.
	   				for(Point p : boxes){
	   		   			Box b = Factory.createBox(p);
	   		   			if(Board.goals.contains(p)) b.onGoal = true;
	   		   			stateHolder.boxes.add(b);
	   		   		}
	   		   		// Remove the box we moved.
	   		   		stateHolder.boxes.remove(examine);
	   		   		// Add new moved box.
	   		   		Box b = Factory.createBox(Factory.getCellDown(examine));
	   		   		if(Board.goals.contains(Factory.getCellDown(examine))) b.onGoal = true;
	   		   		stateHolder.boxes.add(b);
	   		   		// Add state to PriorityQueue.
	   		   		if(open.contains(stateHolder) == false && 
	   		   				closed.contains(stateHolder) == false){
	   		   			
	   		   			//stateHolder.heuristic();
	   		   			stateHolder.setPreviousMove(1);
	   		   			open.add(stateHolder);
	   		   			
	   		   		} else {
	   		   			// Here we should add the state to a queue for reuse.
	   		   		}
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
	   			if(Search.Astar(this, player, Factory.getCellRight(examine), false) != null){
	   				
	   				// Create new state and put it in stateHolder until finished.
	   				stateHolder = Factory.createState();
	   				stateHolder.setParent(this);
	   				// Add new player to state.
	   				stateHolder.player = Factory.createPlayer(examine);
	   				// Create copies of boxes in this state.
	   				for(Point p : boxes){
	   		   			Box b = Factory.createBox(p);
	   		   			if(Board.goals.contains(p)) b.onGoal = true;
	   		   			stateHolder.boxes.add(b);
	   		   		}
	   		   		// Remove the box we moved.
	   		   		stateHolder.boxes.remove(examine);
	   		   		// Add new moved box.
	   		   		Box b = Factory.createBox(Factory.getCellLeft(examine));
	   		   		if(Board.goals.contains(Factory.getCellLeft(examine))) b.onGoal = true;
	   		   		stateHolder.boxes.add(b);
	   		   		// Add state to PriorityQueue.
	   		   		if(open.contains(stateHolder) == false && 
	   		   				closed.contains(stateHolder) == false){
	   		   			
	   		   			//stateHolder.heuristic();
	   		   			stateHolder.setPreviousMove(2);
	   		   			open.add(stateHolder);
	   		   			
	   		   		} else {
	   		   			// Here we should add the state to a queue for reuse.
	   		   		}
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
	   			if(Search.Astar(this, player, Factory.getCellLeft(examine), false) != null){
	   				
	   				// Create new state and put it in stateHolder until finished.
	   				stateHolder = Factory.createState();
	   				stateHolder.setParent(this);
	   				// Add new player to state.
	   				stateHolder.player = Factory.createPlayer(examine);
	   				// Create copies of boxes in this state.
	   				for(Point p : boxes){
	   		   			Box b = Factory.createBox(p);
	   		   			if(Board.goals.contains(p)) b.onGoal = true;
	   		   			stateHolder.boxes.add(b);
	   		   		}
	   		   		// Remove the box we moved.
	   		   		stateHolder.boxes.remove(examine);
	   		   		// Add new moved box.
	   		   		Box b = Factory.createBox(Factory.getCellRight(examine));
	   		   		if(Board.goals.contains(Factory.getCellRight(examine))) b.onGoal = true;
	   		   		stateHolder.boxes.add(b);
	   		   		// Add state to PriorityQueue.
	   		   		if(open.contains(stateHolder) == false &&
	   		   			closed.contains(stateHolder) == false){
	   		   			
	   		   			//stateHolder.heuristic();
	   		   			stateHolder.setPreviousMove(3);
	   		   			open.add(stateHolder);
	   		   			
	   		   		} else {
	   		   			// Here we should add the state to a queue for reuse.
	   		   		}
	   			}
   			}
   		}
   	}
   	
   	/** Returns true if box at point p. */
   	public boolean gotBoxAt(Point p){
   		return boxes.contains(p);
   	}
   	
   	/** Returns parent state. */
   	public void setParent(State parentState){
   		this.parentState = parentState;
   	}
   	
   	/** Returns parent state. */
   	public State getParent(){
   		return parentState;
   	}
   	
   	/** Adds box at point p. */
   	public void addBoxAt(Point p){
   		boxes.add(Factory.createBox(p));
   	}
   	
   	/** Adds player at point p. */
   	public void addPlayerAt(int x, int y){
   		player = Factory.createPlayer(x, y);
   	}
   	
   	/** Adds player at point p. */
   	public Player getPlayer(){
   		return player;
   	}
   	
   	/** State hashCode method. Returns the product of all the boxes hashcodes. */
   	@Override
	public int hashCode() {
   		int multiHash = 1;
   		for(Point box : boxes){
   			multiHash = multiHash + box.hashCode();
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
	    
	    if(otherState.isSolved){
	    	if(Sokoban.debug)System.out.println("WIN");
	    	return true;
	    }
	    
	    // Returns true if players can walk to each other.
	    if(Search.Astar(this, player, otherState.player, false) != null) return true;

	    return false;
   	}
   	
   	/** Heuristic function that sums over all distance from boxes to goals. */
   	public void heuristic(){
   		for(Point box : boxes){
   			Box boxHolder = (Box) box;
   			if(boxHolder.onGoal==false){
	   			for(Point goal : Board.goals){
	   				h = h + Math.abs(box.x-goal.x) + Math.abs(box.y-goal.y);
	   			}
   			}
   		}
   	}
   	
   	/** Returns value of heuristic. */
   	public int getH(){
   		return h;
   	}
   	
   	/** Method to set previous move. 
   	 * Where 0 = up, 1 = down, 2 = left and 3 = right. */
   	public void setPreviousMove(int previousMove){
   		this.previousMove = previousMove;
   	}
   	
   	/** Method to get previous move. 
   	 * Where 0 = up, 1 = down, 2 = left and 3 = right. */
   	public int getPreviousMove(){
   		return previousMove;
   	}

}
