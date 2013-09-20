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

	HashSet<State> visitedStates = new HashSet<State>();
	
	Solver(){
		
		/*
		 *TODO
		 *
		 * in the Map-object is everything needed; constant parameters from the
		 * Map object it self and the initial state which should be expanded;
		 *  
		 */
	}
	public static String getHashIndex(int row,int col){
		
		return  String.valueOf(row) + String.valueOf(col);
	}
	// For searching to boxes only!
	public static Cell cellNeighboor(State state ,
			Cell pStartCell,Cell pEndCell){

		boolean bolFinished = false;
		Cell lCellNeighboor = null;
		
        Comparator<Cell> comparator = new Cell.NormComparator(
        		pEndCell.getRow(),
        		pEndCell.getCol());
		HashSet<String> lSetPrPaths = new HashSet<String>();
        PriorityQueue<Cell> lQueChilds = new PriorityQueue<Cell>(10,comparator);
        
        lQueChilds.add(pStartCell);
		lSetPrPaths.add(getHashIndex(pStartCell.getRow(),pStartCell.getCol()));
		
		while(!lQueChilds.isEmpty() &! bolFinished){
			Cell lCellChild = lQueChilds.remove();
			int lRow = lCellChild.getRow();
			int lCol = lCellChild.getCol();
			int incInt = -1;

			while(incInt <= 1){
				int lRowChild = lRow + incInt;
				String hashIndex = getHashIndex(lRowChild,lCol);
				
				if(!lSetPrPaths.contains(hashIndex)){
					lSetPrPaths.add(hashIndex);
			
					if(!Map.isWall(lRowChild, lCol)){
						
						if(!state.isBox(lRowChild,lCol)){
							lQueChilds.add(new Cell(lCellChild,lRowChild,lCol));
						}
						else if(lRowChild == pEndCell.getRow() &&
								lCol == pEndCell.getCol()){
							lCellNeighboor = new Cell(lCellChild,lRowChild,lCol);
							bolFinished = true;
							break;
						}
					}
				}
				int lColChild = lCol + incInt;
				String hashIndex1 = getHashIndex(lRow,lColChild);
				
				if(!lSetPrPaths.contains(hashIndex1)){
					lSetPrPaths.add(hashIndex1);	
					
					if(!Map.isWall(lRow, lColChild)){
						
						if(!state.isBox(lRow,lColChild)){
							lQueChilds.add(new Cell(lCellChild,lRow,lColChild));
						}
						else if(lRow == pEndCell.getRow() &&
								lColChild == pEndCell.getCol()){
							lCellNeighboor = new Cell(lCellChild,lRow,lColChild);
							bolFinished = true;
							break;
						}
					}
				}
			incInt = incInt + 2;	
			}
		}
		if(lCellNeighboor != null){
			return lCellNeighboor.getParent();
		}
		else{
			return lCellNeighboor;
		}
	}
	
	public static boolean isPathToBox(State state ,Cell pStartCell,Cell pEndCell){
		
		Cell lCell = cellNeighboor(state,pStartCell,pEndCell);
		if(lCell != null){
			return true;
		}
		else{
			return false;
		}
	}
	
	public String solutionPath(){
	
		Vector<State> successorStates= new Vector<State>();
		Map.getInitialState().allSuccessors(successorStates); // the vector should now include all successor states...
		
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
