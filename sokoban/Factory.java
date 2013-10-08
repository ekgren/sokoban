package sokoban;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

/**
 * NEO-SOKOBAN FACTORY CLASS
 *
 */

public class Factory {
	
	// Parameters.
	private static int estimatedMaxNumberOfStates = 500000;
	private static int startStates = 10000;
	
	// Containers of all cells, goals, boxes, states and players.
	private static Cell[][] boardCells = new Cell[50][50];
	
	// Probably remove Goals since cells already contain same info.
	private static Goal[] goals; 
	
	private static Box[] boxes = new Box[estimatedMaxNumberOfStates * 20];
	private static State[] states = new State[estimatedMaxNumberOfStates];
	private static Player[] players = new Player[estimatedMaxNumberOfStates];
	
	public static Queue<State> usedStates = new LinkedList<State>();
	public static Queue<Box> usedBoxes = new LinkedList<Box>();
	public static Queue<Player> usedPlayers = new LinkedList<Player>();
	
	// Total counts of "created" cells, goals, boxes, states and players.
	private static int totalCellCount = 0;
	private static int totalGoalCount = 0;
	private static int totalBoxCount = 0;
	private static int totalStateCount = 0;
	private static int totalExpandedStateCount = 0;
	private static int totalPlayerCount = 0;

	
	/**
	 * Method that "creates" state.
	 */
	public static State createState(){
		// This can be removed if we choose to create all at start instead.
		if(totalStateCount >= startStates) states[totalStateCount] = new State();
		
		// Return.
		if(Sokoban.debug) if( totalStateCount % 10000 == 0) 
			System.out.println(totalStateCount);
		addStateCount();
		return states[totalStateCount-1];	
	}
	
	/**
	 * Method that "creates" player from x, y coordinates.
	 */
	public static Player createPlayer(int x, int y){
		// This can be removed if we choose to create all at start instead.
		if(totalPlayerCount >= startStates) players[totalPlayerCount] = new Player();
		

		players[totalPlayerCount].setLocation(x, y);
		addPlayerCount();
		return players[totalPlayerCount-1];
	}
	
	/**
	 * Method that "creates" player from Point object.
	 */
	public static Player createPlayer(Point p){
		// This can be removed if we choose to create all at start instead.
		if(totalPlayerCount >= startStates) players[totalPlayerCount] = new Player();

		players[totalPlayerCount].setLocation(p.x, p.y);
		addPlayerCount();
		return players[totalPlayerCount-1];

	}
	
	/**
	 * Method that "creates" box.
	 */
	public static Box createBox(Point p){
		// This can be removed if we choose to create all at start instead.
		if(totalBoxCount >= startStates*3) boxes[totalBoxCount] = new Box();

		// Set x and y then return.
		boxes[totalBoxCount].setLocation(p.x, p.y);
		addBoxCount();
		return boxes[totalBoxCount-1];
	}
	
	/**
	 * Method that "creates" box.
	 */
	public static Box createBox(int x, int y){
		// This can be removed if we choose to create all at start instead.
		if(totalBoxCount >= startStates*3) boxes[totalBoxCount] = new Box();

		// Set x and y then return.
		boxes[totalBoxCount].setLocation(x, y);
		addBoxCount();
		return boxes[totalBoxCount-1];
	}
	
	/**
	 * Method that "creates" cell.
	 */
	public static Cell createCell(int x, int y){
		// This can be removed if we choose to create all at start instead.
		boardCells[x][y] = new Cell();
		
		// Set x and y then return.
		boardCells[x][y].setLocation(x, y);
		addCellCount();
		return boardCells[x][y];
	}
	
	/**
	 * Method that returns cell at x, y position.
	 */
	public static Cell getCell(int x, int y){
		return boardCells[x][y];
	}
	
	/**
	 * Method that returns cell at x, y position.
	 */
	public static Cell getCell(Point p){
		return boardCells[p.x][p.y];
	}
	
	/**
	 * Method that returns cell at x, y - 1 position.
	 */
	public static Cell getCellUp(int x, int y){
		return boardCells[x][y - 1];
	}
	
	/**
	 * Method that returns cell at x, y - 1 position.
	 */
	public static Cell getCellUp(Cell cell){
		return boardCells[cell.x][cell.y - 1];
	}
	
	/**
	 * Method that returns cell at x, y - 1 position.
	 */
	public static Cell getCellUp(Point point){
		return boardCells[point.x][point.y - 1];
	}
	
