/* Template provided by Prof. Devin Balkcom for Missionaries and Cannibals
 * CS76, Fall 2016
 * Thursday, 29 September 2016
 * author: Raunak Bhojwani
 * 
 * Mostly exactly like UUSearchProblem.java from Missionaries and Cannibals
 * but with important additions to accommodate AStarSearch()
 *
 * SearchProblem.java for Mazeworld
 */

package mazeworld;
import java.util.*;

public abstract class SearchProblem {
	
	// used to store performance information about search runs.
	//  these should be updated during the process of searches

	// see methods later in this class to update these values
	protected int nodesExplored;
	protected int maxMemory;

	protected SearchNode startNode;
	
	protected interface SearchNode {
		public ArrayList<SearchNode> getSuccessors();
		public boolean goalTest();
		public int getDepth();
		public boolean isSafeState();
		
		public double cost();
		
		// heuristic and priority are necessary for the newly added AStarSearch
		public double heuristic();
		public double priority();
	}

	// breadthFirstSearch:  return a list of connecting Nodes, or null
	// no parameters, since start and goal descriptions are problem-dependent.
	//  therefore, constructor of specific problems should set up start
	//  and goal conditions, etc.
	
	public List<SearchNode> breadthFirstSearch() {
		
		resetStats();
		
		// Use HashMap to store visited nodes and their backpointers
		HashMap<SearchNode, SearchNode> explored = new HashMap<>();
		
		// Use a linked list to implement a queue.
		Queue<SearchNode> frontier = new LinkedList<SearchNode>();
		frontier.add(startNode);
		
		while (!frontier.isEmpty()) {
			incrementNodeCount();
			updateMemory(frontier.size() + explored.size());
			
			// Pop the top node from the queue
			SearchNode currentNode = frontier.remove();
			
			// Check if the current node is the goal state
			if (currentNode.goalTest()) {
				return backchain(currentNode, explored);
			}

			// If not, continue the search through the current node's successors
			List<SearchNode> childNodes = currentNode.getSuccessors();
			for (SearchNode childNode : childNodes) {
				// If the node is unvisited, add it to the explored HashMap, and add it to the queue
				if (!explored.containsValue(childNode)) {
					explored.put(childNode, currentNode);
					frontier.add(childNode);
				}
			}
		}
		// If failure, return null
		return null;
	}
	
	// backchain should only be used by bfs, not the recursive dfs
	private List<SearchNode> backchain(SearchNode node, HashMap<SearchNode, SearchNode> visited) {
		// you will write this method
		List<SearchNode> finalPath = new ArrayList<SearchNode>();
		finalPath.add(node);
		
		// Loop through the HashMap (visited) until you reach the start node
		while (node != startNode) {
			node = visited.get(node);
			finalPath.add(node);
		}
		return finalPath;
	}

	public List<SearchNode> depthFirstMemoizingSearch(int maxDepth) {
		resetStats();
		
		// set up a explored hashmap, with integer values for depth
		HashMap<SearchNode, Integer> explored = new HashMap<SearchNode, Integer>();
		return dfsrm(startNode, explored, 0, maxDepth);	

	}

	// recursive memoizing dfs. Private, because it has the extra
	// parameters needed for recursion.  
	private List<SearchNode> dfsrm(SearchNode currentNode, HashMap<SearchNode, Integer> visited, int depth, int maxDepth) {
		
		// keep track of stats; these calls charge for the current node
		updateMemory(visited.size());
		incrementNodeCount();

		// you write this method. Comments *must* clearly show the
		// "base case" and "recursive case" that any recursive function has.
		
		// Set up a current, and a final path to build recursively
		List<SearchNode> finalPath = new ArrayList<SearchNode>(Arrays.asList(currentNode));
		List<SearchNode> currentPath = new ArrayList<SearchNode>();
		
		visited.put(currentNode, depth);
		
		// Base case if depth too large
		if (depth > maxDepth) {
			return null;	
		}
		// Base case if goal reached
		if (currentNode.goalTest()) {
			return finalPath;
		} 
		// Recursive case
		else {
			// For each child node, check if it is visited, and has the appropriate depth
			List<SearchNode> childNodes = currentNode.getSuccessors();
			for (SearchNode childNode : childNodes) {
				if(!visited.containsKey(childNode) || visited.get(childNode) >= depth + 1) {
					currentPath = dfsrm(childNode, visited, depth + 1, maxDepth);
					if (currentPath != null) {
						// Build final Path
						finalPath.addAll(currentPath);
						return finalPath;
					}
				}
			}
		}
		return null;
	}
	
	
	// set up the iterative deepening search, and make use of dfsrpc
	public List<SearchNode> IDSearch(int maxDepth) {
		resetStats();
		
		// Use a hashset to denote the current path, a list for the final path
		HashSet<SearchNode> currentPath = new HashSet<SearchNode>();
		List<SearchNode> finalPath;

		for (int currentDepth = 0; currentDepth <= maxDepth; currentDepth++) {
			// for each depth, use path checking dfs
			finalPath = dfsrpc(startNode, currentPath, 0, currentDepth);
			if (finalPath != null) {
				return finalPath;
			}
		}
		return null;
		
	}

