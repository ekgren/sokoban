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
	
	private int cost;
	private double gradient = 0;
		

	/**
	 * Get the parent cell of this cell.
	 * @return
	 */
	public Cell getParent(){ return this.parent; }
	
	
	/**
	 * Set the parent cell of this cell.
	 * @param parent
	 */
	public void setParent(Cell parent){	this.parent = parent; }
	
	
	/**
	 * Set cost.
	 * @param cost
	 */
	public void setCost(int cost){ this.cost = cost; }
	

	/**
	 * Get cost. 
	 * @return
	 */
	public int getCost(){ return cost; }
	

	/**
	 * Reset cost.
	 */
	public void resetCost(){ cost = 0; }
	
	
	/**
	 * Get gradient.
	 * @return
	 */
	public double getGradient(){ return gradient; }
	
	
	/**
	 * Set gradient.
	 * @param gradient
	 */
	public void setGradient(double gradient){ this.gradient = gradient; }
}