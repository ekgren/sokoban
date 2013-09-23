/*
 * Box
 * 
 * Verison 0.1
 * 
 * Represents a box, initially its row, col position.
 * 
 */

public class Box {

	private int row;
	private int col;
	private boolean isOnGoal;
	private boolean isReachable;

	
	public Box(int pRow, int pCol){
		row = pRow;
		col = pCol;
		isOnGoal = false;
	}
	
	public Box(int pRow, int pCol, boolean pIsOnGoal){
		row = pRow;
		col = pCol;
		isOnGoal = pIsOnGoal;
	}

    /**
     * Moves the box in a specified direction
     *
     * up = row - 1
     * down = row + 1
     * right = column + 1
     * left = column - 1
     *
     * @param pDir direction to move
     */
	public void move(char pDir){
		switch (pDir) {
            case 'U':
                this.setRow(row - 1);
                break;
            case 'D':
                this.setRow(row + 1);
                break;
            case 'R':
                this.setCol(col + 1);
                break;
            case 'L':
                this.setCol(col - 1);
                break;
        }
	}

	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return col;
	}
	
	private void setRow(int pRow){
		row = pRow;
	}
	
	private void setCol(int pCol){
		col = pCol;
	}
	
	public boolean isReachable(){
		return isReachable;
	}
	
}
