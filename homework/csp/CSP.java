/************************************************************************************************
*   Name:       Huffman.java
*   Date:       05/09/2019
*   @author:    Jeremy Goldberg
*   @author:    Andrey Varakin
*   Purpose:    You must schedule n meetings within some allowable date range.
                Those meetings may have additional constraints (e.g., must occur before a certain 
                date, must not conflict with another meeting).
                A *solution* to this CSP will be some date assigned to each of the n meetings such 
                that all constraints are satisfied.
*   @see:       http://forns.lmu.build/classes/spring-2019/cmsi-282/homework/hw5/homework-5.html
**************************************************************************************************/

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
        List<DateVar> variables = new ArrayList<DateVar>();
        for (int i = 0; i < nMeetings; i++) {
            DateVar meeting = new DateVar(rangeStart, rangeEnd);
            variables.add(meeting);
        }
        variables = pruneDomains(variables, constraints);
        List<LocalDate> solution = backtrackingTree(variables, constraints);
        return solution;
    }
    
    /**
     * Tests node consistency on the given variables and given constraints
     * Removes impossible values from domains of variables
     * @param variables List of DateVar that has all the variables and their domains
     * @param constraints The set of constraints the solution must satisfy
     * @return variables with only valid domains
     */
    private static List<DateVar> pruneDomains(List<DateVar> variables, Set<DateConstraint> constraints) {
        for (DateVar meeting : variables) {
            for (int i = 0; i < meeting.domain.size(); i++) {
                LocalDate date = meeting.domain.get(i);
                LocalDate rightDate;
                for (DateConstraint c : constraints) {
                    if (c.arity() == 1) {
                        rightDate = ((UnaryDateConstraint) c).R_VAL;
                        if (!testConstraint(date, rightDate, c)) {
                            meeting.domain.remove(date);
                            i--;
                            break;
                        }
                    }
                }  
            }
        }
        return variables;
    }
    
    /**
     * Tests whether two given LocalDates pass the given constraint
     * @param leftDate Date on left side of constraint
     * @param rightDate Date on right side of constraint
     * @param c The DateConstraint used in testing
     * @return sat boolean on if condition was satisfied
     */
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
    
    /**
     * Searches for solution by creating a recursive backtracking tree
     * @param variables List of DateVar that has all the variables and their domains
     * @param constraints The set of constraints the solution must satisfy
     */
    private static List<LocalDate> backtrackingTree (List<DateVar> variables, Set<DateConstraint> constraints) {
    	if (variables.get(variables.size() - 1).currentDate != null) {
    	    List<LocalDate> solution = new ArrayList<LocalDate>();
    	    for (DateVar meeting : variables) {
    	        solution.add(meeting.currentDate);
    	    }
    		return solution;
    	}
    	DateVar currentMeeting = null;
    	for (DateVar meeting : variables) {
    	    if (meeting.currentDate == null) {
    	        currentMeeting = meeting;
    	        break;
    	    }
    	}
    	for (LocalDate date : currentMeeting.domain) {
    		currentMeeting.currentDate = date;
    		if (checkConsistency(variables, constraints)) {
    			List<LocalDate> result = backtrackingTree(variables, constraints);
    			if (result != null) {
        			return result;
        		}
    		}
    		currentMeeting.currentDate = null;
    	}
    	return null;
    }
    
    /**
     * Tests whether a given solution to a CSP satisfies all constraints or not
     * @param soln Full instantiation of variables to assigned values, indexed by variable
     * @param constraints The set of constraints the solution must satisfy
     */
    public static boolean checkConsistency (List<DateVar> variables, Set<DateConstraint> constraints) {
        boolean sat = false;
        for (DateConstraint d : constraints) {
            LocalDate leftDate = variables.get(d.L_VAL).currentDate,
                      rightDate = (d.arity() == 1) 
                          ? ((UnaryDateConstraint) d).R_VAL 
                          : variables.get(((BinaryDateConstraint) d).R_VAL).currentDate;
            
            sat = false;
            if (leftDate == null || rightDate == null) {
                sat = true;
                continue;
            }
            sat = testConstraint(leftDate, rightDate, d);
            if (!sat) {
                return sat;
            }
        }
        return sat;
    }
    
    /**
     * Private class for DateVar which holds a list of LocalDates as its domain and
     * The current date the variable is set to
     */
    private static class DateVar {
    	List<LocalDate> domain = new ArrayList<LocalDate>();
    	LocalDate currentDate;
    	
    	/**
         * Constructor for DateVar
         * Adds all days in given range to domain
         * @param rangeStart The first possible day in the domain
         * @param rangeEnd The last possible day in the domain
         */
    	DateVar(LocalDate rangeStart, LocalDate rangeEnd) {
    		for (LocalDate date = rangeStart; !date.isAfter(rangeEnd); date = date.plusDays(1)) {
    			domain.add(date);
    		}
    		
    	}
    	
    }
    
    
}
