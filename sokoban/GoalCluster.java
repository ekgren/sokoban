/*
 * block T1: can only be reached from 1 direction and if a nother goal i occupied this would not be possible...
 * block T2: not a front goal, all goals that are in the "five cross" are block T1
 * block T3: not a front goal, all goals on other "twos" are block T2
 */

package sokoban;

import java.util.Vector;


public class GoalCluster {

	public static int[][] clusterMatrix;
	Vector<Vector<Cell>> gradCells;
	Cell refCell;
	
	/**
	 * Constructor sets the goalCluster matrix. 
	 * Takes the vector with all gradCell collections as parameter.
	 * @param pGradCells
	 */
	public GoalCluster(Vector<Vector<Cell>> pGradCells){
		gradCells = pGradCells;
		clusterMatrix = new int[Board.getNbOfGoals()][Board.getNbOfGoals()];
		
		//TEST:
		setClusterMatrix();
		Visualizer.printMatrixWithLeg(clusterMatrix, "/GoalCluster: ClusterMatrix!");
		
	}
	
	/**
	 * Sets the cluster matrix representing the internal dependencies between all goals.
	 * Before this is done the gradients must be computed 
	 * 
	 */
	public void setClusterMatrix(){			
		for(int goalIndex = 0; goalIndex < Board.getNbOfGoals(); goalIndex++){
			markClusterMatrixForGoal(goalIndex);
		}
	}

	/**
	 * Marks on row in the clusterMatrix.
	 * @param goalIndex
	 */
	private void markClusterMatrixForGoal(int goalIndex){
		int thisGoalRow = gradCells.get(goalIndex).get(0).getRow();
		int thisGoalCol = gradCells.get(goalIndex).get(0).getCol();
		Vector<Cell> ones = new Vector<Cell>();
		Vector<Cell> twos = new Vector<Cell>();
		for(Cell cell: gradCells.get(goalIndex)){
			if(cell.getExpansionLevel() == 1) ones.add(cell);
			if(cell.getExpansionLevel() == 2) twos.add(cell); 
		}
		
		boolean isFrontGoal = false;
		//Check all "ones":
		for(Cell cell_1 : ones){
			if(!Board.isGoal(cell_1.getRow(), cell_1.getCol())){
				//if no goal on one of the "ones" than we must check that there is a two beyond it that is free
				int checkRow = thisGoalRow + 2*(cell_1.getRow()-thisGoalRow); //if on same row than same row...
				int checkCol = thisGoalCol + 2*(cell_1.getCol()-thisGoalCol); //if on same row than same row...
				//if there is no goal in line with that one AND that is a "two" it is a front goal.
				for(Cell cell_2 : twos){
					if( cell_2.getRow() == checkRow 
							&& cell_2.getCol() == checkCol 
							&& !Board.isGoal(checkRow, checkCol) ){
						//There is a free "2" in line with a free "1" which means the goal is a front goal!
						isFrontGoal = true;
						break;
					}
				}
				if(isFrontGoal) break;
			}
		}//End for all goal_1, done checking if the goal is a front goal.
		
		
		//If not a front goal we start checking block types.
		if (!isFrontGoal){
						
			//check and mark for blocks T1:
			if(ones.size() == 1){
				Cell cell_1 = ones.firstElement();
				//check if there is goal on this one:
				if(Board.isGoal(cell_1.getRow(), cell_1.getCol())){
					clusterMatrix[goalIndex][Board.getGoalIndexAt(cell_1.getRow(), cell_1.getCol())] = 1;
				}
				//also check if there is a goal beyond in Line.
				int checkRow = thisGoalRow + 2*(cell_1.getRow()-thisGoalRow); //if on same row than same row...
				int checkCol = thisGoalCol + 2*(cell_1.getCol()-thisGoalCol); //if on same row than same col...
				if(Board.isGoal(checkRow, checkCol)){
					clusterMatrix[goalIndex][Board.getGoalIndexAt(checkRow, checkCol)] = 1;
				}

			}//End if(ones.size == 1)
			else{
				//mark all blocks T2 and T3;
				for(Cell cell_1 : ones){
					//All ones with goals are T2
					if(Board.isGoal(cell_1.getRow(), cell_1.getCol())){
						clusterMatrix[goalIndex][Board.getGoalIndexAt(cell_1.getRow(), cell_1.getCol())] = 2;
					}
					//all cells in line with ones and with goals are T2
					int checkRow = thisGoalRow + 2*(cell_1.getRow()-thisGoalRow); //if on same row than same row...
					int checkCol = thisGoalCol + 2*(cell_1.getCol()-thisGoalCol); //if on same row than same col...
					if(Board.isGoal(checkRow, checkCol)){
						clusterMatrix[goalIndex][Board.getGoalIndexAt(checkRow, checkCol)] = 2;
					}
				}
				for(Cell cell_2 : twos){
					if(Board.isGoal(cell_2.getRow(), cell_2.getCol())){
						// "twos" not in line are considered T3 (all in line allread marked...)
						if(cell_2.getRow() != thisGoalRow && cell_2.getCol() != thisGoalCol){
							clusterMatrix[goalIndex][Board.getGoalIndexAt(cell_2.getRow(), cell_2.getCol())] = 3;
						}
					}
				}
			}//Else: if not ones.size == 1;
		}//End if(not frontGoal)
	}//End Method: markClusterMatrixForGoal.
	
	
    /**
     * Returns cell representing the adjacent position represented by the letters...
     */
    public Cell getAdjPos(Goal pGoal, char pDir){

        switch (pDir) {
            // C = center = current box position
            case 'C':
                refCell.setPos(pGoal.getRow(), pGoal.getCol());
            case 'U':
                refCell.setPos(pGoal.getRow() - 1, pGoal.getCol());
            case 'D':
                refCell.setPos(pGoal.getRow() + 1, pGoal.getCol());
            case 'R':
                refCell.setPos(pGoal.getRow(), pGoal.getCol() + 1);
            case 'L':
                refCell.setPos(pGoal.getRow(), pGoal.getCol() - 1);
                
        }
        return refCell;
    }
    
