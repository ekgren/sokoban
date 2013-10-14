package sokoban;
/*
 * Solver
 * 
 * Version 0.1
 * 
 * 2013.09.19
 *
 */


import java.util.*;

//import sokoban.Cell.NormComparator;

public class Solver {
	

	private State processState;
    // Time field
    private long startTime;
	
	/*
	 * The key-String represents the box configuration only.
	 * 
	 * If this configuration has "been visited" before a vector is returned
	 * containing the DIFFERNT states visited whith this config. (but whith
	 * player in different subspaces...)
	 */
    

	//Static fields for saving creations of Map,Queues, when calling isPathToBox.
    private static Cell.NormComparator comparatorCell = new Cell.NormComparator();
	private static HashSet<String> mapPrPathsCell = new HashSet<String>();
    private static PriorityQueue<Cell> queueCellChildren = new PriorityQueue<Cell>(10000,
    		comparatorCell);
    private static Cell STATIC_CELL = new Cell(0,0);
    
	HashSet<State> visitedStates = new HashSet<State>();
	Comparator stateComp = new StatePriorityComparator();
	OneBoxMoveComparator oneBoxMoveComp = new OneBoxMoveComparator();
	PriorityQueue<State> simpleQueue = new PriorityQueue<State>(10000, stateComp);
    PriorityQueue<State> newQueue = new PriorityQueue<State>(10000, stateComp);

	public Solver(){		
		/*
		 *TODO
		 *
		 * in the Board-object is everything needed; constant parameters from the
		 * Board object it self and the initial state which should be expanded;
		 *  
		 */
	}
	
	/* Must check if this hash function is a good one!*/ 
	public static String getHashString(int row,int col){
		return  String.valueOf(row) + String.valueOf(col);
	}

	// Tried to improve old getFinalState() with a* implementation (did not work(because of longer constructorTime)).
	public State aStar(){
		
		simpleQueue.offer(Board.getInitialState());
		
		while(simpleQueue.peek().isFinalState() == false){
			
			processState = simpleQueue.remove();
			visitedStates.add(processState);
			
			Vector<State> childsOfCurState = new Vector<State>();
			processState.allSuccessors(childsOfCurState); //fills with all children
			
			for (State child : childsOfCurState){
				
				int cost = child.getG() + 1; 
				
				if(simpleQueue.contains(child) == true && cost < child.getG()){
					simpleQueue.remove(child);
				}
				
				if(simpleQueue.contains(child) == false && visitedStates.contains(child) == false){
					simpleQueue.add(child);
				}
			}	
		}
		return simpleQueue.remove();
	}


	public State greedyBFS(){

        int lExpandedNodes = 0;
        int lCreatedNodes = 0;
		
        simpleQueue.add(Board.getInitialState());
        visitedStates.add(Board.getInitialState());
        // First state created
        lCreatedNodes++;

        // Start constructorTime iterating through nodes
        if (Sokoban.profilingMode) startTime = System.currentTimeMillis();

        // The current state var.
        State lCurState;
        Vector<State> childrenOfCurState = new Vector<State>();

        // Expand nodes until the queue is empty or until max iterations
        while (lExpandedNodes < 100000 && !simpleQueue.isEmpty()) {
        	
            // Get state first in line
            lCurState = simpleQueue.poll();
            // Add one; new expanded node
            lExpandedNodes++;

            // Visualizer.printStateDelux(lCurState, "--- State explored in iteration: #" + lExpandedNodes + " ---");
            
            // Clear and get new children of current state
            childrenOfCurState.clear();
            lCurState.allSuccessors(childrenOfCurState); //fills with all children


            // Add the number of new states
            lCreatedNodes = lCreatedNodes + childrenOfCurState.size();

            // Iterate through the children and add them to the queue and in Closed
            for (State child : childrenOfCurState){
                // If the child is final state, then return it!
                if (child.isFinalState()) {

                    // End constructorTime searching for solution
                    if (Sokoban.profilingMode) {
                        long endTime = System.currentTimeMillis() - startTime;
                        double seconds = (double) endTime / 1000;
                        System.err.println("\n--- Greedy BFS ---");
                        System.err.println("Expanded nodes for: " + endTime + " ms");
                        System.err.println("Number of Expanded nodes/second: " + lExpandedNodes / seconds);
                        System.err.println("Number of Created nodes/second: " + lCreatedNodes / seconds);
                    }

                    if(Sokoban.visualizeMode) Visualizer.printState(child, "/Solver: THE FINAL STATE IS FOUND! See below:");
                    return child;

                // If child is NOT in closed (Visited states), add it!
                } else if(!visitedStates.contains(child)){
                    visitedStates.add(child);
                    simpleQueue.add(child);

                } else {
                    // Add the state to the reusable container
                    State.addReusableState(child);
                }
            }
        }

        // End time
        if (Sokoban.profilingMode) {
            long endTime = System.currentTimeMillis() - startTime;
            double seconds = (double) endTime / 1000;
            System.err.println("\n--- Greedy BFS ---");
            System.err.println("Expanded nodes for: " + endTime + " ms");
            System.err.println("Number of Expanded nodes/second: " + lExpandedNodes / seconds);
            System.err.println("Number of Created nodes/second: " + lCreatedNodes / seconds);
        }

        if(Sokoban.debugMode)
            System.out.println("Solver line 77: No final sate was found, returned initial state.");

        return Board.getInitialState();
	} // End greedyBFS
	
	
	public State greedyBFS(Vector<State> pStates){

        int lExpandedNodes = 0;
        int lCreatedNodes = 0;
		
        for(State state : pStates){
        	simpleQueue.add(state);
        	visitedStates.add(state);
            if (state.isFinalState()) {
                if(Sokoban.visualizeMode) Visualizer.printState(state, "/Solver: THE FINAL STATE IS FOUND! See below:");
                return state;
            }
        }// First state created
        lCreatedNodes++;

        // Start constructorTime iterating through nodes
        if (Sokoban.profilingMode) startTime = System.currentTimeMillis();

        // The current state var.
        State lCurState;
        Vector<State> childrenOfCurState = new Vector<State>();

        // Expand nodes until the queue is empty or until max iterations
        while (lExpandedNodes < 10000 && !simpleQueue.isEmpty()) {
        	
            // Get state first in line
            lCurState = simpleQueue.poll();
            // Add one; new expanded node
            lExpandedNodes++;

            // Visualizer.printStateDelux(lCurState, "--- State explored in iteration: #" + lExpandedNodes + " ---");
            
            // Clear and get new children of current state
            childrenOfCurState.clear();
            lCurState.allSuccessors(childrenOfCurState); //fills with all children


            // Add the number of new states
            lCreatedNodes = lCreatedNodes + childrenOfCurState.size();

            // Iterate through the children and add them to the queue and in Closed
            for (State child : childrenOfCurState){
                // If the child is final state, then return it!
                if (child.isFinalState()) {

                    // End constructorTime searching for solution
                    if (Sokoban.profilingMode) {
                        long endTime = System.currentTimeMillis() - startTime;
                        double seconds = (double) endTime / 1000;
                        System.err.println("\n--- Greedy BFS ---");
                        System.err.println("Expanded nodes for: " + endTime + " ms");
                        System.err.println("Number of Expanded nodes/second: " + lExpandedNodes / seconds);
                        System.err.println("Number of Created nodes/second: " + lCreatedNodes / seconds);
                    }

                    if(Sokoban.visualizeMode) Visualizer.printState(child, "/Solver: THE FINAL STATE IS FOUND! See below:");
                    return child;

                // If child is NOT in closed (Visited states), add it!
                } else if(!visitedStates.contains(child)){
                    visitedStates.add(child);
                    simpleQueue.add(child);

                } else {
                    // Add the state to the reusable container
                    State.addReusableState(child);
                }
            }
        }

        // End time
        if (Sokoban.profilingMode) {
            long endTime = System.currentTimeMillis() - startTime;
            double seconds = (double) endTime / 1000;
            System.err.println("\n--- Greedy BFS ---");
            System.err.println("Expanded nodes for: " + endTime + " ms");
            System.err.println("Number of Expanded nodes/second: " + lExpandedNodes / seconds);
            System.err.println("Number of Created nodes/second: " + lCreatedNodes / seconds);
        }

        if(Sokoban.debugMode)
            System.out.println("Solver line 77: No final sate was found, returned initial state.");

        return Board.getInitialState();
	} // End greedyBFS
	
