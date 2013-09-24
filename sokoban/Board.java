package sokoban;

/*
 * Board
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

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;


public class Board {

	/*
	 * CONSTANTS
	 */
	private static int nbRows;
	private static int nbCols;
	private static int nbBoxes; // same as number of goals 

	private static Vector<String> stringRepr; //string Representation of Map
	private static Vector<String> cleanMapStringRepr = new Vector<String>(); //string Representation of Map
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
					if(Sokoban.debugMode){
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
			if(Sokoban.debugMode){
				System.out.println("MapError: There is not an equal amout of boxes and goals!");
			}
		}

		initialState = new State(lBoxesList, lPlayerStartRow, lPlayerStartCol);

		//Set dimension
		deadLocksT0 = new boolean[nbRows][nbCols];
		deadLocksT1 = new boolean[nbBoxes][nbRows][nbCols];
		goalGrad = new int[nbBoxes][nbRows][nbCols];

		
		//Fix the cleanMapString:		
		for(String lRow : stringRepr){
			String lStringCopy = lRow;
			
			lStringCopy = lStringCopy.replace('$', ' '); //Box to nothing
			lStringCopy = lStringCopy.replace('*', '.'); //Box on goal to Goal
			lStringCopy = lStringCopy.replace('@', ' '); //Player to nothing
			lStringCopy = lStringCopy.replace('+', '.'); //Player on goal to Goal
			
			cleanMapStringRepr.add(lStringCopy);
		}
		
		//mark DealLocks and Gradient to respective Goal:
		markDeadlocksAndGrad();

	} // End constructor Map

	
	
	private void markDeadlocksAndGrad(){

		/* Gradient to goal and deadLock (-) is marked according to "BFS" see below.
		 * deadlocks get -1 as value.
		  	########
			#---#-.####
			#-5432123-#
			####43-####
			   #--##
			   ####
		 */

		int lGoalIndex = -1;
		for (Goal goal : goalsList){
			lGoalIndex++;
			
			for(int row=0; row<nbRows; row++){
				for(int col=0; col<nbCols; col++){
					goalGrad[lGoalIndex][row][col] = -1; //marks Deadlock type 1 for this Goal.
				}
			}				
			
			goalGrad[lGoalIndex][goal.getRow()][goal.getCol()] = 0; //marks goals position
			
			boolean[][] lVisitedCell = new boolean[nbRows][nbCols]; //Keeps track of where we have been.

			lVisitedCell[goal.getRow()][goal.getCol()] = true;
			
			//Create queue and add a first node.	
			Queue<Cell> qe=new LinkedList<Cell>();
			qe.add(new Cell(goal.getRow(), goal.getCol(),0)); //last index means "expansion level=0"

			//Main search
			while(!qe.isEmpty()){
				
				int curRow = qe.peek().getRow();
				int curCol = qe.peek().getCol(); //also removes Cell!
				int lMovesFromGoal = qe.poll().getExpansionLevel() + 1; //also removes Cell!

				
				// Next cell to check if the box can come from...
				int lBoxFromRow;
				int lBoxFromCol;
				
				// The cell to check that the player can be to be able to push box...
				int lPlayerPushRow;
				int lPlayerPushCol;

				
				//check "Pushed down from above"	
				if(curRow>1){ //if on 1st row it can never be pushed down (must be walls on 0th row).
					lBoxFromRow = curRow - 1;
					lBoxFromCol = curCol;
					lPlayerPushRow = curRow - 2;
					lPlayerPushCol = curCol;
					
					if(lVisitedCell[lBoxFromRow][lBoxFromCol]){
						//if been here do nothing
					}	
					else if(!walls[lBoxFromRow][lBoxFromCol]){
						if(!walls[lPlayerPushRow][lPlayerPushCol]){
							goalGrad[lGoalIndex][lBoxFromRow][lBoxFromCol] = lMovesFromGoal;
							qe.add(new Cell(lBoxFromRow, lBoxFromCol, lMovesFromGoal));
							lVisitedCell[lBoxFromRow][lBoxFromCol] = true; //save to repeated states...
						}
					}
				}//End if, check "Pushed down from above"
				
				//check "Pushed up from below"	
				if(curRow<nbRows-1){ //if on next to last row it can never be pushed up (must be walls on last row).
					lBoxFromRow = curRow + 1;
					lBoxFromCol = curCol;
					lPlayerPushRow = curRow + 2;
					lPlayerPushCol = curCol;
					
					if(lVisitedCell[lBoxFromRow][lBoxFromCol]){
						//if been here do nothing
					}	
					else if(!walls[lBoxFromRow][lBoxFromCol]){
						if(!walls[lPlayerPushRow][lPlayerPushCol]){
							goalGrad[lGoalIndex][lBoxFromRow][lBoxFromCol] = lMovesFromGoal;
							qe.add(new Cell(lBoxFromRow, lBoxFromCol, lMovesFromGoal));
							lVisitedCell[lBoxFromRow][lBoxFromCol] = true; //save to repeated states...
						}
					}
				}//End if, check "Pushed up from below"
				
				//check "Pushed right from left"	
				if(curCol>1){ //if in column 1 it can never be pushed right (must be walls on column 0).
					lBoxFromRow = curRow;
					lBoxFromCol = curCol-1;
					lPlayerPushRow = curRow;
					lPlayerPushCol = curCol-2;
					
					if(lVisitedCell[lBoxFromRow][lBoxFromCol]){
						//if been here do nothing
					}	
					else if(!walls[lBoxFromRow][lBoxFromCol]){
						if(!walls[lPlayerPushRow][lPlayerPushCol]){
							goalGrad[lGoalIndex][lBoxFromRow][lBoxFromCol] = lMovesFromGoal;
							qe.add(new Cell(lBoxFromRow, lBoxFromCol, lMovesFromGoal));
							lVisitedCell[lBoxFromRow][lBoxFromCol] = true; //save to repeated states...
						}
					}
				}//End if, check "Pushed right from left"
				
				//check "Pushed left from right"	
				if(curCol>1){ //if in next to last column it can never be pushed left (must be walls on last column).
					lBoxFromRow = curRow;
					lBoxFromCol = curCol+1;
					lPlayerPushRow = curRow;
					lPlayerPushCol = curCol+2;
					
					if(lVisitedCell[lBoxFromRow][lBoxFromCol]){
						//if been here do nothing
					}	
					else if(!walls[lBoxFromRow][lBoxFromCol]){
						if(!walls[lPlayerPushRow][lPlayerPushCol]){
							goalGrad[lGoalIndex][lBoxFromRow][lBoxFromCol] = lMovesFromGoal;
							qe.add(new Cell(lBoxFromRow, lBoxFromCol, lMovesFromGoal));
							lVisitedCell[lBoxFromRow][lBoxFromCol] = true; //save to repeated states...
						}
					}
				}//End if, check "Pushed left from right"
			}
		}//End for goal in goalsList
	}

	
	/**
	 * ONLY VERIFICATION METHOD FOR PLOTTING GOAL-GRADIENTS
	 * @param pReferensGoalIndex
	 */
	public static void printGoalGrad(int pReferensGoalIndex){
		System.out.println("Original String representation:");
		for (String row : stringRepr){
			System.out.println(row);
		}
		
		System.out.println("");
		System.out.println("Clean Map String representation:");
		for (String row : cleanMapStringRepr){
			System.out.println(row);
		}
		
		System.out.println("");
		System.out.println("Goal gradient:");
		for (int row = 0; row<nbRows; row++){
			for (int col = 0; col<nbCols; col++){				
				if (goalGrad[pReferensGoalIndex][row][col] != -1){
					System.out.print(goalGrad[pReferensGoalIndex][row][col]);
				}
				else if(col<cleanMapStringRepr.get(row).length()){ //might be shorter than nbCols!
					System.out.print(cleanMapStringRepr.get(row).charAt(col));
				}
			}//End for columns	
			System.out.println(" ");
		}//End for rows
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
	
	public static Vector<String> getStringRepr(){
		return stringRepr;
	}
	
	public static int getNbRows(){
		return nbRows;
	}
	
	public static int getNbCols(){
		return nbCols;
	}
	
	public static int getGoalGrad(int pGoalIndex, int pRow, int pCol){
		return goalGrad[pGoalIndex][pRow][pCol];
	}

	public static int getNbOfBoxes(){
		return nbBoxes;
	}
	public static int getNbOfGoals(){
		return nbBoxes; //should be boxes it is the same!
	}
} // End Class Board
