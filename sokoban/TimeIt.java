package sokoban;
/**
 * NEO-SOKOBAN TIMEIT CLASS.
 * 
 * Class to measure and store execution times.
 * 
 * @author Ariel
 *
 */

public class TimeIt {	
	public static long createStateTotal;
	public static long createState;
	
	public static long heuristicTotal;
	public static long heuristic;
	
	public static long createAllChildrenTotal;
	public static long createAllChildren;
	
	public static long createNewStateTotal;
	public static long createNewState;
	
	public static long boxMoveAllowedTotal;
	public static long boxMoveAllowed;
	
	public static long stateEqualsTotal;
	public static long stateEquals;
	
	public static long deadlock1Total;
	public static long deadlock1;
	
	public static long deadlock2Total;
	public static long deadlock2;
	
	public static long deadlock3Total;
	public static long deadlock3;
	
	public static long searchTimeTotal;
	public static long searchTime;
	
	public static long stateHashCodeTotal;
	public static long stateHashCode;
	
	public static void print(){
        System.err.println("\n--- TIMES ---");
        System.err.println("Create children  : " + createAllChildrenTotal + " ms");
        System.err.println("Heuristic        : " + heuristicTotal + " ms");
        System.err.println("State hashcode   : " + stateHashCodeTotal + " ms");
        System.err.println("State equals     : " + stateEqualsTotal + " ms");
        System.err.println("Create new states: " + createNewStateTotal + " ms");
        System.err.println("Check if box move: " + boxMoveAllowedTotal + " ms");
        System.err.println("Deadlock 1       : " + deadlock1Total + " ms");
        System.err.println("Deadlock 2       : " + deadlock2Total + " ms");
        System.err.println("Deadlock 3       : " + deadlock3Total + " ms");
	}
}