	/**
	 * Same as greedyBFS but make use of the goal cluster algorithim for postprocessing the 
	 * all childs of each state...
	 * @return
	 */
	public State greedyBFSWithGoalCluster(){

        int lExpandedNodes = 0;
        int lCreatedNodes = 0;
		
        simpleQueue.add(Board.getInitialState());
        visitedStates.add(Board.getInitialState());
        // First state created
        lCreatedNodes++;

        // Start constructorTime iterating through nodes
        if (Sokoban.profilingMode) startTime = System.currentTimeMillis();

        // The current state var.
        State lCurState;
        Vector<State> childrenOfCurState = new Vector<State>();

        // Expand nodes until the queue is empty or until max iterations
        while (lExpandedNodes < 50000 && !simpleQueue.isEmpty()) {
        	
            // Get state first in line
            lCurState = simpleQueue.poll();
            // Add one; new expanded node
            lExpandedNodes++;

            // Visualizer.printStateDelux(lCurState, "--- State explored in iteration: #" + lExpandedNodes + " ---");
            
            // Clear and get new children of current state
            childrenOfCurState.clear();
            lCurState.allSuccessors(childrenOfCurState); //fills with all children
            
            /*
             * THE goalCluster part:
             * The function exchange states so that boxes are moved to the best goals in a cluster.
             */
            postProcessAccordingGoalCluster(childrenOfCurState);
        	if(Sokoban.visualizeMode) Visualizer.printStateDelux(childrenOfCurState.lastElement(), "/Solver: childstate last elemement:");

            /*
            for(State state: childrenOfCurState){
            	Visualizer.printStateDelux(state, "childstates:");
            }
             */
            
            // Add the number of new states
            lCreatedNodes = lCreatedNodes + childrenOfCurState.size();

            // Iterate through the children and add them to the queue and in Closed
            for (State child : childrenOfCurState){
                // If the child is final state, then return it!
                if (child.isFinalState()) {

                    // End constructorTime searching for solution
                    if (Sokoban.profilingMode) {
                        long endTime = System.currentTimeMillis() - startTime;
                        double seconds = (double) endTime / 1000;
                        System.err.println("\n--- Greedy BFS ---");
                        System.err.println("Expanded nodes for: " + endTime + " ms");
                        System.err.println("Number of Expanded nodes/second: " + lExpandedNodes / seconds);
                        System.err.println("Number of Created nodes/second: " + lCreatedNodes / seconds);
                    }

                    if(Sokoban.visualizeMode) Visualizer.printState(child, "/Solver: THE FINAL STATE IS FOUND! See below:");
                    return child;

                // If child is NOT in closed (Visited states), add it!
                } else if(!visitedStates.contains(child)){
                    visitedStates.add(child);
                    simpleQueue.add(child);

                } else {
                    // Add the state to the reusable container
                    State.addReusableState(child);
                }
            }
        }

        // End time
        if (Sokoban.profilingMode) {
            long endTime = System.currentTimeMillis() - startTime;
            double seconds = (double) endTime / 1000;
            System.err.println("\n--- Greedy BFS ---");
            System.err.println("Expanded nodes for: " + endTime + " ms");
            System.err.println("Number of Expanded nodes/second: " + lExpandedNodes / seconds);
            System.err.println("Number of Created nodes/second: " + lCreatedNodes / seconds);
        }

        if(Sokoban.debugMode)
            System.out.println("Solver line 77: No final sate was found, returned initial state.");

        return Board.getInitialState();
	} // End greedyBFSWithGoalCluster
	
