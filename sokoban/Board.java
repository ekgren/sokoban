package sokoban;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;

/**
 * NEO-SOKOBAN BOARD CLASS.
 * 
 * Class to store original board.
 * 
 * Current idea is to store the complete board as a set of cells.
 * 
 *
 */
public class Board {
	
	// Cells, boxes, goals and player in map.
	private HashSet<Cell> mapCells = new HashSet<Cell>();
	private HashSet<Box> initialBoxes = new HashSet<Box>();
	public static HashSet<Cell> goals = new HashSet<Cell>();
	private Player player;
	
	// Original map, saved for printing.
	private Vector<StringBuilder> map = new Vector<StringBuilder>();
	
	// Gradient field stuff.
	private Queue<Cell> open = new LinkedList<Cell>();
	private HashSet<Cell> closed = new HashSet<Cell>();
	
	/**
	 * Constructor takes Reader object.
	 */
	public Board(Reader r) throws IOException{
		// Build our board representation from map.
		buildBoard(getMapFromReader(r));
		if(mapCells.size()>200)System.out.println("In Board."); 
		locateCornersAndCorridors();
		locateCornerWallDeadlocks();
		gradientField();
		
		// Debug message.
		if(Sokoban.debug){ System.out.println("Boardsize: " + Integer.toString(mapCells.size()) + " or " + 
				Integer.toString(Factory.getCellCount()) + ", boxes: " + 
				Integer.toString(initialBoxes.size()) + ", goals: " + 
				Integer.toString(goals.size()));
				printBoard();
				printGradient();
				}
	}
	
	public void gradientField(){
		State state = Factory.createState();
		for(Cell mapCell : mapCells){
			int dist = Integer.MAX_VALUE;
			String path = "";
			for(Cell goal : goals){
				path = Search.Astar(state, mapCell, goal, true);
				try{
					if(path.length() < dist) dist = path.length();
				} catch(NullPointerException e) {}
			}
			mapCell.setGradient(dist);
		}
	}	
	
	/** Method that will examine walls between two corners to see if deadlock. */
	public void locateCornerWallDeadlocks(){
		
		// Boolean to check for deadlock and a Cell holder.
		boolean isDeadlock;
		Cell examine;
		
		// Check all cells for corners.
		for(Cell cornerCell : mapCells){
			
			// Examine walls connected to upper left corners.
			if(cornerCell.upperLeftCorner && cornerCell.isGoal == false){
				
				// Set corner cell to examine cell.
				examine = cornerCell;
				
				// Reset isDeadlock boolean for each corner and direction.
				isDeadlock = false;
				
				// Check all cells to the right of corner until either a goal is found,
				// a wall is reached or another corner is reached.
				while(examine != null){
					examine = Factory.getCellRight(examine);
					//System.out.println(examine.toString());
					if(examine == null) break;
					if(examine.isGoal) break;
					if(Factory.getCellUp(examine) != null) break;
					if(examine.upperRightCorner){ isDeadlock = true; break;}
				}
				
				// If another corner was reached without finding a goal it
				// means we found a deadlock and we will not allow boxes in there.
				if(isDeadlock){
					examine = cornerCell;
					while(examine != null){
						examine = Factory.getCellRight(examine);
						if(examine.upperRightCorner){break;}
						examine.boxAllowed = false;
					}
				}
				
				// Reset isDeadlock boolean for each corner and direction.
				isDeadlock = false;
				examine = cornerCell;
				
				// Check all cells below corner until either a goal is found,
				// a wall is reached or another corner is reached.
				while(examine != null){
					examine = Factory.getCellDown(examine);
					if(examine == null) break;
					if(examine.isGoal) break;
					if(Factory.getCellLeft(examine) != null) break;
					if(examine.lowerLeftCorner){ isDeadlock = true; break;}
				}
				
				// If another corner was reached without finding a goal it
				// means we found a deadlock and we will not allow boxes in there.
				if(isDeadlock){
					examine = cornerCell;
					while(examine != null){
						examine = Factory.getCellDown(examine);
						if(examine.lowerLeftCorner){break;}
						examine.boxAllowed = false;
					}
				}
			} // End if upper left corner.
			
			// Examine walls connected to lower right corners.	
			if(cornerCell.lowerRightCorner && cornerCell.isGoal == false){
	
				// Set corner cell to examine cell.
				examine = cornerCell;
				
				// Reset isDeadlock boolean for each corner and direction.
				isDeadlock = false;
				
				// Check all cells to the left of corner until either a goal is found,
				// a wall is reached or another corner is reached.
				while(examine != null){
					examine = Factory.getCellLeft(examine);
					if(examine == null) break;
					if(examine.isGoal) break;
					if(Factory.getCellDown(examine) != null) break;
					if(examine.lowerLeftCorner){ isDeadlock = true; break;}
				}
				
				// If another corner was reached without finding a goal it
				// means we found a deadlock and we will not allow boxes in there.
				if(isDeadlock){
					examine = cornerCell;
					while(examine != null){
						examine = Factory.getCellLeft(examine);
						if(examine.lowerLeftCorner){break;}
						examine.boxAllowed = false;
					}
				}
				
				// Reset isDeadlock boolean for each corner and direction.
				isDeadlock = false;
				examine = cornerCell;
				
				// Check all cells below corner until either a goal is found,
				// a wall is reached or another corner is reached.
				while(examine != null){
					examine = Factory.getCellUp(examine);
					if(examine == null) break;
					if(examine.isGoal) break;
					if(Factory.getCellRight(examine) != null) break;
					if(examine.upperRightCorner){ 
						isDeadlock = true; break;}
				}
				
				// If another corner was reached without finding a goal it
				// means we found a deadlock and we will not allow boxes in there.
				if(isDeadlock){
					examine = cornerCell;
					while(examine != null){
						examine = Factory.getCellUp(examine);
						if(examine.upperRightCorner){break;}
						examine.boxAllowed = false;
					}
				}
			} // End if lower right corner.
		}
	}
	
	
	
