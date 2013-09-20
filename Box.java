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
	
	public void move(){
		
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