	/**
	 * A first try to combine guided and unguided BFS, 
	 * at one point in the search all the 200 highest scoring states 
	 * are simply expanded two levels without sorting in between and then
	 * the heuristic guides search continues. This should be made more sophisticated:
	 * say when every 'I' nodes have been expanded we take the 'N' highest scoring in
	 * the working queue and expand those (without sorting BUT deadlock detection!) 
	 * 'M' times and then but back into queue to be judged by the heuristic along with others.
	 * in this way the horizon is extended a few steps away "one step may lead to low heuristic value"
	 * but taking just one step more might be much better...
	 * 
	 * This method was run by the recursive seacher er below there for it takes a state as parameter 
	 * instead of getting it from Borad.initialstate as the other searchers.
	 * 
	 * @param pStartState
	 * @return
	 */
	public State greedyBFSItDeep(State pStartState){

        int lExpandedNodes = 0;
        int lCreatedNodes = 0;
		
        simpleQueue.add(pStartState);
        visitedStates.add(pStartState);
        // First state created
        lCreatedNodes++;

        // Start constructorTime iterating through nodes
        if (Sokoban.profilingMode) startTime = System.currentTimeMillis();

        // The current state var.
        State lCurState;
        Vector<State> childrenOfCurState = new Vector<State>();

        
        // Expand nodes until the queue is empty or until max iterations
        while (!simpleQueue.isEmpty() && simpleQueue.size() < 5000) {

            // Get state first in line
            lCurState = simpleQueue.poll();
            // Add one; new expanded node
            lExpandedNodes++;

            //if(Sokoban.visualizeMode)  Visualizer.printStateDelux(lCurState, "--- State explored in iteration: #" + lExpandedNodes + " ---");
            childrenOfCurState.clear();
            lCurState.allSuccessors(childrenOfCurState); //fills with all children

            // Add the number of new states
            lCreatedNodes = lCreatedNodes + childrenOfCurState.size();

            // Iterate through the children and add them to the queue and in Closed
            for (State child : childrenOfCurState){
               //if(Sokoban.visualizeMode)  Visualizer.printStateDelux(child, "--- Childs created in iteration: #" + lExpandedNodes + " ---");
                
            	// If the child is final state, then return it!
                if (child.isFinalState()) {

                    // End constructorTime searching for solution
                    if (Sokoban.profilingMode) {
                        long endTime = System.currentTimeMillis() - startTime;
                        double seconds = (double) endTime / 1000;
                        System.err.println("\n--- Greedy BFS ---");
                        System.err.println("Expanded nodes for: " + endTime + " ms");
                        System.err.println("Number of Expanded nodes/second: " + lExpandedNodes / seconds);
                        System.err.println("Number of Created nodes/second: " + lCreatedNodes / seconds);
                    }

                    if(Sokoban.visualizeMode) Visualizer.printState(child, "THE FINAL STATE IS FOUND! See below:");
                    return child;

                // If child is NOT in closed (Visited states), add it!
                } else if(!visitedStates.contains(child)){
                    visitedStates.add(child);
                    simpleQueue.add(child);

                } else {
                    // Add the state to the reusable container
                    State.addReusableState(child);
                }
            }
        }//End while
     
        Queue<State> tempQueue = new LinkedList<State>();

    	if(Sokoban.debugMode) System.out.println("Entered BSF-part1. main queue size: " + simpleQueue.size());

        for (int i = 0; i < 200; i++){
        	lCurState = simpleQueue.poll();
            lExpandedNodes++;
        	childrenOfCurState.clear();
            lCurState.allSuccessors(childrenOfCurState); //fills with all children
            // Add the number of new states
            lCreatedNodes = lCreatedNodes + childrenOfCurState.size();
            for (State child : childrenOfCurState){
                //if(Sokoban.visualizeMode)  Visualizer.printStateDelux(child, "--- Childs created in iteration: #" + lExpandedNodes + " ---");
                 
             	// If the child is final state, then return it!
                 if (child.isFinalState()) {
                     if(Sokoban.visualizeMode) Visualizer.printState(child, "THE FINAL STATE IS FOUND! See below:");
                     return child;

                 // If child is NOT in closed (Visited states), add it!
                 } else if(!visitedStates.contains(child)){
                     visitedStates.add(child);
                     tempQueue.add(child);

                 } else {
                     // Add the state to the reusable container
                     State.addReusableState(child);
                 }
            }
        }
        
    	if(Sokoban.debugMode) System.out.println("Entered BSF-part2: temp queue size: " + tempQueue.size());

        while(tempQueue.size()>0){

        	lCurState = tempQueue.poll();
            lExpandedNodes++;
        	childrenOfCurState.clear();
            lCurState.allSuccessors(childrenOfCurState); //fills with all children
            // Add the number of new states
            lCreatedNodes = lCreatedNodes + childrenOfCurState.size();
            for (State child : childrenOfCurState){
                //if(Sokoban.visualizeMode)  Visualizer.printStateDelux(child, "--- Childs created in iteration: #" + lExpandedNodes + " ---");
                 
             	// If the child is final state, then return it!
                 if (child.isFinalState()) {
                     if(Sokoban.visualizeMode) Visualizer.printState(child, "THE FINAL STATE IS FOUND! See below:");
                     return child;

                 // If child is NOT in closed (Visited states), add it!
                 } else if(!visitedStates.contains(child)){
                     visitedStates.add(child);
                     simpleQueue.add(child);

                 } else {
                     // Add the state to the reusable container
                     State.addReusableState(child);
                 }
            }
        }
         
    	if(Sokoban.debugMode) System.out.println("Entered BestFS-again. main queue size:" + simpleQueue.size() + " Expanded: " + lExpandedNodes);

        // Expand nodes until the queue is empty or until max iterations
        while (!simpleQueue.isEmpty() && lExpandedNodes < 100000) {

            // Get state first in line
            lCurState = simpleQueue.poll();
            // Add one; new expanded node
            lExpandedNodes++;

            //if(Sokoban.visualizeMode)  Visualizer.printStateDelux(lCurState, "--- State explored in iteration: #" + lExpandedNodes + " ---");
            childrenOfCurState.clear();
            lCurState.allSuccessors(childrenOfCurState); //fills with all children

            // Add the number of new states
            lCreatedNodes = lCreatedNodes + childrenOfCurState.size();

            // Iterate through the children and add them to the queue and in Closed
            for (State child : childrenOfCurState){
               //if(Sokoban.visualizeMode)  Visualizer.printStateDelux(child, "--- Childs created in iteration: #" + lExpandedNodes + " ---");
                
            	// If the child is final state, then return it!
                if (child.isFinalState()) {

                    // End constructorTime searching for solution
                    if (Sokoban.profilingMode) {
                        long endTime = System.currentTimeMillis() - startTime;
                        double seconds = (double) endTime / 1000;
                        System.err.println("\n--- Greedy BFS ---");
                        System.err.println("Expanded nodes for: " + endTime + " ms");
                        System.err.println("Number of Expanded nodes/second: " + lExpandedNodes / seconds);
                        System.err.println("Number of Created nodes/second: " + lCreatedNodes / seconds);
                    }

                    if(Sokoban.visualizeMode) Visualizer.printState(child, "THE FINAL STATE IS FOUND! See below:");
                    return child;

                // If child is NOT in closed (Visited states), add it!
                } else if(!visitedStates.contains(child)){
                    visitedStates.add(child);
                    simpleQueue.add(child);

                } else {
                    // Add the state to the reusable container
                    State.addReusableState(child);
                }
            }
        }//End while

        // End time
        if (Sokoban.profilingMode) {
            long endTime = System.currentTimeMillis() - startTime;
            double seconds = (double) endTime / 1000;
            System.err.println("\n--- Greedy BFS ---");
            System.err.println("Expanded nodes for: " + endTime + " ms");
            System.err.println("Number of Expanded nodes/second: " + lExpandedNodes / seconds);
            System.err.println("Number of Created nodes/second: " + lCreatedNodes / seconds);
        }

        if(Sokoban.debugMode)
            System.out.println("Solver line 77: No final sate was found, returned initial state.");

        return Board.getInitialState();
	} // End greedyBFSItDeep
	
		
	/**
	 * Runs the recursive method, where one box is moved at a time.
	 * @return
	 */
	public State recursive(){
		/*	
		Vector<State> states = new Vector<State>();
		states.add(Board.getInitialState());
		for(int i = 0; i < Board.getNbOfBoxes(); i++){
			moveOneBoxToGoal(states, i, 0);
		}

		return greedyBFS(states.lastElement());
		*/
		return greedyBFSItDeep(Board.getInitialState());
	}
	
