package solutionEvaluator;

import sokoban.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;

/**
 * 
 * Class to simulate Kattis and evaluate our sokoban solver.
 * 
 * @author Ariel Ekgren
 *
 */

public class SolutionEvaluator {
	private Sokoban path;
	private String filePath;
	private Vector<StringBuilder> board = new Vector<StringBuilder>();

	public SolutionEvaluator() throws IOException{
		System.out.println("Initializing evaluation... \n");
		long startTime = System.currentTimeMillis();
		
		filePath = "./sokoban/res/TestTrack.in";
		
		// Initializing sokoban solver object with map from file.
		path = new Sokoban(new FileReader(filePath), false);
		
		System.out.println("\nTime to execute: " + Double.toString((System.currentTimeMillis() - startTime)/1000.0) + " seconds.\n");
		animateSolution();
	}
	
	public void animateSolution() throws FileNotFoundException, IOException{
		
		
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
            System.out.println(board.size());
			board.add(new StringBuilder(line));
		} // End while
		//fileBr.close();
		
		//board.elementAt(processNode.y).setCharAt(processNode.x, 'O');
		
		for (int i = 0; i < path.solution.length(); i++){
		    char c = path.solution.charAt(i);
		    System.out.println(c);
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
	}
}
