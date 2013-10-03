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
	
	State initState;
	
	// Time field
    private long startTime;

	HashSet<State> visitedStates = new HashSet<State>();
	//PriorityQueue<State> simpleQueue = new PriorityQueue<State>(10000, comparator);
	
	public Solver(Board board){
		
		if(Sokoban.debug) System.out.println("IN THE SOLVE!");
		initState = Factory.createState();
		initState.initialState(board);
		initState.createChildren();
	}
}
