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
	
	// Heuristic & hashCode.
	private int h = 0;
	private int hashCode;
	private State parentState; // Parent state
	
	// Move to get to this state here.
	// Where 0 = up, 1 = down, 2 = left and 3 = right.
	private int previousMove;
	
	// Boxes and player
	private HashSet<Point> boxes = new HashSet<Point>();
	private Player player;

    
	/** Empty constructor. */
   	public State() {
   	}
   	
   	
   	/** Constructor that takes parent state. */
   	public State(State parentState){
   		this.parentState = parentState;
   	}
   	
   	
   	/** 
   	 * USE ONLY FOR INITIAL STATE. 
   	 * */
   	public void initialState(Board board){
   		this.player = Factory.createPlayer(board.getPlayer());
   		for(Point p : board.getBoxes()){
   			this.boxes.add(Factory.createBox(p));
   		}
   	}
   	
   	
   	/**
   	 * Create children.
   	 * 
   	 * @param open
   	 * @param semiOpen
   	 * @param closed
   	 * @param openHash
   	 */
   	public void createChildren(PriorityQueue<State> open, HashSet<State> semiOpen, HashSet<State> closed, HashSet<State> openHash){
   		if(Sokoban.debugTime) TimeIt.createChildren = System.currentTimeMillis();
   		
   		State bestState = null;
   		// For each box in the box array we call the method boxMoves().
   		for(Point box : boxes){
   			boxMoves(box, open, semiOpen, closed, openHash, bestState);
   		}
   		
   		if(Sokoban.debugTime) TimeIt.createChildrenTotal = TimeIt.createChildrenTotal + System.currentTimeMillis() - TimeIt.createChildren;
   	}
   	
   	
   	/**
   	 * Examine how a specific box can be moved.
   	 * 
   	 * @param box
   	 * @param open
   	 * @param open2
   	 * @param closed
   	 * @param openHash
   	 * @param bestState
   	 */
   	public void boxMoves(Point box, PriorityQueue<State> open, HashSet<State> open2, HashSet<State> closed, HashSet<State> openHash, State bestState){
   		
   		// We start by retrieving the box we want to examine 
   		// from the boxes in current state.
   		Box examine = (Box) box;
   		State stateHolder = null;
   		
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
	   				createNewState(bestState, examine, Factory.getCellUp(examine), openHash, open2, closed, open);
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
	   				createNewState(bestState, examine, Factory.getCellDown(examine), openHash, open2, closed, open);
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
	   				createNewState(bestState, examine, Factory.getCellLeft(examine), openHash, open2, closed, open);
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
	   				createNewState(bestState, examine, Factory.getCellRight(examine), openHash, open2, closed, open);
	   			}
   			}
   		}
   	}
   	
   	
   	/**
   	 * Create new state.
   	 * 
   	 * @param bestState
   	 * @param oldBoxCell
   	 * @param newBoxCell
   	 * @param openHash
   	 * @param open2
   	 * @param closed
   	 * @param open
   	 */
   	public void createNewState(State bestState, Box oldBoxCell, Cell newBoxCell, HashSet<State> openHash, HashSet<State> open2,
   			HashSet<State> closed, PriorityQueue<State> open){
	   		
			// Create new state and put it in stateHolder until finished.
			State stateHolder = Factory.createState();
			stateHolder.setParent(this);
			
			// Add new player to state.
			stateHolder.player = Factory.createPlayer(oldBoxCell);
			
			// Create copies of boxes in this state.
	   		for(Point p : boxes){
	   			Box b = Factory.createBox(p);
	   			if(Board.goals.contains(p)) b.onGoal = true;
	   			stateHolder.boxes.add(b);
	   		}
	   		
	   		// Remove the box we moved.
	   		stateHolder.boxes.remove(oldBoxCell);
	   		// Add new moved box.
	   		Box b = Factory.createBox(newBoxCell);
	   		if(Board.goals.contains(Factory.getCellUp(oldBoxCell))) b.onGoal = true;
	   		stateHolder.boxes.add(b);
			stateHolder.calculateHashCode();
	   		// Add state to PriorityQueue.
	   		if(openHash.contains(stateHolder) == false &&
	   			open2.contains(stateHolder) == false &&
	   			closed.contains(stateHolder) == false && 
	   			Deadlocks.checkWallDeadlock(stateHolder) == false &&
	   			Deadlocks.checkFourBoxesDeadlock(stateHolder) == false){
	   			
	   			stateHolder.heuristic();
	   			stateHolder.setPreviousMove(0);
	   			if(Solver.BFS == false){
		   			if(bestState != null){
		   				if(bestState.h > stateHolder.h){
		   					open2.add(bestState);
		   					bestState = stateHolder;
		   				} else open2.add(stateHolder);
		   			} else bestState = stateHolder;
	   			} else{
	   				openHash.add(stateHolder);
	   				open.add(stateHolder);
	   			}
	   			
	   		} else {
	   			//
	   		}
   	}
   	
   	
   	/**
   	 * Calculate hash code for state.
   	 */
   	public void calculateHashCode(){
   		if(Sokoban.debugTime) TimeIt.stateHashCode = System.currentTimeMillis();
   		
   		for(Point box : boxes){
   			hashCode = hashCode + box.hashCode();
   		}
   		
   		if(Sokoban.debugTime) TimeIt.stateHashCodeTotal = TimeIt.stateHashCodeTotal + System.currentTimeMillis() - TimeIt.stateHashCode;
   	}
   	
   	
   	/**
   	 * State hashCode method. Returns the product of all the boxes hash codes.
   	 * @return
   	 */
   	@Override
	public int hashCode() {
		return hashCode;
	}
   	
   	
   	/**
   	 * State equals method.
   	 * @param obj
   	 * @return
   	 */
   	@Override
	public boolean equals(Object obj) {
   		// Time.
   		if(Sokoban.debugTime) TimeIt.stateEquals = System.currentTimeMillis();
   		
		// If same object... well.
   		if (this == obj){
   			if(Sokoban.debugTime) TimeIt.stateEqualsTotal = TimeIt.stateEqualsTotal + System.currentTimeMillis() - TimeIt.stateEquals;
   			return true;
   		}
   		if (obj == null){
   			if(Sokoban.debugTime) TimeIt.stateEqualsTotal = TimeIt.stateEqualsTotal + System.currentTimeMillis() - TimeIt.stateEquals;
   			return false;
   		}
   		
   		// If object does not belong to State class.
		if (!(obj instanceof State)){
			if(Sokoban.debugTime) TimeIt.stateEqualsTotal = TimeIt.stateEqualsTotal + System.currentTimeMillis() - TimeIt.stateEquals;
			return false;
		}
	    
		// Cast object as State object.
		State otherState = (State) obj;
	    
		
	    // If the states have boxes at different positions return false.
	    for(Point box : otherState.boxes){
	    	if(boxes.contains(box) == false){
	    		if(Sokoban.debugTime) TimeIt.stateEqualsTotal = TimeIt.stateEqualsTotal + System.currentTimeMillis() - TimeIt.stateEquals;
	    		return false;
	    	}
	    }
	    
	    if(otherState.isSolved){
	    	/*for(Point box : otherState.boxes){
	 	    	if(boxes.contains(box) == false) return false;
	 	    }*/
	    	if(Sokoban.debug)System.out.println("WIN");
	    	if(Sokoban.debugTime) TimeIt.stateEqualsTotal = TimeIt.stateEqualsTotal + System.currentTimeMillis() - TimeIt.stateEquals;
	    	return true;
	    }
	    
	    // Returns true if players can walk to each other.
	    if(Search.Astar(this, player, otherState.player, false) != null){
	    	if(Sokoban.debugTime) TimeIt.stateEqualsTotal = TimeIt.stateEqualsTotal + System.currentTimeMillis() - TimeIt.stateEquals;
	    	return true;
	    }
	    
	    if(Sokoban.debugTime) TimeIt.stateEqualsTotal = TimeIt.stateEqualsTotal + System.currentTimeMillis() - TimeIt.stateEquals;
	    return false;
   	}
   	
   	
   	/** 
   	 * Heuristic function that sums over all distance from boxes to goals. 
   	 * */
   	public void heuristic(){
   		if(Sokoban.debugTime) TimeIt.heuristic = System.currentTimeMillis();
        
   		for (Point box : boxes) {
        		h  = h + Factory.getCell(box).getGradient();
        		if(Factory.getCell(box).isGoal) h = h - 1;
        }
        
   		if(Sokoban.debugTime) TimeIt.heuristicTotal = TimeIt.heuristicTotal + System.currentTimeMillis() - TimeIt.heuristic;
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
   	
   	
   	/** Return boxes. */
   	public HashSet<Point> getBoxes(){
   		return boxes;
   	}
   	
   	
   	/** Returns true if box at point p. */
   	public boolean gotBoxAt(Point p){
   		return boxes.contains(p);
   	}
   	
   	
   	/**
   	 * Set parent state.
   	 * 
   	 * @param parentState
   	 */
   	public void setParent(State parentState){
   		this.parentState = parentState;
   	}
   	
   	
   	/**
   	 * Returns parent state.
   	 * 
   	 * @return
   	 */
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
   	
  
   	/** Get player. */
   	public Player getPlayer(){
   		return player;
   	}

}
