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
    	if (memo[r][c - 1] >= memo[c][r - 1]) {
    		result.addAll(collectSolution(rStr, r, cStr, c - 1, memo));
    	}
    	if (memo[r - 1][c] >= memo[c - 1][r]){
    		result.addAll(collectSolution(rStr, r - 1, cStr, c, memo));
    	}
    	return result;
    }
    
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
    
    // [!] TODO: Add any bottom-up specific helpers here!
    
    
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
        throw new UnsupportedOperationException();
    }
    
    // [!] TODO: Add any top-down specific helpers here!
    
    
}
