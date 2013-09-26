package sokoban;

import java.util.Vector;

/**
 * This class holds all the Heuristics. Feel free to add new heuristic methods
 */
public class Heuristic {

    /**
     * This state returns a zero heuristic
     *
     * @param pState input state
     * @return 0
     */
    public static int getHeuristicZ(State pState) {
        return 0;
    }

    /**
     * First simple heuristic - lower value means "better" i.e. closer to solution.
     *
     * @param pState input state
     * @return heuristic value (int)
     */
    public static int getHeuristicSimple(State pState) {

        int lSumOfMovesToClosestGoal = 0;

        for (Box box : pState.getBoxes()) {

            int lowestBoxMove = Integer.MAX_VALUE;

            int nbOfDeadLockT1 = 0;

            for (int goalIndex = 0; goalIndex < Board.getNbOfGoals(); goalIndex++) {
                if (Board.getGoalGrad(goalIndex, box.getRow(), box.getCol()) != -1) {
                    lowestBoxMove = Math.min(lowestBoxMove, Board.getGoalGrad(goalIndex, box.getRow(), box.getCol()));
                } else {
                    nbOfDeadLockT1++;
                }
            }
            if (Board.getNbOfGoals() == nbOfDeadLockT1) {
                return Integer.MAX_VALUE;
            } else {
                lSumOfMovesToClosestGoal = lSumOfMovesToClosestGoal + lowestBoxMove + nbOfDeadLockT1 * 5; //just test!
            }
        }

        return lSumOfMovesToClosestGoal;
    }

    /**
     * David Hasselhoff heuristics - Returns the the distance that is the smallest distance of all boxes
     *
     * @param pState input state
     * @return int
     */
    public static int getDavidHeuristic(State pState) {

        Vector<Box> lBoxes = pState.getBoxes();
        int lStateScore = Integer.MAX_VALUE;
        for (Box box : lBoxes) {
            for (int i = 0; i < Board.getNbOfGoals(); i++) {
                lStateScore = Math.min(lStateScore, Board.getGoalGrad(i, box.getRow(), box.getCol()));
            }
        }
        return lStateScore;
    }

    /**
     * Hasselhoff heuristics - Returns the the average of the box distances ( sum(smallest distance per box)/#boxes )
     *
     * @param pState input state
     * @return int
     */
    public static int getHasselhoffHeuristic(State pState) {

        Vector<Box> lBoxes = pState.getBoxes();
        int lStateScore = 0;
        for (Box box : lBoxes) {
            int lBoxScore = Integer.MAX_VALUE;
            for (int i = 0; i < Board.getNbOfGoals(); i++) {
                lBoxScore = Math.min(lBoxScore, Board.getGoalGrad(i, box.getRow(), box.getCol()));
            }
            lStateScore = lStateScore + lBoxScore;
        }
        return lStateScore / lBoxes.size();
    }

    public static int arielHeuristic(State pState){
    	
    	int lPOINTS = 0;
    	
        for (Box box : pState.getBoxes()) {
        	Vector<Goal> goalCopy = Board.getListOFGoals();
        	int minDist = Integer.MAX_VALUE;
        	int cityBlockDistance;

        	for (Goal goal : goalCopy) {
            	cityBlockDistance = Math.abs(box.getRow() - goal.getRow()) + Math.abs(box.getCol() - goal.getCol());
            	lPOINTS = lPOINTS + cityBlockDistance;
            }
    	}
        //lPOINTS = lPOINTS + pState.getG();
        
    	return lPOINTS;
    }
}
