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
		 * in the Board-object is everything needed; constant parameters from the
		 * Board object it self and the initial state which should be expanded;
		 *  
		 */
	}
	
	/* Must check if this hash function is a good one!*/ 
	public static String getHashString(int row,int col){
		
		return  String.valueOf(row) + String.valueOf(col);
	}
	
	public static String strPath(Cell pCell){
		
		if(pCell.getRow() == pCell.getParent().getRow()){
			if(pCell.getCol() < pCell.getParent().getCol()){
				return "U";
			}
			else{
				return "D";
			}
		}
		else{
			if(pCell.getRow() < pCell.getParent().getRow()){
				return "L";
			}
			else{
				return "R";
			}
		}
	}
	
	/**
	 * Returns cell with position next to box.
	 * @param state
	 * @param pRow
	 * @param pCol
	 * @param pRowBox
	 * @param pColBox
	 * @return
	 */
	public static Cell cellNeighboorToBox(State state ,
			int pRow,int pCol,int pRowBox,int pColBox){
		
		Cell pStartCell = new Cell(pRow,pCol);
		Cell pEndCell = new Cell(pRowBox,pColBox);
		
		boolean bolFinished = false;
		Cell lCellNeighboor = null;
		
        Comparator<Cell> comparator = new Cell.NormComparator(
        		pEndCell.getRow(),
        		pEndCell.getCol());
		HashSet<String> lSetPrPaths = new HashSet<String>();
        PriorityQueue<Cell> lQueChilds = new PriorityQueue<Cell>(10,comparator);
        
        lQueChilds.add(pStartCell);
		lSetPrPaths.add(getHashString(pStartCell.getRow(),pStartCell.getCol()));
		
		while(!lQueChilds.isEmpty() &! bolFinished){
			Cell lCellChild = lQueChilds.remove();
			int lRow = lCellChild.getRow();
			int lCol = lCellChild.getCol();
			int incInt = -1;

			while(incInt <= 1){
				int lRowChild = lRow + incInt;
				String hashIndex1 = getHashString(lRowChild,lCol);
				
				if(!lSetPrPaths.contains(hashIndex1)){
					lSetPrPaths.add(hashIndex1);
			
					if(!Board.isWall(lRowChild, lCol)){
						
						if(!state.isBox(lRowChild,lCol)){
							lQueChilds.add(new Cell(lRowChild,lCol));
						}
						else if(lRowChild == pEndCell.getRow() &&
								lCol == pEndCell.getCol()){
							lCellNeighboor = lCellChild;
							bolFinished = true;
							break;
						}
					}
				}
				int lColChild = lCol + incInt;
				String hashIndex2 = getHashString(lRow,lColChild);
				
				if(!lSetPrPaths.contains(hashIndex2)){
					lSetPrPaths.add(hashIndex2);	
					
					if(!Board.isWall(lRow, lColChild)){
						
						if(!state.isBox(lRow,lColChild)){
							lQueChilds.add(new Cell(lRow,lColChild));
						}
						else if(lRow == pEndCell.getRow() &&
								lColChild == pEndCell.getCol()){
							lCellNeighboor = lCellChild;
							bolFinished = true;
							break;
						}
					}
				}
			incInt = incInt + 2;	
			}
		}
		return lCellNeighboor;
	}
	
	/**
	 * Returns linked cell with position next to box.
	 * @param state
	 * @param pRow
	 * @param pCol
	 * @param pRowBox
	 * @param pColBox
	 * @return
	 */
	public static Cell cellLinkedNeighboorToBox(State state ,
			int pRow,int pCol,int pRowBox,int pColBox){

		Cell pStartCell = new Cell(pRow,pCol);
		Cell pEndCell = new Cell(pRowBox,pColBox);
		boolean bolFinished = false;
		Cell lCellNeighboor = null;
		
        Comparator<Cell> comparator = new Cell.NormComparator(
        		pEndCell.getRow(),
        		pEndCell.getCol());
		HashSet<String> lSetPrPaths = new HashSet<String>();
        PriorityQueue<Cell> lQueChilds = new PriorityQueue<Cell>(10,comparator);
        
        lQueChilds.add(pStartCell);
		lSetPrPaths.add(getHashString(pStartCell.getRow(),pStartCell.getCol()));
		
		while(!lQueChilds.isEmpty() &! bolFinished){
			Cell lCellChild = lQueChilds.remove();
			int lRow = lCellChild.getRow();
			int lCol = lCellChild.getCol();
			int incInt = -1;

			while(incInt <= 1){
				int lRowChild = lRow + incInt;
				String hashIndex1 = getHashString(lRowChild,lCol);
				
				if(!lSetPrPaths.contains(hashIndex1)){
					lSetPrPaths.add(hashIndex1);
			
					if(!Board.isWall(lRowChild, lCol)){
						
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
				String hashIndex2 = getHashString(lRow,lColChild);
				
				if(!lSetPrPaths.contains(hashIndex2)){
					lSetPrPaths.add(hashIndex2);	
					
					if(!Board.isWall(lRow, lColChild)){
						
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
	/**
	 * Returns cell with position next to some position.
	 * @param state
	 * @param pRow
	 * @param pCol
	 * @param pRowBox
	 * @param pColBox
	 * @return
	 */
	public static Cell cellLinkedNeighboorToPath(State pState ,
			int pRow,int pCol,int pRowPath,int pColPath){

		Cell pStartCell = new Cell(pRow,pCol);
		Cell pEndCell = new Cell(pRowPath,pColPath);
		boolean bolFinished = false;
		Cell lCellNeighboor = null;
		
        Comparator<Cell> comparator = new Cell.NormComparator(
        		pEndCell.getRow(),
        		pEndCell.getCol());
		HashSet<String> lSetPrPaths = new HashSet<String>();
        PriorityQueue<Cell> lQueChilds = new PriorityQueue<Cell>(10,comparator);
        
        lQueChilds.add(pStartCell);
		lSetPrPaths.add(getHashString(pStartCell.getRow(),pStartCell.getCol()));
		
		while(!lQueChilds.isEmpty() &! bolFinished){
			Cell lCellChild = lQueChilds.remove();
			int lRow = lCellChild.getRow();
			int lCol = lCellChild.getCol();
			int incInt = -1;

			while(incInt <= 1){
				int lRowChild = lRow + incInt;
				String hashIndex1 = getHashString(lRowChild,lCol);
				
				if(!lSetPrPaths.contains(hashIndex1)){
					lSetPrPaths.add(hashIndex1);
			
					if(!Board.isFree(pState,lRowChild, lCol)){
						
						if(lRowChild == pEndCell.getRow() &&
								lCol == pEndCell.getCol()){
							lCellNeighboor = lCellChild;
							bolFinished = true;
						}
						else{
							lQueChilds.add(new Cell(lCellChild,lRowChild,lCol));
						}	
					}
				}
				int lColChild = lCol + incInt;
				String hashIndex2 = getHashString(lRow,lColChild);
				
				if(!lSetPrPaths.contains(hashIndex2)){
					lSetPrPaths.add(hashIndex2);
			
					if(!Board.isFree(pState,lRow, lColChild)){
						
						if(lRow == pEndCell.getRow() &&
								lColChild == pEndCell.getCol()){
							lCellNeighboor = lCellChild;
							bolFinished = true;
						}
						else{
							lQueChilds.add(new Cell(lCellChild,lRow,lColChild));
						}	
					}
				}
			incInt = incInt + 2;	
			}
		}
		return lCellNeighboor;
	}
	
	public static Cell cellNeighboorToPath(State pState ,
			int pRow,int pCol,int pRowPath,int pColPath){

		Cell pStartCell = new Cell(pRow,pCol);
		Cell pEndCell = new Cell(pRowPath,pColPath);
		boolean bolFinished = false;
		Cell lCellNeighboor = null;
		
        Comparator<Cell> comparator = new Cell.NormComparator(
        		pEndCell.getRow(),
        		pEndCell.getCol());
		HashSet<String> lSetPrPaths = new HashSet<String>();
        PriorityQueue<Cell> lQueChilds = new PriorityQueue<Cell>(10,comparator);
        
        lQueChilds.add(pStartCell);
		lSetPrPaths.add(getHashString(pStartCell.getRow(),pStartCell.getCol()));
		
		while(!lQueChilds.isEmpty() &! bolFinished){
			Cell lCellChild = lQueChilds.remove();
			int lRow = lCellChild.getRow();
			int lCol = lCellChild.getCol();
			int incInt = -1;

			while(incInt <= 1){
				int lRowChild = lRow + incInt;
				String hashIndex1 = getHashString(lRowChild,lCol);
				
				if(!lSetPrPaths.contains(hashIndex1)){
					lSetPrPaths.add(hashIndex1);
			
					if(!Board.isFree(pState,lRowChild, lCol)){
						
						if(lRowChild == pEndCell.getRow() &&
								lCol == pEndCell.getCol()){
							lCellNeighboor = lCellChild;
							bolFinished = true;
						}
						else{
							lQueChilds.add(new Cell(lRowChild,lCol));
						}	
					}
				}
				int lColChild = lCol + incInt;
				String hashIndex2 = getHashString(lRow,lColChild);
				
				if(!lSetPrPaths.contains(hashIndex2)){
					lSetPrPaths.add(hashIndex2);
			
					if(!Board.isFree(pState,lRow, lColChild)){
						
						if(lRow == pEndCell.getRow() &&
								lColChild == pEndCell.getCol()){
							lCellNeighboor = lCellChild;
							bolFinished = true;
						}
						else{
							lQueChilds.add(new Cell(lRow,lColChild));
						}	
					}
				}
			incInt = incInt + 2;	
			}
		}
		return lCellNeighboor;
	}

	
	/**
	 * Returns true if there is path to box
	 * @param state
	 * @param pStartCell
	 * @param pEndCell
	 * @return
	 */
	public static boolean isPathToBox(State pState ,int pRow,int pCol,int pRowBox,
			int pColBox){
		Cell lCell = cellNeighboorToBox(pState,pRow,pCol,pRowBox,pColBox);
		return (lCell != null);
	}
	
	/**
	 * Returns true if there is path to some position.
	 * @param pState
	 * @param pRow
	 * @param pCol
	 * @param pRowPath
	 * @param pColPath
	 * @return
	 */
	public static boolean isPathToPath(State pState,int pRow,int pCol,int pRowPath,
			int pColPath){
		Cell lCell = cellNeighboorToPath(pState,pRow,pCol,pRowPath,pColPath);
		return (lCell != null);
	}
	
	public String solutionPath(){
	
		Vector<State> successorStates= new Vector<State>();
		Board.getInitialState().allSuccessors(successorStates); // the vector should now include all successor states...
		
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
