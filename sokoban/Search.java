package sokoban;

import java.awt.Point;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * NEO-SOKOBAN SEARCH CLASS.
 *
 */
public class Search {
	
	public static String boxMoveSearchString = "";
	public static Point nextBestbox;
	public static Cell endPoint;
	
	/**
	 * A compare class to compare points.
	 */
	static class PointCompare implements Comparator<Point> {
	    
		// The goal of the comparison.
		static Point goal;
		
		// Tiny constructor.
		public PointCompare(){}
		
		/** Set different goal for comparator object. */
		public static void setGoal(Point goal){
			PointCompare.goal = goal;
		}
		
		/** Compare the manhattan distance between two points to a goal. */
		public int compare(final Point a, final Point b) {
	        if ((Math.abs(a.x - goal.x) + Math.abs(a.y - goal.y)) < 
	        	(Math.abs(b.x - goal.x) + Math.abs(b.y - goal.y))) {
	            return -1;
	        }
	        else if ((Math.abs(a.x - goal.x) + Math.abs(a.y - goal.y)) > 
		        	(Math.abs(b.x - goal.x) + Math.abs(b.y - goal.y))) {
	            return 1;
	        }
	        else {
	            return 0;
	        }
	    }
	}
	
	/**
	 * A compare class to compare points.
	 */
	static class BoxPointCompare implements Comparator<Point> {
	    
		// The goal of the comparison.
		static Point goal;
		
		// Tiny constructor.
		public BoxPointCompare(){}
		
		/** Set different goal for comparator object. */
		public static void setGoal(Point goal){
			PointCompare.goal = goal;
		}
		
		/** Compare the manhattan distance between two points to a goal. */
		public int compare(final Point a, final Point b) {
	        if ((Math.abs(a.x - goal.x) + Math.abs(a.y - goal.y)) < 
	        	(Math.abs(b.x - goal.x) + Math.abs(b.y - goal.y))) {
	            return -1;
	        }
	        else if ((Math.abs(a.x - goal.x) + Math.abs(a.y - goal.y)) > 
		        	(Math.abs(b.x - goal.x) + Math.abs(b.y - goal.y))) {
	            return 1;
	        }
	        else {
	            return 0;
	        }
	    }
	}
	
	private static Comparator<Point> pointComparator = new Search.PointCompare();
	private static PriorityQueue<Point> open = new PriorityQueue<Point>(100, pointComparator);
	private static HashSet<Point> closed = new HashSet<Point>();
	
	private static Comparator<Point> boxComparator = new Search.PointCompare();
	private static PriorityQueue<Point> openBox = new PriorityQueue<Point>(100, boxComparator);
	private static HashSet<Point> closedBox = new HashSet<Point>();
	
	
	public static boolean NextToAstar(State state, Point startPoint, Point goalPoint){
		if(Factory.getCellUp(goalPoint) != null && Search.Astar(state, startPoint, Factory.getCellUp(goalPoint), false) != null) return true;
		if(Factory.getCellDown(goalPoint) != null && Search.Astar(state, startPoint, Factory.getCellDown(goalPoint), false) != null) return true;
		if(Factory.getCellLeft(goalPoint) != null && Search.Astar(state, startPoint, Factory.getCellLeft(goalPoint), false) != null) return true;
		if(Factory.getCellRight(goalPoint) != null && Search.Astar(state, startPoint, Factory.getCellRight(goalPoint), false) != null) return true;
		return false;
	}
	
