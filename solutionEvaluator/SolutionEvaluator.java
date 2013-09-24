package solutionEvaluator;

import sokoban.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

public class SolutionEvaluator {

	public SolutionEvaluator() throws IOException{
		System.out.println("Initializing evaluation.");
		long timeStart = System.currentTimeMillis();	
		String file = "C:/Alpha/eclipse/sokobanGit/sokoban/res/00_sample.in";
		Sokoban path = new Sokoban(new FileReader(file));
		System.out.println((timeStart - System.currentTimeMillis()));
	}
	
	public static void main(String[] args) throws IOException{
		SolutionEvaluator MapEval = new SolutionEvaluator();
	}
}
