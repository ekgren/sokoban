/*
 * State
 * 
 * Version 0.1
 * 
 * Represents the Sokoban State.
 * 
 * Should not be confused with the class Map. 
 */

import java.util.Vector;

public class State implements Cloneable {

    private Vector<Box> boxes;
    private int playerRow;
    private int playerCol;

    /**
     * Constructs the internal representation of the State
     *
     * @param
     */
    public State(Vector<Box> pBoxes, int pPlayerRow, int pPlayerCol) {
        boxes = pBoxes;
        playerRow = pPlayerRow;
        playerCol = pPlayerCol;

    } // End constructor State

    /**
     * Constructs a state which is the result of applying one move.
     * "final" in arguments to avoid applying changes to parent state
     *
     * @param pParentState
     * @param pBoxIndex    index if the boxes vector
     * @param pMoveDir     the direction to move the box
     */
    public State(final State pParentState, int pBoxIndex, char pMoveDir) {
        // set boxes
        this.boxes = (Vector<Box>) pParentState.boxes.clone();
        System.out.println(pMoveDir);

        // set player position before moving the box
        playerRow = this.boxes.get(pBoxIndex).getRow();
        playerCol = this.boxes.get(pBoxIndex).getCol();

        // move the box
        this.boxes.get(pBoxIndex).move(pMoveDir);

    } // End constructor State

    /**
     * Operates on an (empty) Vector with States
     * and "fills" with all successor states
     *
     * @param pStates
     */
    public void allSuccessors(Vector<State> pStates) {
        /*
         * TODO
	     * 
	     * Idea taken from HW1
	     * I guess that the reason for sending in an empty vector that this function
	     * "operates" on is to avoid incorrect references...
	     * Since the game information is split up between constants in Map and dynamical in State
	     * one must also send in a reference to pState...
	     */

        pStates.clear();

        int boxIndex = 0;
        for (Box box : boxes) {
                /* If a move is possible, then add the new state in the pStates vector */
            if (tryMove(box, 'U'))
                pStates.add(new State(this, boxIndex, 'U'));
            if (tryMove(box, 'D'))
                pStates.add(new State(this, boxIndex, 'D'));
            if (tryMove(box, 'R'))
                pStates.add(new State(this, boxIndex, 'R'));
            if (tryMove(box, 'L'))
                pStates.add(new State(this, boxIndex, 'L'));
            boxIndex++;
        } // End for boxes
    } // End allSuccessors

    /**
     * Tries to make a move from a certain position
     * <p/>
     * up = row - 1
     * down = row + 1
     * right = column + 1
     * left = column - 1
     *
     * @param pDir direction
     */
    private boolean tryMove(Box pBox, char pDir) {
        int lR = pBox.getRow();
        int lC = pBox.getCol();

        switch (pDir) {
            case 'U':
                return Map.isFree(lR - 1, lC);
            case 'D':
                return Map.isFree(lR + 1, lC);
            case 'R':
                return Map.isFree(lR, lC + 1);
            case 'L':
                return Map.isFree(lR, lC - 1);
        }
        // if wrong input
        return false;

    } // End TryMove

} // End Class State
