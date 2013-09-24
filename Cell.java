
/*
 * Cell
 * 
 * Verison 0.1
 * 
 * Represents a Cell, initially its row, col position.
 * 
 */

import java.util.Comparator;


public class Cell {

	private int row;
	private int col;
	private Cell parent;
	
	public Cell(int pRow, int pCol){
		this.row = pRow;
		this.col = pCol;
	}
	
	public Cell(Cell pParent,int pRow,int pCol){
		this.row = pRow;
		this.col = pCol;
		this.parent = pParent;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getCol(){
		return col;
	}
	
	public Cell getParent(){
		return this.parent;
	}
	
	public void setParent(Cell pCell){
		this.parent = pCell;
	}
	
	public boolean equals(Cell obj) {
	    //null instanceof Object will always return false
	    if (obj == this)
	      return true;
	    return  this.getRow() == ((Cell) obj).getRow() &&
	            this.getCol() == ((Cell) obj).getCol();  
	}
	
	public static class NormComparator implements Comparator<Cell>{
		int lRow = 0;
		int lCol = 0;
		
		public NormComparator(int pRow,int pCol){
			this.lRow = pRow;
			this.lCol = pCol;
		}
		@Override
		public int compare(Cell N1, Cell N2) {
			// TODO Auto-generated method stub
			int d1 = Math.abs(N1.getRow()-lRow) + Math.abs(N1.getCol() - lCol);
			int d2 = Math.abs(N2.getRow()-lRow) + Math.abs(N2.getCol() - lCol);
			return d1-d2;
		}
		public void setStart(int pRow,int pCol){
			this.lRow = pRow;
			this.lCol = pCol;
		}
	}
}