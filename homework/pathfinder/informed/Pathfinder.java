/************************************************************************************************
*   Name:       Pathfinder.java
*   Date:       02/11/2019
*   @author:    Jeremy Goldberg
*   @author:    Andrey Varakin
*   Purpose:    The Pathfinding game proceeds as follows:
*               (*) The MazeProblem is formalized, including the maze layout, 
*                   initial state, goal state, key state, possible mud state(s),
*                   actions, transitions, and a goal test.
*               (*) The Pathfinder agent is provided with the problem.
*               (*) The Pathfinder must find a sequence of actions ("U", "D", "L", or "R") 
*                   that takes it from the initial state to the goal with minimal cost.
*   @see:       http://forns.lmu.build/classes/spring-2019/cmsi-282/homework/hw1/homework-1.html
**************************************************************************************************/

package pathfinder.informed;

import java.util.*;


/**
 * Maze Pathfinding algorithm that implements a basic, uninformed, breadth-first
 * tree search.
 */
public class Pathfinder {

    /**
     * Given a MazeProblem, which specifies the actions and transitions available in
     * the search, returns a solution to the problem as a sequence of actions that
     * leads from the initial to a goal state.
     * 
     * @param problem A MazeProblem that specifies the maze, actions, transitions.
     * @return An ArrayList of Strings representing actions that lead from the
     *         initial to the goal state, of the format: ["R", "R", "L", ...]
     */
    public static ArrayList<String> solve(MazeProblem problem) {
    	ArrayList<MazeState> KEY_STATE_LIST = new ArrayList<MazeState>();
    	KEY_STATE_LIST.add(problem.KEY_STATE);
    	
    	ArrayList<String> firstPath = lowestCostPath(problem.INITIAL_STATE, KEY_STATE_LIST, problem);
    	ArrayList<String> secondPath = lowestCostPath(problem.KEY_STATE, problem.GOAL_STATES, problem);
    	
    	if (firstPath == null || secondPath == null) {
    		return null;
    	}
    	
    	firstPath.addAll(secondPath);
    	return firstPath;
    	
    }
    
    /**
     * Given a MazeState for the starting position and the possible end MazeStates
     * searches through A* to find the optimal path from the start to closest end.
     * 
     * @param beginning A MazeState that specifies the start position.
     * @param ends An ArrayList of MazeStates that specify the possible ending positions
     * @param problem A MazeProblem that specifies the maze, actions, transitions.
     * @return An ArrayList of Strings representing actions that lead from the
     *         start to the least cost end, of the format: ["R", "R", "L", ...]
     */
	private static ArrayList<String> lowestCostPath(MazeState beginning, ArrayList<MazeState> ends, MazeProblem problem) {
		Queue <SearchTreeNode> frontier = new PriorityQueue<>(100, new CostComparator());
		Hashtable<String, MazeState> visited = new Hashtable<>();
		SearchTreeNode first = new SearchTreeNode(beginning, null, null, 0, getDistanceToEnd(beginning, ends));
		frontier.add(first);
		
		while(!frontier.isEmpty()) {
			//Take off the first node in the Queue
			SearchTreeNode current = frontier.poll();
			visited.put(current.state.toString(),current.state);
			
			//Check if the current node is a goal state
			for (int i = 0; i < ends.size(); i++) {
				if (current.state.equals(ends.get(i))) {
					return getSolution(current, first);
				}
			}
			
			//Generate the transitions from current node
			Map<String, MazeState> children = problem.getTransitions(current.state);
			
			for (Map.Entry<String, MazeState> child : children.entrySet()) {
				SearchTreeNode toAdd = new SearchTreeNode(child.getValue(), child.getKey(), current, getHistoryCost(current, problem, child.getValue()), getDistanceToEnd(child.getValue(), ends));
				//Add to Queue if unvisited node 
				if (!visited.contains(toAdd.state)) {
					frontier.add(toAdd);
				}
			}
			
		}
		
		return null;
	}
	
	/**
     * Given a current SearchTreeNode and child MazeState, generate the "travel" cost
     * for the child SearchTreeNode, where the travel cost is the cost of the previous
     * node, plus 3 if child is a Mud space and 1 if else.
     * 
     * @param current A SearchTreeNode that specifies the current node.
     * @param problem A MazeProblem that specifies the maze, actions, transitions.
     * @param child A MazeState of a specific child of the current SearchTreeNode
     * @return An int representing the "travel cost" for the child node
     */
	private static int getHistoryCost (SearchTreeNode current, MazeProblem problem, MazeState child) {
		int cost = current.historyCost;
		
		if (problem.MUD_STATES.contains(child)) {
			cost += 2;
		}
		
		cost += 1;
		return cost;
	}
	
	/**
     * Given a current MazeState and the possible end MazeStates, provide the shortest possible
     * value to the nearest end. Otherwise known as a Manhattan Heuristic
     * 
     * @param current A MazeState that specifies the current position.
     * @param ends An ArrayList of possible end MazeStates
     * @return An int representing the Manhattan Heuristic of the current MazeState
     */
	private static int getDistanceToEnd (MazeState current, ArrayList<MazeState> ends) {
		int[] solutions = new int[ends.size()];
		for (int i = 0; i < ends.size(); i++) {
			solutions[i] = Math.abs(current.row - ends.get(i).row) + Math.abs(current.col - ends.get(i).col);
		}
		Arrays.sort(solutions);
		return solutions[0];
	}
		
	/**
     * Given an end SearchTreeNode and beginning SearchTreeNode, travel up the tree
     * using parent's generating and ArrayList of past actions
     * 
     * @param end A SearchTreeNode that specifies the last node in the path.
     * @param beginning A SearchTreeNode that specifies the first node in the path
     * @return An ArrayList of Strings representing actions that lead from the
     *         first to the last node, of the format: ["R", "R", "L", ...]
     */
    private static ArrayList<String> getSolution(SearchTreeNode end, SearchTreeNode beginning) {
        ArrayList<String> solution = new ArrayList<>();
        while (!end.state.equals(beginning.state)) {
            solution.add(0, end.action);
            end = end.parent;
        }
        return solution;
    }

}

/**
 * SearchTreeNode that is used in the Search algorithm to construct the Search
 * tree.
 */
class SearchTreeNode {

	MazeState state;
	String action;
	SearchTreeNode parent;
	int historyCost;
	int heuristicCost;
	int totalCost;

	/**
	 * Constructs a new SearchTreeNode to be used in the Search Tree.
	 * 
	 * @param state  The MazeState (col, row) that this node represents.
	 * @param action The action that *led to* this state / node.
	 * @param parent Reference to parent SearchTreeNode in the Search Tree.
	 * @param historyCost The cost of "traveling" to this state
	 * @param heuristicCost The possible cost to the closest end
	 */
	SearchTreeNode(MazeState state, String action, SearchTreeNode parent, int historyCost, int heuristicCost) {
		this.state = state;
		this.action = action;
		this.parent = parent;
		this.historyCost = historyCost;
		this.heuristicCost = heuristicCost;
		this.totalCost = historyCost + heuristicCost;
	}

}

/**
 * Cost Comparator that is used in the priority queue to evaluate priority
 * of search tree nodes
 */
class CostComparator implements Comparator<SearchTreeNode> {
	/**
	 * Compares the total costs of two search tree nodes
	 * 
	 * @param x SearchTreeNode
	 * @param y SearchTreeNode
	 * @return int (-) x < y || (+) if x > y || 0 if x = y
	 */
    public int compare(SearchTreeNode x, SearchTreeNode y) {
        return x.totalCost - y.totalCost;
    }
}