package sokoban;
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
		
		System.out.println("");
		System.out.println("Clean Map String representation:");
		for (StringBuilder row : cleanMapStringRepr){
			System.out.println(row);
		}
	}
	
	public static void printOriginalMap(int pReferensGoalIndex){
		
		System.out.println("");
		System.out.println("Original String representation:");
		for (StringBuilder row : stringRepr){
			System.out.println(row);
		}
	}
	
	/**
	 * Prints goal gradient Board with reference to pReferensGoalIndex
	 * @param pReferensGoalIndex
	 */
	public static void printGoalGrad(int pReferensGoalIndex){
		
		int lNbRows = Board.getNbRows();
		int lNbCols = Board.getNbCols();
		int[][][] lGoalGrad = Board.getGoalGrad();
		
		System.out.println("");
		System.out.println("Goal gradient:");
		for (int row = 0; row<lNbRows; row++){
			for (int col = 0; col<lNbCols; col++){				
				if (lGoalGrad[pReferensGoalIndex][row][col] != -1){
					System.out.print(lGoalGrad[pReferensGoalIndex][row][col]);
				}
				else if(col<cleanMapStringRepr.get(row).length()){ //might be shorter than nbCols!
					System.out.print(cleanMapStringRepr.get(row).charAt(col));
				}
			}//End for columns	
			System.out.println(" ");
		}//End for rows
	}
	
	public static void printState(State pState){
        Vector<StringBuilder> lBoardPrint = stringRepr;
		System.out.println("");
		System.out.println("Print state:");

        // insert player position
        int lPlayerRow = pState.getPlayerCol();
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
	
	
}
