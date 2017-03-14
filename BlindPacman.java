/* CS76, Fall 2016
 * Thursday, 29 September 2016
 * author: Raunak Bhojwani
 *
 * BlindPacman.java for Mazeworld
 *
 * This class extends SearchProblem to create a relatively abstract 
 * representation of the blind robot problem
 * 
 * This class is loosely based on MultiRobotCoordination.java and there are
 * similarities in structure and functions. I did this to make sure
 * the way I approach such problems is consistent.
 */
package mazeworld;

import java.util.*;


public class BlindPacman extends SearchProblem {

	// Constants to describe each of the available moves to each of the robots
	public static int[] NORTH = {0, 1};
	public static int[] EAST = {1, 0};
	public static int[] SOUTH = {0, -1};
	public static int[] WEST = {-1, 0};
	public static int[] DONTMOVE = {0, 0};
    private static int moves[][] = {NORTH, EAST, SOUTH, WEST, DONTMOVE};
    
    // Use ints to represent the current position, as well as the edges of the maze
    private static int startX, startY, farthestEast, farthestWest, farthestSouth, farthestNorth;
    private MazeRepresentation maze;
    private static double greatestLength;
    
    private static int[] goalState;

    public BlindPacman(MazeRepresentation maze, int x, int y, int[] goal) {
        this.maze = maze;
        
        startX = x;
        startY = y;
        
        farthestNorth = this.maze.numRows - 1;
        farthestSouth = 0;
        farthestEast = this.maze.numColumns - 1;
        farthestWest = 0;
        
        goalState = goal;
        
        greatestLength = Math.max(this.maze.numColumns,this.maze.numRows);
        
        startNode = new BlindPacmanNode(new int[this.maze.numRows][this.maze.numColumns], x, y, 0, "", this.maze.numRows*this.maze.numColumns);
    }



//  Class that represents a node on the graph to be searched
    public class BlindPacmanNode implements SearchNode {

    	// The current state of the problem -- represented by a 2D boolean array that will help nullify columns and 
    	// rows until we find our blind robot
        protected int[][] state;
        // The cost keeps track of how far the node is from the start, important for A* Search
        private double cost;
        
        private int possibilities,x,y;
        private String directionMoved;

        public BlindPacmanNode(int[][] state, int x, int y, double c,  String direction, int numberOfPossibilities) {
            this.state = state;
            this.x = x;
            this.y = y;
            this.directionMoved = direction;
            cost = c;
            possibilities = numberOfPossibilities;
        }
        
        // Based heavily on already written getSuccessors
        public ArrayList<SearchNode> getSuccessors() {

            ArrayList<SearchNode> successors = new ArrayList<SearchNode>();

            for (int[] move: moves) {
            	
            	// find new x and y coordinates
                int xNew = x + move[0];
                int yNew = y + move[1];
                
                // if the new coordinates are considered legal
                if(maze.isLegal(xNew, yNew)) {
                    
                	//create a temporary state to work with
                    int[][] tempState = new int[state.length][state[0].length];

                    for(int i=0; i<state.length; i++){
                        for(int j=0; j<state[0].length; j++){
                            tempState[i][j] = state[i][j];
                        }
                    }
                    
                    // calculate how far you have moved since the start
                    int xChange = Math.abs(xNew - startX);
                    int yChange = Math.abs(yNew - startY);
                    int decreasedPossibilities = 0;
                    
                    SearchNode successor;
                    
                    // if the blind robot moved east
                    if(Arrays.equals(move, EAST)){
                    	
                    	// decrement the columns that are now not important
                        if(xNew - startX > 0 )  {
                        	decreasedPossibilities = cutColumns(tempState, farthestEast - xChange + 1);
                        	// get rid of columns where you know the blind robot is NOT.
                        }
                        successor = new BlindPacmanNode(tempState,xNew, yNew, cost() + 1.0,  "EAST", possibilities-decreasedPossibilities);
                    }     
                    
                    else if(Arrays.equals(move, WEST)){
                        if(xNew - startX < 0 ) 
                            decreasedPossibilities = cutColumns(tempState, farthestWest + xChange - 1);
                        successor = new BlindPacmanNode(tempState,xNew, yNew, cost() + 1.0, "WEST", possibilities-decreasedPossibilities);
                    }
                    
                    else if(Arrays.equals(move, NORTH)){  
                        if(yNew - startY > 0 )
                            decreasedPossibilities = cutRows(tempState, farthestNorth - yChange + 1);
                        successor = new BlindPacmanNode(tempState,xNew, yNew, cost() + 1.0, "NORTH", possibilities-decreasedPossibilities);
                    }
                    
                    else{
                        if(yNew - startY < 0 )
                            decreasedPossibilities = cutRows(tempState, farthestSouth+yChange-1);
                        successor = new BlindPacmanNode(tempState,xNew, yNew, cost() + 1.0, "SOUTH", possibilities-decreasedPossibilities);
                    }
                    successors.add(successor);
                }
            }
            return successors;
        }
        
        // Not really required
        public boolean goalTest() {
            for(int i=0; i<state.length;i++) {
                if(state[i] == goalState){
                	return true;
                }
            }
            return false;
        }

        public String toString() {
            if(directionMoved=="") {
                return "    Start is ";
            }
            else {
                return directionMoved+" to ";
            }
        }

        
        private int cutRows(int[][] state, int row){
            int numRowsCut = 0;
            for(int i=0; i<state[0].length; i++) {
                state[row][i] = (Integer) null;
                numRowsCut++;
            }
            return numRowsCut;
        }

        private int cutColumns(int[][] state, int column){
            int numColumnsCut = 0;
            for(int i=0; i<state.length; i++) {
               state[i][column] = (Integer) null;
                    numColumnsCut++;
            }
            return numColumnsCut;
        }

        public double cost() {
            return cost;
        }

        public double heuristic() {
            return possibilities/greatestLength;
        }
     
        public double priority() {
            return heuristic() + cost();
        }
        
        // Filler functions because of SearchNode
        public int getDepth() {
        	return 0;
        }
		public boolean isSafeState() {
			return true;
		}

    }


}