
/*
 * Map
 * 
 * Version 0.1
 * 
 * Represents the Sokoban map (contains all info).
 * It separates the fixed part of the map (walls and goals)
 * from dynamic (player and boxes).
 * 
 * Contains several "layers" of representation for:
 * - typeIDeadLock
 * - gradient to goals
 * - ...
 * 
 * Should not be confused with the class State which is
 * a representation of a particular state/node in a game
 * 
 * Should be declared "final" "static"...? for one level/solution.
 */

import java.util.Vector;


public class Board {
	
	/*
	 * CONSTANTS
	 */
	private static int nbRows;
	private static int nbCols;
	private static int nbBoxes; // same as number of goals 

	private static Vector<String> stringRepr; //string Representation of Map
	private static Vector<String> cleanMapString = new Vector<String>(); //string Representation of Map
	private static State initialState; //The initial game state
	
	/*
	 * LAYERS
	 * Other representation ArrayList etc. is possible, discuss this.
	 * e.g. ArrayList<Wall> walls; or HashMap...
	 */
	
	private static boolean[][] walls; //all "fixed" parts of map i.e. walls are true [row][col].
	private static boolean[][] goals; //all goals are true [row][col].
	private static Vector<Goal> goalsList = new Vector<Goal>(); //might sometimes or always be more efficient can have both...

	private static boolean [][] deadLocksT0; //marks Type0 deadlocks = corners (independet of goals) [row][col].
	private static boolean[][][] deadLocksT1; //marks Type1 deadlocks [goal][row][col].
	private static int[][][] goalGrad; //Gradient to each goal considering only walls [goal][row][col].


	/**
	 * Constructs the internal representation of the Map
	 * from the standard string representation.
	 *
	 * @param pMessage the compact string representation of the state
	 */
	public Board(final Vector<String> pStringRepr) {
		
		//Storing String representation
		stringRepr = pStringRepr;
		cleanMapString = pStringRepr; // could be implemented to print the "empty map"...
				
		//Counting columns
		int lMaxNbOfCol = 0;
		for(String row : stringRepr){
			if(row.length() > lMaxNbOfCol){
				lMaxNbOfCol = row.length();
			}
        }
		
		//Set class constants
		nbRows = pStringRepr.size();
		nbCols = lMaxNbOfCol;
	
		//Set dimensions
		walls = new boolean[nbRows][nbCols];
		goals = new boolean[nbRows][nbCols];	
		
		
		int lPlayerStartRow = 0; //can never be zero since there must be wall here...
		int lPlayerStartCol = 0; //can never be zero since there must be wall here...
		Vector<Box> lBoxesList = new Vector<Box>();
		
		for(int row=0; row<nbRows; row++){
			if (stringRepr.get(row).length() > lMaxNbOfCol) {
				lMaxNbOfCol = stringRepr.get(row).length();
			}
			for(int col=0; col<stringRepr.get(row).length(); col++){
				
				if(stringRepr.get(row).charAt(col) == ' '){ //Free space
					//Do nothing, this comes first since most common.
				}
				else if(stringRepr.get(row).charAt(col) == '#'){ //Wall
					walls[row][col]=true; //All automatically false initially
				}
				else if(stringRepr.get(row).charAt(col) == '$'){ //Box
					lBoxesList.add(new Box(row, col, false));
				}
				else if(stringRepr.get(row).charAt(col) == '.'){ //Goal
					goals[row][col]=true; //All automatically false initially
					goalsList.add(new Goal(row, col, false));
				}
				else if(stringRepr.get(row).charAt(col) == '*'){ //Box on goal
					goals[row][col]=true; //All automatically false initially
					goalsList.add(new Goal(row, col, true)); //true means is occupied.
					lBoxesList.add(new Box(row, col, true)); //true means is on goal
				}
				else if(stringRepr.get(row).charAt(col) == '@'){ //Sokoban Player
					lPlayerStartRow = row;
					lPlayerStartCol = col;
				}
				else if(stringRepr.get(row).charAt(col) == '+'){ //Sokoban PLayer on Goal
					lPlayerStartRow = row;
					lPlayerStartCol = col;
					goals[row][col]=true; //All automatically false initially
					goalsList.add(new Goal(row, col, false));
				}
				else{
					if(Main.debugMode){
						System.out.println("MapError: Unknown character: " 
					+ stringRepr.get(row).charAt(col) + " found!" );
					}
				}
	        }
        }
		

		//Check and set number of boxes/goals
		if (goalsList.size() == lBoxesList.size()){
			nbBoxes = lBoxesList.size();
		}
		else{
			if(Main.debugMode){
				System.out.println("MapError: There is not an equal amout of boxes and goals!");
			}
		}
		
		initialState = new State(lBoxesList, lPlayerStartRow, lPlayerStartCol);
		
		//Set dimension
		deadLocksT0 = new boolean[nbRows][nbCols];
		deadLocksT1 = new boolean[nbBoxes][nbRows][nbCols];
		goalGrad = new int[nbBoxes][nbRows][nbCols];
		
		//mark DealLocks and Gradient to respective Goal:
		markDeadlocksAndGrad();
		
	} // End constructor Map

	private void markDeadlocksAndGrad(){
		
		/* Gradient to goal is marked according to "BFS" see below
		  	########
			#765#1.####
			#654321234#
			####432####
			   #54##
			   ####
		 */
		
		/* DeadLocks are marked accordning to each gaol respectively
	  		########
			#ddd#d.####
			#d       d#
			####  d####
		   	   #dd##
		       ####
		 */
		
		/*
		 * Should find and mark all deadlock to each goal respectively and the gradient (distance to goal)
		 * This can be done at the same time, there fore one function.
		 */
	}
	
	public static State getInitialState(){
		return initialState;
	}

	public static boolean isWall(int pRow, int pCol){
		return walls[pRow][pCol]; //this must be developed further to check for dead states etc.
	}
	
	public static boolean isGoal(int pRow, int pCol){

		return goals[pRow][pCol]; //this must be developed further to check for dead states etc.
	}

	public static boolean isDeadLockT0(int pRow, int pCol){
		return deadLocksT0[pRow][pCol];
	}

	public static boolean isDeadLockT1(int pGoal, int pRow, int pCol){
		return deadLocksT1[pGoal][pRow][pCol];
	}
	
	/**
	 * Checks if position is not wall and not box.
	 * @param pState
	 * @param pRow
	 * @param pCol
	 * @return
	 */
	public static boolean isFree(State pState,int pRow,int pCol){
		return (!isWall(pRow,pCol)&&!pState.isBox(pRow,pCol));
	}

	public static Vector<Goal> getListOFGoals(){
		return goalsList;
	}

} // End Class Map
