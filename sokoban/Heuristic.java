package sokoban;

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
    
    public static int arielHeuristic(State pState){
    	return 0;
    }
}
