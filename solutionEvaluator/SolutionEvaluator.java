package solutionEvaluator;

import sokoban.Sokoban;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
 * 
 * Class to simulate Kattis and evaluate our sokoban solver.
 * 
 * @author Ariel Ekgren
 *
 */

public class SolutionEvaluator {
	private static Sokoban path;
	private String filePath;
	private Vector<StringBuilder> board = new Vector<StringBuilder>();

    // Insert Level number
    private MapReader map = new MapReader(31);

	public SolutionEvaluator() throws IOException{
		System.out.println("Initializing evaluation... \n");
		long startTime = System.currentTimeMillis();
		
		filePath = "./sokoban/res/TestTrack.in";

		// Initializing sokoban solver object with map from file.
		// path = new Sokoban(new StringReader(map.getMap()), false);
		path = new Sokoban(new FileReader(filePath), false);
		
		System.out.println("\nTime to execute: " + Double.toString((System.currentTimeMillis() - startTime)/1000.0) + " seconds.\n");
		animateSolution();
	}
	
	public void animateSolution() throws FileNotFoundException, IOException{

		//BufferedReader fileBr = new BufferedReader(new StringReader(map.getMap()));
		BufferedReader fileBr = new BufferedReader(new FileReader(filePath));
		
		String line = null;
		boolean STATE = false;
		
		int[] xy = new int[2];
		int[] player = new int[2];
		
		while(fileBr.ready()) {
			line = fileBr.readLine();
			if(STATE == false){
                xy[0] = line.indexOf("+");
                if ( xy[0] != -1 ) {
                	player[0] = xy[0];
                    player[1] = xy[1];
                    STATE = true;
                }
        
                xy[0] = line.indexOf("@");
                if ( xy[0] != -1 ) {
                        player[0] = xy[0];
                        player[1] = xy[1];
                        STATE = true;
                }
                xy[1] += 1;
			}
            //System.out.println(board.size());
			if(line.length()!=0) board.add(new StringBuilder(line));
		} // End while
		fileBr.close();
		
		printMap();
		for (int i = 0; i < path.solution.length(); i++){
		    char c = path.solution.charAt(i);
		    if(c=='u'){
		    	board.elementAt(player[1]).setCharAt(player[0], ' ');
				player[1] = player[1] - 1;
				board.elementAt(player[1]).setCharAt(player[0], '@');
		    	}
		    else if(c=='d'){
		    	board.elementAt(player[1]).setCharAt(player[0], ' ');
				player[1] = player[1] + 1;
				board.elementAt(player[1]).setCharAt(player[0], '@');
		    	}
		    else if(c=='l'){
		    	board.elementAt(player[1]).setCharAt(player[0], ' ');
				player[0] = player[0] - 1;
				board.elementAt(player[1]).setCharAt(player[0], '@');
		    	}
		    else if(c=='r'){
		    	board.elementAt(player[1]).setCharAt(player[0], ' ');
				player[0] = player[0] + 1;
				board.elementAt(player[1]).setCharAt(player[0], '@');
		    	}
		    else if(c=='U'){
		    	board.elementAt(player[1]).setCharAt(player[0], ' ');
				player[1] = player[1] - 1;
				board.elementAt(player[1]).setCharAt(player[0], '@');
				if(board.elementAt(player[1] - 1).charAt(player[0])=='.'){
					board.elementAt(player[1] - 1).setCharAt(player[0], '*');
				} else {
					board.elementAt(player[1] - 1).setCharAt(player[0], '$');
				}
		    	}
		    else if(c=='D'){
		    	board.elementAt(player[1]).setCharAt(player[0], ' ');
				player[1] = player[1] + 1;
				board.elementAt(player[1]).setCharAt(player[0], '@');
				if(board.elementAt(player[1] + 1).charAt(player[0])=='.'){
					board.elementAt(player[1] + 1).setCharAt(player[0], '*');
		    	} else {
		    		board.elementAt(player[1] + 1).setCharAt(player[0], '$');
		    	}
		    	}
		    else if(c=='L'){
		    	board.elementAt(player[1]).setCharAt(player[0], ' ');
				player[0] = player[0] - 1;
				board.elementAt(player[1]).setCharAt(player[0], '@');
				if(board.elementAt(player[1]).charAt(player[0] - 1)=='.'){
					board.elementAt(player[1]).setCharAt(player[0] - 1, '*');
		    	} else {
		    		board.elementAt(player[1]).setCharAt(player[0] - 1, '$');
		    	}
		    	}
		    else if(c=='R'){
		    	board.elementAt(player[1]).setCharAt(player[0], ' ');
				player[0] = player[0] + 1;
				board.elementAt(player[1]).setCharAt(player[0], '@');
				if(board.elementAt(player[1]).charAt(player[0] + 1)=='.'){
					board.elementAt(player[1]).setCharAt(player[0] + 1, '*');
		    	} else {
		    		board.elementAt(player[1]).setCharAt(player[0] + 1, '$');
		    	}
		    	}
		    printMap();
			}
		
	}
	
    public void printMap(){
        for(StringBuilder s : board){
                System.out.println(s);
        }
        try {
            Thread.sleep(300);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
	
	public static void main(String[] args) throws IOException{
		SolutionEvaluator MapEval = new SolutionEvaluator();
        System.out.println(path.solution.toUpperCase());
	}
}
