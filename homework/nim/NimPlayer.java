/************************************************************************************************
*   Name:       NimPlayer.java
*   Date:       03/01/2019
*   @author:    Jeremy Goldberg
*   @author:    Andrey Varakin
*   Purpose:    Implement the choose(state) method that returns the integer indicating the action 
*               the agent should take from the given state. I have generously also included the 
*               GameTreeNode class to aid in this pursuit, as well as a signature for the a-b 
*               pruning / minimax search component.
*   @see:       http://forns.lmu.build/classes/spring-2019/cmsi-282/homework/hw2/homework-2.html
**************************************************************************************************/

package nim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Artificial Intelligence responsible for playing the game of Nim!
 * Implements the alpha-beta-pruning mini-max search algorithm
 */
public class NimPlayer {
    
    private final int MAX_REMOVAL;
    
    NimPlayer (int MAX_REMOVAL) {
        this.MAX_REMOVAL = MAX_REMOVAL;
    }
    
    /**
     * 
     * @param   remaining   Integer representing the amount of stones left in the pile
     * @return  An int action representing the number of stones to remove in the range
     *          of [1, MAX_REMOVAL]
     */
    public int choose (int remaining) {
        GameTreeNode root = new GameTreeNode(remaining, 0, true);
        Map<GameTreeNode, Integer> visited = new HashMap<>();
        int minimax = alphaBetaMinimax(root, Integer.MIN_VALUE, Integer.MAX_VALUE, root.isMax, visited);
        for (GameTreeNode child : root.children) {
        	if (child.score == minimax) {
        		return child.action;
        	}
        }
        return -1; //Should never reach here
    }
    
    /**
     * Constructs the minimax game tree by the tenets of alpha-beta pruning with
     * memoization for repeated states.
     * @param   node    The root of the current game sub-tree
     * @param   alpha   Smallest minimax score possible
     * @param   beta    Largest minimax score possible
     * @param   isMax   Boolean representing whether the given node is a max (true) or min (false) node
     * @param   visited Map of GameTreeNodes to their minimax scores to avoid repeating large subtrees
     * @return  Minimax score of the given node + [Side effect] constructs the game tree originating
     *          from the given node
     */
    private int alphaBetaMinimax (GameTreeNode node, int alpha, int beta, boolean isMax, Map<GameTreeNode, Integer> visited) {
    	if (node.remaining == 0) {
    		node.score = isMax ? 0 : 1;
    		visited.put(node, node.score);
    		return node.score;
    	}
    	
    	node.score = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    	int testScore = isMax ? alpha : beta;
    	
    	generateAllChildren(node);
    	
    	for (GameTreeNode child : node.children) {
    		//If previously generated child score, use that, else recurse
            int childScore = visited.containsKey(child) ? visited.get(child) : alphaBetaMinimax(child, alpha, beta, !isMax, visited);
            //a-b pruning
            node.score = isMax ? Integer.max(node.score, childScore) : Integer.min(node.score, childScore);
            testScore = isMax ? Integer.max(testScore, node.score) : Integer.min(testScore, node.score);
            visited.put(node, node.score);
            if (beta <= alpha) {
                break;
            }
        }
        return node.score;
    }
    
    /**
     * 
     * @param   node   GameTreeNode to expand
     * @return  a tree with all possible children of the given node
     */
    private void generateAllChildren(GameTreeNode node) {
    	for (int i = 1; i <= MAX_REMOVAL && (node.remaining - i >= 0); i++) {
			GameTreeNode newChild = new GameTreeNode(node.remaining - i, i, !node.isMax);
    		node.children.add(newChild);
		}
    }

}

/**
 * GameTreeNode to manage the Nim game tree.
 */
class GameTreeNode {
    
    int remaining, action, score;
    boolean isMax;
    ArrayList<GameTreeNode> children;
    
    /**
     * Constructs a new GameTreeNode with the given number of stones
     * remaining in the pile, and the action that led to it. We also
     * initialize an empty ArrayList of children that can be added-to
     * during search, and a placeholder score of -1 to be updated during
     * search.
     * 
     * @param   remaining   The Nim game state represented by this node: the #
     *          of stones remaining in the pile
     * @param   action  The action (# of stones removed) that led to this node
     * @param   isMax   Boolean as to whether or not this is a maxnode
     */
    GameTreeNode (int remaining, int action, boolean isMax) {
        this.remaining = remaining;
        this.action = action;
        this.isMax = isMax;
        children = new ArrayList<>();
        score = -1;
    }
    
    @Override
    public boolean equals (Object other) {
        return other instanceof GameTreeNode 
            ? remaining == ((GameTreeNode) other).remaining && 
              isMax == ((GameTreeNode) other).isMax &&
              action == ((GameTreeNode) other).action
            : false;
    }
    
    @Override
    public int hashCode () {
        return remaining + ((isMax) ? 1 : 0);
    }
    
}