	/**
	 * Method that processes input from Reader object and returns vector 
	 * containing string representations of map.
	 */
	public Vector<String> getMapFromReader(Reader r) throws IOException{
		
		// Create a vector to contain strings from reader.
		Vector<String> map = new Vector<String>();
		
		// Process file.
		BufferedReader fileBr = new BufferedReader(r);
		
		// Initialize string.
		String line;
		
		// Loop over file.
		while((line = fileBr.readLine()) != null) {
			map.add(line);
		} // End while
		fileBr.close();
		
		// Return vector containing string representation of map.
		return map;
	}
	
	/**
	 * Looking for corners and corridors in board.
	 */
	public void locateCornersAndCorridors(){
		// Loop over all cells in board.
		for(Cell cell : mapCells){
			
			if(Factory.getCellUp(cell) == null) cell.wallUp = true;
			if(Factory.getCellDown(cell) == null) cell.wallDown = true;
			if(Factory.getCellLeft(cell) == null) cell.wallLeft = true;
			if(Factory.getCellRight(cell) == null) cell.wallRight = true;
			
			// Check if cell is upper left corner.
			if(Factory.getCellUp(cell) == null && 
			   Factory.getCellDown(cell) != null &&
			   Factory.getCellLeft(cell) == null &&
			   Factory.getCellRight(cell) != null){
				   cell.upperLeftCorner = true;
				   if(cell.isGoal == false){
					   cell.boxAllowed = false;
				   }
			   	}
			
			// Check if cell is upper right corner.
			else if(Factory.getCellUp(cell) == null && 
				    Factory.getCellDown(cell) != null && 
				    Factory.getCellLeft(cell) != null &&
				    Factory.getCellRight(cell) == null){
					   cell.upperRightCorner = true;
					   if(cell.isGoal == false){
						   cell.boxAllowed = false;
					   }
			   	}
			
			// Check if cell is lower left corner.
			else if(Factory.getCellUp(cell) != null && 
					Factory.getCellDown(cell) == null && 
					Factory.getCellLeft(cell) == null &&
					Factory.getCellRight(cell) != null){
					   cell.lowerLeftCorner = true;
					   if(cell.isGoal == false){
						   cell.boxAllowed = false;
					   }
			   	}
			
			// Check if cell is lower right corner.
			else if(Factory.getCellUp(cell) != null && 
					Factory.getCellDown(cell) == null && 
					Factory.getCellLeft(cell) != null &&
					Factory.getCellRight(cell) == null){
				   		cell.lowerRightCorner = true;
				   		if(cell.isGoal == false){
				   			cell.boxAllowed = false;
				   		}
				}
			
			// Check if cell is horizontal corridor.
			else if(Factory.getCellUp(cell) == null && 
					Factory.getCellDown(cell) == null && 
					Factory.getCellLeft(cell) != null &&
					Factory.getCellRight(cell) != null){
						cell.horizontalCorridor = true;
				}
			
			// Check if cell is vertical corridor.
			else if(Factory.getCellUp(cell) != null && 
					Factory.getCellDown(cell) != null && 
					Factory.getCellLeft(cell) == null &&
					Factory.getCellRight(cell) == null){
						cell.verticalCorridor = true;
				}
		}
	}
	
