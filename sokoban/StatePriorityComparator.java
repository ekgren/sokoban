package sokoban;

import java.util.Comparator;

public class StatePriorityComparator implements Comparator<State>{

	private double h1;
	private double h2;

	
	@Override
	public int compare(State pState1, State pState2) {

		h1 = pState1.getH();
		h2 = pState2.getH();
		
		if (h1 < h2){
			return -1; //move forward
		}
		else if (h2 < h1){
			return 1;
		}
		return 0;
	}
}