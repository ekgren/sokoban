package sokoban;
import java.util.Vector;

/*
 * Visualizer
 * 
 * Contains methods for all kinds of visualization during development
 */

public class Visualizer {
	
	private static Vector<String> stringRepr; //string Representation of Map
	private static Vector<String> cleanMapStringRepr = new Vector<String>(); //string Representation of Map

	public Visualizer(){
		
		this.stringRepr = Board.getStringRepr();
		
		//Fix the cleanMapString:		
		for(String lRow : stringRepr){
			String lStringCopy = lRow;
			
			lStringCopy = lStringCopy.replace('$', ' '); //Box to nothing
			lStringCopy = lStringCopy.replace('*', '.'); //Box on goal to Goal
			lStringCopy = lStringCopy.replace('@', ' '); //Player to nothing
			lStringCopy = lStringCopy.replace('+', '.'); //Player on goal to Goal
			
			cleanMapStringRepr.add(lStringCopy);
		}
	}
	
	public static void printCleanMap(int pReferensGoalIndex){
		
		System.out.println("");
		System.out.println("Clean Map String representation:");
		for (String row : cleanMapStringRepr){
			System.out.println(row);
		}
	}
	
	public static void printOriginalMap(int pReferensGoalIndex){
		
		System.out.println("");
		System.out.println("Original String representation:");
		for (String row : stringRepr){
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
		
		
	}
	
	
}
