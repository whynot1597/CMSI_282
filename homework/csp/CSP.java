package csp;

import java.time.LocalDate;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

/**
 * CSP: Calendar Satisfaction Problem Solver
 * Provides a solution for scheduling some n meetings in a given
 * period of time and according to some unary and binary constraints
 * on the dates of each meeting.
 */
public class CSP {

    /**
     * Public interface for the CSP solver in which the number of meetings,
     * range of allowable dates for each meeting, and constraints on meeting
     * times are specified.
     * @param nMeetings The number of meetings that must be scheduled, indexed from 0 to n-1
     * @param rangeStart The start date (inclusive) of the domains of each of the n meeting-variables
     * @param rangeEnd The end date (inclusive) of the domains of each of the n meeting-variables
     * @param constraints Date constraints on the meeting times (unary and binary for this assignment)
     * @return A list of dates that satisfies each of the constraints for each of the n meetings,
     *         indexed by the variable they satisfy, or null if no solution exists.
     */
    public static List<LocalDate> solve (int nMeetings, LocalDate rangeStart, LocalDate rangeEnd, Set<DateConstraint> constraints) {
        List<LocalDate> solution = new ArrayList<LocalDate>();
        solution = backtrackingTree(solution, nMeetings, rangeStart, rangeEnd, constraints);
        return solution;
    }
    
    private static List<LocalDate> backtrackingTree (List<LocalDate> assignment, int nMeetings, LocalDate rangeStart, LocalDate rangeEnd, Set<DateConstraint> constraints) {
    	if (assignment.size() == nMeetings) {
    		return assignment;
    	}
    	DateVar newDate = new DateVar(assignment.size(), rangeStart, rangeEnd, constraints);
    	for (LocalDate date : newDate.domain) {
    		assignment.add(assignment.size(), date);
    		if (testSolution(assignment, constraints)) {
    			List<LocalDate> result = backtrackingTree(assignment, nMeetings, rangeStart, rangeEnd, constraints);
    			if (result != null) {
        			return result;
        		}
    		}
    		assignment.remove(assignment.size() - 1);
    	}
    	return null;
    }
    
    /**
     * Tests whether a given solution to a CSP satisfies all constraints or not
     * @param soln Full instantiation of variables to assigned values, indexed by variable
     * @param constraints The set of constraints the solution must satisfy
     */
    private static boolean testSolution (List<LocalDate> soln, Set<DateConstraint> constraints) {
        for (DateConstraint d : constraints) {
        	if (d.L_VAL >= soln.size()) {
        		continue;
        	}
            LocalDate leftDate = soln.get(d.L_VAL),
                      rightDate = null;
            if (d.arity() == 1) {
            	rightDate = ((UnaryDateConstraint) d).R_VAL;
            } else if (((BinaryDateConstraint) d).R_VAL < soln.size()) {
            	rightDate = soln.get(((BinaryDateConstraint) d).R_VAL);
            } else {
            	continue;
            }
            
            if(!testConstraint(leftDate, rightDate, d)) {
            	return false;
            }
        }
        return true;
    }
    
    private static boolean testConstraint(LocalDate leftDate, LocalDate rightDate, DateConstraint c) {
    	boolean sat = false;
		switch (c.OP) {
        case "==": if (leftDate.isEqual(rightDate))  sat = true; break;
        case "!=": if (!leftDate.isEqual(rightDate)) sat = true; break;
        case ">":  if (leftDate.isAfter(rightDate))  sat = true; break;
        case "<":  if (leftDate.isBefore(rightDate)) sat = true; break;
        case ">=": if (leftDate.isAfter(rightDate) || leftDate.isEqual(rightDate))  sat = true; break;
        case "<=": if (leftDate.isBefore(rightDate) || leftDate.isEqual(rightDate)) sat = true; break;
        }
		return sat;
    }
    
    private static class DateVar {
    	List<LocalDate> domain = new ArrayList<LocalDate>();
    	
    	DateVar(int currentMeeting, LocalDate rangeStart, LocalDate rangeEnd, Set<DateConstraint> constraints) {
    		for (LocalDate date = rangeStart; !date.isAfter(rangeEnd); date = date.plusDays(1)) {
    			boolean sat = true;
    			for (DateConstraint c : constraints) {
    				if (c.L_VAL == currentMeeting && 
    				        c.arity() == 1 && 
    				        !testConstraint(date, ((UnaryDateConstraint) c).R_VAL, c)) {
        						sat = false;
        						break;
    				}
    			}
    			if (sat) { domain.add(date); };
    		}
    	}
    	
    }
    
    
}
