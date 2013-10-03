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
			return 0;
		}
	}
	
	State initState;
	State solvedState;
	State processState;
	
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
		solvedState.addPlayerAt(-666, -666);
		
		// Create first children.
		initState.createChildren(open, closed);

		// Start exploration.
		while(open.peek().equals(solvedState) == false){
			
			// Get first element from open.
        	processState = open.remove();
        	processState.createChildren(open, closed);
        	closed.add(processState);
			
		}
		
		System.out.println("wtf?");
	}
}