	/**
	 * Method that takes two points, a board state and a boolean and checks if 
	 * it is possible to walk between the two points. 
	 * @param state
	 * @param startPoint
	 * @param goalPoint
	 * @param returnPath
	 * @return
	 */
	public static String Astar(State state, Point startPoint, Point goalPoint, boolean returnPath) {
			
			boolean searchState = true;
			Point processCell = null;
			
			// Creating start and goal cells.
			Cell startCell = Factory.getCell(startPoint);
			Cell goalCell = Factory.getCell(goalPoint);
			
			// Set startCell parent to null to get rid of old searches.
			startCell.setParent(null);
			
			// Initializing comparator from cell class with goal position.
			Search.PointCompare.setGoal(goalCell);
			
			// Creating open and closed set for search.
			//PriorityQueue<Point> open = new PriorityQueue<Point>(100,comparator);
			open.clear();
			closed.clear();
			
	        // Initializing search.
	        open.offer(startCell);
	        try{
		        while(open.peek().equals(goalPoint)==false){

		        	// Get first element from open.
		        	processCell = open.remove();
		        	
		        	// Add it to closed.
		        	closed.add(processCell);
		        	
		        	/*
		        	 * See if neighbors can be created and if they can 
		        	 * add neighborCell to open if it's not already in there
		        	 * or in closed.
		        	 */
		        	
		        	// Check there is a map cell up and that it's not occupied by box.
		        	if(Factory.getCellUp(processCell) != null && 
		        			state.gotBoxAt(Factory.getCellUp(processCell)) == false){
		        		
		        		// Check that the new cell is neither in open or closed.
		        		if(open.contains(Factory.getCellUp(processCell)) == false 
		        				&& closed.contains(Factory.getCellUp(processCell)) == false){
		        			
		        			// If it is not then set parent and add it to open.
		        			Factory.getCellUp(processCell).setParent((Cell)processCell);
		        			open.add(Factory.getCellUp(processCell));
		        		}
		        	}
		        	
		        	// Check there is a map cell down and that it's not occupied by box.
		        	if(Factory.getCellDown(processCell) != null && 
		        			state.gotBoxAt(Factory.getCellDown(processCell)) == false){
		        		
		        		// Check that the new cell is neither in open or closed.
		        		if(open.contains(Factory.getCellDown(processCell)) == false 
		        				&& closed.contains(Factory.getCellDown(processCell)) == false){
		        			
		        			// If it is not then set parent and add it to open.
		        			Factory.getCellDown(processCell).setParent((Cell)processCell);
		        			open.add(Factory.getCellDown(processCell));
		        		}
		        	}
		        	
		        	// Check there is a map cell left and that it's not occupied by box.
		        	if(Factory.getCellLeft(processCell) != null && 
		        			state.gotBoxAt(Factory.getCellLeft(processCell)) == false){
		        		
		        		// Check that the new cell is neither in open or closed.
		        		if(open.contains(Factory.getCellLeft(processCell)) == false 
		        				&& closed.contains(Factory.getCellLeft(processCell)) == false){
		        			
		        			// If it is not then set parent and add it to open.
		        			Factory.getCellLeft(processCell).setParent((Cell)processCell);
		        			open.add(Factory.getCellLeft(processCell));
		        		}
		        	}
		        	
		        	// Check there is a map cell right and that it's not occupied by box.
		        	if(Factory.getCellRight(processCell) != null && 
		        			state.gotBoxAt(Factory.getCellRight(processCell)) == false){

		        		// Check that the new cell is neither in open or closed.
		        		if(open.contains(Factory.getCellRight(processCell)) == false 
		        				&& closed.contains(Factory.getCellRight(processCell)) == false){
		        			
		        			// If it is not then set parent and add it to open.
		        			Factory.getCellRight(processCell).setParent((Cell)processCell);
		        			open.add(Factory.getCellRight(processCell));
		        		}
		        	}
		    	}
	        } catch(NullPointerException e) {
	        	// If we run out of cells to search we can't find a way.
	        	searchState = false;
	        }
	        if(searchState==true){
	        	
	        	// If return path set to true: create and return string with path.
	        	if(returnPath){
	        		String walkingPath = "";
	        		Cell child = (Cell) open.remove();
	        		Cell parent;
	        		while(child.getParent() != null){
	        			parent = child.getParent();
	        			if(parent.x-child.x == 0 && parent.y-child.y > 0){
	        				walkingPath = 'u' + walkingPath;
	        			}
	        			if(parent.x-child.x == 0 && parent.y-child.y < 0){
	        				walkingPath = 'd' + walkingPath;
	        			}
	        			if(parent.x-child.x > 0 && parent.y-child.y == 0){
	        				walkingPath = 'l' + walkingPath;
	        			}
	        			if(parent.x-child.x < 0 && parent.y-child.y == 0){
	        				walkingPath = 'r' + walkingPath;
	        			}
	        			child = parent;
	        		}
	        		return walkingPath;
	        		
	        		// Else just return empty string.
	        		} else return "";
	        }
	        // If no path found: return null.
			else return null;
		}
	
