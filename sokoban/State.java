package sokoban;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import com.sun.xml.internal.ws.util.StringUtils;

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
	private double h = 0;
	private int hashCode;
	private State parentState; // Parent state
	
	// Move to get to this state here.
	// Where 0 = up, 1 = down, 2 = left and 3 = right.
	private int previousMove;
	
	// Boxes and player
	private HashSet<Point> boxes = new HashSet<Point>();
	private Player player;
	
	// List containing children.
	private static Queue<State> childStates = new LinkedList<State>();
	
	private static int xMax;
	private static int yMax;
	
	private static ArrayList<char[]> map = new ArrayList<char[]>();
		
	
	// 40 prime numbers for hashing.
	private static int[] primes = {31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 
		79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 
		157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229};

    
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
   	 * Create child states from valid moves.
   	 * 
   	 * @return
   	 */
   	public Queue<State> createAllChildren(){
   		// Start time for this method.
   		if(Sokoban.debugTime) TimeIt.createAllChildren = System.currentTimeMillis();
   		
   		// Clear old child states.
   		childStates.clear();
   		
   		// For each box in the box array we call the method boxMoves.
   		for(Point box : boxes){
   			if(boxUp(box)) childStates.add(createNewState(box, Factory.getCellUp(box), 0));
   			if(boxDown(box)) childStates.add(createNewState(box, Factory.getCellDown(box), 1));
   			if(boxLeft(box)) childStates.add(createNewState(box, Factory.getCellLeft(box), 2));
   			if(boxRight(box)) childStates.add(createNewState(box, Factory.getCellRight(box), 3));
   		}
   		
   		// End time for this method.
   		if(Sokoban.debugTime) TimeIt.createAllChildrenTotal = TimeIt.createAllChildrenTotal + System.currentTimeMillis() - TimeIt.createAllChildren;
   		
   		// Return new child states.
   		return childStates;
   	}
   	
   	
   	/** Create a new state. */
   	public State createNewState(Point currentPosition, Point newPosition, int previousMove){
   		
   		if(Sokoban.debugTime) TimeIt.createNewState = System.currentTimeMillis();
   		
   		// Create new state and put it in stateHolder until finished.
   		State stateHolder = Factory.createState();
		stateHolder.setParent(this);
		stateHolder.setPreviousMove(previousMove);
		
		// Add new player to state.
		stateHolder.player = Factory.createPlayer(currentPosition);
		
		// Create copies of boxes in this state.
   		for(Point p : boxes){
   			Box b = Factory.createBox(p);
   			if(Board.goals.contains(p)) b.onGoal = true;
   			stateHolder.boxes.add(b);
   		}
   		
   		// Remove the box we moved.
   		stateHolder.boxes.remove(currentPosition);
   		
   		// Add new moved box.
   		Box b = Factory.createBox(newPosition);
   		if(Board.goals.contains(newPosition)) b.onGoal = true;
   		stateHolder.boxes.add(b);
		
   		// Calculate hash code and heuristic.
   		stateHolder.calculateHashCode();
   		stateHolder.heuristic();
   		
   		// End time for this method.
   		if(Sokoban.debugTime) TimeIt.createNewStateTotal = TimeIt.createNewStateTotal + System.currentTimeMillis() - TimeIt.createNewState;
   		
		return stateHolder;
   	}
   	
   	
   	/**
   	 * Check if box can be moved up.
   	 * 
   	 * @param box
   	 * @return
   	 */
   	public boolean boxUp(Point box){
   		
   		if(Sokoban.debugTime) TimeIt.boxMoveAllowed = System.currentTimeMillis();
   		
   		// We examine if we can move the box UP on the map. 
   		// To move the box up both the cell above and below it need to be free.
   		if(Factory.getCellUp(box) != null &&
   		   Factory.getCellDown(box) != null &&
   		   Factory.getCellUp(box).boxAllowed){
   			
   			// We then have to make sure that there is no box in the desired direction of push
   			// or where the player needs to be to do the push.
   			if(boxes.contains(Factory.getCellUp(box)) == false &&
   				boxes.contains(Factory.getCellDown(box))== false){
	   			
   				// If we can move the box in this direction we also have to check if the player
	   			// can move to the cell to push the box.
	   			if(Search.Astar(this, player, Factory.getCellDown(box), false) != null){
	   				
	   				// End time for this method.
	   		   		if(Sokoban.debugTime) TimeIt.boxMoveAllowedTotal = TimeIt.boxMoveAllowedTotal + System.currentTimeMillis() - TimeIt.boxMoveAllowed;
	   		   		
	   				return true;
	   				
	   			} else {
	   				// End time for this method.
	   		   		if(Sokoban.debugTime) TimeIt.boxMoveAllowedTotal = TimeIt.boxMoveAllowedTotal + System.currentTimeMillis() - TimeIt.boxMoveAllowed;
	   		   		return false;
	   			}
   			} else{
   				// End time for this method.
   		   		if(Sokoban.debugTime) TimeIt.boxMoveAllowedTotal = TimeIt.boxMoveAllowedTotal + System.currentTimeMillis() - TimeIt.boxMoveAllowed;
   				return false;
   			}
   		} else {
				// End time for this method.
		   		if(Sokoban.debugTime) TimeIt.boxMoveAllowedTotal = TimeIt.boxMoveAllowedTotal + System.currentTimeMillis() - TimeIt.boxMoveAllowed;
   			return false;
   		}
   	}
   	
   	
   	/**
   	 * Check if box can be moved down.
   	 * 
   	 * @param box
   	 * @return
   	 */
   	public boolean boxDown(Point box){
   		
   		if(Sokoban.debugTime) TimeIt.boxMoveAllowed = System.currentTimeMillis();
   		
   		// We examine if we can move the box DOWN on the map.
   		// To move the box down both the cell above and below it need to be free.
   		if(Factory.getCellDown(box) != null &&
   		   Factory.getCellUp(box) != null &&
   		   Factory.getCellDown(box).boxAllowed){

   			// We then have to make sure that there is no box in the desired direction of push
   			// or where the player needs to be to do the push.
   			if(boxes.contains(Factory.getCellDown(box)) == false &&
   	   			boxes.contains(Factory.getCellUp(box))== false){
   				
   				// If we can move the box in this direction we also have to check if the player
	   			// can move to the cell to push the box.
	   			if(Search.Astar(this, player, Factory.getCellUp(box), false) != null){
	   				// End time for this method.
	   		   		if(Sokoban.debugTime) TimeIt.boxMoveAllowedTotal = TimeIt.boxMoveAllowedTotal + System.currentTimeMillis() - TimeIt.boxMoveAllowed;
	   				return true;
	   				
	   			} else{
	   				// End time for this method.
	   		   		if(Sokoban.debugTime) TimeIt.boxMoveAllowedTotal = TimeIt.boxMoveAllowedTotal + System.currentTimeMillis() - TimeIt.boxMoveAllowed;
	   		   		return false;
	   			}
   			} else{
   				// End time for this method.
   		   		if(Sokoban.debugTime) TimeIt.boxMoveAllowedTotal = TimeIt.boxMoveAllowedTotal + System.currentTimeMillis() - TimeIt.boxMoveAllowed;
   				return false;
   			}
   		} else{
			// End time for this method.
	   		if(Sokoban.debugTime) TimeIt.boxMoveAllowedTotal = TimeIt.boxMoveAllowedTotal + System.currentTimeMillis() - TimeIt.boxMoveAllowed;
   			return false;
   		}
   	}
   		
   	/**
   	 * Check if box can be moved left.
   	 * 
   	 * @param box
   	 * @return
   	 */
   	public boolean boxLeft(Point box){
   		
   		if(Sokoban.debugTime) TimeIt.boxMoveAllowed = System.currentTimeMillis();
   		
   		// We examine if we can move the box to the LEFT on the map.
   		// To move the box left both the cell to the left and to the right of it need to be free.
   		if(Factory.getCellLeft(box) != null &&
   		   Factory.getCellRight(box) != null &&
   		   Factory.getCellLeft(box).boxAllowed){
   			
   			// We then have to make sure that there is no box in the desired direction of push
   			// or where the player needs to be to do the push.
   			if(boxes.contains(Factory.getCellLeft(box)) == false &&
   	   				boxes.contains(Factory.getCellRight(box))== false){
	   			
	   			// If we can move the box in this direction we also have to check if the player
	   			// can move to the cell to push the box.
	   			if(Search.Astar(this, player, Factory.getCellRight(box), false) != null){
	   				
	   				// End time for this method.
	   		   		if(Sokoban.debugTime) TimeIt.boxMoveAllowedTotal = TimeIt.boxMoveAllowedTotal + System.currentTimeMillis() - TimeIt.boxMoveAllowed;
	   				
	   				return true;
	   				
	   			} else {
	   				// End time for this method.
	   		   		if(Sokoban.debugTime) TimeIt.boxMoveAllowedTotal = TimeIt.boxMoveAllowedTotal + System.currentTimeMillis() - TimeIt.boxMoveAllowed;
	   				return false;
	   			}
   			} else {
   				// End time for this method.
   		   		if(Sokoban.debugTime) TimeIt.boxMoveAllowedTotal = TimeIt.boxMoveAllowedTotal + System.currentTimeMillis() - TimeIt.boxMoveAllowed;
   				return false;
   			}
   		} else {
			// End time for this method.
	   		if(Sokoban.debugTime) TimeIt.boxMoveAllowedTotal = TimeIt.boxMoveAllowedTotal + System.currentTimeMillis() - TimeIt.boxMoveAllowed;
   			return false;
   		}
   	}
   	
   	
   	/**
   	 * Check if box can be moved right.
   	 * 
   	 * @param box
   	 * @return
   	 */
   	public boolean boxRight(Point box){
   		
   		if(Sokoban.debugTime) TimeIt.boxMoveAllowed = System.currentTimeMillis();
   		
   		// We examine if we can move the box to the RIGHT on the map.
   		// To move the box right both the cell to the left and to the right of it need to be free.
   		if(Factory.getCellRight(box) != null &&
   		   Factory.getCellLeft(box) != null &&
   		   Factory.getCellRight(box).boxAllowed){
   			
   			// We then have to make sure that there is no box in the desired direction of push
   			// or where the player needs to be to do the push.
   			if(boxes.contains(Factory.getCellRight(box)) == false &&
   	   				boxes.contains(Factory.getCellLeft(box))== false){
	   			
	   			// If we can move the box in this direction we also have to check if the player
	   			// can move to the cell to push the box.
	   			if(Search.Astar(this, player, Factory.getCellLeft(box), false) != null){
	   				
	   				// End time for this method.
	   		   		if(Sokoban.debugTime) TimeIt.boxMoveAllowedTotal = TimeIt.boxMoveAllowedTotal + System.currentTimeMillis() - TimeIt.boxMoveAllowed;
	   				
	   				return true;
	   				
	   			} else {
	   				// End time for this method.
	   		   		if(Sokoban.debugTime) TimeIt.boxMoveAllowedTotal = TimeIt.boxMoveAllowedTotal + System.currentTimeMillis() - TimeIt.boxMoveAllowed;
	   				return false;
	   			}
   			} else {
   				// End time for this method.
   		   		if(Sokoban.debugTime) TimeIt.boxMoveAllowedTotal = TimeIt.boxMoveAllowedTotal + System.currentTimeMillis() - TimeIt.boxMoveAllowed;
   				return false;
   			}
   		} else {
			// End time for this method.
	   		if(Sokoban.debugTime) TimeIt.boxMoveAllowedTotal = TimeIt.boxMoveAllowedTotal + System.currentTimeMillis() - TimeIt.boxMoveAllowed;
   			return false;
   		}
   	}
   	
   	
   	/**
   	 * Calculate hash code for state.
   	 */
   	public void calculateHashCode(){
   		if(Sokoban.debugTime) TimeIt.stateHashCode = System.currentTimeMillis();
   		
   		xMax = 0;
   		yMax = 0;
   		
   		for(Point box : boxes){
   			if(box.x > xMax) xMax = box.x;
   			if(box.y > yMax) yMax = box.y;
   		}
   		
   		map.clear();
   		
   		for(int i = 0; i < yMax + 1; ++i){
   			char[] array = new char[xMax + 1];
   		    Arrays.fill(array, Character.forDigit(0, 10));
   		    map.add(array);
   		}
   		
   		for(Point box : boxes){
   			map.get(box.y)[box.x] = Character.forDigit(1, 10);
   		}
   		
   		for(int i = 0; i < map.size(); i++){
   			hashCode = hashCode + primes[i]*Integer.parseInt(new String(map.get(i)), 2);
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
        		if(Factory.getCell(box).isGoal) h = h - 2;
        }
        
   		if(Sokoban.debugTime) TimeIt.heuristicTotal = TimeIt.heuristicTotal + System.currentTimeMillis() - TimeIt.heuristic;
   	}
   	
   	
   	/** Returns value of heuristic. */
   	public double getH(){
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
