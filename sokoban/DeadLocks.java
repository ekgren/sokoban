package sokoban;

//import java.awt.Point;
import java.util.HashSet;

/**
 * DEADLOCK CLASS FROM NEO-SOKO. 
 *
 */
public class DeadLocks {
	
	public static String hashString(int pRow,int pCol) {
		//Not most efficient way and has not been confirmed to work!
		return String.valueOf(pRow) + String.valueOf(pCol);
	}
	
	private static HashSet<Box> setBoxes = new HashSet<Box>(100);
	
	/*
	 * Checks for these types of deadlocks (and symmetries).
	 * 
	 * 1)                    2)       #
	 *    #$  $#    ##    $$    #$   $$
	 *    #$  $#    $$    ##     $#  #  
	 *          			       
	 * 						 	 
	 * 						
	 * 3)	$$		
	 *  	$$
	 *  
	 * 4)  #		 #  	 $$		$$
	 *    ##$		$##		##$		$##
	 *     $$		$$		 #		 #
	 *   
	 *   Method is planned to be used at tryMove method, as one of last conditions
	 */
	public static boolean checkDeadLocks(State pState,Box pBox ,int pMoveToRow, int pMoveToCol,
			char pDir){

		//Checking 1) and 2)
		for(Box box : pState.getBoxes()){
			
			//Don't check deadlock with last moved box
			if(pBox.getRow() != box.getRow() && pBox.getCol() != box.getCol()){
				
				//Check if there is wall to the left
				if(Board.isWall(pMoveToRow, pMoveToCol-1)){
					
					//Column must be same for this type of deadLock
					if(box.getCol() == pMoveToCol){
						
						//If both boxes are on goal it is not a deadlock
						if(box.isOnGoal() && Board.isGoal(pMoveToRow, pMoveToCol));

						//Check if box is above
						else if(box.getRow()-1 == pMoveToRow){

							//Check if wall to left or right of box
							if(Board.isWall(box.getRow(), box.getCol()-1) ||
									Board.isWall(box.getRow(), box.getCol()+1)){
								setBoxes.clear();
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								System.out.println("Check Two");
								Visualizer.printState(pState, "Type 1 or 2) wall to left");
								return true;
							}
						}
						//Check if box is below
						else if(box.getRow()+1 == pMoveToRow){
						
							//Check if wall is to left or right of box
							if(Board.isWall(box.getRow(), box.getCol()+1) ||
									Board.isWall(box.getRow(), box.getCol()-1)){
								setBoxes.clear();
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								System.out.println("Check Two");
								Visualizer.printState(pState, "Type 1) wall to left");
								return true;
							}
						}
					}
				}
				
				//Check if there is wall to the right
				if(Board.isWall(pMoveToRow, pMoveToCol+1)){
					
					//Column must be same for this type of deadLock
					if(box.getCol() == pMoveToCol){
						
						//If both boxes are on goal it is not a deadlock
						if(box.isOnGoal() && Board.isGoal(pMoveToRow, pMoveToCol));

						//Check if box is above
						else if(box.getRow()-1 == pMoveToRow){

							//Check if wall to left or right of box
							if(Board.isWall(box.getRow(), box.getCol()-1) ||
									Board.isWall(box.getRow(), box.getCol()+1)){
								setBoxes.clear();
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								System.out.println("Check Two");
								Visualizer.printState(pState, "Type 1or2) wall to right");
								return true;
							}
						}
						//Check if box is below
						else if(box.getRow()+1 == pMoveToRow){
						
							//Check if wall is to left or right of box
							if(Board.isWall(box.getRow(), box.getCol()+1) ||
									Board.isWall(box.getRow(), box.getCol()-1)){
								setBoxes.clear();
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								System.out.println("Check Two");
								Visualizer.printState(pState, "Type 1or2) wall to right");
								return true;
							}
						}
					}
				}

				//Check if wall above or below
				else if(Board.isWall(pMoveToRow+1, pMoveToCol) ||
						Board.isWall(pMoveToRow-1,pMoveToCol)){
					
					//Row must be same for this type deadLock
					if(box.getRow() == pMoveToRow){
					
						//If both boxes are on goal it is not a deadlock
						if(box.isOnGoal() && Board.isGoal(pMoveToRow, pMoveToCol));
						
						//Else check wall above or left
						else if(Board.isWall(box.getRow()+1, box.getCol()) ||
								Board.isWall(box.getRow()-1, box.getCol())){
						
							//Check of box is to left or right
							if(box.getCol() +1 == pMoveToCol || box.getCol() -1 == pMoveToCol){
								setBoxes.clear();
								//System.out.println(box.getRow());
								//System.out.println(box.getCol());
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								System.out.println("Check Two");
								Visualizer.printState(pState, "Type 1or2) wall above or below");
								return true;
							}
						}
					}
				}
				//Add all boxes and check the two other types of deadlocks
				setBoxes.add(box);
			}
		}
			
			
			
		/*Check for 2x2 cubes and outside corners of types (and symmetries)
			 #
			##$
			 $   <--$
			 
			  $
			  ^
			  |
			 
		If move was UP, we check U, L, R and UR or UL*/
		if(pDir == 'U'){
			
			//Check if box is above
			if(setBoxes.contains(hashString(pMoveToRow+1, pMoveToCol))){
				
				//Check if box is to the left
				if(setBoxes.contains(hashString(pMoveToRow, pMoveToCol-1))){
			
					//Check if wall is Up left
					if(Board.isWall(pMoveToRow+1, pMoveToCol-1)){
					
						//If all boxes are on goals it is not a deadlock
						if(!Board.isGoal(pMoveToRow+1, pMoveToCol) &! 	//U
								Board.isGoal(pMoveToRow, pMoveToCol) &! 
								Board.isGoal(pMoveToRow, pMoveToCol-1)){ //R
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 4)");
							setBoxes.clear();
							return true;
						}
					}
					//Check if box is Up left
					else if(setBoxes.contains(hashString(pMoveToRow+1, pMoveToCol-1))){
						//If all boxes are on goals it is not a deadlock
						if(!Board.isGoal(pMoveToRow+1, pMoveToCol) &! 	//U
								Board.isGoal(pMoveToRow, pMoveToCol) &! 
								Board.isGoal(pMoveToRow, pMoveToCol-1) &! //R
								Board.isGoal(pMoveToRow+1, pMoveToCol-1)){ //UR
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 3) wall to left");
							setBoxes.clear();
							return true;
						}
					}
				}
				//Check if box is to the right
				else if(setBoxes.contains(hashString(pMoveToRow, pMoveToCol+1))){
					//Check if box or wall is Up right

					//Check if wall is Up right
					if(Board.isWall(pMoveToRow+1, pMoveToCol+1)){
						//If all boxes are on goals it'nt a deadlock
						if(!Board.isGoal(pMoveToRow, pMoveToCol) &!  
								Board.isGoal(pMoveToRow, pMoveToCol+1) &! //L
								Board.isGoal(pMoveToRow+1, pMoveToCol)){ //U
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 4) wall to left");
							setBoxes.clear();
							return true;
						}
					}
					else if(setBoxes.contains(hashString(pMoveToRow+1, pMoveToCol+1))){
						//If all boxes are on goals it'nt a deadlock
						if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
								Board.isGoal(pMoveToRow, pMoveToCol+1) &! // L
								Board.isGoal(pMoveToRow+1, pMoveToCol) &! // U
								Board.isGoal(pMoveToRow+1, pMoveToCol+1)){ // UL
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 3) wall to left");
							setBoxes.clear();
							return true;
						}
					}
				}
			}
		}
		//If move was Down, we check D,L,R and DL or DR
		else if(pDir == 'D'){
			//Check if box is below
			if(setBoxes.contains(hashString(pMoveToRow-1, pMoveToCol))){
				//Check if box is to left
				if(setBoxes.contains(hashString(pMoveToRow, pMoveToCol-1))){
					//Check if box is DL
					if(setBoxes.contains(hashString(pMoveToRow-1, pMoveToCol-1))){
						//If all boxes on goal it is not a deadlock
						if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
								Board.isGoal(pMoveToRow-1, pMoveToCol) &! //D
								Board.isGoal(pMoveToRow, pMoveToCol-1) &! // L
								Board.isGoal(pMoveToRow-1, pMoveToCol-1)){ // DL
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 3) wall to left");
							setBoxes.clear();
							return true;

						}
					}	
					//Check if wall is DL
					else if(Board.isWall(pMoveToRow-1, pMoveToCol-1)){
						//If all boxes on goal it is not a deadlock
						if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
								Board.isGoal(pMoveToRow-1, pMoveToCol) &! // D
								Board.isGoal(pMoveToRow, pMoveToCol-1)){ // L
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 4) wall to left");
							setBoxes.clear();
							return true;
						}
					}
				}
			}
			//Check if box is to the right
			if(setBoxes.contains(hashString(pMoveToRow, pMoveToCol+1))){
				//Check if box is DR
				if(setBoxes.contains(hashString(pMoveToRow-1, pMoveToCol+1))){
					//If all boxes on goal it is not a deadlock
					if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
							Board.isGoal(pMoveToRow, pMoveToCol+1) &! // R
							Board.isGoal(pMoveToRow-1, pMoveToCol+1) &! // DR
							Board.isGoal(pMoveToRow-1, pMoveToCol+1)){ // D
						System.out.println("Box pos");
						System.out.println(pBox.getRow());
						System.out.println(pBox.getCol());
						System.out.println("Moving to");
						System.out.println(pMoveToRow);
						System.out.println(pMoveToCol);
						System.out.println("Check Two");
						Visualizer.printState(pState, "Type 3) wall to left");
						setBoxes.clear();
						return true;
					}
				}
				//Check if wall is DR
				if(Board.isWall(pMoveToRow-1, pMoveToCol+1)){
					//If all boxes on goal it is not a deadlock
					if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
							Board.isGoal(pMoveToRow-1, pMoveToCol) &! // D
							Board.isGoal(pMoveToRow, pMoveToCol+1)){ // R
						System.out.println("Box pos");
						System.out.println(pBox.getRow());
						System.out.println(pBox.getCol());
						System.out.println("Moving to");
						System.out.println(pMoveToRow);
						System.out.println(pMoveToCol);
						System.out.println("Check Two");
						Visualizer.printState(pState, "Type 1) wall to left");
						setBoxes.clear();
						return true;
					}
				}
			}
		}
		
		//If move was Left, we check L,D,U and LD or LU
		else if(pDir == 'L'){
			//Check if box is to the left
			if(setBoxes.contains(hashString(pMoveToRow, pMoveToCol-1))){
				//Check if box is above
				if(setBoxes.contains(hashString(pMoveToRow+1, pMoveToCol))){
					//Check if box is LU
					if(setBoxes.contains(hashString(pMoveToRow+1, pMoveToCol-1))){
						//If all boxes on goal it is not a deadlock
						if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
								Board.isGoal(pMoveToRow, pMoveToCol-1) &! // L
								Board.isGoal(pMoveToRow+1, pMoveToCol) &! // U
								Board.isGoal(pMoveToRow+1, pMoveToCol-1)){ // UL
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 3) wall to left");
							setBoxes.clear();
							return true;
						}
					}
					//Check if wall is LU
					if(Board.isWall(pMoveToRow+1, pMoveToCol-1)){
						//If all boxes on goal it is not a deadlock
						if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
								Board.isGoal(pMoveToRow, pMoveToCol-1) &! // L
								Board.isGoal(pMoveToRow+1, pMoveToCol)){ // U
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 4) wall to left");
							setBoxes.clear();
							return true;
						}
					}
				}
				//Check if box is below
				if(setBoxes.contains(hashString(pMoveToRow-1, pMoveToCol))){
					//Check if box LD
					if(setBoxes.contains(hashString(pMoveToRow-1, pMoveToCol-1))){
						//If all boxes on goal it is not a deadlock
						if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
								Board.isGoal(pMoveToRow, pMoveToCol-1) &! // L
								Board.isGoal(pMoveToRow-1, pMoveToCol) &! // D
								Board.isGoal(pMoveToRow-1, pMoveToCol-1)){ // DL
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 3)");
							setBoxes.clear();
							return true;
						}
					}
					//Check if wall is LD
					if(Board.isWall(pMoveToRow-1, pMoveToCol-1)){
						//If all boxes on goal it is not a deadlock
						if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
								Board.isGoal(pMoveToRow, pMoveToCol-1) &! // L
								Board.isGoal(pMoveToRow-1, pMoveToCol)){ // D
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 4)");
							setBoxes.clear();
							return true;
						}
					}
				}
			}
		}

		//Move was Right, we check R, U, D and RU or RD
		else if(pDir == 'R'){
			//Check if box is to the right
			if(setBoxes.contains(hashString(pMoveToRow, pMoveToCol+1))){
				//Check if box is above
				if(setBoxes.contains(hashString(pMoveToRow+1, pMoveToCol))){
					//Check if box is RU
					if(setBoxes.contains(hashString(pMoveToRow+1, pMoveToCol+1))){
						
						//If all boxes on goal it is not a deadlock
						if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
								Board.isGoal(pMoveToRow, pMoveToCol+1) &! // R
								Board.isGoal(pMoveToRow+1, pMoveToCol) &! // U
								Board.isGoal(pMoveToRow+1, pMoveToCol+1)){ // UR
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 3)");
							setBoxes.clear();
							return true;
						}
					}
					//Check if wall is RU
					if(Board.isWall(pMoveToRow+1, pMoveToCol+1)){
						//If all boxes on goal it is not a deadlock
						if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
								Board.isGoal(pMoveToRow, pMoveToCol+1) &! // R
								Board.isGoal(pMoveToRow+1, pMoveToCol)){ // U
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 4)");
							setBoxes.clear();
							return true;
						}
					}
				}
				
				//Check if box is below
				if(setBoxes.contains(hashString(pMoveToRow-1, pMoveToCol))){
					//Check if box or is RD
					if(setBoxes.contains(hashString(pMoveToRow-1, pMoveToCol+1))){
						//If all boxes on goal it is not a deadlock
						if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
								Board.isGoal(pMoveToRow, pMoveToCol+1) &! // R
								Board.isGoal(pMoveToRow-1, pMoveToCol) &! // D
								Board.isGoal(pMoveToRow-1, pMoveToCol+1)){ // DR
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 3)");
							setBoxes.clear();
							return true;
						}
					}
					
					//Check if wall is RD
					if(Board.isWall(pMoveToRow-1, pMoveToCol+1)){
						//If all boxes on goal it is not a deadlock
						if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
								Board.isGoal(pMoveToRow, pMoveToCol+1) &! // R
								Board.isGoal(pMoveToRow-1, pMoveToCol)){ // D
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 4)");
							setBoxes.clear();
							return true;
						}
					}
				}
			}
		}
		
		/*We have these corner types left (and symmetries)
		 * 
		 *  	   #
		 *  	  ##$
		 *  $->	  	$
		 *  
		 *         $ 
		 *         ^
		 *  	   |
		 */
		
		//Check wall above
		if(Board.isWall(pMoveToRow+1, pMoveToCol)){
			//Check box to left
			if(setBoxes.contains(hashString(pMoveToRow,pMoveToCol-1))){
				//Check box UP Left
				if(setBoxes.contains(hashString(pMoveToRow+1,pMoveToCol-1))){
					//If all boxes on goal it is not a deadlock
					if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
							Board.isGoal(pMoveToRow, pMoveToCol-1) &! // L
							Board.isGoal(pMoveToRow+1, pMoveToCol-1)){ // UL
						System.out.println("Box pos");
						System.out.println(pBox.getRow());
						System.out.println(pBox.getCol());
						System.out.println("Moving to");
						System.out.println(pMoveToRow);
						System.out.println(pMoveToCol);
						System.out.println("Check Two");
						Visualizer.printState(pState, "Type 4)");
						setBoxes.clear();
						return true;
					}
				}
			}
			//Check box to right
			if(setBoxes.contains(hashString(pMoveToRow,pMoveToCol+1))){
				//Check box UP Right
				if(setBoxes.contains(hashString(pMoveToRow+1,pMoveToCol+1))){
					
					//If all boxes on goal it is not a deadlock
					if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
							Board.isGoal(pMoveToRow, pMoveToCol+1) &! // R
							Board.isGoal(pMoveToRow+1, pMoveToCol+1)){ // UR
						System.out.println("Box pos");
						System.out.println(pBox.getRow());
						System.out.println(pBox.getCol());
						System.out.println("Moving to");
						System.out.println(pMoveToRow);
						System.out.println(pMoveToCol);
						System.out.println("Check Two");
						Visualizer.printState(pState, "Type 4)");
						setBoxes.clear();
						return true;
					}
				}
			}
		}
		//Check wall below
		if(Board.isWall(pMoveToRow-1, pMoveToCol)){
			//Check box to left
			if(setBoxes.contains(hashString(pMoveToRow,pMoveToCol-1))){
				//Check box Down Left
				if(setBoxes.contains(hashString(pMoveToRow-1,pMoveToCol-1))){
					//If all boxes on goal it is not a deadlock
					if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
							Board.isGoal(pMoveToRow, pMoveToCol-1) &! // L
							Board.isGoal(pMoveToRow-1, pMoveToCol-1)){ // DL
						System.out.println("Box pos");
						System.out.println(pBox.getRow());
						System.out.println(pBox.getCol());
						System.out.println("Moving to");
						System.out.println(pMoveToRow);
						System.out.println(pMoveToCol);
						System.out.println("Check Two");
						Visualizer.printState(pState, "Type 4)");
						setBoxes.clear();
						return true;
					}
				}
			}
			//Check box to right
			if(setBoxes.contains(hashString(pMoveToRow,pMoveToCol+1))){
				//Check box Down Right
				if(setBoxes.contains(hashString(pMoveToRow-1,pMoveToCol+1))){
					//If all boxes on goal it is not a deadlock
					if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
							Board.isGoal(pMoveToRow, pMoveToCol+1) &! // R
							Board.isGoal(pMoveToRow-1, pMoveToCol+1)){ // DR
						System.out.println("Box pos");
						System.out.println(pBox.getRow());
						System.out.println(pBox.getCol());
						System.out.println("Moving to");
						System.out.println(pMoveToRow);
						System.out.println(pMoveToCol);
						System.out.println("Check Two");
						Visualizer.printState(pState, "Type 4)");
						setBoxes.clear();
						return true;
					}
				}
			}
		}
		//Check wall to right
		if(Board.isWall(pMoveToRow, pMoveToCol+1)){
			//Check if box above 
			if(setBoxes.contains(hashString(pMoveToRow+1,pMoveToCol))){
				//Check box Up Right
				if(setBoxes.contains(hashString(pMoveToRow+1,pMoveToCol+1))){
					//If all boxes on goal it is not a deadlock
					if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
							Board.isGoal(pMoveToRow+1, pMoveToCol) &! // U
							Board.isGoal(pMoveToRow+1, pMoveToCol+1)){ // UR
						System.out.println("Box pos");
						System.out.println(pBox.getRow());
						System.out.println(pBox.getCol());
						System.out.println("Moving to");
						System.out.println(pMoveToRow);
						System.out.println(pMoveToCol);
						System.out.println("Check Two");
						Visualizer.printState(pState, "Type 4)");
						setBoxes.clear();
						return true;
					}
				}
			}
			//Check box below
			if(setBoxes.contains(hashString(pMoveToRow-1,pMoveToCol))){
				//Check box Down Right
				if(setBoxes.contains(hashString(pMoveToRow-1,pMoveToCol+1))){
					//If all boxes on goal it is not a deadlock
					if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
							Board.isGoal(pMoveToRow-1, pMoveToCol) &! // D
							Board.isGoal(pMoveToRow-1, pMoveToCol+1)){ // DR
						System.out.println("Box pos");
						System.out.println(pBox.getRow());
						System.out.println(pBox.getCol());
						System.out.println("Moving to");
						System.out.println(pMoveToRow);
						System.out.println(pMoveToCol);
						System.out.println("Check Two");
						Visualizer.printState(pState, "Type 4)");
						setBoxes.clear();
						return true;
					}
				}
			}
		}

		//Check wall to left
		if(Board.isWall(pMoveToRow, pMoveToCol-1)){
			//Check if box above 
			if(setBoxes.contains(hashString(pMoveToRow+1,pMoveToCol))){
				//Check box Up left
				if(setBoxes.contains(hashString(pMoveToRow+1,pMoveToCol-1))){
					//If all boxes on goal it is not a deadlock
					if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
							Board.isGoal(pMoveToRow+1, pMoveToCol) &! // U
							Board.isGoal(pMoveToRow+1, pMoveToCol-1)){ // UL
						System.out.println("Box pos");
						System.out.println(pBox.getRow());
						System.out.println(pBox.getCol());
						System.out.println("Moving to");
						System.out.println(pMoveToRow);
						System.out.println(pMoveToCol);
						System.out.println("Check Two");
						Visualizer.printState(pState, "Type 4)");
						setBoxes.clear();
						return true;
					}
				}
			}
			//Check box below
			if(setBoxes.contains(hashString(pMoveToRow-1,pMoveToCol))){
				//Check box Down left
				if(setBoxes.contains(hashString(pMoveToRow-1,pMoveToCol-1))){
					//If all boxes on goal it is not a deadlock
					if(!Board.isGoal(pMoveToRow, pMoveToCol) &! 
							Board.isGoal(pMoveToRow-1, pMoveToCol) &! // D
							Board.isGoal(pMoveToRow-1, pMoveToCol-1)){ // DL
						System.out.println("Box pos");
						System.out.println(pBox.getRow());
						System.out.println(pBox.getCol());
						System.out.println("Moving to");
						System.out.println(pMoveToRow);
						System.out.println(pMoveToCol);
						System.out.println("Check Two");
						Visualizer.printState(pState, "Type 4)");
						setBoxes.clear();
						return true;
					}
				}
			}
		}
		return false;
	}
}

