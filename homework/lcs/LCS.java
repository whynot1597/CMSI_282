/************************************************************************************************
*   Name:       LCS.java
*   Date:       03/29/2019
*   @author:    Jeremy Goldberg
*   @author:    Andrey Varakin
*   Purpose:    Implement the Longest Common Subsequence algorithm from both the bottom-up and 
*    			top-down dynamic programming approaches. Finds all LCS between two Strings
*   @see:       http://forns.lmu.build/classes/spring-2019/cmsi-282/homework/hw3/homework-3.html
**************************************************************************************************/

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
     * Fills a table by either top-down or bottom-up and returns the LCSs
     * of the given strings
     * @param rStr   The String found along the table's rows
     * @param cStr   The String found along the table's cols
     * @param isBU   true if bottom-up method, false if top-down
     * @return The longest common subsequence between rStr and cStr +
     *         [Side Effect] sets memoCheck to refer to table
     */
    private static Set<String> executeLCS(String rStr, String cStr, boolean isBU) {
        memoCheck = new int[rStr.length() + 1][cStr.length() + 1];
        if (isBU) {
            fillTableBU(rStr, cStr);
        } else {
            fillTableTD(rStr, cStr);
        }
        return collectSolution(rStr, rStr.length(), cStr, cStr.length()); 
    }
    
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
    private static Set<String> collectSolution(String rStr, int r, String cStr, int c) {
        //Base Case: currently in gutter
    	if (r == 0 || c == 0) {
    		return new HashSet<String>(Arrays.asList(""));
    	}
    	int rStrInd = r - 1;
    	int cStrInd = c - 1;
    	//Case same char: Add the matched letter to all substrings in the set returned by recursing top-left
    	if (rStr.charAt(rStrInd) == cStr.charAt(cStrInd)) {
    		return appendChar(rStr.charAt(rStrInd), collectSolution(rStr, rStrInd, cStr, cStrInd));
    	}
    	Set<String> result = new HashSet<String>();
    	//Case Mismatched Letters: if cell to left is greater than or equal to cell above
    	if (memoCheck[r][cStrInd] >= memoCheck[rStrInd][c]) {
    		result.addAll(collectSolution(rStr, r, cStr, cStrInd));
    	}
    	//Case Mismatched Letters: if cell above is greater than or equal to cell to left
    	if (memoCheck[rStrInd][c] >= memoCheck[r][cStrInd]){
    		result.addAll(collectSolution(rStr, rStrInd, cStr, c));
    	}
    	return result;
    }
    
    /**
     * Appends a single char to the end of every string in the given set
     * @param c         char to be appended
     * @param solutions Set of strings
     * @return new set of strings with char c append to each one
     */
    private static Set<String> appendChar(char c, Set<String> solutions) {
        Set<String> result = new HashSet<String>();
        for (String s : solutions) {
            result.add(s + c);
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
     * Calls executeLCS to fill table and return set of LCSs of give strings
     * @param rStr The String found along the table's rows
     * @param cStr The String found along the table's cols
     * @return The longest common subsequence between rStr and cStr +
     *         [Side Effect] sets memoCheck to refer to table
     */
    public static Set<String> bottomUpLCS (String rStr, String cStr) {
        return executeLCS(rStr, cStr, true);
    }
    
    /**
     * Bottom-up dynamic programming approach to the LCS problem, which
     * solves larger and larger subproblems iterative using a tabular
     * memoization structure.
     * @param rStr The String found along the table's rows
     * @param cStr The String found along the table's cols
     * @return The longest common subsequence between rStr and cStr +
     *         [Side Effect] sets memoCheck to refer to table
     */
    private static void fillTableBU (String rStr, String cStr) {
        for (int i = 1, rStrInd = 0; rStrInd < rStr.length(); i++, rStrInd++) {
            for (int j = 1, cStrInd = 0; cStrInd < cStr.length(); j++, cStrInd++) {
                //Case same char: add 1 from top left cell
                if (checkMatchedLetters(rStrInd, cStrInd, rStr, cStr)) {
                    memoCheck[i][j] = memoCheck[rStrInd][cStrInd] + 1;
                //Case diff char: take max of cell above and cell to left
                } else {
                    memoCheck[i][j] = Math.max(memoCheck[rStrInd][j], memoCheck[i][cStrInd]);
                }
            }
        }
        return;
    }
    
    // -----------------------------------------------
    // Top-Down LCS
    // -----------------------------------------------
    
    /**
     * Calls executeLCS to fill table and return set of LCSs of give strings
     * @param rStr The String found along the table's rows
     * @param cStr The String found along the table's cols
     * @return The longest common subsequence between rStr and cStr +
     *         [Side Effect] sets memoCheck to refer to table
     */
    public static Set<String> topDownLCS (String rStr, String cStr) {
        return executeLCS(rStr, cStr, false);
    }
    
    /**
     * Top-down dynamic programming approach to the LCS problem, which
     * solves smaller and smaller subproblems recursively using a tabular
     * memoization structure.
     * @param rStr The String found along the table's rows
     * @param cStr The String found along the table's cols
     * @return The longest common subsequence between rStr and cStr +
     *         [Side Effect] sets memoCheck to refer to table  
     */
    private static void fillTableTD(String rStr, String cStr) {
        lcsRecursiveHelper(rStr, rStr.length(), cStr, cStr.length());
        return;
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
    static void lcsRecursiveHelper (String rStr, int rInd, String cStr, int cInd) {
        //Base Case: reach gutter or previously computed cell
    	if (rInd == 0 || cInd == 0 || memoCheck[rInd][cInd] != 0) {
    		return;
    	}
    	int rStrInd = rInd - 1;
    	int cStrInd = cInd - 1;
    	//Case: same char: add 1 from recursed on top left cell
    	if (rStr.charAt(rStrInd) == cStr.charAt(cStrInd)) {
    		lcsRecursiveHelper(rStr, rStrInd, cStr, cStrInd);
    		memoCheck[rInd][cInd] = memoCheck[rStrInd][cStrInd] + 1;
    		return;
    	}
    	//Case diff char: take max of recursed top and recursed left cell
    	lcsRecursiveHelper(rStr, rStrInd, cStr, cInd);
    	lcsRecursiveHelper(rStr, rInd, cStr, cStrInd);
    	memoCheck[rInd][cInd] = Math.max(memoCheck[rStrInd][cInd], memoCheck[rInd][cStrInd]);
    	return;
    }
    
    
}
