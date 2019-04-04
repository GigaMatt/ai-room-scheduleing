# Artificial Intelligence Class Scheduling with Tree Searching
## Java Project for Implementing A.I. Tree Searches (Simulated Annealing + Backtracking)
![alt text](https://raw.githubusercontent.com/gigamatt/ai-room-scheduleing/master/img/readme_image.png)

## About
Built in Java, the _AI Room Scheduling_ project implements more of the fundamental search methods used for problem solving in artificial intelligence, and to expand on utilizing heuristicss for improving search. In this project, we experiment with a basic formulation of the class scheduling problem. 

_Tree Search + Pathfinding_ is a midterm project by Matthew Montoya & Hiram Rios for UTEP's Artificial Intelligence, CS 4320.

## How To Compile/Run
1. Open the directory to **/ai-room-scheduling/**
2. Execute jar file by typing in: **java -jar homework3.jar < execution-parameters >**

**NOTE FOR EXECUTION PARAMETERS:**
1. The first parameter is the number of buildings (int value).
2. The second parameter is the number of rooms (int value).
3. The third paramter is the number of courses (int value).
4. The fourth parameter is the time limit (in seconds [int value]).
5. The fifth parameter is the algorithm number (int value [see below]).<br />
**Algorithm 0 is Naive Baseline.**<br />
**Algorithm 1 is Simulated Annealing.**<br />
**Algorithm 2 is Backtracking.**<br />
6. The sixth paramter is the seed value (long value)<br />

Example: **java -jar homework3.jar 2 15 15 200 2 1000** passes 2 buildings, 15 rooms, 15 courses, 200 seconds, algorithm #2, and a seed value of 1000 to be evaluated.

Alternatively, the _.java_ files may be imported into your IDE and run locally

## Dependancies
1. Java 8 or later
2. Microsoft Windows 7 or later or macOS High Sierra or later
