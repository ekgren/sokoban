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
	private static int nbGoals;

	private static Vector<String> stringRepr; //string Representation of Map
	private static Vector<String> cleanMapStringRepr = new Vector<String>(); //string Representation of Map
	private static State initialState; //The initial game state

	/*
	 * LAYERS
	 * Other representation ArrayList etc. is possible, discuss this.
	 * e.g. ArrayList<Wall> walls; or HashMap...
	 */

	public static Cell [][] matrixCells; //Cells for saving garbage collection time.
	private static boolean[][] walls; //all "fixed" parts of map i.e. walls are true [row][col].
	private static boolean[][] goals; //all goals are true [row][col].
	private static int[][] goalsIndexAtPos; //all goals are true [row][col].

	private static Vector<Goal> goalsList = new Vector<Goal>(); //might sometimes or always be more efficient can have both...

	public static boolean [][] deadLocksT0; //marks Type0 deadlocks = corners (independet of goals) [row][col].
	private static int[][][] goalGrad; //Gradient to each goal considering only walls [goal][row][col].
	public static Vector<Vector<Cell>> gradCells; //contains the cells with grad<=2 used in GoalCluster
	//Includes the information about deadlocksT1 marked by -1 in goalGrad!
	private static int[][] goalGradMerged; // Summed GoalGrad
	public static int[] goalWheight;


	/**
	 * Constructs the internal representation of the Map
	 * from the standard string representation.
	 *
	 * @param pStringRepr the compact string representation of the state
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
		matrixCells = new Cell[nbRows][nbCols];
		walls = new boolean[nbRows][nbCols];
		goals = new boolean[nbRows][nbCols];	
		goalsIndexAtPos = new int[nbRows][nbCols]; //all zero initially

		int lPlayerStartRow = 0; //can never be zero since there must be wall here...
		int lPlayerStartCol = 0; //can never be zero since there must be wall here...
		Vector<Box> lBoxesList = new Vector<Box>();

		int lBoxesOnGoalCounter = 0;
		int lGoalIndexCounter = 1;


		for(int row=0; row<nbRows; row++){
			if (stringRepr.get(row).length() > lMaxNbOfCol) {
				lMaxNbOfCol = stringRepr.get(row).length();
			}
			for(int col=0; col<stringRepr.get(row).length(); col++){

				if(stringRepr.get(row).charAt(col) == ' '){ //Free space
					//Do nothing, this comes first since most common.
					matrixCells[row][col] = new Cell(row,col); //So don't make new cells at every srch.
				}
				else if(stringRepr.get(row).charAt(col) == '#'){ //Wall
					walls[row][col]=true; //All automatically false initially
				}
				else if(stringRepr.get(row).charAt(col) == '$'){ //Box
					matrixCells[row][col] = new Cell(row,col); //So don't make new cells at every srch.
					lBoxesList.add(new Box(row, col, false));
				}
				else if(stringRepr.get(row).charAt(col) == '.'){ //Goal
					matrixCells[row][col] = new Cell(row,col); //So don't make new cells at every srch.
					goals[row][col]=true; //All automatically false initially
					goalsIndexAtPos[row][col] = lGoalIndexCounter; //all zero initially
					goalsList.add(new Goal(row, col, false));
					lGoalIndexCounter++; //should start at 1 to be able to tell diff. from zero where ter is no goal.

				}
				else if(stringRepr.get(row).charAt(col) == '*'){ //Box on goal
					matrixCells[row][col] = new Cell(row,col); //So don't make new cells at every srch.
					goals[row][col]=true; //All automatically false initially
					goalsIndexAtPos[row][col] = lGoalIndexCounter;
					goalsList.add(new Goal(row, col, true)); //true means is occupied.
					lBoxesList.add(new Box(row, col, true)); //true means is on goal
					lBoxesOnGoalCounter++;
					lGoalIndexCounter++;
				}
				else if(stringRepr.get(row).charAt(col) == '@'){ //Sokoban Player
					matrixCells[row][col] = new Cell(row,col); //So don't make new cells at every srch.
					lPlayerStartRow = row;
					lPlayerStartCol = col;
				}
				else if(stringRepr.get(row).charAt(col) == '+'){ //Sokoban PLayer on Goal
					matrixCells[row][col] = new Cell(row,col); //So don't make new cells at every srch.
					lPlayerStartRow = row;
					lPlayerStartCol = col;
					goals[row][col]=true; //All automatically false initially
					goalsIndexAtPos[row][col] = lGoalIndexCounter;
					goalsList.add(new Goal(row, col, false));
					lGoalIndexCounter++;
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

		nbBoxes = lBoxesList.size();
		nbGoals = nbBoxes;

		if(Sokoban.debugMode){
			if(lPlayerStartRow == 0 || lPlayerStartCol == 0 ) System.out.println("/Board: NO PLAYER?");
			if (goalsList.size() != lBoxesList.size()){
				System.out.println("/Board: MapError: There is not an equal amout of boxes and goals!!");
				System.out.println("#goals: " + goalsList.size());
				System.out.println("#boxes: " + lBoxesList.size());
			}
		}

		//Pass on info about which goals are initially occupied to initial state, later updated by state itself.
		boolean[] lGoalsOccupied = new boolean[nbGoals];
		for (int goalIndex = 0; goalIndex < nbGoals; goalIndex++){
			if (goalsList.get(goalIndex).isOccupied()){
				lGoalsOccupied[goalIndex] = true;
			}
		}


		//Set dimension
		deadLocksT0 = new boolean[nbRows][nbCols];
		goalGrad = new int[nbBoxes][nbRows][nbCols];
		goalGradMerged = new int[nbRows][nbCols];
		goalWheight = new int[nbBoxes];

		initialState = new State(lBoxesList, lPlayerStartRow, lPlayerStartCol, lGoalsOccupied, lBoxesOnGoalCounter);


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
		// sum the gradients
		initialState.setGoalGradMerged(); //This needs the markDeadLocksAndGrad to be run..
		setGoalGradMerged(initialState);
		setGoalWeight();
		if(Sokoban.debugMode) Visualizer.printVector(goalWheight, "/Board: Goal weights:");	
		GoalCluster gc = new GoalCluster(gradCells);
	} // End constructor Map


	private void setGoalWeight(){
		int goalIndex = 0;
		for (Goal goal : goalsList){
			int nbOfGoalsCloseby = 0;
			// The coordinates, starting in the upper left corner in the 3 by 3 surrounding
			int upperLeftRow = goal.getRow() - 2;
			int upperLeftCol = goal.getCol() - 2;
			// Iterate through every row end every column
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					int curRow = upperLeftRow + i;
					int curCol = upperLeftCol + j;
					if (curRow > 0 && curCol >0 && curRow < nbRows && curCol < nbCols 
							&& isGoal(curRow, curCol)) {
						nbOfGoalsCloseby++;
					}
				}
			}// End check surrounding1
			
			upperLeftRow = goal.getRow() - 1;
			upperLeftCol = goal.getCol() - 1;
			// Iterate through every row end every column
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (isGoal(upperLeftRow + i, upperLeftCol + j)) {
						nbOfGoalsCloseby++;
					}
				}
			}// End check surrounding2
			goalWheight[goalIndex] = nbOfGoalsCloseby;
			goalIndex++;
		}
	}
		
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

		gradCells = new Vector<Vector<Cell>>();
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
			
			// Vector where the cells with gradient number up to two is saved.
			Vector<Cell> gradUpToTwoCells = new Vector<Cell>();
			gradUpToTwoCells.add(new Cell(goal.getRow(), goal.getCol(),0));
			
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
							//Also saves the closest cells:
							if(lMovesFromGoal<=2) 
								gradUpToTwoCells.add(new Cell(lBoxFromRow, lBoxFromCol, lMovesFromGoal));
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
							if(lMovesFromGoal<=2) 
								gradUpToTwoCells.add(new Cell(lBoxFromRow, lBoxFromCol, lMovesFromGoal));
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
							if(lMovesFromGoal<=2) 
								gradUpToTwoCells.add(new Cell(lBoxFromRow, lBoxFromCol, lMovesFromGoal));
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
							if(lMovesFromGoal<=2) 
								gradUpToTwoCells.add(new Cell(lBoxFromRow, lBoxFromCol, lMovesFromGoal));
						}
					}
				}//End if, check "Pushed left from right"
			}
			//Saves these cells to the vector gradCells that collects them.
			gradCells.add(gradUpToTwoCells);
			
		}//End for goal in goalsList

		// marks all deadlocks type 1, i.e. where the number is -1 for all goals gradients...
		for(int row = 0; row < nbRows; row++){
			for(int col = 0; col < nbCols; col++){
				boolean allDeadT1 = true;
				for(int goalIndex = 0; goalIndex < nbGoals; goalIndex++){
					if (goalGrad[goalIndex][row][col] != -1){
						allDeadT1 = false;
						break;
					}
				}
				if (allDeadT1){
					deadLocksT0[row][col]=true;
				}
			}
		}
	}

	/**
	 * Merge goalGrad
	 */
	private static void setAllGoalGradMerged() {
		// set high values
		for (int row = 0; row < nbRows; row++) {
			// for each col
			for (int col = 0; col < nbCols; col++) {
				goalGradMerged[row][col] = -1;
			}
		}

		// for every goal
		for (int i = 0; i < nbBoxes; i++) {
			// for each row
			for (int row = 0; row < nbRows; row++) {
				// for each col
				for (int col = 0; col < nbCols; col++) {
					// Choose the min value
					if (goalGradMerged[row][col] > goalGrad[i][row][col] && goalGrad[i][row][col] != -1)
						goalGradMerged[row][col] = goalGrad[i][row][col];
				}
			}
		}

		/* Print the board
        for (int row = 0; row < nbRows; row++) {
            // for each col
            System.out.println();
            for (int col = 0; col < nbCols; col++) {
                System.out.print(goalGradMerged[row][col]);

            }
        }
		 */
	}

	public static void setGoalGradMerged(State pState) {

		// set high values
		for (int row = 0; row < nbRows; row++) {
			// for each col
			for (int col = 0; col < nbCols; col++) {
				goalGradMerged[row][col] = -1;
			}
		}

		int value;
		// for each row
		for (int row = 0; row < nbRows; row++) {
			// for each col
			for (int col = 0; col < nbCols; col++) {
				// for every goal
				value = Integer.MAX_VALUE;
				for (int i = 0; i < nbBoxes; i++) {
					if (pState.isGoalOccupied(i)) continue;
					// Choose the min value
					if (value > goalGrad[i][row][col] && goalGrad[i][row][col] != -1) {
						goalGradMerged[row][col] = goalGrad[i][row][col];
						value = goalGrad[i][row][col];
					}
				}
			}
		}

		/* Print the board
        for (int row = 0; row < nbRows; row++) {
            // for each col
            System.out.println();
            for (int col = 0; col < nbCols; col++) {
                System.out.print(goalGradMerged[row][col]);

            }
        }
		 */
	}


	public static State getInitialState(){
		return initialState;
	}

	public static boolean isWall(int pRow, int pCol){
		return walls[pRow][pCol]; 
	}
	
	public static boolean isWall(Cell pCell){
		return walls[pCell.getRow()][pCell.getCol()]; 
	}

	public static boolean isGoal(int pRow, int pCol){
		return goals[pRow][pCol];
	}
	
	public static boolean isGoal(Cell pCell){
		return goals[pCell.getRow()][pCell.getCol()];
	}

	public static int getGoalIndexAt(int pRow, int pCol){
		return goalsIndexAtPos[pRow][pCol] - 1; 
	}


	public static boolean isDeadLockT0(int pRow, int pCol){
		return deadLocksT0[pRow][pCol];
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

	/*
	 * Should not be needed?
	 */
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

	public static int getGoalGradMerged(int pRow, int pCol) {
		return goalGradMerged[pRow][pCol];
	}
	public static int[][] getGoalGradMerged() {
		return goalGradMerged;
	}

	public static int getNbOfBoxes(){
		return nbBoxes;
	}
	public static int getNbOfGoals(){
		return nbBoxes; //should be boxes it is the same!
	}
} // End Class Board