	// set up the depth-first-search (path-checking version), 
	//  but call dfspc to do the real work
	public List<SearchNode> depthFirstPathCheckingSearch(int maxDepth) {
		resetStats();
		
		// I wrote this method for you.  Nothing to do.
		HashSet<SearchNode> currentPath = new HashSet<SearchNode>();
		return dfsrpc(startNode, currentPath, 0, maxDepth);
	}

	// recursive path-checking dfs. Private, because it has the extra
	// parameters needed for recursion.
	private List<SearchNode> dfsrpc(SearchNode currentNode, HashSet<SearchNode> currentPath, int depth, int maxDepth) {
		
		// keep track of stats; these calls charge for the current node
		updateMemory(currentPath.size());
		incrementNodeCount();

		// you write this method. Comments *must* clearly show the
		// "base case" and "recursive case" that any recursive function has.
		
		// Set up lists for current and final paths
		List<SearchNode> finalPath = new ArrayList<SearchNode>(Arrays.asList(currentNode));
		List<SearchNode> path = new ArrayList<SearchNode>();
		
		// Base case: depth too large
		if (depth > maxDepth) {
			return null;
		}
		
		currentPath.add(currentNode);
		
		// Base case: if goal reached
		if (currentNode.goalTest()) {
			return finalPath;
		} 
		// Recursive case
		else {
			List<SearchNode> childNodes = currentNode.getSuccessors();
			for (SearchNode childNode : childNodes) {
				if (!currentPath.contains(childNode)) {
					path = dfsrpc(childNode, currentPath, depth + 1, maxDepth); // for each child, if not already on the path, build path recursively
					if (path != null) {
						finalPath.addAll(path);
						return finalPath;
					}
				}
			}
		}
		currentPath.remove(currentNode);
		return null;
	}
	
	// A* Search implementation -- NEW METHOD
	public List<SearchNode> AStarSearch() {
		resetStats();
		
		// Use HashMap to store visited nodes and their backpointers
		HashMap<SearchNode, SearchNode> explored = new HashMap<>();
		
		// instantiate a new HashMap to keep a track of each node's priority
		HashMap<SearchNode,Double> priorities = new HashMap<>();
		priorities.put(startNode,startNode.priority()); // this makes use of the priority function that was newly added earlier

		// instantiate a new priority to queue to queue up nodes that are being visited
		PriorityQueue<SearchNode> frontier = new PriorityQueue<>();
		frontier.add(startNode);
		explored.put(startNode, null);

		while (!frontier.isEmpty()) {
			
			incrementNodeCount();
			updateMemory(frontier.size() + priorities.size() + explored.size());

			SearchNode currentNode = frontier.poll(); //poll instead of peek to retrieve and remove

			if (currentNode.goalTest()) {
				return backchain(currentNode, explored);
			}
			else if (priorities.containsKey(currentNode) && currentNode.priority()>priorities.get(currentNode)) {
				continue; // do not add node to frontier if it is visited
			}
			
			List<SearchNode> childNodes = currentNode.getSuccessors();

			for (SearchNode child: childNodes) {
				if (!priorities.containsKey(child) || child.priority() < priorities.get(child)) {
					explored.put(child, currentNode);
					frontier.add(child);
					priorities.put(child, child.priority());
				}
			}
		}
		return null; // return failure if no path found
	}

	protected void resetStats() {
		nodesExplored = 0;
		maxMemory = 0;
	}
	
	protected void printStats() {
		System.out.println("Nodes explored during last search:  " + nodesExplored);
		System.out.println("Maximum memory usage during last search " + maxMemory);
	}
	
	protected void updateMemory(int currentMemory) {
		maxMemory = Math.max(currentMemory, maxMemory);
	}
	
	protected void incrementNodeCount() {
		nodesExplored++;
	}

}
