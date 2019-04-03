/**
 * CS 165a -- Artificial Intelligence
 * Lab 02 // Assignment 3
 * SearchAlgorithm.java
 * By: Hiram A Rios && Matthew S Montoya
 * Instructor: Dr. Chistopher Kiekintveld
 * TA: Khandoker Rahad
 * Purpose: To implement and test two different search methods for solving a class-scheduling problem (Simulated Annealing + Backtracking).
 * Last Modified: April 2, 2019
 */

import java.util.ArrayList;

public class SearchAlgorithm {

	/**
	 * Simulated Annealing: First Search Chosen
	 * @param problem
	 * @param deadline
	 * @return
	 */
	public Schedule simulatedAnnealingSolver(SchedulingProblem problem, long deadline) {
		Schedule current_schedule = problem.getEmptySchedule();
		Schedule next_schedule = problem.getEmptySchedule();
		int temperature = 10000;
		double energy_change_delta;

		while(true) {
			if(temperature == 0)
				return current_schedule;
			next_schedule = createNextSchedule(problem);
			energy_change_delta = problem.evaluateSchedule(next_schedule) - problem.evaluateSchedule(current_schedule);
			if(energy_change_delta > 0)
				current_schedule = next_schedule;
			temperature--;
		}
	}

	/**
	 * Simulated Annealing 2: Approach at improving standard simulated annealing
	 * @param problem
	 * @param deadline
	 * @return
	 */
	public Schedule simulatedAnnealingSolver2(SchedulingProblem problem, long deadline) {
		Schedule current_schedule = problem.getEmptySchedule();
		Schedule next_schedule = problem.getEmptySchedule();
		double energy_change_delta, temperature = 1.0, min_temperature = .00001, alpha_value = .09;

		while(true) {
			if (temperature<min_temperature)
				return current_schedule;
			next_schedule = createNextSchedule(problem);
			energy_change_delta = 2.71828*((problem.evaluateSchedule(next_schedule)-problem.evaluateSchedule(current_schedule))/temperature);
			if(energy_change_delta > Math.random()) {
				current_schedule = next_schedule;
			}
			temperature*=alpha_value;
		}
	}

	/**
	 * Solve Constraint Satisfaction Problem: Step 1 in Backtracking (2nd solution chosen)
	 * @param problem
	 * @param deadline
	 * @return
	 */
	public Schedule cspSolver(SchedulingProblem problem, long deadline) {
		Schedule schedule_solution = problem.getEmptySchedule();
		ArrayList<Course> temp_course_list = new ArrayList<Course>(problem.courses);
		schedule_solution = cspSolverRec(problem, schedule_solution, temp_course_list);
		return schedule_solution;
	}

	/**
	 * Solve Constraint Satisfaction Problem Recursively: Step 2 in Backtracking
	 * @param problem
	 * @param solution
	 * @param temp_course_list
	 * @return
	 */
	private Schedule cspSolverRec(SchedulingProblem problem, Schedule solution, ArrayList<Course> temp_course_list) {
		if(temp_course_list.size() == 0)
			return solution;
		Course course = minimumRemainingValue(temp_course_list);
		if(course == null)
			return solution;
		temp_course_list.remove(temp_course_list.indexOf(course));
		int j=0;
		int slots[] = leastConstraining(course,problem.courses);
		int pos = Integer.MAX_VALUE;
		for(int i=0;i<slots.length;i++) {
			if((slots[i]<pos) && (slots[i]>0)) {
				pos = slots[i];
				j=i;
			}
		}
		for(int k=0;k<problem.rooms.size();k++) {
			if (solution.schedule[k][j] < 0) {
				if(course.enrolledStudents <= problem.rooms.get(k).capacity) {
					solution.schedule[k][j] = problem.courses.indexOf(course);
					break;
				}
			}
		}
		course.scheduled = true;
		solution = cspSolverRec(problem,solution,temp_course_list);
		if(!Solved(problem)) {
			course.timeSlotValues[j] = 0;
			temp_course_list.add(course);
		}
		return solution;
	}

	/**
	 * Create Next Schedule: Creates new schedule from a given problem
	 * @param problem
	 * @return
	 */
	private Schedule createNextSchedule(SchedulingProblem problem) {
		int x = 0;
		int y = 0;
		Schedule next_schedule = problem.getEmptySchedule();

		for(int i=0; i<problem.courses.size(); i++) {
			Course randomCourse = problem.courses.get(i);
			x = (int)(Math.random()*randomCourse.timeSlotValues.length);
			y = (int)(Math.random()*problem.rooms.size());

			while(next_schedule.schedule[y][x]!=-1) {
				x = (int)(Math.random()*randomCourse.timeSlotValues.length);
				y = (int)(Math.random()*problem.rooms.size());
			}
			next_schedule.schedule[y][x]=i;
		}
		return next_schedule;
	}

