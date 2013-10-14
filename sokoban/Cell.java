package sokoban;

/*
 * Cell
 * 
 * Verison 0.1
 * 
 * Represents a Cell, initially its row, col position.
 * 
 */

import java.awt.Point;
import java.util.Comparator;


public class Cell extends Point{

	private Cell parent;
	private int expansionLevel;
	
	
	public Cell(int pRow, int pCol){
		this.x = pRow;
		this.y = pCol;
	}
	
	public Cell(Cell pParent,int pRow,int pCol){
		this.x = pRow;
		this.y = pCol;
		this.parent = pParent;
	}
	
	public Cell(int pRow,int pCol, int pExpansionLevel){
		this.x = pRow;
		this.y= pCol;
		this.expansionLevel = pExpansionLevel;
	}
	
	public int getRow(){
		return this.x;
	}
	
	public int getCol(){
		return this.y;
	}
	
	
	public int getExpansionLevel(){
		return expansionLevel;
	}
	
	public Cell getParent(){
		return this.parent;
	}
	
	public void setRow(int rowIndex){
		this.x = rowIndex;
	}
	
	public void setCol(int colIndex){
		this.y = colIndex;
	}
	
	public void setPos(int pRow, int pCol){
		this.x = pRow;
		this.y = pCol;
	}
	
	public void setParent(Cell pCell){
		this.parent = pCell;
	}
	
	/*
	public boolean equals(Cell obj) {
	    //null instance of Object will always return false
	    if (obj == this)
	      return true;
	    //Else check if fields are the same.
	    return  this.getRow() == ((Cell) obj).getRow() &&
	            this.getCol() == ((Cell) obj).getCol();  
	}*/
	

	public static class NormComparator implements Comparator<Cell>{
		//NormComparator which uses Manhattan distance.
		int lRow = 0;
		int lCol = 0;
		
		public NormComparator(){
			
		}
		
		public NormComparator(int pRow,int pCol){
			this.lRow = pRow;
			this.lCol = pCol;
		}
		@Override
		public int compare(Cell N1, Cell N2) {
			int d1 = Math.abs(N1.getRow()-lRow) + Math.abs(N1.getCol() - lCol);
			int d2 = Math.abs(N2.getRow()-lRow) + Math.abs(N2.getCol() - lCol);
			return d1-d2;
		}
		//Since we are using static NormComparator we want to change to what position we are
		//searching to.
		public void setGoal(int pRow,int pCol){
			this.lRow = pRow;
			this.lCol = pCol;
		}
	}
}