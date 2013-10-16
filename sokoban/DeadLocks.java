package sokoban;

import java.awt.Point;
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

	private static Point STATIC_POINT = new Point(0,0);
	private static HashSet<Box> setBoxes = new HashSet<Box>(Board.getNbOfBoxes());

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
	 * 4)  #$ 		$#   	 $$		$$
	 *     $$		$$		 #$		$#
	 *      				 		 
	 *   
	 *   Method is planned to be used at tryMove method, as one of last conditions
	 */
	public static boolean checkDeadLocks(State pState,Box pBox ,int pMoveToRow, int pMoveToCol,
			char pDir,boolean debug){
		
		for(Box box : pState.getBoxes()){

			if(pBox.getRow() != box.getRow() || pBox.getCol() != box.getCol()){
				//Do not check deadlocks with the box we are moving

				if(box.isOnGoal() && Board.isGoal(pMoveToRow, pMoveToCol)){
					//If both boxes are on goal it is not this type of deadlock
				}
				else{
					
					if(box.getCol() == pMoveToCol){
						//Column must be same for this type of deadLock
						
						if(Board.isWall(pMoveToRow, pMoveToCol-1)){
							//Check if there is wall to the left
							
							if(box.getRow()-1 == pMoveToRow){
								//Check if box is above
								
								if(Board.isWall(box.getRow(), box.getCol()-1) ||
										Board.isWall(box.getRow(), box.getCol()+1)){
									//Check if wall to left or right of box
									if(debug){
										System.out.println("Box pos");
										System.out.println(pBox.getRow());
										System.out.println(pBox.getCol());
										System.out.println("Moving to");
										System.out.println(pMoveToRow);
										System.out.println(pMoveToCol);
										System.out.println("Check Two");
										Visualizer.printState(pState, "Type 1 or 2) wall to left");
									}
									setBoxes.clear();
									return true;
								}
							}
							if(box.getRow()+1 == pMoveToRow){
								//Check if box is below
								
								if(Board.isWall(box.getRow(), box.getCol()+1) ||
										Board.isWall(box.getRow(), box.getCol()-1)){
									//Check if wall is to left or right of box
									if(debug){
										System.out.println("Box pos");
										System.out.println(pBox.getRow());
										System.out.println(pBox.getCol());
										System.out.println("Moving to");
										System.out.println(pMoveToRow);
										System.out.println(pMoveToCol);
										System.out.println("Check Two");
										Visualizer.printState(pState, "Type 1or2) wall to left");
									}
									setBoxes.clear();
									return true;
								}
							}
						}
					
						if(Board.isWall(pMoveToRow, pMoveToCol+1)){
							//Check if there is wall to the right

							if(box.getRow()-1 == pMoveToRow){
								//Check if box is above
								
								if(Board.isWall(box.getRow(), box.getCol()-1) ||
										Board.isWall(box.getRow(), box.getCol()+1)){
									//Check if wall to left or right of box
									if(debug){
										System.out.println("Box pos");
										System.out.println(pBox.getRow());
										System.out.println(pBox.getCol());
										System.out.println("Moving to");
										System.out.println(pMoveToRow);
										System.out.println(pMoveToCol);
										System.out.println("Check Two");
										Visualizer.printState(pState, "Type 1or2) wall or right");
									}
									setBoxes.clear();
									return true;
								}
							}
							if(box.getRow()+1 == pMoveToRow){
								//Check if box is below
								
								if(Board.isWall(box.getRow(), box.getCol()+1) ||
										Board.isWall(box.getRow(), box.getCol()-1)){
									//Check if wall is to left or right of box
									if(debug){
										System.out.println("Box pos");
										System.out.println(pBox.getRow());
										System.out.println(pBox.getCol());
										System.out.println("Moving to");
										System.out.println(pMoveToRow);
										System.out.println(pMoveToCol);
										Visualizer.printState(pState, "Type 1or2) wall or right");
									}
									setBoxes.clear();
									return true;
								}
							}
						}
					}

					if(box.getRow() == pMoveToRow){
						//Row must be same for this type deadLock
						
						if(Board.isWall(pMoveToRow+1,pMoveToCol) || 
								Board.isWall(pMoveToRow-1,pMoveToCol)){
							//Check if wall is above or below							

							if((box.getCol()+1) == pMoveToCol || (box.getCol()-1) == pMoveToCol){
								//Check if box is to the left or to the right	

								if(Board.isWall(box.getRow()+1,box.getCol()) || 
										Board.isWall(box.getRow()-1,box.getCol())){
									//Check if wall above or below of box
									if(debug){
										System.out.println("Box pos");
										System.out.println(pBox.getRow());
										System.out.println(pBox.getCol());
										System.out.println("Moving to");
										System.out.println(pMoveToRow);
										System.out.println(pMoveToCol);
										Visualizer.printState(pState,"type1or2) deadLocks");
									}
									setBoxes.clear();
									return true;
								}
							}
						}
					}
				}
				setBoxes.add(box);
				//Add all boxes and check the two other types of deadlocks
			}
		}
		/*
		  Check for 2x2 cubes and outside corners of types (and symmetries)
			 #
			##$
			 $   <--$
			  $
			  ^
			  |
			*/
			 
		if(pDir == 'U'){
			//If move was UP, we check U, L, R and UR or UL
			
			STATIC_POINT.setLocation(pMoveToCol, pMoveToRow-1);
			if(setBoxes.contains(STATIC_POINT)){
				//Check if box is above

				STATIC_POINT.setLocation(pMoveToCol-1, pMoveToRow);
				if(setBoxes.contains(STATIC_POINT)){
					//Check if box is to the left

					if(Board.isWall(pMoveToRow-1, pMoveToCol-1)){
						//Check if wall is Up left

						if(!Board.isGoal(pMoveToRow-1, pMoveToCol) || 	//U
								!Board.isGoal(pMoveToRow, pMoveToCol) || 
								!Board.isGoal(pMoveToRow, pMoveToCol-1)){ //L
							//If any box is not on goal it is a deadlock
							if(debug){
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								System.out.println("Check Two");
								Visualizer.printState(pState, "Type 3or4)");
							}
							setBoxes.clear();
							return true;
						}
					}
					STATIC_POINT.setLocation(pMoveToCol-1, pMoveToRow-1);
					if(setBoxes.contains(STATIC_POINT)){
						//Check if box is Up left
						
						if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
								!Board.isGoal(pMoveToRow-1, pMoveToCol) || 	//U
								!Board.isGoal(pMoveToRow, pMoveToCol-1) || //L
								!Board.isGoal(pMoveToRow-1, pMoveToCol-1)){ //UL
							//If any box is not on goal it is a deadlock
							if(debug){
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								System.out.println("Check Two");
								Visualizer.printState(pState, "3or4");	
							}
							setBoxes.clear();
							return true;
						}
					}
				}
				STATIC_POINT.setLocation(pMoveToCol+1, pMoveToRow);
				if(setBoxes.contains(STATIC_POINT)){
					//Check if box is to the right

					if(Board.isWall(pMoveToRow-1, pMoveToCol+1)){
						//Check if wall is Up right

						if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
								!Board.isGoal(pMoveToRow, pMoveToCol+1) || //R
								!Board.isGoal(pMoveToRow-1, pMoveToCol)){ //U
							//If any box is not on goal it is a deadlock
							if(debug){
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								System.out.println("Check Two");
								Visualizer.printState(pState, "Type 3or4");
							}
							setBoxes.clear();
							return true;
						}
					}
					STATIC_POINT.setLocation(pMoveToCol+1, pMoveToRow-1);
					if(setBoxes.contains(STATIC_POINT)){
						//Check if box is UR

						if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
								!Board.isGoal(pMoveToRow, pMoveToCol+1) || // R
								!Board.isGoal(pMoveToRow-1, pMoveToCol) || // U
								!Board.isGoal(pMoveToRow-1, pMoveToCol+1)){ // UR
							//If any box is not on goal it is a deadlock
							if(debug){
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								System.out.println("Check Two");
								Visualizer.printState(pState, "Type 3or4");
							}
							setBoxes.clear();
							return true;
						}
					}
				}
			}
		}
		else if(pDir == 'D'){
			//If move was Down, we check D,L,R and DL or DR

			STATIC_POINT.setLocation(pMoveToCol, pMoveToRow+1);
			if(setBoxes.contains(STATIC_POINT)){
				//Check if box is below
				
				STATIC_POINT.setLocation(pMoveToCol-1, pMoveToRow);
				if(setBoxes.contains(STATIC_POINT)){
					//Check if box is to left

					STATIC_POINT.setLocation(pMoveToCol-1, pMoveToRow+1);
					if(setBoxes.contains(STATIC_POINT)){
						//Check if box is DL

						if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
								!Board.isGoal(pMoveToRow+1, pMoveToCol) || //D
								!Board.isGoal(pMoveToRow, pMoveToCol-1) || // L
								!Board.isGoal(pMoveToRow+1, pMoveToCol-1)){ // DL
							//If any box is not on goal it is a deadlock
							if(debug){
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								System.out.println("Check Two");
								Visualizer.printState(pState, "Type 3or4");
							}
							setBoxes.clear();
							return true;
						}
					}	
					else if(Board.isWall(pMoveToRow+1, pMoveToCol-1)){
						//Check if wall is DL

						if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
								!Board.isGoal(pMoveToRow+1, pMoveToCol) || // D
								!Board.isGoal(pMoveToRow, pMoveToCol-1)){ // L
							//If any box is not on goal it is a deadlock
							if(debug){
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								System.out.println("Check Two");
								Visualizer.printState(pState, "Type 3or4");
							}
							setBoxes.clear();
							return true;
						}
					}
				}
				STATIC_POINT.setLocation(pMoveToCol+1, pMoveToRow);
				if(setBoxes.contains(STATIC_POINT)){
					//Check if box is to the right
					
					STATIC_POINT.setLocation(pMoveToCol+1, pMoveToRow-1);
					if(setBoxes.contains(STATIC_POINT)){
						//Check if box is DR

						if(!Board.isGoal(pMoveToRow, pMoveToCol) || 
								!Board.isGoal(pMoveToRow, pMoveToCol+1) || // R
								!Board.isGoal(pMoveToRow+1, pMoveToCol+1) || // DR
								!Board.isGoal(pMoveToRow+1, pMoveToCol)){ // D
							//If any box is not on goal it is a deadlock
							if(debug){
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								System.out.println("Check Two");
								Visualizer.printState(pState, "Type 3or4");
							}
							setBoxes.clear();
							return true;
						}
					}
					if(Board.isWall(pMoveToRow+1, pMoveToCol+1)){
						//Check if wall is DR

						if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
								!Board.isGoal(pMoveToRow+1, pMoveToCol) || // D
								!Board.isGoal(pMoveToRow, pMoveToCol+1)){ // R
							//If any box is not on goal it is a deadlock
							if(debug){
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								System.out.println("Check Two");
								Visualizer.printState(pState, "Type 3or4");
							}
							setBoxes.clear();
							return true;
						}
					}
				}
			}
		}
		else if(pDir == 'L'){
			//If move was Left, we check L,D,U and LD or LU

			STATIC_POINT.setLocation(pMoveToCol-1, pMoveToRow);
			if(setBoxes.contains(STATIC_POINT)){
				//Check if box is to the left

				STATIC_POINT.setLocation(pMoveToCol, pMoveToRow-1);
				if(setBoxes.contains(STATIC_POINT)){
					//Check if box is above

					STATIC_POINT.setLocation(pMoveToCol-1, pMoveToRow-1);
					if(setBoxes.contains(STATIC_POINT)){
						//Check if box is LU

						if(!Board.isGoal(pMoveToRow, pMoveToCol) || 
								!Board.isGoal(pMoveToRow, pMoveToCol-1) || // L
								!Board.isGoal(pMoveToRow-1, pMoveToCol) || // U
								!Board.isGoal(pMoveToRow-1, pMoveToCol-1)){ // LU
							//If any box is not on goal it is a deadlock
							if(debug){
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								System.out.println("Check Two");
								Visualizer.printState(pState, "Type 3or4");
							}
							setBoxes.clear();
							return true;
						}
					}
					if(Board.isWall(pMoveToRow-1, pMoveToCol-1)){
						//Check if wall is LU

						if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
								!Board.isGoal(pMoveToRow, pMoveToCol-1) || // L
								!Board.isGoal(pMoveToRow-1, pMoveToCol)){ // U
							//If any box is not on goal it is a deadlock
							if(debug){
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								System.out.println("Check Two");
								Visualizer.printState(pState, "Type 3or4");
							}
							setBoxes.clear();
							return true;
						}
					}
				}
				STATIC_POINT.setLocation(pMoveToCol, pMoveToRow+1);
				if(setBoxes.contains(STATIC_POINT)){
					//Check if box is below

					STATIC_POINT.setLocation(pMoveToCol-1, pMoveToRow+1);
					if(setBoxes.contains(STATIC_POINT)){
						//Check if box LD
						
						if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
								!Board.isGoal(pMoveToRow, pMoveToCol-1) || // L
								!Board.isGoal(pMoveToRow+1, pMoveToCol) || // D
								!Board.isGoal(pMoveToRow+1, pMoveToCol-1)){ // DL
							//If any box is not on goal it is a deadlock
							if(debug){
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								System.out.println("Check Two");
								Visualizer.printState(pState, "Type 3or4");
							}
							setBoxes.clear();
							return true;
						}
					}
					if(Board.isWall(pMoveToRow+1, pMoveToCol-1)){
						//Check if wall is LD
						
						if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
								!Board.isGoal(pMoveToRow, pMoveToCol-1) || // L
								!Board.isGoal(pMoveToRow+1, pMoveToCol)){ // D
							//If any box is not on goal it is a deadlock
							if(debug){
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								System.out.println("Check Two");
								Visualizer.printState(pState, "Type 3or4");
							}
							setBoxes.clear();
							return true;
						}
					}
				}
			}
		}
		else if(pDir == 'R'){
			//Move was Right, we check R, U, D and RU or RD
		
			STATIC_POINT.setLocation(pMoveToCol+1, pMoveToRow);
			if(setBoxes.contains(STATIC_POINT)){
				//Check if box is to the right

				STATIC_POINT.setLocation(pMoveToCol, pMoveToRow-1);
				if(setBoxes.contains(STATIC_POINT)){
					//Check if box is above

					STATIC_POINT.setLocation(pMoveToCol+1, pMoveToRow-1);
					//Set position to check if is RU
					if(setBoxes.contains(STATIC_POINT)){
						//Check if box is RU

						if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
								!Board.isGoal(pMoveToRow, pMoveToCol+1) || // R
								!Board.isGoal(pMoveToRow-1, pMoveToCol) || // U
								!Board.isGoal(pMoveToRow-1, pMoveToCol+1)){ // UR
							//If any box is not on goal it is a deadlock
							if(debug){
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								System.out.println("Check Two");
								Visualizer.printState(pState, "Type 3or4");
							}
							setBoxes.clear();
							return true;
						}
					}
					if(Board.isWall(pMoveToRow-1, pMoveToCol+1)){
						//Check if wall is RU

						if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
								!Board.isGoal(pMoveToRow, pMoveToCol+1) || // R
								!Board.isGoal(pMoveToRow-1, pMoveToCol)){ // U
							//If any box is not on goal it is a deadlock
							if(debug){
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								Visualizer.printState(pState, "Type 3or4");
							}
							setBoxes.clear();
							return true;
						}
					}
				}
				STATIC_POINT.setLocation(pMoveToCol, pMoveToRow+1);
				if(setBoxes.contains(STATIC_POINT)){
					//Check if box is below

					STATIC_POINT.setLocation(pMoveToCol+1, pMoveToRow+1);
					if(setBoxes.contains(STATIC_POINT)){
						//Check if box or is RD
						
						if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
								!Board.isGoal(pMoveToRow, pMoveToCol+1) || // R
								!Board.isGoal(pMoveToRow+1, pMoveToCol) || // D
								!Board.isGoal(pMoveToRow+1, pMoveToCol+1)){ // DR
							//If any box is not on goal it is a deadlock
							if(debug){
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								Visualizer.printState(pState, "Type 3or4");
							}
							setBoxes.clear();
							return true;
						}
					}
					if(Board.isWall(pMoveToRow+1, pMoveToCol+1)){
						//Check if wall is RD

						if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
								!Board.isGoal(pMoveToRow, pMoveToCol+1) || // R
								!Board.isGoal(pMoveToRow+1, pMoveToCol)){ // D
							//If any box is not on goal it is a deadlock
							if(debug){
								System.out.println("Box pos");
								System.out.println(pBox.getRow());
								System.out.println(pBox.getCol());
								System.out.println("Moving to");
								System.out.println(pMoveToRow);
								System.out.println(pMoveToCol);
								System.out.println("Check Two");
								Visualizer.printState(pState, "Type 3or4");
							}
							setBoxes.clear();
							return true;
						}
					}
				}
			}
		}

		/*We have these corner types left (and symmetries)
		 * 
		 *  	   
		 *  	   #$
		 *  $->	  	$
		 *  
		 *         $ 
		 *         ^
		 *  	   |
		 */

		if(Board.isWall(pMoveToRow-1, pMoveToCol)){
			//Check wall above
			
			STATIC_POINT.setLocation(pMoveToCol-1, pMoveToRow);
			if(setBoxes.contains(STATIC_POINT)){
				//Check box to left
				
				STATIC_POINT.setLocation(pMoveToCol-1, pMoveToRow-1);
				if(setBoxes.contains(STATIC_POINT)){
					//Check box UP Left

					if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
							!Board.isGoal(pMoveToRow, pMoveToCol-1) || // L
							!Board.isGoal(pMoveToRow-1, pMoveToCol-1)){ // UL
						//If any box is not on goal it is a deadlock
						if(debug){
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 3or4");
						}
						setBoxes.clear();
						return true;
					}
				}
			}
			STATIC_POINT.setLocation(pMoveToCol+1, pMoveToRow);
			if(setBoxes.contains(STATIC_POINT)){
				//Check box to right

				STATIC_POINT.setLocation(pMoveToCol+1, pMoveToRow-1);
				if(setBoxes.contains(STATIC_POINT)){
					//Check box UP Right

					if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
							!Board.isGoal(pMoveToRow, pMoveToCol+1) || // R
							!Board.isGoal(pMoveToRow-1, pMoveToCol+1)){ // UR
						//If any box is not on goal it is a deadlock
						if(debug){
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 3or4");
						}
						setBoxes.clear();
						return true;
					}
				}
			}
		}
		
		if(Board.isWall(pMoveToRow+1, pMoveToCol)){
			//Check wall below

			STATIC_POINT.setLocation(pMoveToCol-1, pMoveToRow);
			if(setBoxes.contains(STATIC_POINT)){
				//Check box to left

				STATIC_POINT.setLocation(pMoveToCol-1, pMoveToRow+1);
				if(setBoxes.contains(STATIC_POINT)){
					//Check box Down Left

					if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
							!Board.isGoal(pMoveToRow, pMoveToCol-1) || // L
							!Board.isGoal(pMoveToRow+1, pMoveToCol-1)){ // DL
						//If any box is not on goal it is a deadlock
						if(debug){
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 3or4");
						}
						setBoxes.clear();
						return true;
					}
				}
			}
			STATIC_POINT.setLocation(pMoveToCol+1, pMoveToRow);
			if(setBoxes.contains(STATIC_POINT)){
				//Check box to right
				
				STATIC_POINT.setLocation(pMoveToCol+1, pMoveToRow+1);
				if(setBoxes.contains(STATIC_POINT)){
					//Check box Down Right

					if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
							!Board.isGoal(pMoveToRow, pMoveToCol+1) || // R
							!Board.isGoal(pMoveToRow+1, pMoveToCol+1)){ // DR
						//If any box is not on goal it is a deadlock
						if(debug){
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 3or4");
						}
						setBoxes.clear();
						return true;
					}
				}
			}
		}
		if(Board.isWall(pMoveToRow, pMoveToCol+1)){
			//Check wall to right

			STATIC_POINT.setLocation(pMoveToCol, pMoveToRow-1);
			if(setBoxes.contains(STATIC_POINT)){
				//Check if box above 

				STATIC_POINT.setLocation(pMoveToCol+1, pMoveToRow-1);
				if(setBoxes.contains(STATIC_POINT)){
					//Check box Up Right

					if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
							!Board.isGoal(pMoveToRow-1, pMoveToCol) || // U
							!Board.isGoal(pMoveToRow-1, pMoveToCol+1)){ // UR
						//If any box is not on goal it is a deadlock
						if(debug){
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 3or4");
						}
						setBoxes.clear();
						return true;
					}
				}
			}
			STATIC_POINT.setLocation(pMoveToCol, pMoveToRow+1);
			if(setBoxes.contains(STATIC_POINT)){
				//Check box below

				STATIC_POINT.setLocation(pMoveToCol+1, pMoveToRow+1);
				if(setBoxes.contains(STATIC_POINT)){
					//Check box Down Right
					
					if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
							!Board.isGoal(pMoveToRow+1, pMoveToCol) || // D
							!Board.isGoal(pMoveToRow+1, pMoveToCol+1)){ // DR
						//If any box is not on goal it is a deadlock
						if(debug){
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 3or4");
						}
						setBoxes.clear();
						return true;
					}
				}
			}
		}
		if(Board.isWall(pMoveToRow, pMoveToCol-1)){
			//Check wall to left

			STATIC_POINT.setLocation(pMoveToCol, pMoveToRow-1);
			if(setBoxes.contains(STATIC_POINT)){
				//Check if box above

				STATIC_POINT.setLocation(pMoveToCol-1, pMoveToRow-1);
				if(setBoxes.contains(STATIC_POINT)){
					//Check box Up left

					if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
							!Board.isGoal(pMoveToRow-1, pMoveToCol) || // U
							!Board.isGoal(pMoveToRow-1, pMoveToCol-1)){ // UL
						//If any box is not on goal it is a deadlock
						if(debug){
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 3or4");
						}
						setBoxes.clear();
						return true;
					}
				}
			}
			STATIC_POINT.setLocation(pMoveToCol, pMoveToRow+1);
			if(setBoxes.contains(STATIC_POINT)){
				//Check box below

				STATIC_POINT.setLocation(pMoveToCol-1, pMoveToRow+1);
				if(setBoxes.contains(STATIC_POINT)){
					//Check box Down left

					if(!Board.isGoal(pMoveToRow, pMoveToCol) ||
							!Board.isGoal(pMoveToRow+1, pMoveToCol) || // D
							!Board.isGoal(pMoveToRow+1, pMoveToCol-1)){ // DL
						//If any box is not on goal it is a deadlock
						if(debug){
							System.out.println("Box pos");
							System.out.println(pBox.getRow());
							System.out.println(pBox.getCol());
							System.out.println("Moving to");
							System.out.println(pMoveToRow);
							System.out.println(pMoveToCol);
							System.out.println("Check Two");
							Visualizer.printState(pState, "Type 3or4");
						}
						setBoxes.clear();
						return true;
					}
				}
			}
		}
		setBoxes.clear();
		return false;
		//No deadlocks
	}
}

