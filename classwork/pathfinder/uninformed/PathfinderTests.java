package pathfinder.uninformed;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

/**
 * Unit tests for Maze Pathfinder. Tests include completeness and
 * optimality.
 */
public class PathfinderTests {

    @Test
    public void testPathfinder_t0() {
        String[] maze = {
            "XXXX",
            "X.IX",
            "XG.X",
            "XXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        // result will be a 2-tuple (isSolution, cost) where
        // - isSolution = 0 if it is not, 1 if it is
        // - cost = numerical cost of proposed solution
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]); // Test that result is a solution
        assertEquals(2, result[1]); // Ensure that the solution is optimal
    }
    
    @Test
    public void testPathfinder_t1() {
        String[] maze = {
            "XXXXXXX",
            "X.....X",
            "XIX.X.X",
            "XX.X..X",
            "XG....X",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        int[] result = prob.testSolution(solution);
        assertEquals(1,  result[0]); // Test that result is a solution
        assertEquals(12, result[1]); // Ensure that the solution is optimal
    }
    
    /*@Test
    public void testPathfinder_t2() {
        String[] maze = {
            "XXXXXXXXXXXXX",
            "XIX...X...XGX",
            "X.X.X.X.X.X.X",
            "X.X.X.X.X.X.X",
            "X...X...X...X",
            "XXXXXXXXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        int[] result = prob.testSolution(solution);
        System.out.println(solution);
        assertEquals(1,  result[0]); // Test that result is a solution
        assertEquals(28, result[1]); // Ensure that the solution is optimal
    }*/

    @Test
    public void testPathfinder_t3() {
        String[] maze = {
            "XXXXXXXXXXXXX",
            "XIX...X.....X",
            "X...X.X.XX..X",
            "XXXX..X.XG..X",
            "X........X..X",
            "XXXXXXXXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        int[] result = prob.testSolution(solution);
        System.out.println(solution);
        assertEquals(1,  result[0]); // Test that result is a solution
        assertEquals(20, result[1]); // Ensure that the solution is optimal
    }
    
    @Test
    public void testPathfinder_t4() {
        String[] maze = {
            "XXXX",
            "XIGX",
            "XXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        int[] result = prob.testSolution(solution);
        assertEquals(1,  result[0]);
        assertEquals(1, result[1]);
    }
    
    @Test
    public void testPathfinder_t5() {
        String[] maze = {
            "XXXXXXX",
            "XI....X",
            "XXXXX.X",
            "X.....X",
            "X.XXXXX",
            "X....GX",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        int[] result = prob.testSolution(solution);
        assertEquals(1,  result[0]);
        assertEquals(16, result[1]);
    }
    
}
