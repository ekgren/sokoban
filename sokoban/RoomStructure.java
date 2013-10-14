/*
 * Labels each cell in a board matrix 
 * 
 */

package sokoban;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public class RoomStructure {
	
	private static RoomStructureCell[][] roomCells;
	
	RoomStructure(State pInitialState){
		//Initiate all cells:
		roomCells = new RoomStructureCell[Board.getNbRows()][Board.getNbCols()];
		for(RoomStructureCell[] row : roomCells){
			for(RoomStructureCell cell : row){
				cell = new RoomStructureCell(false); //not visited in search
			}
		}
	}
	
	private void setRoomStructure(State pInitialState){
		/*
		 * TODO
		 * expand all cells from all box(i) without crossing or including any goals
		 * 
		 */

		int RoomNbCounter = 0;
		for(Box box : pInitialState.getBoxes()){

			Vector<RoomStructureCell> thisRoomCells = new Vector<RoomStructureCell>();
			Queue<RoomStructureCell> notExpanded = new LinkedList<RoomStructureCell>();
			RoomStructureCell startBoxCell = roomCells[box.getRow()][box.getCol()];
			
			if(startBoxCell.isVisitedInSearch()){
				break; //continue to next box! DON'T update RoomNbCounter!
			}

			startBoxCell.setIsVisitedInSearch();
			
			//Start bredden first:
			notExpanded.add(startBoxCell);
			Vector<RoomStructureCell> tempVector = new Vector<RoomStructureCell>();
			while(notExpanded.size() > 0){
				thisRoomCells.add(notExpanded.peek());
				tempVector.clear();
				tempVector.add(notExpanded.poll());
				allValidChilds(tempVector);
				for(RoomStructureCell cell : tempVector){
					notExpanded.add(cell);
				}
			}
			//All reachable cells are added to thisRoomCells.
			for(RoomStructureCell cell : thisRoomCells){
				cell.setRoomNb(RoomNbCounter);
			}
		
			RoomNbCounter++;
		}
	}
	
	/**
	 * Takes a vector with one cell as input and removes it and add all childs
	 * that are valid, including that they have not been visited...
	 * these are ponters from the matrix with all cells....
	 */
	private void allValidChilds(Vector<RoomStructureCell> pCellVector){
		
	}
	
}


class RoomStructureCell{
	private int roomNb;
	private boolean isRoomBoundary;
	private boolean isVisitedInSearch;
	private int room1;
	private int room2;


	public RoomStructureCell(int pRoomNb, boolean pIsRoomBoundary, boolean pIsVisitedInSearch) {
		this.roomNb = pRoomNb;
		this.isRoomBoundary = pIsRoomBoundary;
		this.isVisitedInSearch = pIsVisitedInSearch;
	}

	public RoomStructureCell(boolean pIsVisitedInSearch) {
		this.isVisitedInSearch = pIsVisitedInSearch;
		this.roomNb = -1; //Have ont been set!
	}
	
	public void setRoomNb(int pRoomNb){
		this.roomNb = pRoomNb;
	}
	public int getRoomNb(){
		return roomNb;
	}
	public boolean isRoomBoundary(){
		return isRoomBoundary;
	}
	public boolean isVisitedInSearch(){
		return isVisitedInSearch;
	}
	public void setIsVisitedInSearch(){
		isVisitedInSearch = true;
	}
}

