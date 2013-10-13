package sokoban;

import java.awt.Point;

/**
 * NEO-SOKOBAN FACTORY CLASS
 *
 */

public class Factory {
	
	// Array sizes depend on this number.
	private static int estimatedMaxNumberOfStates = 500000;
	
	// Number of states created at start depend on this number.
	private static int startStates = 0;
	
	// Containers of all cells, boxes, states and players.
	private static Cell[][] boardCells = new Cell[50][50];
	private static Box[] boxes = new Box[estimatedMaxNumberOfStates * 20];
	private static State[] states = new State[estimatedMaxNumberOfStates];
	private static Player[] players = new Player[estimatedMaxNumberOfStates];
	
	// Total counts of "created" cells, boxes, states and players.
	private static int totalCellCount = 0;
	private static int totalBoxCount = 0;
	private static int totalStateCount = 0;
	private static int totalPlayerCount = 0;
	
	// 
	public static int expandedNodes = 0;
	public static int createdNodes = 0;

	
	/**
	 * Method that "creates" state.
	 * @return
	 */
	public static State createState(){
		// Create new player if we are out of premade ones.
		if(totalStateCount >= startStates) states[totalStateCount] = new State();
		
		// If debug print every 10000 state.
		if(Sokoban.debug) if( totalStateCount % 10000 == 0) 
			System.out.println(totalStateCount);
		
		// Return.
		addStateCount();
		return states[totalStateCount-1];	
	}
	
	
	/**
	 * Method that "creates" player from x, y coordinates.
	 * @param x
	 * @param y
	 * @return
	 */
	public static Player createPlayer(int x, int y){
		// Create new player if we are out of premade ones.
		if(totalPlayerCount >= startStates) players[totalPlayerCount] = new Player();
		
		// Set x and y then return.
		players[totalPlayerCount].setLocation(x, y);
		addPlayerCount();
		return players[totalPlayerCount-1];
	}
	
	
	/**
	 * Method that "creates" player from Point object.
	 * @param p
	 * @return
	 */
	public static Player createPlayer(Point p){
		// Create new player if we are out of premade ones.
		if(totalPlayerCount >= startStates) players[totalPlayerCount] = new Player();
		
		// Set x and y then return.
		players[totalPlayerCount].setLocation(p.x, p.y);
		addPlayerCount();
		return players[totalPlayerCount-1];

	}
	
	
	/**
	 * Method that "creates" box from Point object.
	 * @param p
	 * @return
	 */
	public static Box createBox(Point p){
		// Create new state if we are out of premade ones.
		if(totalBoxCount >= startStates*3) boxes[totalBoxCount] = new Box();

		// Set x and y then return.
		boxes[totalBoxCount].setLocation(p.x, p.y);
		addBoxCount();
		return boxes[totalBoxCount-1];
	}
	

	/**
	 * Method that "creates" box from x and y coordinates.
	 * @param x
	 * @param y
	 * @return
	 */
	public static Box createBox(int x, int y){
		// Create new box if we are out of premade ones.
		if(totalBoxCount >= startStates*3) boxes[totalBoxCount] = new Box();

		// Set x and y then return.
		boxes[totalBoxCount].setLocation(x, y);
		addBoxCount();
		return boxes[totalBoxCount-1];
	}
	
	
	/**
	 * Method that "creates" cell from x and y coordinates..
	 * @param x
	 * @param y
	 * @return
	 */
	public static Cell createCell(int x, int y){

		boardCells[x][y] = new Cell();
		
		// Set x and y then return.
		boardCells[x][y].setLocation(x, y);
		addCellCount();
		return boardCells[x][y];
	}
	
	
	/**
	 * Method that returns a cell from at x and y value.
	 * @param x
	 * @param y
	 * @return
	 */
	public static Cell getCell(int x, int y){ return boardCells[x][y]; }
	
	
	/**
	 * Method that returns cell at a points x and y position.
	 * @param p
	 * @return
	 */
	public static Cell getCell(Point p){ return boardCells[p.x][p.y]; }
	
	
	/**
	 * Method that returns cell one position up from given x and y coordinates.
	 * @param x
	 * @param y
	 * @return
	 */
	public static Cell getCellUp(int x, int y){	return boardCells[x][y - 1]; }
	
	
	/**
	 * Method that returns cell at x, y - 1 position.
	 * @param point
	 * @return
	 */
	public static Cell getCellUp(Point point){ return boardCells[point.x][point.y - 1]; }
	
	
	/**
	 * Method that returns cell at x, y + 1 position.
	 * @param x
	 * @param y
	 * @return
	 */
	public static Cell getCellDown(int x, int y){ return boardCells[x][y + 1]; }
	
	
	/**
	 * Method that returns cell at x, y + 1 position.
	 * @param point
	 * @return
	 */
	public static Cell getCellDown(Point point){ return boardCells[point.x][point.y + 1]; }
	
	
	/**
	 * Method that returns cell at x - 1, y position.
	 * @param x
	 * @param y
	 * @return
	 */
	public static Cell getCellLeft(int x, int y){ return boardCells[x - 1][y]; }

	
	/**
	 * Method that returns cell at x - 1, y position.
	 * @param point
	 * @return
	 */
	public static Cell getCellLeft(Point point){ return boardCells[point.x - 1][point.y]; }
	

	/**
	 * Method that returns cell at x + 1, y position.
	 * @param x
	 * @param y
	 * @return
	 */
	public static Cell getCellRight(int x, int y){ return boardCells[x + 1][y]; }
	
	
	/**
	 * Method that returns cell at x + 1, y position.
	 * @param point
	 * @return
	 */
	public static Cell getCellRight(Point point){ return boardCells[point.x + 1][point.y]; }

	
	/**
	 * Method that creates boxes in all places in boxes array.
	 */
	public static void initializeBoxes(){
		for (int i = 0; i < startStates*3; i++) {
			boxes[i] = new Box();
			}
	}
	
	
	/**
	 * Method that creates states in all places in states array.
	 */
	public static void initializeStates(){
		for (int i = 0; i < startStates; i++) {
			states[i] = new State();
			}
	}
	
	
	/** 
	 * Method that creates players in all places in players array. 
	 */
	public static void initializePlayers(){
		for (int i = 0; i < startStates; i++) {
			players[i] = new Player();
			}
	}
	
	
	/**
	 * Method that adds one to the total cell count.
	 */
	public static void addCellCount(){ totalCellCount = totalCellCount + 1; }
	
	
	/**
	 * Method that returns total cell count.
	 * @return
	 */
	public static int getCellCount(){ return totalCellCount; }
	
	
	/** 
	 * Method that adds one to the total box count. 
	 */
	public static void addBoxCount(){ totalBoxCount = totalBoxCount + 1; }
	
	
	/**
	 * Method that returns total box count.
	 * @return
	 */
	public static int getBoxCount(){ return totalBoxCount; }
	
	
	/** 
	 * Method that adds one to the total state count. 
	 */
	public static void addStateCount(){	totalStateCount = totalStateCount + 1; }
	
	
	/**
	 * Method that returns total state count.
	 * @return
	 */
	public static int getStateCount(){ return totalStateCount; }
	
	
	/**
	 * Method that returns total bananas.
	 * @return
	 */
	public static int getCreatedStates(){ return estimatedMaxNumberOfStates; }
	
	
	/** 
	 * Method that adds one to the total player count.
	 */
	public static void addPlayerCount(){ totalPlayerCount = totalPlayerCount + 1; }
	
	
	/**
	 * Method that returns total player count.
	 * @return
	 */
	public static int getPlayerCount(){	return totalPlayerCount; }
}