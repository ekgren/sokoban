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
	Comparator comparator = new StatePriorityComparator();
	PriorityQueue<State> simpleQueue = new PriorityQueue<State>(10000, comparator);
    PriorityQueue<State> newQueue = new PriorityQueue<State>(10000, comparator);

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

		//While workning on goalClustering...
		if(Sokoban.debugMode) Visualizer.printGoalGrad(0);

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
        while (lExpandedNodes < 10000 && !simpleQueue.isEmpty()) {
        	
            // Get state first in line
            lCurState = simpleQueue.poll();
            // Add one; new expanded node
            lExpandedNodes++;

            // Visualizer.printStateDelux(lCurState, "--- State explored in iteration: #" + lExpandedNodes + " ---");

            if (lCurState.nbOfBoxesOnGoal / Board.getNbOfGoals() < 1) {
                // Clear and get new children of current state
                childrenOfCurState.clear();
                lCurState.allSuccessors(childrenOfCurState); //fills with all children
            } else {
                childrenOfCurState.clear();
                lCurState.gradientDecentSuccessor(childrenOfCurState, lCurState.getLastBoxMovedIndex());
                //lCurState.selectiveSuccessors(childrenOfCurState, lCurState.getLastBoxMovedIndex());
                simpleQueue.add(lCurState);
            }

            
            
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
	 * The vector should only contain one state from which we want to move one box...
	 * recurNb should be set to zero initially.
	 *
	 * @param pState
	 * @param pBoxIndex
	 * @param recurNb
	 * @return
	 */
	public void moveOneBoxToClosestGoal(Vector<State> pTempStorage, int pBoxIndex, int recurNb){
		recurNb++;
		Board.setGoalGradMerged(pTempStorage.lastElement());
		if(Sokoban.visualizeMode) Visualizer.printGoalGradMerged();
		if(Sokoban.visualizeMode) Visualizer.printVector(pTempStorage.lastElement().goalsOccupied, "OccupiedGoals:");
		int prevGradValueUnderBox = Integer.MAX_VALUE;
		int curGradValueUnderBox = Board.getGoalGradMerged(
				pTempStorage.lastElement().getBox(pBoxIndex).getRow(),
				pTempStorage.lastElement().getBox(pBoxIndex).getCol() );
		
		while(curGradValueUnderBox < prevGradValueUnderBox && curGradValueUnderBox > 0){
			prevGradValueUnderBox = curGradValueUnderBox; //update last value
			//adds the new state after move and sets cur grad value to the pos it moved to.
			curGradValueUnderBox = 
					pTempStorage.lastElement().gradientDecentSuccessor(pTempStorage, pBoxIndex);
			
			if(Sokoban.visualizeMode) Visualizer.printState(pTempStorage.lastElement(), "Recurs. for box: " + pBoxIndex);
		}
		
		if(curGradValueUnderBox > 0 && recurNb < Board.getNbOfBoxes()){
			if(Sokoban.debugMode) System.out.println("Entered recursion: "+recurNb);
			int blockBoxIndex = pTempStorage.lastElement().getBlockingBoxIndex(pBoxIndex);
			if (blockBoxIndex!=-1){ // -1 means Could not find bloxking box...
				moveOneBoxToClosestGoal(pTempStorage, 
					pTempStorage.lastElement().getBlockingBoxIndex(pBoxIndex),
					recurNb);
			}
		}		
	}
	
	
	/**
	 * TODO
	 * This function should implement a greedyBFS using the GoalGrad for the
	 * specidifed goal (not merged!) to move one box to this goal. 
	 * If possible remove the state sent in in pTempStorage and add the new state
	 * with the box moved and RETURN TRUE.
	 * If not possible (no solution found) keep the state in the vector and RETURN FALSE.
	 * 
	 * this function should move one box to the goal specified by the index.
	 * 
	 * @param pTempStorage
	 * @param pBoxIndex
	 * @param goalIndex
	 */
	public boolean moveOneBoxToGoal(Vector<State> pTempStorage, int pBoxIndex, int goalIndex){
	
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
	 * @param pStates
	 */
	public void postProcessAccordngGoalCluster(Vector<State> pStates){

		//Queue with states that still need processing
		Queue<State> processingQueue = new LinkedList<State>();
		//moves all states where the last move was to goal to the Queue: (filter out these, the others are left in vector)
		int i = 0;
		while(i<pStates.size()){
			if(pStates.get(i).getLastMovedBox().isOnGoal()){
				processingQueue.add(pStates.remove(0)); //move over to queue for processing.
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

			//check in clusterMatrix if is block to any other goal:
			int lowestValueBlockType = 5; //highest block value is 3;
			int lowestValueRowIndex = -1;		
			for(int matrixRow = 0; matrixRow<Board.getNbOfGoals(); matrixRow++){
				if(GoalCluster.getClusterMatrixValue(matrixRow, goalIndex) != 0
						&& GoalCluster.getClusterMatrixValue(matrixRow, goalIndex) < lowestValueBlockType){
					lowestValueBlockType = GoalCluster.getClusterMatrixValue(matrixRow, goalIndex);
					lowestValueRowIndex = matrixRow;
				}
			}

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
				boolean moveSuccesful = moveOneBoxToGoal(tempVect, state.getLastBoxMovedIndex(), lowestValueRowIndex);
				//if succesful put back in Queue - maybe it should be moved further on!
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
						if(GoalCluster.getClusterMatrixValue(goalIndex, lowestValueRowIndex) <= blockType){
							//No more moving put back in original state vector:
							pStates.add(state); //(already polled from queue)
						}
						else{
							tempVect.add(state);
							boolean moveSuccesful = moveOneBoxToGoal(tempVect, state.getLastBoxMovedIndex(), lowestValueRowIndex);
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

