package sokoban;

import java.util.Comparator;

public class StatePriorityComparator implements Comparator<State>{

	@Override
	public int compare(State pState1, State pState2) {

		int h1 = heuristic1(pState1);
		int h2 = heuristic1(pState2);
		
		if (h1 < h2){
			return -1; //move forward?!
		}
		else if (h2 < h1){
			return 1;
		}
		return 0;
	}

	/**
	 * First simple heuristic
	 * 
	 * lower value means "better" i.e. closer to solution.
	 * @param pState
	 * @return
	 */
	public int heuristic1(State pState){
		
		int lSumOfMovesToClosestGoal = 0;
				
		for (Box box : pState.getBoxes()){
			
			int lowestBoxMove = Integer.MAX_VALUE;
	
			int nbOfDeadLockT1 = 0;
			
			for (int goalIndex = 0; goalIndex < Board.getNbOfGoals(); goalIndex++){
				if( Board.getGoalGrad(goalIndex, box.getRow(), box.getCol()) != -1){
					lowestBoxMove = Math.min(lowestBoxMove, Board.getGoalGrad(goalIndex, box.getRow(), box.getCol()));
				}
				else{
					nbOfDeadLockT1++;
				}
			}
			if(Board.getNbOfGoals() == nbOfDeadLockT1){
				return Integer.MAX_VALUE;
			}
			else{
				lSumOfMovesToClosestGoal = lSumOfMovesToClosestGoal + lowestBoxMove + nbOfDeadLockT1*5; //just test!
			}
		}
		
		return lSumOfMovesToClosestGoal;
	}
}