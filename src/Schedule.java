/**
 * CS 165a -- Artificial Intelligence
 * Lab 02 // Assignment 3
 * Schedule.java
 * By: Hiram A Rios && Matthew S Montoya
 * Instructor: Dr. Chistopher Kiekintveld
 * TA: Khandoker Rahad
 * Purpose: To implement and test two different search methods for solving a class-scheduling problem (Simulated Annealing + Backtracking).
 * Last Modified: April 3, 2019
 */

 public class Schedule {
  int[][] schedule;

  Schedule(int nRooms, int nTimeSlots) {
    schedule = new int[nRooms][nTimeSlots];
  }
}
