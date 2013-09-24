package sokoban;

/*
 * Client 
 * 
 * Version 0.1
 * 
 * Contains or imports Sokoban maps from file server
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Client {

	/**
	 * Constructor
	 * 
	 * Initializes standard Client
	 */
	public Client(){
		
	}
	
	/**
	 * Returns the Map (class) representation from predefined FilePath
	 * 
	 * @return Map
	 * @throws IOException
	 */
	public Board getBoardFromFile() throws IOException{
		
		Vector<String> board = new Vector<String>();

		BufferedReader fileBr = new BufferedReader(
				new FileReader("/Users/andershuss/Documents/JavaWS/BPT/Boards/00_sample.in"));
		
		String line = null;

		while(fileBr.ready()) {
			line = fileBr.readLine();
			board.add(line);
		} // End while
		fileBr.close();
		
		return new Board(board);
	}
	
	/**
	 * Returns the Map (class) representation from specified file
	 * 
	 * @param pFilePath
	 * @return Map
	 * @throws IOException
	 */
	public Board getBoardFromFile(String pFilePath) throws IOException{
		
		Vector<String> board = new Vector<String>();

		BufferedReader fileBr = new BufferedReader(
				new FileReader(pFilePath));
		
		String line = null;
		
		while(fileBr.ready()) {
			line = fileBr.readLine();
			board.add(line);
		} // End while
		fileBr.close();
		
		return new Board(board);
	}
	
	public Board getBoardFromCollection(int pLevel) throws IOException{
		
		Vector<String> board = new Vector<String>();

		BufferedReader fileBr = new BufferedReader(
				new FileReader("/pFilePath/")); //ALLA banor...
		
		String line = null;
		
		while(fileBr.ready()) {
			line = fileBr.readLine();
			board.add(line);
		} // End while
		fileBr.close();
		
		//Plocka ut just en level...
		
		
		return new Board(board);
	}
}