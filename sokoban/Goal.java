package sokoban;
/*
 * Goal
 * 
 * Verison 0.1
 * 
 * Represents a Goal, initially its row, col position.
 * 
 */

public class Goal {

	private int row;
	private int col;
	private boolean isOccupied;
	
	public Goal(int pRow, int pCol){
		row = pRow;
		col = pCol;
		isOccupied = false;
	}
	
	public Goal(int pRow, int pCol, boolean pIsOnGoal){
		row = pRow;
		col = pCol;
		isOccupied = pIsOnGoal;
	}
	
	
	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return col;
	}
}
