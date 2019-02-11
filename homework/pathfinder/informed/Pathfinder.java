/************************************************************************************************

**************************************************************************************************/

package pathfinder.informed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.Queue;

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
    	
    	ArrayList<String> firstPath = lowestCostPath(problem.INITIAL_STATE, KEY_STATE_LIST);
    	ArrayList<String> secondPath = lowestCostPath(problem.KEY_STATE, problem.GOAL_STATES);
    	
    	firstPath.addAll(secondPath);
    	return firstPath;
    	
    }

	private static ArrayList<String> lowestCostPath(MazeState beginning, ArrayList<MazeState> ends) {
		Queue <SearchTreeNode> frontier = new PriorityQueue<>(100, CostComparator());
		frontier.add(new SearchTreeNode(beginning, null, null, 0, getDistanceToEnd(beginning, ends)));
		
		boolean foundEnd = false;
		while(!foundEnd) {
			
		}
	}
	
	private static int getDistanceToEnd (MazeState current, ArrayList<MazeState> ends) {
		int[] solutions = new int[ends.size()];
		for (int i = 0; i < ends.size(); i++) {
			solutions[i] = Math.abs(current.row - ends.get(i).row) + Math.abs(current.col - ends.get(i).col);
		}
		Arrays.sort(solutions);
		return solutions[0];
	}
		

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

class CostComparator implements Comparator<SearchTreeNode> {
    public int compare(SearchTreeNode x, SearchTreeNode y) {
        if (x.totalCost < y.totalCost) {
            return -1;
        }
        if (x.totalCost > y.totalCost) {
            return 1;
        }
        return 0;
    }
}