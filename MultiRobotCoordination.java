/* CS76, Fall 2016
 * Thursday, 29 September 2016
 * author: Raunak Bhojwani
 *
 * MultiRobotCoordination.java for Mazeworld
 *
 * This class extends SearchProblem to create a relatively abstract 
 * representation of the multiple robot coordination problem
 * 
 * This class is loosely based on CannibalProblem.java and there are
 * similarities in structure and functions. I did this to make sure
 * the way I approach such problems is consistent.
 */
package mazeworld;

import java.util.ArrayList;

public class MultiRobotCoordination extends SearchProblem {
	
	protected int numRobots;
    private int[] goalState;
    private MazeRepresentation maze;
	
	// Constants to describe each of the available moves to each of the robots
	public static int[] NORTH = {0, 1};
	public static int[] EAST = {1, 0};
	public static int[] SOUTH = {0, -1};
	public static int[] WEST = {-1, 0};
	public static int[] DONTMOVE = {0, 0};
    private static int moves[][] = {NORTH, EAST, SOUTH, WEST, DONTMOVE};

    public MultiRobotCoordination( int currentRobot, int[] startState, int[] goalState, MazeRepresentation maze) {
    	
    	// Copy the current state into your startState
        int[] currentState = new int[startState.length+1];
        for(int i=0; i<startState.length; i++) {
            currentState[i] = startState[i];
        }

        currentState[startState.length] = currentRobot;
        
        //Create the start node
        startNode = new MultiRobotMazeNode(currentState, 0);
        
        // Instantiate class variables
        this.maze = maze;
        this.goalState = goalState;
        this.numRobots = ((startState.length -1)/2);
        
    }

    //  Class that represents a node on the graph to be searched
    public class MultiRobotMazeNode implements SearchNode {

        // The current state of the problem
        protected int[] problemState;
        // The cost keeps track of how far the node is from the start, important for A* Search
        private double cost;

        public MultiRobotMazeNode(int[] currentState, double cost) {
            problemState = currentState;
            this.cost = cost;
        }
        
        // Get Successors of the current Node. Modeled on getSuccessors from Missionaries and Cannibals
        public ArrayList<SearchNode> getSuccessors() {

            ArrayList<SearchNode> successors = new ArrayList<SearchNode>();
            
            for (int[] move: moves) {
            	
            	// for every move available, create a temporary possible state
            	int[] tempState = new int[problemState.length];

                for(int i=0; i<problemState.length; i++){
                    tempState[i] = problemState[i];
                }
                int currentRobot = problemState[problemState.length-1];
                
                int currentRobotX = problemState[2*currentRobot] + move[0];
                int currentRobotY = problemState[2*currentRobot+1] + move[1];

                tempState[2*currentRobot] = currentRobotX;
                tempState[2*currentRobot+1] = currentRobotY;
                
                // change which robot's turn it is
                tempState[problemState.length-1] = (currentRobot+1)%numRobots;

                
                // Check legalist of this possible state, and check if robots collide. If not, add to successors
                if(maze.isLegal(currentRobotX, currentRobotY) && !roboticCollision(currentRobotX, currentRobotY, tempState)) {
                    
                	SearchNode successor = new MultiRobotMazeNode(tempState, cost() + 1.0);
                    successors.add(successor);
                }
            }
            return successors;
        }

        // Check for robot collisions. Note that x,y will collide with themselves in the new possible state, so 
        // I use a secondTime boolean to indicate when the if statement has been triggered for the second time 
        private boolean roboticCollision(int x, int y, int[] newState){
            boolean secondTime = false;
            
            for(int i = 0; i<numRobots; i++){
                if(newState[2*i] == x && newState[2*i+1] == y) {
                    if (secondTime) {
                    	return true;
                    }
                    secondTime = true;
                }
            }
            return false;
        }
        
        // Check if current state = goal state
        public boolean goalTest() {
        	
            for(int i=0; i<goalState.length; i++){  	
                if(problemState[i] != goalState[i]) {
                	return false;
                }    
            }
            return true;
        }

        public String toString() {
        	
        	String toString = "";
        	toString += "The state of the problem is: ";
        	
            for(int i=0; i<numRobots; i++) {
                toString +="("+problemState[2*i]+","+problemState[2*i+1];
            }
            
            toString += ", and it is currently this robot's move: "+problemState[problemState.length-1];
            toString +=", with a depth of: "+cost() +")";
            
            return toString;
        }

        public double cost() {
            return cost;
        }
        
        // simple, optimistic and monotonistic heuristic -- the total manhattan distance to goal
        public double heuristic() {
  
            double manhattan = 0;
            for(int i = 0; i<goalState.length; i++){
                manhattan += Math.abs(goalState[i] - problemState[i]);
            }
            return manhattan;
        }
        
        // need these functions to due the implementation of SearchNode
        public int getDepth() {
        	return 0;
        }
		public boolean isSafeState() {
			return true;
		}
		
        public double priority() {
            return heuristic() + cost();
        }

    }
}