	/**
	 * Combination of moving all boxes close to goals and then running goal cluster-algorithm
	 * and finally greedyBFS...
	 */
	public State directToGoalsWithGoalGoalCluster(){
		Vector<State> states = new Vector<State>();
		Vector<State> tempStatesMoveToGoals = new Vector<State>();
		Vector<State> tempStateGoalClustAlg = new Vector<State>();

		tempStatesMoveToGoals.add(Board.getInitialState());
		//just in case something goes wrong also add the initial state:
		states.add(Board.getInitialState());
		
		for(int i = 0; i < Board.getNbOfBoxes(); i++){
			//Operates on the state so that last state in vector will have the box on goal.
			moveOneBoxToClosestGoal(tempStatesMoveToGoals, i);
			//Add the element where the box is on goal
			tempStateGoalClustAlg.add(tempStatesMoveToGoals.lastElement());
			
			//Add an intermediate state to the main out vector:
			if(tempStatesMoveToGoals.size() >=3){
				//might be a good idea to save intermediate state two moves before goal.
				states.add(tempStatesMoveToGoals.get(tempStatesMoveToGoals.size() - 3));
			}
			else if(tempStatesMoveToGoals.size() >2){
				//might be a good idea to save intermediate state two 1 move before goal.
				states.add(tempStatesMoveToGoals.get(tempStatesMoveToGoals.size() - 2)); 
			}
			
			//Move box to unblocking goal:
			//postProcessAccordingGoalCluster(tempStateGoalClustAlg);
			//Add as intermediate state to main out vector
			states.add(tempStateGoalClustAlg.lastElement());
			//Clear an add last state:
			tempStatesMoveToGoals.clear();
			tempStatesMoveToGoals.add(tempStateGoalClustAlg.lastElement());
			//clear for next iteration;
			tempStateGoalClustAlg.clear();
		}
		for(State state : states){
			Visualizer.printStateDelux(state, "State sent to greedyBFS");
		}
		
		return(Board.getInitialState());
		//return greedyBFS(states);
	}
	
	
	/**
	 * Most efficient way to (try) to move one box to A SPECIFIC goal. If it succeeds the
	 * inOutStatesVector will only contain the state where the box is on goal. 
	 * Otherwise 1-3 states where it is on the lowest gradDecentValue.
	 * 
	 * returns true it the box reached the goal.
	 * 
	 * @param inOutStatesVector
	 * @param pBoxIndex
	 * @param pGoalIndex
	 * @return
	 */
	public boolean moveOneBoxToGoal(Vector<State> inOutStatesVector, int pBoxIndex, int pGoalIndex){
		
		//Operates on the vector with the simple grad-decent pushing.
		boolean reachedGoalWithGradOnly = moveOneBoxToGoalGradDecentOnly(inOutStatesVector, pBoxIndex, pGoalIndex);
		
		//If it did not reach goal it uses a limited search.
		if(!reachedGoalWithGradOnly){
			return moveOneBoxToGoalGreedyBFS(inOutStatesVector, pBoxIndex, pGoalIndex);
		}
		else{
			return reachedGoalWithGradOnly;
		}
	}
	
	/**
	 * Most efficient way to (try) to move one box to CLOSEST goal. If it succeeds the
	 * inOutStatesVector will only contain the state where the box is on goal. 
	 * Otherwise 1-3 states where it is on the lowest gradDecentValue.
	 * 
	 * returns true it the box reached the goal.
	 * 
	 * @param inOutStatesVector
	 * @param pBoxIndex
	 * @param pGoalIndex
	 * @return
	 */
	public boolean moveOneBoxToClosestGoal(Vector<State> inOutStatesVector, int pBoxIndex){
		
		//Operates on the vector with the simple grad-decent pushing.
		boolean reachedGoalWithMergedGradOnly = moveOneBoxToClosestGoalGradDecentOnly(inOutStatesVector, pBoxIndex, 0);
		
		//If it did not reach goal it uses a limited search.
		if(!reachedGoalWithMergedGradOnly){
			return moveOneBoxToClosestGoalGreedyBFS(inOutStatesVector, pBoxIndex);
		}
		else{
			return reachedGoalWithMergedGradOnly;
		}
	}
	
	/**
	 * This function moves one box "down" the gradient using the GoalGrad for the
	 * specified goal (not merged!) to move one box to this goal. 
	 * 
	 * If possible remove the state (should be ONLY ONE) sent in in inOutStatesVector and adds the new state
	 * with the box moved to the goal and RETURN TRUE.
	 * 
	 * If not possible (no solution found) it keeps the initial state in the vector and adds 
	 * the three states where the box is on lowest grad-value, and RETURN FALSE.
	 * 
	 * this function should move one box to the goal specified by the index.
	 * 
	 * @param pTempStorage
	 * @param pBoxIndex
	 * @param pGoalIndex
	 */
	public boolean moveOneBoxToGoalGradDecentOnly(Vector<State> inOutStatesVector, int pBoxIndex, int pGoalIndex){
		
		Vector<State> tempStorage = new Vector<State>();
		tempStorage.add(inOutStatesVector.firstElement());
		int prevGradValueUnderBox = Integer.MAX_VALUE;
		int curGradValueUnderBox = Board.getGoalGrad(pGoalIndex,
				tempStorage.lastElement().getBox(pBoxIndex).getRow(),
				tempStorage.lastElement().getBox(pBoxIndex).getCol());
		
		while(curGradValueUnderBox < prevGradValueUnderBox && curGradValueUnderBox > 0){
			prevGradValueUnderBox = curGradValueUnderBox; //update last value
			//adds the new state after move and sets cur grad value to the pos it moved to.
			curGradValueUnderBox = 
					tempStorage.lastElement().gradientDecentSuccessor(tempStorage, pBoxIndex, pGoalIndex);
			
			if(Sokoban.visualizeMode) Visualizer.printState(tempStorage.lastElement(), "/Solver/MoveOneBoxToGoal: " + pBoxIndex);
		}
		if(curGradValueUnderBox == 0){
			inOutStatesVector.add(tempStorage.lastElement());
			return true;
		}
		else{
			if(tempStorage.size() >=3){
				//might be a good idea to save intermediate state two moves before goal.
				inOutStatesVector.add(tempStorage.get(tempStorage.size() - 3));
				inOutStatesVector.add(tempStorage.get(tempStorage.size() - 2));
				inOutStatesVector.add(tempStorage.get(tempStorage.size() - 1));
			}
			else if(tempStorage.size() >2){
				//might be a good idea to save intermediate state two 1 move before goal.
				inOutStatesVector.add(tempStorage.get(tempStorage.size() - 2)); 
				inOutStatesVector.add(tempStorage.get(tempStorage.size() - 1)); 
			}
			else{
				inOutStatesVector.add(tempStorage.get(tempStorage.size() - 1)); 
			}
			return false;
		}
		
	}
	
	
/**
 * The vector should only contain one state from which we want to move one box...
 * recurNb should be set to zero initially.
 * 
 * It fills the Vector with all intermediate states.
 *
 * @param pState
 * @param pBoxIndex
 * @param recurNb
 * @return
 */
public boolean moveOneBoxToClosestGoalGradDecentOnly(Vector<State> inOutStatesVector, int pBoxIndex, int recurNb){
	
	Vector<State> tempStorage = new Vector<State>();
	tempStorage.add(inOutStatesVector.firstElement());
	int prevGradValueUnderBox = Integer.MAX_VALUE;
	int curGradValueUnderBox = inOutStatesVector.firstElement().getGoalGradMerged( 
			inOutStatesVector.firstElement().getBox(pBoxIndex).getRow(),
			inOutStatesVector.firstElement().getBox(pBoxIndex).getCol());
	
	while(curGradValueUnderBox < prevGradValueUnderBox && curGradValueUnderBox > 0){
		prevGradValueUnderBox = curGradValueUnderBox; //update last value
		//adds the new state after move and sets cur grad value to the pos it moved to.
		curGradValueUnderBox = 
				inOutStatesVector.lastElement().gradientDecentMergedSuccessor(tempStorage, pBoxIndex);	
		if(Sokoban.visualizeMode) Visualizer.printState(inOutStatesVector.lastElement(), "/Solver/moveOne...ClosestGoalGradDecentOnly: for box: " + pBoxIndex);
	}
	if(curGradValueUnderBox == 0){
		inOutStatesVector.add(tempStorage.lastElement());
		return true;
	}
	else{
		if(tempStorage.size() >=3){
			//might be a good idea to save intermediate state two moves before goal.
			inOutStatesVector.add(tempStorage.get(tempStorage.size() - 3));
			inOutStatesVector.add(tempStorage.get(tempStorage.size() - 2));
			inOutStatesVector.add(tempStorage.get(tempStorage.size() - 1));
		}
		else if(tempStorage.size() >2){
			//might be a good idea to save intermediate state two 1 move before goal.
			inOutStatesVector.add(tempStorage.get(tempStorage.size() - 2)); 
			inOutStatesVector.add(tempStorage.get(tempStorage.size() - 1)); 
		}
		else{
			inOutStatesVector.add(tempStorage.get(tempStorage.size() - 1)); 
		}
		return false;
	}
	
	
	/*
	if(curGradValueUnderBox > 0 && recurNb < Board.getNbOfBoxes()){
		if(Sokoban.debugMode) System.out.println("Entered recursion: "+recurNb);
		int blockBoxIndex = pTempStorage.lastElement().getBlockingBoxIndex(pBoxIndex);
		if (blockBoxIndex != -1){ // -1 means Could not find blocking box...
			moveOneBoxToClosestGoal(pTempStorage, 
				pTempStorage.lastElement().getBlockingBoxIndex(pBoxIndex),
				recurNb);
		}
	}	
	*/	
}
	
