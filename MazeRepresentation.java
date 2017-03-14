/* CS76, Fall 2016
 * Thursday, 29 September 2016
 * author: Raunak Bhojwani
 *
 * MazeRepresentation.java for Mazeworld
 * Credit: Idea taken from Piazza comment, help on reading Java files found on StackOverflow
 * 
 * This class attempts to represent a .txt file of a maze
 * as a mazeTable - a 2 dimensional array of chars that represents the maze
 */
package mazeworld;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MazeRepresentation {
	final static Charset ENCODING = StandardCharsets.UTF_8; // Encoding to help read lines of txt file
	
	public int numColumns;
	public int numRows;
	
	private char[][] mazeTable;


	public static MazeRepresentation readMazeTxt (String filename) {
		MazeRepresentation maze = new MazeRepresentation();

		try {
			List<String> lines = readFile(filename);
			maze.numRows = lines.size(); // number of lines in txt file is no. of rows of maze
			int i = 0;
			
			maze.mazeTable = new char[maze.numRows][]; // develop the maze representative table accordingly
			
			for (String line : lines) {
				maze.numColumns = line.length(); // line length indicates number of columns
				maze.mazeTable[maze.numRows - i - 1] = new char[maze.numColumns]; // develop the maze representative table accordingly
				for (int x = 0; x < line.length(); x++) {
					maze.mazeTable[maze.numRows - i - 1][x] = line.charAt(x); //copy what is in maze to appropriate part of mazeTable
				}
				i++;
			}

			return maze;
		} 
		
		catch (IOException E) {
			return null;
		}
	}

	// Important function to check if robots are colliding
	public char getCoordinate(int x, int y) {
		return mazeTable[y][x];
	}
	
	// Check if the current coordinate (x,y) is within the confines of the maze and if it is an empty tile
	public boolean isLegal(int x, int y) {
		if(x >= 0 && y >= 0 && x < numColumns && y < numRows) {
			return getCoordinate(x, y) == '.';
		}
		return false;
	}
	
	// Reading
	private static List<String> readFile(String fileName) throws IOException {
		Path filePath = Paths.get(fileName);
		return Files.readAllLines(filePath, ENCODING);
	}
	
	// If you want to print the maze
	public String toString() {
		String s = "";
		for (int y = 0; y < numRows; y++) {
			for (int x = 0; x < numColumns; x++) {
				s += mazeTable[y][x];
			}
			s += "\n";
		}
		return s;
	}

	public static void main(String args[]) {
		MazeRepresentation maze = MazeRepresentation.readMazeTxt("complex.txt"); // or huge.txt, easy.txt, corridor.txt, empty.txt
	}

}