	/**
	 * Method that returns cell at x, y + 1 position.
	 */
	public static Cell getCellDown(int x, int y){
		return boardCells[x][y + 1];
	}
	
	/**
	 * Method that returns cell at x, y + 1 position.
	 */
	public static Cell getCellDown(Cell cell){
		return boardCells[cell.x][cell.y + 1];
	}
	
	/**
	 * Method that returns cell at x, y + 1 position.
	 */
	public static Cell getCellDown(Point point){
		return boardCells[point.x][point.y + 1];
	}
	
	/**
	 * Method that returns cell at x - 1, y position.
	 */
	public static Cell getCellLeft(int x, int y){
		return boardCells[x - 1][y];
	}
	
	/**
	 * Method that returns cell at x - 1, y position.
	 */
	public static Cell getCellLeft(Cell cell){
		return boardCells[cell.x - 1][cell.y];
	}
	
	/**
	 * Method that returns cell at x - 1, y position.
	 */
	public static Cell getCellLeft(Point point){
		return boardCells[point.x - 1][point.y];
	}
	
	/**
	 * Method that returns cell at x + 1, y position.
	 */
	public static Cell getCellRight(int x, int y){
		return boardCells[x + 1][y];
	}
	
	/**
	 * Method that returns cell at x + 1, y position.
	 */
	public static Cell getCellRight(Cell cell){
		return boardCells[cell.x + 1][cell.y];
	}
	
	/**
	 * Method that returns cell at x + 1, y position.
	 */
	public static Cell getCellRight(Point point){
		return boardCells[point.x + 1][point.y];
	}
	
	/**
	 * Method that "creates" goal.
	 */
	public static Goal createGoal(int x, int y){
		// This can be removed if we choose to create all at start instead.
		goals[totalGoalCount] = new Goal();
		
		// Set x and y then return.
		goals[totalGoalCount].setLocation(x, y);
		addGoalCount();
		return goals[totalGoalCount-1];
	}
	
	/** Method that creates goals in all places in goals array. */
	public static void initializeGoals(){
		for (int i = 0; i < goals.length; i++) {
			goals[i] = new Goal();
			}
	}
	
	/** Method that creates boxes in all places in boxes array. */
	public static void initializeBoxes(){
		for (int i = 0; i < startStates*3; i++) {
			boxes[i] = new Box();
			}
	}
	
	/** Method that creates states in all places in states array. */
	public static void initializeStates(){
		for (int i = 0; i < startStates; i++) {
			states[i] = new State();
			}
	}
	
	/** Method that creates players in all places in players array. */
	public static void initializePlayers(){
		for (int i = 0; i < startStates; i++) {
			players[i] = new Player();
			}
	}
	
	/** Method that adds one to the total cell count. */
	public static void addCellCount(){
		totalCellCount = totalCellCount + 1;
	}
	
	/** Method that returns total cell count */
	public static int getCellCount(){
		return totalCellCount;
	}
	
	/** Method that adds one to the total goal count. */
	public static void addGoalCount(){
		totalGoalCount = totalGoalCount + 1;
	}
	
	/** Method that returns total goal count */
	public static int getGoalCount(){
		return totalGoalCount;
	}
	
	/** Method that adds one to the total box count. */
	public static void addBoxCount(){
		totalBoxCount = totalBoxCount + 1;
	}
	
	/** Method that returns total box count */
	public static int getBoxCount(){
		return totalBoxCount;
	}
	
	/** Method that adds one to the total state count. */
	public static void addStateCount(){
		totalStateCount = totalStateCount + 1;
	}
	
	/** Method that adds one to the total state count. */
	public static void addExpandedStateCount(){
		totalExpandedStateCount = totalExpandedStateCount + 1;
	}
	
	/** Method that returns total state count */
	public static int getStateCount(){
		return totalStateCount;
	}
	
	/** Method that returns total state count */
	public static int getExpandedStateCount(){
		return totalExpandedStateCount;
	}
	
	/** Method that returns total bananas. */
	public static int getCreatedStates(){
		return estimatedMaxNumberOfStates;
	}
	
	/** Method that adds one to the total player count. */
	public static void addPlayerCount(){
		totalPlayerCount = totalPlayerCount + 1;
	}
	
	/** Method that returns total player count */
	public static int getPlayerCount(){
		return totalPlayerCount;
	}

}