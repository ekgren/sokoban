/*
 * Solver
 * 
 * Version 0.1
 * 
 * 2013.09.19
 *
 */

import java.util.*;

public class Solver {

	Map map;
	HashSet<State> visitedStates = new HashSet<State>();
	
	Solver(Map pMap){
		
		map = pMap;
		
		/*
		 *TODO
		 *
		 * in the Map-object is everything needed; constant parameters from the
		 * Map object it self and the initial state which should be expanded;
		 *  
		 */
	}
	
	public String solutionPath(){
	
		Vector<State> successorStates= new Vector<State>();
		map.allSuccessors(successorStates); // the vector should now include all successor states...
		
		/*
		 * TODO for sure...	
		 */
		return "test: UuullL";
	}
	
	private void heuristic(){
		/*
		 * 
		 * TODO
		 * 
		 * Might of course not be a void function...
		 */
	}
	
}