	/**
	 * Least Constraining: Find course selection that leads the least constraining on other course_list
	 * @param current_course
	 * @param course_list
	 * @return
	 */
	private int[] leastConstraining(Course current_course, ArrayList<Course> course_list) { 
		int[] conflicts = new int[current_course.timeSlotValues.length];
		Course course = null;
		for(int i=0; i<course_list.size(); i++) {
			course = course_list.get(i);
			if(course_list.indexOf(current_course) != i) {
				for(int j=0; j<conflicts.length; j++) {
					if((course.timeSlotValues[j]>0) && (current_course.timeSlotValues[j]>0))
						conflicts[j]++;
				}
			}
		}
		return conflicts;
	}

	/**
	 * Minimum Remaining Value: Used for improving search
	 * @param course_list_list
	 * @return
	 */
	private Course minimumRemainingValue(ArrayList<Course> course_list_list) {
		Course course = null;
		Course least_course = null;
		int minimum = Integer.MAX_VALUE;
		int number;
		for(int i=0; i<course_list_list.size(); i++) {
			course = course_list_list.get(i);
			number = 0;
			for(int j=0;j<course.timeSlotValues.length;j++) {
				if(course.timeSlotValues[j]>0)
					number++;
			}
			if(number<minimum) {
				minimum = number;
				least_course = course;
			}
			if(minimum == number) {
				least_course = degree(course_list_list, course, least_course);
			}
		}
		return least_course;
	}

	/**
	 * Degree Heuristic: Tie breaker for minimumRemainingValue
	 * @param course_list
	 * @param first_course
	 * @param second_course
	 * @return
	 */
	private Course degree(ArrayList<Course> course_list, Course first_course, Course second_course) {
		//Base Cases
		if(first_course == null && second_course == null)
			return null;
		if(first_course == null)
			return second_course;
		if(second_course == null)
			return first_course;
		int[] conflicts_first_course = new int[first_course.timeSlotValues.length];
		int[] conflicts_second_course = new int[first_course.timeSlotValues.length];
		int first_course_location = course_list.indexOf(first_course);
		int second_course_location = course_list.indexOf(second_course);
		Course course;
		for(int i=0;i<course_list.size();i++) {
			course = course_list.get(i);
			if((course_list.indexOf(first_course) != i) && (i != second_course_location)) {
				for(int j=0; j<conflicts_first_course.length; j++) {
					if((course.timeSlotValues[j]>0) && (first_course.timeSlotValues[j]>0))
						conflicts_first_course[j]++;
				}
			}
			if((course_list.indexOf(second_course) != i) && (i != first_course_location)) {
				for(int j=0; j<conflicts_second_course.length; j++) {
					if((course.timeSlotValues[j]>0) && (second_course.timeSlotValues[j]>0))
						conflicts_second_course[j]++;
				}
			}
		}
		int first_course_total=0;
		int second_course_total=0;
		for(int i=0; i<first_course.timeSlotValues.length; i++) {
			first_course_total+= conflicts_first_course[i];
			second_course_total+= conflicts_second_course[i];
		}
		if(second_course_total>first_course_total)
			return second_course;
		else
			return first_course;
	}

	/**
	 * Solved: 
	 * @param problem
	 * @return
	 */
	private boolean Solved(SchedulingProblem problem) {
		for(int i=0; i<problem.courses.size(); i++) {
			Course course = problem.courses.get(i);
			if(!course.scheduled)
				return false;
		}
		return true;
	}

	/**
	 * Naive Baseline
	 * @param problem
	 * @param deadline
	 * @return
	 */
	public Schedule naiveBaseline(SchedulingProblem problem, long deadline) {

		// get an empty solution to start from
		Schedule solution = problem.getEmptySchedule();

		for (int i = 0; i < problem.courses.size(); i++) {
			Course c = problem.courses.get(i);
			boolean scheduled = false;
			for (int j = 0; j < c.timeSlotValues.length; j++) {
				if (scheduled) break;
				if (c.timeSlotValues[j] > 0) {
					for (int k = 0; k < problem.rooms.size(); k++) {
						if (solution.schedule[k][j] < 0) {
							solution.schedule[k][j] = i;
							scheduled = true;
							break;
						}
					}
				}
			}
		}
		return solution;
	}
}
