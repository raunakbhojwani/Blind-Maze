/* CS76, Fall 2016
 * Thursday, 29 September 2016
 * author: Raunak Bhojwani
 *
 * BlindPacmanDriver.java for Mazeworld
 * Driver Code to test BlindPacman robotic problem
 */
package mazeworld;
import java.util.List;

import mazeworld.SearchProblem.SearchNode;

public class BlindPacmanDriver {

    static MazeRepresentation maze = MazeRepresentation.readMazeTxt("complex.txt"); // empty.txt, easy.txt, corridor.txt, complex.txt, huge.txt

  
    private static void solutionSearch() {


	     System.out.println("Blind Robot Pacman Maze! There is 1 robot in the Complex Maze");
	     int[] startState = {2,0};
	     int[] goalState = {2,2};

	    int x = startState[0];
	    int y = startState[1];
   
        BlindPacman mazeProblem = new BlindPacman(maze, x, y, goalState);
    


        List<SearchNode> bfsPath = mazeProblem.breadthFirstSearch();
        System.out.println("Breadth First Search:  ");
        for(SearchNode i:bfsPath){
           System.out.print(i.toString());
        }
        System.out.print(" Terminated\n");
        System.out.println("    Number of moves are "+bfsPath.size());
        mazeProblem.printStats();


        List<SearchNode> dfsPath = mazeProblem.depthFirstPathCheckingSearch(5000);
        System.out.println("Depth First Search:  ");
        for(SearchNode i:dfsPath){
        System.out.print(i.toString());
        }
        System.out.print(" Terminated\n");
        System.out.println("    Number of moves are "+dfsPath.size());
        mazeProblem.printStats();

       List<SearchNode> astarPath = mazeProblem.AStarSearch();
       System.out.println("A* Search:  ");
       for(SearchNode i:astarPath){
           System.out.print(i.toString());
       }
       System.out.print(" Terminated\n");
       System.out.println("    Number of moves are " + astarPath.size());
       mazeProblem.printStats();

    }
    
    
    public static void main(String[] args) {
        solutionSearch();
    }
}