	/**
	 * Used greedyBFS with goalGrad as heuristic to move one specified box
	 * to a specified goal.
	 * can take several "start options" (states) in the input vector 
	 * but only returns one state if he box was moved to goal otherwise:
	 * TODO add some extra intermediate steps?
	 * 
	 * @param inputvector
	 * @param pBoxIndex
	 * @param pGoalIndex
	 */
	public boolean moveOneBoxToGoalGreedyBFS(Vector<State> inOutStatesVector, int pBoxIndex, int pGoalIndex){
		
		//Set the comparator for this serach:
		oneBoxMoveComp.setTargetGoalAndBox(pBoxIndex, pGoalIndex);
		
		//Create a search queue;
		PriorityQueue<State> open = new PriorityQueue<State>(100, oneBoxMoveComp);
		//"Local" HashSet: Only the one box's position is relevant!
		HashSet<State> visitedPositions = new HashSet<State>(); //MUST BE CHANGED TO FASTER ALGORITHM
		//Add inputStates to search Queue:
		
		for(State state : inOutStatesVector){
			open.add(state);
			//check that none of the initial state
			if( 0 == Board.getGoalGrad(pGoalIndex, 
					state.getBox(pBoxIndex).getRow(),
					state.getBox(pBoxIndex).getCol()) ){
				//return only the correct state.
				inOutStatesVector.clear();
				inOutStatesVector.add(state);
				return true;
			}
		}

		//Start search:
		Vector<State> tempChildStates = new Vector<State>();
		int iterations = 0;
		while(open.size() > 0 && iterations < 100){
	    	State curState = open.poll();
	    	tempChildStates.clear();
	    	curState.selectiveSuccessors(tempChildStates, pBoxIndex);
	    	for (State child : tempChildStates){
	    		if(0 == Board.getGoalGrad(pGoalIndex, 
						child.getBox(pBoxIndex).getRow(),
						child.getBox(pBoxIndex).getCol()) ){
	    			//On TargetGoal!
	    			inOutStatesVector.clear();
					inOutStatesVector.add(child);
					return true;
	    		}
	    		else{
	    			open.add(child);
	    			visitedPositions.add(child);
	    		}
	    	}
	    	iterations++;
	    }
		if(Sokoban.debugMode){
			System.out.println("/Solver/MoveOne...GreedyBFS: NO path was find for moving box: "
									+ pBoxIndex + " to goal: " + pGoalIndex);
			System.out.println("...queue size: " + open.size() + ", iterations: " + iterations);
		}
		//Same States are left in inOutVector... could also add intermediate states from the search!
		return false;
	}
	
	/**
	 * Used greedyBFS with goalGradMerged as heuristic to move one specified box.
	 * can take several "start options" (states) in the input vector 
	 * but only returns one state if he box was moved to goal otherwise:
	 * TODO add some extra intermediate steps?
	 * 
	 * @param inputvector
	 * @param pBoxIndex
	 * @param pGoalIndex
	 */
	public boolean moveOneBoxToClosestGoalGreedyBFS(Vector<State> inOutStatesVector, int pBoxIndex){
		
		//Set the comparator for this serach:
		oneBoxMoveComp.setBoxToClosestGoal(pBoxIndex);
		
		//Create a search queue;
		PriorityQueue<State> open = new PriorityQueue<State>(100, oneBoxMoveComp);
		//"Local" HashSet: Only the one box's position is relevant!
		HashSet<State> visitedPositions = new HashSet<State>(); //MUST BE CHANGED TO FASTER ALGORITHM
		//Add inputStates to search Queue:
		
		for(State state : inOutStatesVector){
			open.add(state);
			//check that none of the initial state
			if( 0 == Board.getGoalGradMerged( state.getBox(pBoxIndex).getRow(),
					state.getBox(pBoxIndex).getCol()) ){
				//return only the correct state.
				inOutStatesVector.clear();
				inOutStatesVector.add(state);
				return true;
			}
		}

		//Start search:
		Vector<State> tempChildStates = new Vector<State>();
		int iterations = 0;
		while(open.size() > 0 && iterations < 100){
	    	State curState = open.poll();
	    	tempChildStates.clear();
	    	curState.selectiveSuccessors(tempChildStates, pBoxIndex);
	    	for (State child : tempChildStates){
	    		if(0 == Board.getGoalGradMerged( child.getBox(pBoxIndex).getRow(),
						child.getBox(pBoxIndex).getCol()) ){
	    			//On TargetGoal!
	    			inOutStatesVector.clear();
					inOutStatesVector.add(child);
					return true;
	    		}
	    		else{
	    			open.add(child);
	    			visitedPositions.add(child);
	    		}
	    	}
	    	iterations++;
	    }
		if(Sokoban.debugMode){
			System.out.println("/Solver/MoveOne...ClosestGreedyBFS: NO path was find for moving box: "
									+ pBoxIndex + "to closest goal");
			System.out.println("...queue size: " + open.size() + ", iterations: " + iterations);
		}
		//Same States are left in inOutVector... could also add intermediate states from the search!
		return false;
	}
	
	
	