    /**
     * Returns cell representing the adjacent position represented by the letters...
     * same as above but for two
     * @param pGoal
     * @param pDir1
     * @param pDir2
     * @return
     */
    public Cell getAdjPos(Goal pGoal, char pDir1, char pDir2){

    	//set position relative goal
    	switch (pDir1) {
    	// C = center = current box position
	    	case 'C':
	    		refCell.setPos(pGoal.getRow(), pGoal.getCol());
	    	case 'U':
	    		refCell.setPos(pGoal.getRow() - 1, pGoal.getCol());
	    	case 'D':
	    		refCell.setPos(pGoal.getRow() + 1, pGoal.getCol());
	    	case 'R':
	    		refCell.setPos(pGoal.getRow(), pGoal.getCol() + 1);
	    	case 'L':
	    		refCell.setPos(pGoal.getRow(), pGoal.getCol() - 1);

    	}
        
    	// move relative its own position one more time.
        switch (pDir2) {
        // C = center = current box position
	        case 'C':
	        	//Dont move
	        case 'U':
	        	refCell.setPos(refCell.getRow() - 1, refCell.getCol());
	        case 'D':
	        	refCell.setPos(refCell.getRow() + 1, refCell.getCol());
	        case 'R':
	        	refCell.setPos(refCell.getRow(), refCell.getCol() + 1);
	        case 'L':
	        	refCell.setPos(refCell.getRow(), refCell.getCol() - 1);
        }
        return refCell;
    }
	
	public static int getClusterMatrixValue(int pGoalIndexRow, int pGoalIndexCol){
		return clusterMatrix[pGoalIndexRow][pGoalIndexCol];
	}
}

