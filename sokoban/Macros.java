package sokoban;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Vector;

public class Macros {

	private static OneBoxMoveComparator oneBoxMoveComp = new OneBoxMoveComparator();
	
	/**
	 * Tries to push all boxes to the closest goals.
	 * 
	 * inOutStatesVector:
	 * input: ONE STATE from which the macro should be tried.
	 * output: ONE STATE - the best the macro could do.
	 * 
	 * returns: trun only if all boxes could be moved to a goal (final state).
	 * 
	 * @return
	 */
	public static boolean allBoxesToClosestGoals(Vector<State> inOutStatesVector){
		
		if(inOutStatesVector.size() != 1){
			if(Sokoban.debugMode) System.out.println("/Macros/testMoveBoxesToClosestGoals: several states as input");
		}
		
		boolean allMovesPossible = true;
		for(int i = 0; i < Board.getNbOfBoxes(); i++){
			allMovesPossible = moveOneBoxToClosestGoalGreedyBFS(inOutStatesVector, i);
			//now it contains the result of moving one box...
			
			//Post-process:
			postProcessAccordingGoalCluster(inOutStatesVector);
		}
		return allMovesPossible;
	}
	
	/**
	 * Combination of moving all boxes close to goals and then running goal cluster-algorithm
	 * and finally greedyBFS...
	 */
	public static State directToGoalsWithGoalGoalCluster(){
		
		Vector<State> states = new Vector<State>();
		Vector<State> tempStatesMoveToGoals = new Vector<State>();
		Vector<State> tempStateGoalClustAlg = new Vector<State>();

		tempStatesMoveToGoals.add(Board.getInitialState());
		//just in case something goes wrong also add the initial state:
		states.add(Board.getInitialState());
		
		/*
		 * For all boxes:
		 */
		for(int i = 0; i < Board.getNbOfBoxes(); i++){
			//Operates on the state so that last state in vector will have the box on goal.
			moveOneBoxToClosestGoal(tempStatesMoveToGoals, i);
			//Add the element where the box is on goal
			tempStateGoalClustAlg.add(tempStatesMoveToGoals.lastElement());
			if(Sokoban.debugMode) System.out.println("/Solver: tempStatesMove... size: " + tempStatesMoveToGoals.size());
			if(Sokoban.debugMode) System.out.println("/Solver: tempStatesGoalC... size: " + tempStatesMoveToGoals.size());

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
			postProcessAccordingGoalCluster(tempStateGoalClustAlg);
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
	public static boolean moveOneBoxToGoal(Vector<State> inOutStatesVector, int pBoxIndex, int pGoalIndex){
		
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
	public static boolean moveOneBoxToClosestGoal(Vector<State> inOutStatesVector, int pBoxIndex){
		
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
	public static boolean moveOneBoxToGoalGradDecentOnly(Vector<State> inOutStatesVector, int pBoxIndex, int pGoalIndex){
		
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
public static boolean moveOneBoxToClosestGoalGradDecentOnly(Vector<State> inOutStatesVector, int pBoxIndex, int recurNb){
	
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
	public static boolean moveOneBoxToGoalGreedyBFS(Vector<State> inOutStatesVector, int pBoxIndex, int pGoalIndex){
		
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
	public static boolean moveOneBoxToClosestGoalGreedyBFS(Vector<State> inOutStatesVector, int pBoxIndex){
		
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
	    	Visualizer.printStateDelux(curState, "/Solver: moveOneBoxToClosestGoalGreedyBFS");
	    	System.out.println("open.size: " + open.size() + " iteration: " + iterations);
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
	    			if(visitedPositions.contains(child)){
	    				//nothing...
	    			}
	    			else{
		    			open.add(child);
		    			visitedPositions.add(child);
	    			}
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
	public static boolean moveOneBoxToPosition(Vector<State> pTempStorage, int pBoxIndex, int goalIndex){
	
		return false;
	}
	
	
	/**
	 * Checks if the last move made in EACH state: if that move was to a goal it checks
	 * if it should be moved further onto another goal according to the goal clusterMatrix.
	 * 
	 * inOutStatesVector:
	 * Input: a number of states that should be post-processed.
	 * Output: corresponding number of states either untouched or with box moved 
	 * 	to unblocking goal.
	 * 
	 * TESTED: There is a problem with the definition of front goals, some that should be considered
	 * front are not accordning to cur def. this could be solved by introducing edgeGoals...
	 * 
	 * @param inOutStatesVector
	 */
	public static void postProcessAccordingGoalCluster(Vector<State> inOutStatesVector){
		
		if(Sokoban.debugMode){
			Visualizer.printState(inOutStatesVector.firstElement(),
					"/Macros/postProcessAccordingGoalCluster: entered method for state:");
		}
		
		//Queue with states that still need processing
		Queue<State> processingQueue = new LinkedList<State>();
		
		//Moves states from input where the last move was to goal to the Queue:
		int i = 0;
		while(i<inOutStatesVector.size()){
			if(inOutStatesVector.get(i).getLastMovedBox().isOnGoal()){
				if(Sokoban.visualizeMode) Visualizer.printStateDelux(inOutStatesVector.get(i), "state in PostProcess kept...");
				processingQueue.add(inOutStatesVector.remove(i)); //move over to queue for processing.
			}
			else{
				i++; //Go t next object.
			}
		}

		//if the states where the last moved box is on goal we need to check clusterMatrix!
		while(processingQueue.size() > 0){
			//look at state:
			State curState = processingQueue.poll();

			int goalIndex = Board.getGoalIndexAt(
					curState.getLastMovedBox().getRow(), curState.getLastMovedBox().getCol());

			//check in clusterMatrix if it is "a block" to any other goal:
			int lowestValueBlockType = 5; //highest block value is 3 (working on type 4);
			int lowestValueRowIndex = -1;		
			for(int matrixRow = 0; matrixRow<Board.getNbOfGoals(); matrixRow++){
				if(GoalCluster.getClusterMatrixValue(matrixRow, goalIndex) != 0
						&& GoalCluster.getClusterMatrixValue(matrixRow, goalIndex) < lowestValueBlockType
						&& !curState.isGoalOccupied(matrixRow)){ //If this goal that it is blocking is allready occ. we dont worry...
					lowestValueBlockType = GoalCluster.getClusterMatrixValue(matrixRow, goalIndex);
					lowestValueRowIndex = matrixRow;
				}
			}
			
			//temporary vector for sending state to move methods.
			Vector<State> tempVect = new Vector<State>();

			if(lowestValueBlockType == 5){
				//Not blocking any box put back to original vector!
				inOutStatesVector.add(curState); //(already polled from queue)
			}			
			/*
			 * if the value == 1,2,3 the box should be moved further on
			 * to the goal with index corresponding to matrix row.
			 */
			else if(lowestValueBlockType == 1){
				tempVect.add(curState.getParent()); //Even if we go back one state/step we should try to move last box for curent!
				boolean moveSuccessful = moveOneBoxToGoalGreedyBFS(tempVect, curState.getLastBoxMovedIndex(), lowestValueRowIndex);
				//if successful put back in Queue - maybe it should be moved further on!
				if(moveSuccessful){
					processingQueue.add(tempVect.remove(0));
				}
				else{
					/*
					 * TODO
					 * This should not happen too often if everything works but must be take care of!
					 * This means the box is in a very bad position
					 */
					inOutStatesVector.add(curState); //(already polled from queue)
					tempVect.clear();
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
							inOutStatesVector.add(curState); //(already polled from queue)
						}
						else{
							tempVect.add(curState.getParent());
							boolean moveSuccesful = moveOneBoxToGoalGreedyBFS(tempVect, curState.getLastBoxMovedIndex(), lowestValueRowIndex);
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
								inOutStatesVector.add(curState); //(already polled from queue)
								tempVect.clear();
							}
						}
						break; //if lowestValueBlockType == 2 no need to check for 3.
					}//End if == blockType
				}
			}
		}
	}//End postProcessAccordningGoalCluster.
	

}
