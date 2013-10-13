package sokoban;

import java.awt.Point;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * NEO-SOKOBAN SOLVER CLASS.
 * 
 * 
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
	private static State processState2;
	
	// Time field.
    private long startTime;

    // Queue stuff.
	private Comparator<State> comparator = new StatePriorityComparator();
	private PriorityQueue<State> open = new PriorityQueue<State>(10000, comparator);
	private HashSet<State> closed = new HashSet<State>();
	private Queue<State> children;
	private Queue<Box> boxQueue = new LinkedList<Box>();
	
	
	/**
	 * Solver constructor.
	 * @param board
	 */
	public Solver(Board board){
		if(Sokoban.debug) System.out.println("Solver: initiated.");
		
		// Create initial state.
		initState = Factory.createState();
		initState.initialState(board);
		
		if(Sokoban.debug) System.out.println("Solver: initial state created.");
		if(Sokoban.debug) Sokoban.board.printState(initState);
		processState = initState;

		for(Box box : Sokoban.board.getBoxes()){
			if(Sokoban.debug) System.out.println("Solver: for box.");
			if(Sokoban.debug) System.out.println(box.toString());
			
			if(Factory.getCell(box).isGoal==false && Factory.getCell(box).getGradient() > 3 && Search.NextToAstar(processState, processState.getPlayer(), box)){
				if(Sokoban.debug) System.out.println("Solver: Execute fast move.");
//				processState.getBoxes().remove(box);
				String path = Search.BoxAstar(processState, box, box.dreamGoal, true);
				
				processState2 = processState.createNewState(box, Search.endPoint, Search.endPoint.lastMove, true);
				
				processState2.setParent(processState);
				processState2.longStep = true;
				processState2.path = path;
//				processState.getBoxes().add(box);
				processState = processState2;
				if(Sokoban.debug) Sokoban.board.printState(processState);
			} else {

			}
		}
		
		
		// Create solved state with all boxes on goals.
		solvedState = Factory.createState();
		for(Cell goal : board.getGoals()) solvedState.addBoxAt(goal);
		solvedState.isSolved = true;
		
		// Start timer.
		if(Sokoban.debugTime) startTime = System.currentTimeMillis(); 
		
		// Create first children.
		if(processState != null) open.add(processState);
		else open.add(initState);
		
		State test = open.peek();
		if(Sokoban.debug) Sokoban.board.printState(test);
				
		// Start exploration.
		while(open.peek().equals(solvedState) == false){
			Factory.expandedNodes++;
			
			/*
			if(Factory.expandedNodes<100){
				Sokoban.board.printState(open.peek());
			}*/
			
			// Get first element from open.
        	processState = open.remove();
        	closed.add(processState);
        	
        	// Get children.
        	children = processState.createAllChildren();
        	
        	// For each child.
        	while(children.isEmpty() == false){
        		
        		// Get child.
        		processState = children.remove();
        		
        		// Check if visited state.
        		if(closed.contains(processState) == false){
        			
        			// Check that no two boxes are next to each other against a wall.
        			/* Clocking start. */ if(Sokoban.debugTime) TimeIt.deadlock1 = System.currentTimeMillis();
        			boolean deadlock1 = Deadlocks.checkWallDeadlock(processState);
        			/* Clocking end. */ if(Sokoban.debugTime) TimeIt.deadlock1Total = TimeIt.deadlock1Total + System.currentTimeMillis() - TimeIt.deadlock1;
        			
        			// If not deadlock proceed.
        			if(deadlock1 == false){
        				
        				// Check that four boxes are not pushed together.
        				/* Clocking start. */ if(Sokoban.debugTime) TimeIt.deadlock2 = System.currentTimeMillis();
        	   			boolean deadlock2 = Deadlocks.checkFourBoxesDeadlock(processState);
        	   			/* Clocking end. */ if(Sokoban.debugTime) TimeIt.deadlock2Total = TimeIt.deadlock2Total + System.currentTimeMillis() - TimeIt.deadlock2;
        	   			
        	   			// If not deadlock proceed.
        	   			if(deadlock2 == false){
        	   				
        	   				// Check that three boxes not stuck at corner.
        	   				/* Clocking start. */ if(Sokoban.debugTime) TimeIt.deadlock3 = System.currentTimeMillis();
            	   			boolean deadlock3 = Deadlocks.checkWallDeadlock(processState);
            	   			/* Clocking end. */ if(Sokoban.debugTime) TimeIt.deadlock3Total = TimeIt.deadlock3Total + System.currentTimeMillis() - TimeIt.deadlock3;
                			
            	   			// If not deadlock proceed.
            	   			if(deadlock3 == false){

            	   				// Add child to open and closed.
            	   				Factory.createdNodes++;
                    			open.add(processState); 
                    			closed.add(processState);		
                			}
        	   			}
        			}
        		}
        	} // End for each child.
		}
		
		
		// Print statement with times.
        if (Sokoban.debugTime) {
        	long endTime = System.currentTimeMillis() - startTime;
	        double seconds = (double) endTime / 1000;
	        System.err.println("\n--- Greedy BFS ---");
	        System.err.println("Expanded nodes for: " + endTime + " ms");
	        System.err.println("Number of Expanded nodes/second: " + Factory.expandedNodes / seconds);
	        System.err.println("Number of Created nodes/second: " + Factory.getStateCount() / seconds);
        }
		
		// Set the last state as solvedState.
		solvedState = open.remove();
	}
	
	
	/**
	 * Method for retrieving the final solution.
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
			
			if(currentState.longStep == true){
				solution = currentState.path + solution;
				currentState = currentState.getParent();
			}
			else {
				solution = fromIntToString(currentState.getPreviousMove()) + solution;
				
				if(currentState.getPreviousMove() == 0) goalCell = Factory.getCellDown(currentState.getPlayer());
				else if(currentState.getPreviousMove() == 1) goalCell = Factory.getCellUp(currentState.getPlayer());
				else if(currentState.getPreviousMove() == 2) goalCell = Factory.getCellRight(currentState.getPlayer());
				else if(currentState.getPreviousMove() == 3) goalCell = Factory.getCellLeft(currentState.getPlayer());
				
				currentState = currentState.getParent();
				
				startCell = Factory.getCell(currentState.getPlayer());
				
				solution = Search.Astar(currentState, startCell, goalCell, true) + solution;
			}
		}
		
		return solution;
	}
	
	
	/**
	 * Method that converts the integer representation of previous step to string.
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