	/**
	 * TODO
	 * This function should implement a greedyBFS (Manhattan distance) to move one 
	 * box to the required position. 
	 * -If possible remove the state sent in in pTempStorage and add the new state
	 * with the box moved and RETURN TRUE.
	 * -If not possible (no solution found) keep the state in the vector and RETURN FALSE.
	 * 
	 * this function should move one box to the goal specified by the index.
	 * 
	 * @param pTempStorage
	 * @param pBoxIndex
	 * @param goalIndex
	 */
	public boolean moveOneBoxToPosition(Vector<State> pTempStorage, int pBoxIndex, int goalIndex){
	
		return false;
	}
	
	
	/**
	 * checks the last move made in each state and if that move was to a goal it checks
	 * if it should be moved further onto another goal according to the goal clusterMatrix.
	 * 
	 * Does NOT FILL WITH INTERMEDIATE STEPS
	 * 
	 * @param pStates
	 */
	public void postProcessAccordingGoalCluster(Vector<State> pStates){

		//Queue with states that still need processing
		Queue<State> processingQueue = new LinkedList<State>();
		//moves all states where the last move was to goal to the Queue: (filter out these, the others are left in vector)
		int i = 0;
		while(i<pStates.size()){
			if(pStates.get(i).getLastMovedBox().isOnGoal()){
				if(Sokoban.visualizeMode) Visualizer.printStateDelux(pStates.get(i), "state in PostProcess kept...");
				processingQueue.add(pStates.remove(i)); //move over to queue for processing.
			}
			else{
				i++; //Go t next object.
			}
		}

		//if the states where the last moved box is on goal we need to check clusterMatrix!
		while(processingQueue.size() > 0){
			//look at first state:
			State state = processingQueue.poll();
			//temporary vector for sending state to move methods.
			Vector<State> tempVect = new Vector<State>();

			int goalIndex = Board.getGoalIndexAt(
					state.getLastMovedBox().getRow(), state.getLastMovedBox().getCol());

			if(Sokoban.debugMode) System.out.println("/Solver: goal index " + goalIndex);
			//check in clusterMatrix if is block to any other goal:
			int lowestValueBlockType = 5; //highest block value is 3;
			int lowestValueRowIndex = -1;		
			for(int matrixRow = 0; matrixRow<Board.getNbOfGoals(); matrixRow++){
				if(GoalCluster.getClusterMatrixValue(matrixRow, goalIndex) != 0
						&& GoalCluster.getClusterMatrixValue(matrixRow, goalIndex) < lowestValueBlockType
						&& !state.isGoalOccupied(matrixRow)){ //If this goal that it is blocking is allready occ. we dont worry...
					lowestValueBlockType = GoalCluster.getClusterMatrixValue(matrixRow, goalIndex);
					lowestValueRowIndex = matrixRow;
				}
			}
			if(Sokoban.debugMode) System.out.println("/Solver: blockType: " + lowestValueBlockType
					+ ", for other goal: " + lowestValueRowIndex);
			if(lowestValueBlockType == 5){
				//Not blocking any box put back to original vector!
				pStates.add(state); //(already polled from queue)
			}
			/*
			 * if the value == 1,2,3 the box should be moved further on
			 * to the goal with index corresponding to matrix row.
			 */
			else if(lowestValueBlockType == 1){
				tempVect.add(state);
				boolean moveSuccesful = moveOneBoxToGoalGradDecentOnly(tempVect, state.getLastBoxMovedIndex(), lowestValueRowIndex);
				//if succesful put back in Queue - maybe it should be moved further on!
				if(Sokoban.debugMode) System.out.println("/Solver: moveSuccessfull: "+moveSuccesful);

				if(moveSuccesful){
					processingQueue.add(tempVect.remove(0));
				}
				else{
					/*
					 * TODO
					 * This should not happen to often if everything works but must be take care of!
					 * DEAD LOCK? FOR NOW - JUST SEND THE STATE BACK...
					 */
					pStates.add(state); //(already polled from queue)
				}
			}
			else{ //looping just two times for block type 2 and 3:
				for(int blockType = 2; blockType <=3 ; blockType++){
					if(lowestValueBlockType == blockType){
						/*
						 * check for mutual pointing if the other goal points back to this goal
						 * with same or lower block type: do not move.
						 */
						if(GoalCluster.getClusterMatrixValue(goalIndex, lowestValueRowIndex) <= blockType
								&& GoalCluster.getClusterMatrixValue(goalIndex, lowestValueRowIndex) != 0){
							//No more moving put back in original state vector:
							pStates.add(state); //(already polled from queue)
						}
						else{
							tempVect.add(state);
							boolean moveSuccesful = moveOneBoxToGoalGradDecentOnly(tempVect, state.getLastBoxMovedIndex(), lowestValueRowIndex);
							//if successful put back in Queue - maybe it should be moved further on!
							if(moveSuccesful){
								processingQueue.add(tempVect.remove(0));
							}
							else{
								/*
								 * TODO
								 * This should not happen to often if everything works but must be take care of!
								 * DEAD LOCK? FOR NOW - JUST SEND THE STATE BACK...
								 */
								pStates.add(state); //(already polled from queue)
							}
						}
						break; //if lowestValueBlockType == 2 no need to check for 3.
					}//End if == blockType
				}
			}
		}
	}
	
	
	/**
	 * Returns move necessary from cell to first parent cell.
	 * @param pCell
	 * @return
	 */
	public static String strChecker(Cell pCell){
	
		if(pCell.getRow() == pCell.getParent().getRow()){
			// If rows are equal we are moving left or right, since we are going backwards,
			// the condition are reversed.
			if(pCell.getCol() > pCell.getParent().getCol()){
				return "l";
			}
			else{
				return "r";
			}
		}
		else{
			if(pCell.getRow() > pCell.getParent().getRow()){
				return "u";
			}
			else{
				return "d";
			}
		}
	}

