/*
 * Compare two states according 
 */

package sokoban;

import java.util.Comparator;

public class OneBoxMoveComparator implements Comparator<State>{

	private int targetGoalIndex;
	private int boxToMoveIndex;
	private boolean toClosestGoal;
	private double h1;
	private double h2; 

	
	public void setTargetGoalAndBox(int pBoxIndex, int pGoalIndex){
		this.boxToMoveIndex = pBoxIndex;
		this.targetGoalIndex = pGoalIndex;
		this.toClosestGoal = false;
	}
	
	public void setBoxToClosestGoal(int pBoxIndex){
		this.boxToMoveIndex = pBoxIndex;
		this.toClosestGoal = true;
	}
	
	@Override
	public int compare(State pState1, State pState2) {

		if(toClosestGoal){
			h1 = pState1.getGoalGradMerged( pState1.getBox(boxToMoveIndex).getRow(), 
					pState1.getBox(boxToMoveIndex).getCol() );	

			h2 = pState2.getGoalGradMerged( pState2.getBox(boxToMoveIndex).getRow(), 
					pState2.getBox(boxToMoveIndex).getCol() );
		}
		else{
			h1 = Board.getGoalGrad( targetGoalIndex, 
					pState1.getBox(boxToMoveIndex).getRow(), 
				  	pState1.getBox(boxToMoveIndex).getCol() );	

			h2 = Board.getGoalGrad( targetGoalIndex, 
					pState2.getBox(boxToMoveIndex).getRow(), 
					pState2.getBox(boxToMoveIndex).getCol() );
		}
		
		//if box ended up on a place where it can't get to the goal give high value.
		if(h1 < 0){
			h1 = Integer.MAX_VALUE;
		}
		if(h2 < 0){
			h2 = Integer.MAX_VALUE;
		}
		
		if (h1 < h2){
			return -1; //move forward
		}
		else if (h2 < h1){
			return 1; //move backward
		}
		return 0;
	}
}