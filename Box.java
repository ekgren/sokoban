/*
 * Box
 * 
 * Verison 0.1
 * 
 * Represents a box, initially its row, col position.
 * 
 */

public class Box {

	
	public String hashString() {
		//Not most efficient way and has not been confirmed to work!
		return String.valueOf(row) + String.valueOf(col);
	}

	@Override
	public boolean equals(Object obj) {
	/*
	 * Auto generated, maybe not necessary.
	 */
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Box other = (Box) obj;
		if (col != other.col)
			return false;
		if (isOnGoal != other.isOnGoal)
			return false;
		if (isReachable != other.isReachable)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

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