	/**
	 * Method that takes two points, a board state and a boolean and checks if 
	 * it is possible to walk between the two points. 
	 * @param state
	 * @param startPoint
	 * @param goalPoint
	 * @param returnPath
	 * @return
	 */
	public static String BoxAstar(State state, Point box, Point goalPoint, boolean returnPath) {
		
			// New state that we move box in.
			State newState = Factory.createState();
			
			// If we can't push the box to the goal we move it to the next best position.
			Search.nextBestbox = null;
			
			// Dont remember what this variable does.
			boolean searchState = true;
			
			// The cell that we process.
			Point processCell = null;
						
			// Remove box to avoid collisions.
			state.getBoxes().remove(box);
			
			// Creating start and goal cells.
			Cell startCell = Factory.getCell(box);
			Cell goalCell = Factory.getCell(goalPoint);
			
			// Next best cell.
			Cell nextBestCell = null;
			
			// Set startCell parent to null to get rid of old searches.
			startCell.setParent(null);
			startCell.lastMove = 4;
			
			// Initializing comparator from cell class with goal position.
			Search.BoxPointCompare.setGoal(goalCell);
			
			// Creating open and closed set for search.
			//PriorityQueue<Point> open = new PriorityQueue<Point>(100,comparator);
			openBox.clear();
			closedBox.clear();
			
	        // Initializing search.
	        openBox.offer(startCell);
	        
	        try{
		        while(openBox.peek().equals(goalPoint)==false){
		        	
		        	// Get first element from open.
		        	processCell = openBox.remove();
		        	
		        	// Add it to closed.
		        	closedBox.add(processCell);
		        	
		        	/*
		        	 * See if neighbors can be created and if they can 
		        	 * add neighborCell to open if it's not already in there
		        	 * or in closed.
		        	 */
		        	
		        	// Check there is a map cell up and that it's not occupied by box.
		        	if(state.boxUp(processCell, true)){
		        		
		        		// Check that the new cell is neither in open or closed.
		        		if(openBox.contains(Factory.getCellUp(processCell)) == false 
		        				&& closedBox.contains(Factory.getCellUp(processCell)) == false){
		        			
		        			// If it is not then set parent and add it to open.
		        			Factory.getCellUp(processCell).setBoxParent((Cell)processCell);
		        			Factory.getCellUp(processCell).boxMoveSearch = Search.boxMoveSearchString;
		        			Factory.getCellUp(processCell).lastMove = 0;
		        			openBox.add(Factory.getCellUp(processCell));
		        		}
		        	}
		        	
		        	// Check there is a map cell down and that it's not occupied by box.
		        	if(state.boxDown(processCell, true)){
		        		
		        		// Check that the new cell is neither in open or closed.
		        		if(openBox.contains(Factory.getCellDown(processCell)) == false 
		        				&& closedBox.contains(Factory.getCellDown(processCell)) == false){
		        			
		        			// If it is not then set parent and add it to open.
		        			Factory.getCellDown(processCell).setBoxParent((Cell)processCell);
		        			Factory.getCellDown(processCell).boxMoveSearch = Search.boxMoveSearchString;
		        			Factory.getCellDown(processCell).lastMove = 1;
		        			openBox.add(Factory.getCellDown(processCell));
		        		}
		        	}
		        	
		        	// Check there is a map cell left and that it's not occupied by box.
		        	if(state.boxLeft(processCell, true)){
		        		
		        		// Check that the new cell is neither in open or closed.
		        		if(openBox.contains(Factory.getCellLeft(processCell)) == false 
		        				&& closedBox.contains(Factory.getCellLeft(processCell)) == false){
		        			
		        			// If it is not then set parent and add it to open.
		        			Factory.getCellLeft(processCell).setBoxParent((Cell)processCell);
		        			Factory.getCellLeft(processCell).boxMoveSearch = Search.boxMoveSearchString;
		        			Factory.getCellLeft(processCell).lastMove = 2;
		        			openBox.add(Factory.getCellLeft(processCell));
		        		}
		        	}
		        	
		        	// Check there is a map cell right and that it's not occupied by box.
		        	if(state.boxRight(processCell, true)){

		        		// Check that the new cell is neither in open or closed.
		        		if(openBox.contains(Factory.getCellRight(processCell)) == false 
		        				&& closedBox.contains(Factory.getCellRight(processCell)) == false){
		        			
		        			// If it is not then set parent and add it to open.
		        			Factory.getCellRight(processCell).setBoxParent((Cell)processCell);
		        			Factory.getCellRight(processCell).boxMoveSearch = Search.boxMoveSearchString;
		        			Factory.getCellRight(processCell).lastMove = 3;
		        			openBox.add(Factory.getCellRight(processCell));
		        		}
		        	}
		    	}
	        } catch(NullPointerException e) {
	        	// If we run out of cells to search we can't find a way.
	        	searchState = false;
	        	int min = Integer.MAX_VALUE;
	        	for(Point cell : closedBox){
	        		if(Math.abs(cell.x - goalCell.x) + Math.abs(cell.y - goalCell.y) < min){
	        			min = Math.abs(cell.x - goalCell.x) + Math.abs(cell.y - goalCell.y);
	        			nextBestCell = (Cell) cell;
	        		}
	        	}
	        	Search.nextBestbox = nextBestCell;
	        }
	        
	        state.getBoxes().add(box);
	        
        	// If return path set to true: create and return string with path.
        	if(returnPath){
        		String walkingPath = "";
        		Cell child;
        		if(searchState==true) child = (Cell) openBox.remove();
        		else child = nextBestCell;
        		Search.endPoint = child;
        		Cell parent = null;
        		while(child.getBoxParent() != null){
        			parent = child.getBoxParent();
        			if(parent.x-child.x == 0 && parent.y-child.y > 0){
        				walkingPath = child.boxMoveSearch + 'U' + walkingPath;
        			}
        			if(parent.x-child.x == 0 && parent.y-child.y < 0){
        				walkingPath = child.boxMoveSearch + 'D' + walkingPath;
        			}
        			if(parent.x-child.x > 0 && parent.y-child.y == 0){
        				walkingPath = child.boxMoveSearch + 'L' + walkingPath;
        			}
        			if(parent.x-child.x < 0 && parent.y-child.y == 0){
        				walkingPath = child.boxMoveSearch + 'R' + walkingPath;
        			}
        			child = parent;
        		}
        		return walkingPath;
	        		
        		// Else just return empty string.
        		} else return "";
		}
}