	/**
	 * Returns moves necessary to this cell from all parent cells.
	 * @param pCell
	 * @return
	 */
	public static String strPath(Cell pCell){
		//Print out all necessary moves from linked cells.
		String strMoves = "";
		while(pCell.getParent() != null){
			strMoves += strChecker(pCell);
			pCell = pCell.getParent();
		}
		return strMoves;
	}

		
	/**
	 * Returns linked cell from some position to another position (only used when printing
	 * solution path).
	 * @param pState
	 * @param pRow
	 * @param pCol
	 * @param pRowPath
	 * @param pColPath
	 * @return
	 */
	public static Cell cellLinkedToPath(State pState,
                                        int pRow, int pCol, int pRowPath, int pColPath) {

		//See documentation for method below cellToPath, this is method is only used for 
		//printing out solution path.
		Cell pStartCell = new Cell(pRow,pCol);
		Cell pEndCell = new Cell(pRowPath,pColPath);
		boolean bolFinished = false;
		Cell lCellNeighbor = null;
		
        Comparator<Cell> comparator = new Cell.NormComparator(
        		pEndCell.getRow(),
        		pEndCell.getCol());
		HashSet<String> lSetPrPaths = new HashSet<String>();
        PriorityQueue<Cell> lQueChildren = new PriorityQueue<Cell>(10,comparator);
        
        lQueChildren.add(pStartCell);
		lSetPrPaths.add(getHashString(pStartCell.getRow(),pStartCell.getCol()));

		
		while(!lQueChildren.isEmpty() &! bolFinished){
			Cell lCellChild = lQueChildren.remove();
			int lRow = lCellChild.getRow();
			int lCol = lCellChild.getCol();
			int incInt = -1;

			while(incInt <= 1){
				int lRowChild = lRow + incInt;
				String hashIndex1 = getHashString(lRowChild,lCol);
				
				if(!lSetPrPaths.contains(hashIndex1)){
					lSetPrPaths.add(hashIndex1);
			
					if(Board.isFree(pState,lRowChild, lCol)){
						
						if(lRowChild == pEndCell.getRow() &&
								lCol == pEndCell.getCol()){
							lCellNeighbor = new Cell(lCellChild,lRowChild,lCol);
							bolFinished = true;
						}
						else{
							lQueChildren.add(new Cell(lCellChild,lRowChild,lCol));
						}	
					}
				}
				int lColChild = lCol + incInt;
				String hashIndex2 = getHashString(lRow,lColChild);
				
				if(!lSetPrPaths.contains(hashIndex2)){
					lSetPrPaths.add(hashIndex2);
			
					if(Board.isFree(pState,lRow, lColChild)){
						
						if(lRow == pEndCell.getRow() &&
								lColChild == pEndCell.getCol()){
							lCellNeighbor = new Cell(lCellChild,lRow,lColChild);
							bolFinished = true;
						}
						else{
							lQueChildren.add(new Cell(lCellChild,lRow,lColChild));
						}	
					}
				}
			incInt = incInt + 2;	
			}
		}
		return lCellNeighbor;
	}
	
	
	/**
	 * Returns cell next to some position (the position we are searching to can not
	 * be a box).
	 * @param pState
	 * @param pRow
	 * @param pCol
	 * @param pRowPath
	 * @param pColPath
	 * @return
	 */
	public static Cell cellNeighborToPath(State pState,
                                          int pRow, int pCol, int pRowPath, int pColPath) {

		// If searching to wall.
		if(Board.isWall(pRowPath, pColPath)){
			return null;
		}
		
		// If we are at searched position.
		if(pRow == pRowPath && pCol == pColPath){
			return Board.matrixCells[pRow][pCol];
		}
		
		//Get start and end positions cell position from matrix.		
		Cell pStartCell = Board.matrixCells[pRow][pCol];
		Cell pEndCell = Board.matrixCells[pRowPath][pColPath];
		
		//Set comparator to look for path we are searching for.
		comparatorCell.setGoal(pRowPath, pColPath);
		//Child queue for search
		//First child is starting node.
        queueCellChildren.add(pStartCell);
        //Map for visited positions.
        mapPrPathsCell.add(getHashString(pStartCell.getRow(),pStartCell.getCol()));

        while(!queueCellChildren.isEmpty()){
        	Cell lCellChild = queueCellChildren.remove();
        	//increment for making children (L,R,U and D)
			int incInt = -1;

			while(incInt <= 1){
				String hashIndex1 = getHashString(lCellChild.getRow() + incInt,lCellChild.getCol());
				
				//If not position visited before.
				if(!mapPrPathsCell.contains(hashIndex1)){
					mapPrPathsCell.add(hashIndex1);
					//Make row child first. (lCellChild.getRow() + incInt)
					//No walls
					if(Board.isFree(pState,lCellChild.getRow() + incInt, lCellChild.getCol())){

						if(lCellChild.getRow() + incInt == pEndCell.getRow() &&
								lCellChild.getCol() == pEndCell.getCol()){
							//We have found path.
							STATIC_CELL.setRow(pEndCell.getRow());
							STATIC_CELL.setCol(pEndCell.getCol());
							mapPrPathsCell.clear();
					       	queueCellChildren.clear();
							return STATIC_CELL;
						}
						else{
							//If we have not found path we add children to queue and continue
							queueCellChildren.add(Board.matrixCells[lCellChild.getRow()+incInt][lCellChild.getCol()]);
						}	
					}
				}
				// Repeat for column children.
				String hashIndex2 = getHashString(lCellChild.getRow(),lCellChild.getCol() + incInt);
				
				if(!mapPrPathsCell.contains(hashIndex2)){
					mapPrPathsCell.add(hashIndex2);
			
					if(Board.isFree(pState,lCellChild.getRow(), lCellChild.getCol() + incInt)){
						
						//If our position child is searched position we are done.
						if(lCellChild.getRow() == pEndCell.getRow() &&
								lCellChild.getCol() + incInt == pEndCell.getCol()){
							//Clear map,and queue return cell for searched position.
							STATIC_CELL.setRow(pEndCell.getRow());
							STATIC_CELL.setCol(pEndCell.getCol());
							mapPrPathsCell.clear();
					       	queueCellChildren.clear();
							return STATIC_CELL;
						}
						else{
							queueCellChildren.add(Board.matrixCells[lCellChild.getRow()][lCellChild.getCol()+incInt]);
						}	
					}
				}
			//Set increment for children to 1.
			incInt = incInt + 2;	
			}
		}
        //No path clear map,queue and return null for no path.
        mapPrPathsCell.clear();
       	queueCellChildren.clear();
        return null;
        
	}

