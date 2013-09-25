package solutionEvaluator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import sokoban.Sokoban;

public class MapReader {
	
	private String filePath = "./sokoban/res/all.slc";
	private int mapNo;
	private int mapCount = 0;
	private boolean thisMap = false;
	
	public MapReader(int mapNo) throws IOException{
		this.mapNo = mapNo;
		
		BufferedReader test = new BufferedReader(new FileReader(filePath));
		while(true){
			String line = test.readLine();
			if(line.charAt(0) == ';') mapCount = mapCount + 1;
			else if(mapCount == mapNo) System.out.println(line);
			else if(mapCount > mapNo) break;
		}
	}
	
	public static void main(String[] args) throws IOException {
		MapReader MAP = new MapReader(21);
	} // main
}
