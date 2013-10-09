package sokoban;

import java.awt.Point;

public class Deadlocks {
	/**
	 * Checks for deadlocks like these.
	 * 
	 * #    #   
	 * #$  $#  ####   $$
	 * #$  $#   $$   ####
	 * #    #
	 * 
	 * @param state
	 * @return
	 */
	public static boolean checkWallDeadlock(State state){
		for(Point box : state.getBoxes()){
			if(state.getBoxes().contains(Factory.getCellUp(box))){
				if(Factory.getCell(box).wallLeft == true && Factory.getCellUp(box).wallLeft == true &&
					( (Factory.getCell(box).isGoal == false && Factory.getCellUp(box).isGoal == false) || 
					  (Factory.getCell(box).isGoal == true && Factory.getCellUp(box).isGoal == false) ||
					  (Factory.getCell(box).isGoal == false && Factory.getCellUp(box).isGoal == true))) return true;
				if(Factory.getCell(box).wallRight == true && Factory.getCellUp(box).wallRight == true &&
					( (Factory.getCell(box).isGoal == false && Factory.getCellUp(box).isGoal == false) || 
					  (Factory.getCell(box).isGoal == true && Factory.getCellUp(box).isGoal == false) ||
					  (Factory.getCell(box).isGoal == false && Factory.getCellUp(box).isGoal == true))) return true;
			}
			if(state.getBoxes().contains(Factory.getCellDown(box))){
				if(Factory.getCell(box).wallLeft && Factory.getCellDown(box).wallLeft && 
					( (Factory.getCell(box).isGoal == false && Factory.getCellDown(box).isGoal == false) || 
					  (Factory.getCell(box).isGoal == true && Factory.getCellDown(box).isGoal == false) ||
					  (Factory.getCell(box).isGoal == false && Factory.getCellDown(box).isGoal == true))) return true;
				if(Factory.getCell(box).wallRight && Factory.getCellDown(box).wallRight &&
					( (Factory.getCell(box).isGoal == false && Factory.getCellDown(box).isGoal == false) || 
					  (Factory.getCell(box).isGoal == true && Factory.getCellDown(box).isGoal == false) ||
					  (Factory.getCell(box).isGoal == false && Factory.getCellDown(box).isGoal == true))) return true;
			}
			if(state.getBoxes().contains(Factory.getCellLeft(box))){
				if(Factory.getCell(box).wallUp && Factory.getCellLeft(box).wallUp &&
					( (Factory.getCell(box).isGoal == false && Factory.getCellLeft(box).isGoal == false) || 
					  (Factory.getCell(box).isGoal == true && Factory.getCellLeft(box).isGoal == false) ||
					  (Factory.getCell(box).isGoal == false && Factory.getCellLeft(box).isGoal == true))) return true;
				if(Factory.getCell(box).wallDown && Factory.getCellLeft(box).wallDown &&
					( (Factory.getCell(box).isGoal == false && Factory.getCellLeft(box).isGoal == false) || 
					  (Factory.getCell(box).isGoal == true && Factory.getCellLeft(box).isGoal == false) ||
					  (Factory.getCell(box).isGoal == false && Factory.getCellLeft(box).isGoal == true))) return true;
			}
			if(state.getBoxes().contains(Factory.getCellRight(box))){
				if(Factory.getCell(box).wallUp && Factory.getCellRight(box).wallUp &&
					( (Factory.getCell(box).isGoal == false && Factory.getCellRight(box).isGoal == false) || 
					  (Factory.getCell(box).isGoal == true && Factory.getCellRight(box).isGoal == false) ||
					  (Factory.getCell(box).isGoal == false && Factory.getCellRight(box).isGoal == true))) return true; 
				if(Factory.getCell(box).wallDown && Factory.getCellRight(box).wallDown && 
					( (Factory.getCell(box).isGoal == false && Factory.getCellRight(box).isGoal == false) || 
					  (Factory.getCell(box).isGoal == true && Factory.getCellRight(box).isGoal == false) ||
					  (Factory.getCell(box).isGoal == false && Factory.getCellRight(box).isGoal == true))) return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Checks for four boxes together.
	 * 
	 *  $$
	 *  $$
	 * 
	 * @param state
	 * @return
	 */
	public static boolean checkFourBoxesDeadlock(State state){
		for(Point box : state.getBoxes()){
			if(Factory.getCell(box).wallLeft == false && Factory.getCell(box).wallUp == false &&
				Factory.getCellLeft(box).wallUp == false && state.getBoxes().contains(Factory.getCellLeft(box)) &&
				state.getBoxes().contains(Factory.getCellUp(box)) 
				&& state.getBoxes().contains(Factory.getCellLeft(Factory.getCellUp(box)))){
					//if(Sokoban.debug) Sokoban.board.printState(state);
					return true;
			}
			if(Factory.getCell(box).wallRight == false && Factory.getCell(box).wallUp == false &&
				Factory.getCellRight(box).wallUp == false && state.getBoxes().contains(Factory.getCellRight(box)) &&
				state.getBoxes().contains(Factory.getCellUp(box)) 
				&& state.getBoxes().contains(Factory.getCellRight(Factory.getCellUp(box)))){
					//if(Sokoban.debug) Sokoban.board.printState(state);
					return true;
			}
			if(Factory.getCell(box).wallLeft == false && Factory.getCell(box).wallDown == false &&
				Factory.getCellLeft(box).wallDown == false && state.getBoxes().contains(Factory.getCellLeft(box)) &&
				state.getBoxes().contains(Factory.getCellDown(box)) 
				&& state.getBoxes().contains(Factory.getCellLeft(Factory.getCellDown(box)))){
					//if(Sokoban.debug) Sokoban.board.printState(state);
					return true;
			}
			if(Factory.getCell(box).wallRight == false && Factory.getCell(box).wallDown == false &&
				Factory.getCellRight(box).wallDown == false && state.getBoxes().contains(Factory.getCellRight(box)) &&
				state.getBoxes().contains(Factory.getCellDown(box)) 
				&& state.getBoxes().contains(Factory.getCellRight(Factory.getCellDown(box)))){
					//if(Sokoban.debug) Sokoban.board.printState(state);
					return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Checks for deadlock around corner.
	 * 
	 *   $$  $$    #    #
	 *  ##$  $##  ##$  $##
	 *   #    #    $$  $$
	 *   
	 * @param state
	 * @return
	 */
	public static boolean checkThreeBoxesCornerDeadlock(State state){
		for(Point box : state.getBoxes()){
			if(Factory.getCell(box).wallLeft == false && Factory.getCell(box).wallUp == false &&
				Factory.getCellLeft(box).wallUp == true && state.getBoxes().contains(Factory.getCellLeft(box)) &&
				state.getBoxes().contains(Factory.getCellUp(box))){
					//if(Sokoban.debug) Sokoban.board.printState(state);
					return true;
			}
			if(Factory.getCell(box).wallRight == false && Factory.getCell(box).wallUp == false &&
				Factory.getCellRight(box).wallUp == true && state.getBoxes().contains(Factory.getCellRight(box)) &&
				state.getBoxes().contains(Factory.getCellUp(box))){
					//if(Sokoban.debug) Sokoban.board.printState(state);
					return true;
			}
			if(Factory.getCell(box).wallLeft == false && Factory.getCell(box).wallDown == false &&
				Factory.getCellLeft(box).wallDown == true && state.getBoxes().contains(Factory.getCellLeft(box)) &&
				state.getBoxes().contains(Factory.getCellDown(box)) 
				&& state.getBoxes().contains(Factory.getCellLeft(Factory.getCellDown(box)))){
					//if(Sokoban.debug) Sokoban.board.printState(state);
					return true;
			}
			if(Factory.getCell(box).wallRight == false && Factory.getCell(box).wallDown == false &&
				Factory.getCellRight(box).wallDown == true && state.getBoxes().contains(Factory.getCellRight(box)) &&
				state.getBoxes().contains(Factory.getCellDown(box))){
					//if(Sokoban.debug) Sokoban.board.printState(state);
					return true;
			}
		}
		return false;
	}
}
