package sokoban;

import java.awt.Point;

/**
 * NEO-SOKOBAN BOX CLASS.
 * 
 * Extends java Point class.
 */
@SuppressWarnings("serial")
public class Box extends Point{
	
	// True if box is on goal.
	public boolean onGoal = false;
	public Point dreamGoal = null;

}
