package sokoban;
import java.util.Queue;
import java.util.Vector;

/*
 * Visualizer
 * 
 * Contains methods for all kinds of visualization during development
 */

public class Visualizer {
	
	private static Vector<StringBuilder> stringRepr = new Vector<StringBuilder>(); //string Representation of Map
	private static Vector<StringBuilder> cleanMapStringRepr = new Vector<StringBuilder>(); //string Representation of Map

	public Visualizer(){
		
		//Fix the cleanMapString:		
		for(String lRow : Board.getStringRepr()){
			String lStringCopy = lRow;
			stringRepr.add(new StringBuilder(lStringCopy));
			
			lStringCopy = lStringCopy.replace('$', ' '); //Box to nothing
			lStringCopy = lStringCopy.replace('*', '.'); //Box on goal to Goal
			lStringCopy = lStringCopy.replace('@', ' '); //Player to nothing
			lStringCopy = lStringCopy.replace('+', '.'); //Player on goal to Goal
			
			cleanMapStringRepr.add(new StringBuilder(lStringCopy));
		}
	}
	
	public static void printCleanMap(int pReferensGoalIndex){
		
		if (Sokoban.debugMode){ System.out.println("");
			System.out.println("Clean Map String representation:");
			for (StringBuilder row : cleanMapStringRepr){
				System.out.println(row);
			}
		}
	}
	
	public static void printOriginalMap(int pReferensGoalIndex){
		if (Sokoban.debugMode) {
			System.out.println("");
			System.out.println("Original String representation:");
			for (StringBuilder row : stringRepr){
				System.out.println(row);
			}
		}
	}
	
	/**
	 * Prints goal gradient Board with reference to pReferensGoalIndex
	 * @param pReferensGoalIndex
	 */
	public static void printGoalGrad(int pReferensGoalIndex){
		
		int lNbRows = Board.getNbRows();
		int lNbCols = Board.getNbCols();
		
		System.out.println("");
		System.out.println("Goal gradient:");
		for (int row = 0; row<lNbRows; row++){
			for (int col = 0; col<lNbCols; col++){				
				if(col<cleanMapStringRepr.get(row).length()){

					char lCharAt = cleanMapStringRepr.get(row).charAt(col);
					if (lCharAt == '#'){
						System.out.print(lCharAt);
					}
					else if (Board.getGoalGrad(pReferensGoalIndex, row, col) != -1){
						System.out.print(Board.getGoalGrad(pReferensGoalIndex, row, col));
					}
					else if(col<cleanMapStringRepr.get(row).length()){ //might be shorter than nbCols!
						System.out.print("-");//cleanMapStringRepr.get(row).charAt(col));
					}
				}
			}//End for columns	
			System.out.println(" ");
		}//End for rows
	}
	
	public static void printState(State pState, String pLabel){
        Vector<StringBuilder> lBoardPrint = new Vector<StringBuilder>();
        
        for(StringBuilder row : cleanMapStringRepr){ //must copy each element separately!
        	lBoardPrint.add(new StringBuilder(row.toString()));
        }
        

        System.out.println("");
		System.out.println(pLabel);
		
        // insert player position
        int lPlayerRow = pState.getPlayerRow();
        int lPlayerCol = pState.getPlayerCol();

        if (Board.isGoal(lPlayerRow, lPlayerCol))
        	//board.elementAt(processNode.y).setCharAt(processNode.x, 'O');
            lBoardPrint.elementAt(lPlayerRow).setCharAt(lPlayerCol, '+');
        else
        	lBoardPrint.elementAt(lPlayerRow).setCharAt(lPlayerCol, '@');

        // insert boxes
        for (Box box : pState.getBoxes()) {        	
        	if (box.isOnGoal())
                lBoardPrint.elementAt(box.getRow()).setCharAt(box.getCol(), '*');
            else
                lBoardPrint.elementAt(box.getRow()).setCharAt(box.getCol(), '$');
        }
        for(StringBuilder s : lBoardPrint){
        	System.out.println(s);
        }
    }
	
	public static void printState(State pState){
		printState(pState, "State with no label:");
	}

	public static void printStateDelux(State pState, String pLabel){
		printState(pState, pLabel);
		System.out.println("#Boxes on goal: " + pState.nbOfBoxesOnGoal);
		System.out.print("Goal index occ.: ");
		printVector(pState.goalsOccupied," ");

	}
	
	public static void printStatesInQueue(Queue pQueue){
		Object[] states = pQueue.toArray();
		for(Object state : states){
			printState((State) state);
		}
	}
	
	public static void printMatrix(int[][] pObject, String pLabel){
		System.out.println();
		System.out.println(pLabel);
		for( int row=0; row < pObject.length; row++){
			for( int col=0; col < pObject[row].length; col++){
				System.out.print(pObject[row][col]);
			}
			System.out.println();
		}
	}
	public static void printMatrix(boolean[][] pObject, String pLabel){
		System.out.println();
		System.out.println(pLabel);
		for( int row=0; row < pObject.length; row++){
			for( int col=0; col < pObject[row].length; col++){
				if(pObject[row][col]){
					System.out.print("t");
				}
				else{
					System.out.print("f");
				}
			}
			System.out.println();
		}
	}
	
	public static void printVector(int[] pObject, String pLabel){
		System.out.println(pLabel);
		for( int col=0; col < pObject.length; col++){
				System.out.print(pObject[col] +" ");
		}
		System.out.println("");
	}
	
	public static void printVector(boolean[] pObject, String pLabel){
		System.out.println(pLabel);
		for( int col=0; col < pObject.length; col++){
			if(pObject[col]){
				System.out.print("t ");
			}
			else{
				System.out.print("f ");
			}
		}
		System.out.println("");
	}
	
}