	/**
	 * Returns cell next to some position (the position we are searching to can not
	 * be a box).
	 * SEARCH WITHOUT REGARDNIG BOXES.
	 * Same method as cellNeighborToPath but searches an empty board. (no boxes)
	 * @param pState
	 * @param pRow
	 * @param pCol
	 * @param pRowPath
	 * @param pColPath
	 * @return
	 */
	public static Cell cellNeighborToPathEmpty(int pRow, int pCol, int pRowPath, int pColPath) {

		// If searching to wall.
		if(Board.isWall(pRowPath, pColPath)){
			return null;
		}
		
		// If we are at searched position.
		if(pRow == pRowPath && pCol == pColPath){
			return Board.matrixCells[pRow][pCol];
		}
		
		//Get start and end positions cell position from matrix.		
		Cell pStartCell = Board.matrixCells[pRow][pCol];
		Cell pEndCell = Board.matrixCells[pRowPath][pColPath];
		
		//Set comparator to look for path we are searching for.
		comparatorCell.setGoal(pRowPath, pColPath);
		//Child queue for search
		//First child is starting node.
        queueCellChildren.add(pStartCell);
        //Map for visited positions.
        mapPrPathsCell.add(getHashString(pStartCell.getRow(),pStartCell.getCol()));

        while(!queueCellChildren.isEmpty()){
        	Cell lCellChild = queueCellChildren.remove();
        	//increment for making children (L,R,U and D)
			int incInt = -1;

			while(incInt <= 1){
				String hashIndex1 = getHashString(lCellChild.getRow() + incInt,lCellChild.getCol());
				
				//If not position visited before.
				if(!mapPrPathsCell.contains(hashIndex1)){
					mapPrPathsCell.add(hashIndex1);
					//Make row child first. (lCellChild.getRow() + incInt)
					//No walls
					if(Board.isWall(lCellChild.getRow() + incInt, lCellChild.getCol())){

						if(lCellChild.getRow() + incInt == pEndCell.getRow() &&
								lCellChild.getCol() == pEndCell.getCol()){
							//We have found path.
							STATIC_CELL.setRow(pEndCell.getRow());
							STATIC_CELL.setCol(pEndCell.getCol());
							mapPrPathsCell.clear();
					       	queueCellChildren.clear();
							return STATIC_CELL;
						}
						else{
							//If we have not found path we add children to queue and continue
							queueCellChildren.add(Board.matrixCells[lCellChild.getRow()+incInt][lCellChild.getCol()]);
						}	
					}
				}
				// Repeat for column children.
				String hashIndex2 = getHashString(lCellChild.getRow(),lCellChild.getCol() + incInt);
				
				if(!mapPrPathsCell.contains(hashIndex2)){
					mapPrPathsCell.add(hashIndex2);
			
					if(Board.isWall(lCellChild.getRow(), lCellChild.getCol() + incInt)){
						
						//If our position child is searched position we are done.
						if(lCellChild.getRow() == pEndCell.getRow() &&
								lCellChild.getCol() + incInt == pEndCell.getCol()){
							//Clear map,and queue return cell for searched position.
							STATIC_CELL.setRow(pEndCell.getRow());
							STATIC_CELL.setCol(pEndCell.getCol());
							mapPrPathsCell.clear();
					       	queueCellChildren.clear();
							return STATIC_CELL;
						}
						else{
							queueCellChildren.add(Board.matrixCells[lCellChild.getRow()][lCellChild.getCol()+incInt]);
						}	
					}
				}
			//Set increment for children to 1.
			incInt = incInt + 2;	
			}
		}
        //No path clear map,queue and return null for no path.
        mapPrPathsCell.clear();
       	queueCellChildren.clear();
        return null;
        
	}
	
	
	/**
	 * Returns true if there is path to some position.
	 * @param pState
	 * @param pRow
	 * @param pCol
	 * @param pRowPath
	 * @param pColPath
	 * @return
	 */
	public static boolean isPathToPath(State pState,int pRow,int pCol,int pRowPath,
			int pColPath){
		
		// if lCell is null we there is no path.
		Cell lCell = cellNeighborToPath(pState, pRow, pCol, pRowPath, pColPath);
		return (lCell != null);
	}
	
	/**
	 * Returns true if there is path to some position.
	 * Same method as isPathToPath but searches an empty board. (no boxes)
	 * @param pState
	 * @param pRow
	 * @param pCol
	 * @param pRowPath
	 * @param pColPath
	 * @return
	 */
	public static boolean isPathToPathEmpty(int pRow,int pCol,int pRowPath, int pColPath){
		
		// if lCell is null we there is no path.
		Cell lCell = cellNeighborToPathEmpty(pRow, pCol, pRowPath, pColPath);
		return (lCell != null);
	}
	
	/**
	 * Returns cell with needed player position to perform this state.
	 * @param pState
	 * @return
	 */
	public static Cell cellLinkedToState(State pState){
		char lMove = pState.getCharLastMove();
		
		Cell lCellNeededPlayerPos = null;
		
		if(lMove == 'U'){
			//if last move was U we need cell with position playerRow + 1 since 
			//row 0, col 0 is at upper left corner.
			lCellNeededPlayerPos = new Cell(pState.getPlayerRow()+1,pState.getPlayerCol());
		}
		else if(lMove == 'D'){
			lCellNeededPlayerPos = new Cell(pState.getPlayerRow()-1,pState.getPlayerCol());
		}
		else if(lMove == 'L'){
			lCellNeededPlayerPos = new Cell(pState.getPlayerRow(),pState.getPlayerCol()+1);
		}
		else if(lMove == 'R'){
			lCellNeededPlayerPos = new Cell(pState.getPlayerRow(),pState.getPlayerCol()-1);
		}
		return lCellNeededPlayerPos;
	}
	
	/**
	 * Returns solution string from goal state endState.
	 * @param pEndState
	 * @return
	 */
	public static String getStrToGoal(State pEndState){
		
		String goalString = "";
		
		//Two steps from initial-state we can always add getCharsLastMove and
		//use while (when we are one step from initial state we have to to different)
		while(pEndState.getParent().getParent()!=null){
			//Add charLastMove (L,R,U or D)
			goalString = pEndState.getCharLastMove() + goalString;

			//If we for example, have added string U. We have player position-row is U-1			
			Cell currentPos = cellLinkedToState(pEndState);
			//Next position is at parent-state player position.
			//We link these positions with cells.
			Cell nextPos = cellLinkedToPath(pEndState.getParent(), currentPos.getRow(),
                    currentPos.getCol(), pEndState.getParent().getPlayerRow(),
                    pEndState.getParent().getPlayerCol());
			
			//if next position is null we have that parent state player position is also
			// U-1 (easier to see with figure).
            if (nextPos != null) {
            	//Else print path between the player positions.
				goalString = strPath(nextPos) + goalString;
			}

			pEndState = pEndState.getParent();
			}
		

		//If we are two steps from parent == null, we have that state.parent is 
		//initial state where player position is equal to start position.
		goalString = pEndState.getCharLastMove() + goalString;
		//CurrentPos is player position necessary to perform this state.
		Cell currentPos = cellLinkedToState(pEndState);
		if(currentPos.getRow()==pEndState.getParent().getPlayerRow() &&
				currentPos.getCol()==pEndState.getParent().getPlayerCol()){
			//If currentPos is equal to start position we are done.
			return goalString;
		}
		else{
			//Else we have to link and add string between the two different player positions.
			Cell nextPos = cellLinkedToPath(pEndState.getParent(),currentPos.getRow(),currentPos.getCol(),
					pEndState.getParent().getPlayerRow(),
					pEndState.getParent().getPlayerCol());
			goalString = strPath(nextPos) + goalString;
		}
		return goalString;
	}
}

