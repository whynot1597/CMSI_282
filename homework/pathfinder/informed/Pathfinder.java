/************************************************************************************************

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
    	
    	firstPath.addAll(secondPath);
    	return firstPath;
    	
    }

	private static ArrayList<String> lowestCostPath(MazeState beginning, ArrayList<MazeState> ends, MazeProblem problem) {
		Queue <SearchTreeNode> frontier = new PriorityQueue<>(100, new CostComparator());
		SearchTreeNode first = new SearchTreeNode(beginning, null, null, 0, getDistanceToEnd(beginning, ends));
		frontier.add(first);
		
		boolean foundEnd = false;
		while(!foundEnd) {
			SearchTreeNode current = frontier.poll();
			for (int i = 0; i < ends.size(); i++) {
				if (current.state.equals(ends.get(i))) {
					return getSolution(current, first);
				}
			}
			
			Map<String, MazeState> children = problem.getTransitions(current.state);
			
			for (Map.Entry<String, MazeState> child : children.entrySet()) {
				frontier.add(new SearchTreeNode(child.getValue(), child.getKey(), current, getHistoryCost(current, problem), getDistanceToEnd(current.state, ends)));
			}
		}
		
		return null;
	}
	
	private static int getHistoryCost (SearchTreeNode current, MazeProblem problem) {
		int cost = 0;
		if (current.parent != null) {
			cost = current.parent.totalCost;
		}
		
		for (int i = 0; i < problem.MUD_STATES.size(); i++) {
			if (current.state.equals(problem.MUD_STATES.get(i))) {
				cost += 2;
			}
		}
		cost += 1;
		return cost;
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