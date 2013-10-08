package sokoban;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * NEO-SOKOBAN SOLVER CLASS.
 */

public class Solver {
	
	
	/**
	 * Comparator class.
	 */
	public class StatePriorityComparator implements Comparator<State>{

		@Override
		public int compare(State state1, State state2) {
			double h1 = state1.getH();
			double h2 = state2.getH();
			if (h1 < h2){
				return -1; //move forward?!
			}
			else if (h2 < h1){
				return 1;
			}
			return 0;
		}
	}
	
	// States.
	private static State initState;
	private static State solvedState;
	private static State processState;
	public static boolean BFS = true;
	
	// Time field.
    private long startTime;

    // Queue stuff.
	private Comparator<State> comparator = new StatePriorityComparator();
	private PriorityQueue<State> open = new PriorityQueue<State>(10000, comparator);
	private HashSet<State> closed = new HashSet<State>();
	private Queue<State> children;
	
	
	/**
	 * 
	 * @param board
	 */
	public Solver(Board board){
		if(Sokoban.debug) System.out.println("IN THE SOLVE!");
		
		// Create initial state.
		initState = Factory.createState();
		initState.initialState(board);
		
		// Create solved state.
		solvedState = Factory.createState();
		for(Cell goal : board.getGoals()) solvedState.addBoxAt(goal);
		solvedState.isSolved = true;
		
		if(Sokoban.debugTime) startTime = System.currentTimeMillis(); 
		
		// Create first children.
		open.add(initState);
		
		// Start exploration.
		while(open.peek().equals(solvedState) == false){
			// Get first element from open.
        	processState = open.remove();
        	closed.add(processState);
        	
        	children = processState.createAllChildren();
        	
        	while(children.isEmpty() == false){
        		processState = children.remove();
        		if(closed.contains(processState) == false &&
    				Deadlocks.checkWallDeadlock(processState) == false &&
    	   			Deadlocks.checkFourBoxesDeadlock(processState) == false &&
    	   			Deadlocks.checkWallDeadlock(processState) == false){
        			// Be careful might not want to add it 
        			// to open and closed at the same time.
        			open.add(processState); 
        			closed.add(processState);
        		}
        	}
		}
		
		
		// End time
        if (Sokoban.debugTime) {
        	long endTime = System.currentTimeMillis() - startTime;
	        double seconds = (double) endTime / 1000;
	        System.err.println("\n--- Greedy BFS ---");
	        System.err.println("Expanded nodes for: " + endTime + " ms");
	        System.err.println("Number of Expanded nodes/second: " + Factory.getStateCount() / seconds);
	        System.err.println("Number of Created nodes/second: " + Factory.getCreatedStates() / seconds);
    }
		
		// Set the last state as solvedState.
		solvedState = open.remove();
	}
	
	
	/**
	 * Method for retrieving the final solution.
	 * 
	 * @return
	 */
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
			
			solution = fromIntToString(currentState.getPreviousMove()) + solution;
			
			if(currentState.getPreviousMove() == 0) goalCell = Factory.getCellDown(currentState.getPlayer());
			else if(currentState.getPreviousMove() == 1) goalCell = Factory.getCellUp(currentState.getPlayer());
			else if(currentState.getPreviousMove() == 2) goalCell = Factory.getCellRight(currentState.getPlayer());
			else if(currentState.getPreviousMove() == 3) goalCell = Factory.getCellLeft(currentState.getPlayer());
			
			currentState = currentState.getParent();
			
			startCell = Factory.getCell(currentState.getPlayer());
			
			solution = Search.Astar(currentState, startCell, goalCell, true) + solution;
		}
		
		return solution;
	}
	
	
	/**
	 * 
	 * @param previousMove
	 * @return
	 */
	public static String fromIntToString(int previousMove){
		if(previousMove == 0) return "U";
		if(previousMove == 1) return "D";
		if(previousMove == 2) return "L";
		if(previousMove == 3) return "R";
		return "";
	}
}
