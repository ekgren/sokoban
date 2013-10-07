package sokoban;

import java.awt.Point;

/**
 * NEO-SOKOBAN CELL CLASS.
 * 
 * The basic building block of our map representation.
 */

@SuppressWarnings("serial")
public class Cell extends Point{
	
	// Boolean to tell if cell is goal.
	public boolean isGoal = false;
	
	// True if box is allowed on cell, false otherwise.
	public boolean boxAllowed = true;
	
	// Board corner information.
	public boolean upperLeftCorner = false;
	public boolean upperRightCorner = false;
	public boolean lowerLeftCorner = false;
	public boolean lowerRightCorner = false;
	
	// Board corridor information.
	public boolean horizontalCorridor = false;
	public boolean verticalCorridor = false;
	
	// Wall info
	public boolean wallUp = false;
	public boolean wallDown = false;
	public boolean wallLeft = false;
	public boolean wallRight = false;

	// Parent cell. Will be changed when cell is used for search.
	private Cell parent;
	
	// Heuristic value for BFGS, maybe we wont use this.
	private int heuristicValue;
	private int cost;
	private int gradient = 0;
		
	/** Get the parent cell of this cell. */
	public Cell getParent(){
		return this.parent;
	}
	
	/** Set the parent cell of this cell. */
	public void setParent(Cell parent){
		this.parent = parent;
	}
	
	/** Manhattan distance heuristic for search. */
	public void heuristic(int goalX, int goalY){
		this.heuristicValue = (int)(Math.abs(this.getX() - goalX) + Math.abs(this.getY() - goalY));
	}
	
	/** Set cost. */
	public void setCost(int cost){
		this.cost = cost;
	}
	
	/** Get cost. */
	public int getCost(){
		return cost;
	}
	
	/** Reset cost. */
	public void resetCost(){
		cost = 0;
	}
	
	/** Get gradient. */
	public int getGradient(){
		return gradient;
	}
	
	/** Set gradient. */
	public void setGradient(int gradient){
		this.gradient = gradient;
	}
}