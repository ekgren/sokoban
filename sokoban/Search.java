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
	
	private static Comparator<Point> pointComparator = new Search.PointCompare();
	private static PriorityQueue<Point> open = new PriorityQueue<Point>(100, pointComparator);
	private static HashSet<Point> closed = new HashSet<Point>();
	
	
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
}
