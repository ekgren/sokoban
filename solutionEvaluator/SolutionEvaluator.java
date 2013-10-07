package solutionEvaluator;

import sokoban.Sokoban;

import java.io.*;
import java.util.Vector;

/**
 * Class to simulate Kattis and evaluate our sokoban solver.
 *
 */

public class SolutionEvaluator {
	private static Sokoban path;
	private Vector<StringBuilder> board = new Vector<StringBuilder>();

    // Insert Level number (0 = TestTrack)

    private int levelNo = 0;
    private MapReader map;

	public SolutionEvaluator() throws IOException{
		System.out.println("Initializing evaluation... \n");


		// Initializing sokoban solver object with map from mapReader.
        map = new MapReader(levelNo + 1);
        
        long startTime = System.currentTimeMillis();
        // Try to solve
		path = new Sokoban(new StringReader(map.getMap()), true);
		System.out.println("\nTime to execute: " + Double.toString((System.currentTimeMillis() - startTime)/1000.0) + " seconds.\n");
        // Animate solution
		//animateSolution();
	}
	
	public void animateSolution() throws FileNotFoundException, IOException{
		
		// Method to animate solution to see if it's correct!
		
		BufferedReader fileBr = new BufferedReader(new StringReader(map.getMap()));
		
		String line;
		boolean STATE = false;
		
		int[] xy = new int[2];
		int[] player = new int[2];
		
		while((line = fileBr.readLine()) != null) {
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
		    	if(board.elementAt(player[1]).charAt(player[0])=='+')
					board.elementAt(player[1]).setCharAt(player[0], '.');
				else
					board.elementAt(player[1]).setCharAt(player[0], ' ');
				player[1] = player[1] - 1;
				if(board.elementAt(player[1]).charAt(player[0])=='.')
					board.elementAt(player[1]).setCharAt(player[0], '+');
				else
					board.elementAt(player[1]).setCharAt(player[0], '@');
		    	}
		    else if(c=='d'){
		    	if(board.elementAt(player[1]).charAt(player[0])=='+')
					board.elementAt(player[1]).setCharAt(player[0], '.');
				else
					board.elementAt(player[1]).setCharAt(player[0], ' ');
				player[1] = player[1] + 1;
				if(board.elementAt(player[1]).charAt(player[0])=='.')
					board.elementAt(player[1]).setCharAt(player[0], '+');
				else
					board.elementAt(player[1]).setCharAt(player[0], '@');
		    	}
		    else if(c=='l'){
		    	if(board.elementAt(player[1]).charAt(player[0])=='+')
					board.elementAt(player[1]).setCharAt(player[0], '.');
				else
					board.elementAt(player[1]).setCharAt(player[0], ' ');
				player[0] = player[0] - 1;
				if(board.elementAt(player[1]).charAt(player[0])=='.')
					board.elementAt(player[1]).setCharAt(player[0], '+');
				else
					board.elementAt(player[1]).setCharAt(player[0], '@');
		    	}
		    else if(c=='r'){
		    	if(board.elementAt(player[1]).charAt(player[0])=='+')
					board.elementAt(player[1]).setCharAt(player[0], '.');
				else
					board.elementAt(player[1]).setCharAt(player[0], ' ');
				player[0] = player[0] + 1;
				if(board.elementAt(player[1]).charAt(player[0])=='.')
					board.elementAt(player[1]).setCharAt(player[0], '+');
				else
					board.elementAt(player[1]).setCharAt(player[0], '@');
		    	}
		    else if(c=='U'){
		    	if(board.elementAt(player[1]).charAt(player[0])=='+')
					board.elementAt(player[1]).setCharAt(player[0], '.');
				else
					board.elementAt(player[1]).setCharAt(player[0], ' ');
				player[1] = player[1] - 1;
				if(board.elementAt(player[1]).charAt(player[0])=='*')
					board.elementAt(player[1]).setCharAt(player[0], '+');
				else
					board.elementAt(player[1]).setCharAt(player[0], '@');
				if(board.elementAt(player[1] - 1).charAt(player[0])=='.'){
					board.elementAt(player[1] - 1).setCharAt(player[0], '*');
				} else {
					board.elementAt(player[1] - 1).setCharAt(player[0], '$');
				}
		    	}
		    else if(c=='D'){
		    	if(board.elementAt(player[1]).charAt(player[0])=='+')
					board.elementAt(player[1]).setCharAt(player[0], '.');
				else
					board.elementAt(player[1]).setCharAt(player[0], ' ');
				player[1] = player[1] + 1;
				if(board.elementAt(player[1]).charAt(player[0])=='*')
					board.elementAt(player[1]).setCharAt(player[0], '+');
				else
					board.elementAt(player[1]).setCharAt(player[0], '@');
				if(board.elementAt(player[1] + 1).charAt(player[0])=='.'){
					board.elementAt(player[1] + 1).setCharAt(player[0], '*');
		    	} else {
		    		board.elementAt(player[1] + 1).setCharAt(player[0], '$');
		    	}
		    	}
		    else if(c=='L'){
		    	if(board.elementAt(player[1]).charAt(player[0])=='+')
					board.elementAt(player[1]).setCharAt(player[0], '.');
				else
					board.elementAt(player[1]).setCharAt(player[0], ' ');
				player[0] = player[0] - 1;
				if(board.elementAt(player[1]).charAt(player[0])=='*')
					board.elementAt(player[1]).setCharAt(player[0], '+');
				else
					board.elementAt(player[1]).setCharAt(player[0], '@');
				if(board.elementAt(player[1]).charAt(player[0] - 1)=='.'){
					board.elementAt(player[1]).setCharAt(player[0] - 1, '*');
		    	} else {
		    		board.elementAt(player[1]).setCharAt(player[0] - 1, '$');
		    	}
		    	}
		    else if(c=='R'){
		    	if(board.elementAt(player[1]).charAt(player[0])=='+')
					board.elementAt(player[1]).setCharAt(player[0], '.');
				else
					board.elementAt(player[1]).setCharAt(player[0], ' ');
				player[0] = player[0] + 1;
				if(board.elementAt(player[1]).charAt(player[0])=='*')
					board.elementAt(player[1]).setCharAt(player[0], '+');
				else
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
            Thread.sleep(100);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
	
	public static void main(String[] args) throws IOException{
		SolutionEvaluator MapEval = new SolutionEvaluator();
        System.out.println(path.solution.toUpperCase());
	}
}
