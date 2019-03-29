package lcs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LCS {
    
    /**
     * memoCheck is used to verify the state of your tabulation after
     * performing bottom-up and top-down DP. Make sure to set it after
     * calling either one of topDownLCS or bottomUpLCS to pass the tests!
     */
    public static int[][] memoCheck;
    
    // -----------------------------------------------
    // Shared Helper Methods
    // -----------------------------------------------
    
    /**
     * Given two strings and a table collects a Set of longest common substrings
     * @param rStr The String found along the table's rows
     * @param r    The index of the current row
     * @param cStr The String found along the table's cols
     * @param c    The index of the current column
     * @param memo The table of corresponding to longest common substring lengths
     * @return The longest common subsequences between rStr and cStr +
     *         [Side Effect] sets memoCheck to refer to table
     */
    private static Set<String> collectSolution (String rStr, int r, String cStr, int c, int[][] memo) {
    	if (r == 0 || c == 0) {
    		return new HashSet<String>(Arrays.asList(""));
    	}
    	if (rStr.charAt(r - 1) == cStr.charAt(c - 1)) {
    		Set<String> result = new HashSet<String>();
    		for (String s : collectSolution(rStr, r - 1, cStr, c - 1, memo)) {
    			result.add(s + rStr.charAt(r - 1));
    		}
    		return result;
    	}
    	Set<String> result = new HashSet<String>();
    	if (memo[r][c - 1] >= memo[r - 1][c]) {
    		result.addAll(collectSolution(rStr, r, cStr, c - 1, memo));
    	}
    	if (memo[r - 1][c] >= memo[r][c - 1]){
    		result.addAll(collectSolution(rStr, r - 1, cStr, c, memo));
    	}
    	return result;
    }
    
    /**
     * Given two strings and index of char at each string returns true
     * if same char else false
     * @param index1 index for char in rStr
     * @param index2 index for char in cStr
     * @param rStr   The String found along the table's rows
     * @param cStr   The String found along the table's cols
     * @return bool  Same char
     */
    private static boolean checkMatchedLetters(int index1, int index2, String rStr, String cStr) {
    	return rStr.charAt(index1) == cStr.charAt(index2);
    }
    

    // -----------------------------------------------
    // Bottom-Up LCS
    // -----------------------------------------------
    
    /**
     * Bottom-up dynamic programming approach to the LCS problem, which
     * solves larger and larger subproblems iterative using a tabular
     * memoization structure.
     * @param rStr The String found along the table's rows
     * @param cStr The String found along the table's cols
     * @return The longest common subsequence between rStr and cStr +
     *         [Side Effect] sets memoCheck to refer to table
     */
    public static Set<String> bottomUpLCS (String rStr, String cStr) {
        memoCheck = new int[rStr.length() + 1][cStr.length() + 1];
        for (int i = 1; i < rStr.length() + 1; i++) {
        	for (int j = 1; j < cStr.length() + 1; j++) {
        		if (checkMatchedLetters(i - 1, j - 1, rStr, cStr)) {
                	memoCheck[i][j] = memoCheck[i - 1][j - 1] + 1;
                } else {
                	memoCheck[i][j] = Math.max(memoCheck[i - 1][j], memoCheck[i][j - 1]);
                }
        	}
        }
        return collectSolution(rStr, rStr.length(), cStr, cStr.length(), memoCheck);
    }    
    
    // -----------------------------------------------
    // Top-Down LCS
    // -----------------------------------------------
    
    /**
     * Top-down dynamic programming approach to the LCS problem, which
     * solves smaller and smaller subproblems recursively using a tabular
     * memoization structure.
     * @param rStr The String found along the table's rows
     * @param cStr The String found along the table's cols
     * @return The longest common subsequence between rStr and cStr +
     *         [Side Effect] sets memoCheck to refer to table  
     */
    public static Set<String> topDownLCS (String rStr, String cStr) {
    	memoCheck = new int[rStr.length() + 1][cStr.length() + 1];
    	lcsRecursiveHelper(rStr, rStr.length(), cStr, cStr.length(), memoCheck);
    	return collectSolution(rStr, rStr.length(), cStr, cStr.length(), memoCheck);
    }
    
    /**
     * Completes the memoization table using top-down dynamic programming
     * @param rStr The String along the memoization table's rows
     * @param rInd The current letter's index in rStr
     * @param cStr The String along the memoization table's cols
     * @param cInd The current letter's index in cStr
     * @param memo The memoization table
     * @return void Completes TDDP table
     */
    static void lcsRecursiveHelper (String rStr, int rInd, String cStr, int cInd, int[][] memo) {
    	if (rInd == 0 || cInd == 0 || memo[rInd][cInd] != 0) {
    		return;
    	}
    	if (rStr.charAt(rInd - 1) == cStr.charAt(cInd - 1)) {
    		lcsRecursiveHelper(rStr, rInd - 1, cStr, cInd - 1, memo);
    		memo[rInd][cInd] = memo[rInd - 1][cInd - 1] + 1;
    		return;
    	}
    	lcsRecursiveHelper(rStr, rInd - 1, cStr, cInd, memo);
    	lcsRecursiveHelper(rStr, rInd, cStr, cInd - 1, memo);
    	memo[rInd][cInd] = Math.max(memo[rInd - 1][cInd], memo[rInd][cInd - 1]);
    	return;
    }
    
    
}
