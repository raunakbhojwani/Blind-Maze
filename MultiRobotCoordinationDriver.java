/* CS76, Fall 2016
 * Thursday, 29 September 2016
 * author: Raunak Bhojwani
 *
 * MultiRobotCoordinationDriver.java for Mazeworld
 * Driver Code to test Multiple Robot Coordination problem
 */
package mazeworld;
import java.util.List;

import mazeworld.SearchProblem.SearchNode;

public class MultiRobotCoordinationDriver {

    static MazeRepresentation maze = MazeRepresentation.readMazeTxt("complex.txt"); // empty.txt, easy.txt, corridor.txt, complex.txt, huge.txt

    // assumes maze and mazeView instance variables are already available
    private static void solutionSearch() {

//        System.out.println("Multi Robot Coordination Maze! There are 3 robots in the Empty Maze");
//        int[] startState = {0,0,0,3,5,6};
//        int[] goalState = {6,0,6,1,6,2};

//        System.out.println("Multi Robot Coordination Maze! There are 3 robots in the Easy Maze");
//        int[] startState = {0,0,0,2,5,6};
//        int[] goalState = {6,1,6,2,6,0};


//        System.out.println("Multi Robot Coordination Maze! There are 3 robots in the Corridor Maze");
//        int[] startState = {0,0,0,1,0,2};
//        int[] goalState = {6,2,6,1,6,0};

         System.out.println("Multi Robot Coordination Maze! There are 3 robots in the Complex Maze");
         int[] startState = {3,1,3,2,3,3};
         int[] goalState = {3,3,3,2,3,1};


//        System.out.println("Multi Robot Coordination Maze! There are 3 robots in the Huge Maze");
//        int[] startState = {9,30,24,16};
//        int[] goalState = {27,8,22,5};

        int currentRobot = 0;
        MultiRobotCoordination multiRobotProblem = new MultiRobotCoordination(currentRobot, startState, goalState, maze);


        List<SearchNode> bfsSolution = multiRobotProblem.breadthFirstSearch();
        System.out.println("Breadth First Search:  ");
        multiRobotProblem.printStats();
        System.out.println("    Number of moves made: "+bfsSolution.size());

        List<SearchNode> dfsSolution = multiRobotProblem.depthFirstPathCheckingSearch(5000);
        System.out.println("Depth First Search:  ");
        multiRobotProblem.printStats();
        System.out.println("    Number of moves made: "+dfsSolution.size());

        List<SearchNode> AStarSolution = multiRobotProblem.AStarSearch();
        System.out.println("A* Search:  ");
        multiRobotProblem.printStats();
        System.out.println("    Number of moves made: " + AStarSolution.size());
        

    }
    
    
    public static void main(String[] args) {
        solutionSearch();
    }
}