	/**
	 * Method that creates board from Vector<string> representation of map. 
	 */
	public void buildBoard(Vector<String> map){
		// Saving map for later printout.
		
		// Initializing some prerequisites for building board from map.
		char c;
		int xMax;
		int yMax;
		int y = 0;
		
		//Some booleans for map logic.
		boolean firstWall = false;
		
		// Check height of map.
		yMax = map.size();
		
		// Loop over string representation of map.
		// For each row in map.
		for(String mapRow: map){
			// Save to string representation of board.
			if(mapRow.length()!=0) this.map.add(new StringBuilder(mapRow));
			
			if(y != 0 && y != yMax - 1){
				// Set firstWall to false for each row.
				firstWall = false;
				
				// Checking length of string.
				xMax = mapRow.length();
				//For each column in map.
				for(int x = 0; x < xMax; x++){
					
					//if(Sokoban.debug) System.out.println(Integer.toString(x) + ", " + Integer.toString(y));
					
					// Check character at x, y position of map.
					c = map.get(y).charAt(x);
					
					// If char at x, y is white space.
					if(c == ' '){
						
						// Check if first wall of map has been encountered and
						// that the x value is not the last value of string.
						// If false we are outside of map, if true create cell.
						if(firstWall && x != xMax - 1){
							mapCells.add(Factory.createCell(x, y));
						}
					}
					
					// If char at x, y is wall create nothing.
					else if( c == '#'){
						firstWall = true;
					}
					// If char at x, y is box create map cell and box.
					else if( c == '$'){
						mapCells.add(Factory.createCell(x, y));
						initialBoxes.add(Factory.createBox(x, y));
					}
					// If char at x, y is box on goal create map cell, box and goal.
					else if( c == '*'){
						mapCells.add(Factory.createCell(x, y));
						initialBoxes.add(Factory.createBox(x, y));
						goals.add(Factory.getCell(x,y));
						Factory.getCell(x,y).isGoal = true;
					}
					// If char at x, y is player create map cell and player.
					else if( c == '@'){
						mapCells.add(Factory.createCell(x, y));
						player = Factory.createPlayer(x,y);
					}
					// If char at x, y is player on goal create map cell, goal and player.
					else if( c == '+'){
						mapCells.add(Factory.createCell(x, y));
						goals.add(Factory.getCell(x,y));
						Factory.getCell(x,y).isGoal = true;
						player = Factory.createPlayer(x,y);
					}
					// If char at x, y is goal create map cell and goal.
					else if( c == '.'){
						mapCells.add(Factory.createCell(x, y));
						goals.add(Factory.getCell(x,y));
						Factory.getCell(x,y).isGoal = true;
					}
				} // End for each column loop.
			}
			y = y + 1;
		} // End for each row loop.
	}
	
	public void printBoard(){
		for(Cell mapCell : mapCells ){
			//map.elementAt((int)mapCell.getY()).setCharAt((int)mapCell.getX(), '-');
			if(mapCell.horizontalCorridor == true){
				map.elementAt((int)mapCell.getY()).setCharAt((int)mapCell.getX(), 'h');
			}
			if(mapCell.verticalCorridor == true){
				map.elementAt((int)mapCell.getY()).setCharAt((int)mapCell.getX(), 'v');
			}
			if(mapCell.boxAllowed == false){
				map.elementAt((int)mapCell.getY()).setCharAt((int)mapCell.getX(), 'b');
			}
			if(mapCell.isGoal == true){
				map.elementAt((int)mapCell.getY()).setCharAt((int)mapCell.getX(), '.');
			}
		}
		for(StringBuilder s : map){
            System.out.println(s);
		}
	}
	
	public void printGradient(){
		for(Cell mapCell : mapCells ){
			if(mapCell.getGradient()<10)
			map.elementAt((int)mapCell.getY()).setCharAt((int)mapCell.getX(),Character.forDigit(mapCell.getGradient(), 10));
			else map.elementAt((int)mapCell.getY()).setCharAt((int)mapCell.getX(),'L');
		}
		for(StringBuilder s : map){
            System.out.println(s);
		}
	}
	
	public void printState(State state){
		for(Cell mapCell : mapCells ){
			if(mapCell.isGoal)
			map.elementAt((int)mapCell.getY()).setCharAt((int)mapCell.getX(),'.');
			else map.elementAt((int)mapCell.getY()).setCharAt((int)mapCell.getX(),' ');
		}
		for(Point box : state.getBoxes()){
			if(Factory.getCell(box).isGoal)
			map.elementAt((int)box.getY()).setCharAt((int)box.getX(),'*');
			else map.elementAt((int)box.getY()).setCharAt((int)box.getX(),'$');
		}
		for(StringBuilder s : map){
            System.out.println(s);
		}
	}
	
	public HashSet<Box> getBoxes(){
		return initialBoxes;
	}
	
	public HashSet<Cell> getGoals(){
		return goals;
	}
	
	public Player getPlayer(){
		return player;
	}
}