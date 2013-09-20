/**
 * BoxMove
 * 
 * Version 0.1
 * 
 * THIS CLASS IS PROBABLY NOT NEEDED
 * 
 * Encapsulates a BOX move not to confuse with player move!
 */

public class BoxMove {

	private int moveType;
	private int whichBox;

	/**
	 * 
	 * @param pType 
	 * Should be one of the following: MOVE_BOG, MOVE_UP, MOVE_DOWN, MOVE_RIGHT, MOVE_LEFT.
	 */
	public BoxMove(MoveType pType, int pWhichBox) {
		this.moveType = pType.getInternalValue();
		this.whichBox = pWhichBox;
	} // End constructor Move


	public enum MoveType {
		MOVE_BOG(0), //only used in initial state
		MOVE_UP(1),
		MOVE_DOWN(2),
		MOVE_RIGHT(3),
		MOVE_LEFT(4);


		private int internalValue;

		MoveType(int pValue) {
			this.internalValue = pValue;
		} // End constructor MoveType

		public int getInternalValue() {
			return this.internalValue;
		} // End getInternalValue
	} // End MoveType
} // End class BoxMove
