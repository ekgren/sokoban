/*
 * Cell
 * 
 * Verison 0.1
 * 
 * Represents a Cell, initially just its row, col position.
 * 
 */

public class Cell {

	private int row;
	private int col;
	
	public Cell(int pRow, int pCol){
		row = pRow;
		col = pCol;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return col;
	}
}
