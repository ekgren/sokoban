package sokoban;

import java.awt.Point;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * NEO-SOKOBAN SOLVER CLASS.
 *
 */

public class Solver {
	
	/** Comparator for states. */
	public class StatePriorityComparator implements Comparator<State>{

		@Override
		public int compare(State state1, State state2) {
			if(state1.getH() < state2.getH()) return -1;
			if(state1.getH() > state2.getH()) return 1;
			return 0;
		}
	}
	
	private static State initState;
	private static State solvedState;
	private static State processState;
	
	// Time field
    private long startTime;

	private Comparator<State> comparator = new StatePriorityComparator();
	private PriorityQueue<State> open = new PriorityQueue<State>(10000, comparator);
	private HashSet<State> closed = new HashSet<State>();
	
	public Solver(Board board){
		if(Sokoban.debug) System.out.println("IN THE SOLVE!");
		
		// Create initial state.
		initState = Factory.createState();
		initState.initialState(board);
		
		// Create solved state.
		solvedState = Factory.createState();
		for(Cell goal : board.getGoals()) solvedState.addBoxAt(goal);
		solvedState.isSolved = true;
		
		// Create first children.
		initState.createChildren(open, closed);

		// Start exploration.
		while(open.peek().equals(solvedState) == false){
			
			// Get first element from open.
        	processState = open.remove();
        	processState.createChildren(open, closed);
        	closed.add(processState);
			
		}
		
		// Set the last state as solvedState.
		solvedState = open.remove();
	}
	
	/**  */
	public static String getSolution(){
		// Solution String.
		String solution = "";
		
		// Cells.
		Cell goalCell = null;
		Cell startCell;
		
		// Current state.
		State currentState = solvedState;
		
		// While not initial state.
		while(currentState.getParent() != null){
			
			solution = fromIntToString(currentState.getPreviousMove()) + solvedState;
			
			if(currentState.getPreviousMove() == 0) goalCell = Factory.getCellDown(currentState.getPlayer());
			else if(currentState.getPreviousMove() == 1) goalCell = Factory.getCellUp(currentState.getPlayer());
			else if(currentState.getPreviousMove() == 2) goalCell = Factory.getCellRight(currentState.getPlayer());
			else if(currentState.getPreviousMove() == 3) goalCell = Factory.getCellLeft(currentState.getPlayer());
			
			currentState = currentState.getParent();
			
			startCell = Factory.getCell(currentState.getPlayer());
			
			solution = Search.AstarString(currentState, startCell, goalCell);
		}
		
		return solution;
	}
	
	public static String fromIntToString(int previousMove){
		if(previousMove == 0) return "U";
		if(previousMove == 1) return "D";
		if(previousMove == 2) return "L";
		if(previousMove == 3) return "R";
		return "";
	}
}
