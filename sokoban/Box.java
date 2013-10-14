package sokoban;

import java.awt.Point;
/*
 * Box
 * 
 * Verison 0.1
 * 
 * Represents a box, initially its row, col position.
 * 
 */

public class Box extends Point{

	
	public String hashString() {
		//Not most efficient way and has not been confirmed to work!
		return String.valueOf(this.x) + String.valueOf(this.y);
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
		if (this.y != other.y)
			return false;
		if (isOnGoal != other.isOnGoal)
			return false;
		if (isReachable != other.isReachable)
			return false;
		if (this.x != other.x)
			return false;
		return true;
	}

	private boolean isOnGoal;
	private boolean isReachable;

	
	public Box(int pRow, int pCol){
		isOnGoal = false;
	}
	
	public Box(int pRow, int pCol, boolean pIsOnGoal){
		this.x = pRow;
		this.y = pCol;
		isOnGoal = pIsOnGoal;
	}
	
	/**
	 * Constructs a "deep copy".
	 * @param pBox
	 */
	public Box(Box pBox){
		this.x = pBox.getRow();
		this.y = pBox.getCol();
		this.isOnGoal = pBox.isOnGoal();
		this.isReachable = pBox.isReachable();
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
                this.setRow(this.x - 1);
                break;
            case 'D':
                this.setRow(this.x + 1);
                break;
            case 'R':
                this.setCol(this.y + 1);
                break;
            case 'L':
                this.setCol(this.y - 1);
                break;
        }
	}

	public void setIsOnGoal(boolean pIsOnGoal){
		this.isOnGoal = pIsOnGoal;
	}
	
	public boolean isOnGoal(){
		return isOnGoal;
	}
	
	public int getRow(){
		return this.x;
	}
	
	public int getCol(){
		return this.y;
	}
	
	private void setRow(int pRow){
		this.x = pRow;
	}
	
	private void setCol(int pCol){
		this.y = pCol;
	}
	
	public boolean isReachable(){
		return isReachable;
	}
	
}
