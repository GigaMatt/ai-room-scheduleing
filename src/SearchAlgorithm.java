import java.util.ArrayList;

public class SearchAlgorithm {

  // Your search algorithm should return a solution in the form of a valid
  // schedule before the deadline given (deadline is given by system time in ms)
  public Schedule solve(SchedulingProblem problem, long deadline) {

    // get an empty solution to start from
	  Schedule current = problem.getEmptySchedule();
	  Schedule next = problem.getEmptySchedule();
	  int T = 10000;
	  double deltaE;
	    
	  while(true) {
		  if(T == 0) {
			  return current;
		  }
		  next = createNext(problem);
		  deltaE = problem.evaluateSchedule(next) - problem.evaluateSchedule(current);
		  if(deltaE > 0) {
			  current = next;
		  }
		  T--;
	  }
  }
	  
  private Schedule createNext(SchedulingProblem problem) {
	  int j = 0;
	  int k = 0;
	  Schedule next = problem.getEmptySchedule();
	  for(int i = 0; i < problem.courses.size(); i++) {
		  Course randCourse = problem.courses.get(i);
		  j = (int) (Math.random() * randCourse.timeSlotValues.length);
		  k = (int) (Math.random() * problem.rooms.size());
		  while(next.schedule[k][j] != -1) {
			  j = (int) (Math.random() * randCourse.timeSlotValues.length);
			  k = (int) (Math.random() * problem.rooms.size());
		  }
		  next.schedule[k][j] = i;
	  }
	  return next;
  }

  public Schedule solveCSP(SchedulingProblem problem, long deadline) {
	  Schedule solution = problem.getEmptySchedule();
	  ArrayList<Course> tmp = new ArrayList<Course>(problem.courses);
	  solution = solveCSPRec(problem,solution,tmp);
	  return solution;
  }
  
  private Schedule solveCSPRec(SchedulingProblem problem, Schedule sol, ArrayList<Course> tmp) {
	  if(tmp.size() == 0) {
		  return sol;
	  }
	 Course c = minRemaining(tmp);
	 if(c == null) return sol;
	 tmp.remove(tmp.indexOf(c));
	 int j=0;
	 int slots[] = leastContraining(c,problem.courses);
	 int pos = Integer.MAX_VALUE;
	 for(int i=0;i<slots.length;i++) {
		 if((slots[i]<pos) && (slots[i]>0)) {
			 pos = slots[i];
			 j=i;
		 }
	 }
	 for(int k=0;k<problem.rooms.size();k++) {
		 if (sol.schedule[k][j] < 0) {
			 if(c.enrolledStudents <= problem.rooms.get(k).capacity) {
				 sol.schedule[k][j] = problem.courses.indexOf(c);
				 break;
			 }
		 }
	 }
	 c.scheduled = true;
	 sol = solveCSPRec(problem,sol,tmp);
	 if(!Solved(problem)) {
		 c.timeSlotValues[j] = 0;
		 tmp.add(c);
	 }
	 return sol;
  }
  
  //find course selection that leads the least constraining on other courses
  private int[] leastContraining(Course curr, ArrayList<Course> courses) { 
	  int[] conflicts = new int[curr.timeSlotValues.length];
	  Course c = null;
	  for(int i=0;i<courses.size();i++) {
		  c = courses.get(i);
		  if(courses.indexOf(curr) != i) {
			  for(int j=0;j<conflicts.length;j++) {
				  if((c.timeSlotValues[j]>0) && (curr.timeSlotValues[j]>0)) conflicts[j]++;
			  }
		  }
	  }
	  return conflicts;
  }
  
  //finds course with least remaining possible time slots
  private Course minRemaining(ArrayList<Course> courses) {
	  Course c = null;
	  Course least = null;
	  int min = Integer.MAX_VALUE;
	  int num;
	  for(int i=0;i<courses.size();i++) {
		  c = courses.get(i);
		  num = 0;
		  for(int j=0;j<c.timeSlotValues.length;j++) {
			  if(c.timeSlotValues[j] > 0) num++;
		  }
		  if(num<min) {
			  min = num;
			  least = c;
		  }
		  if(min == num) {
			  least = degree(courses,c,least);
		  }
	  }
	  return least;
  }
  
  private Course degree(ArrayList<Course> courses, Course c1, Course c2) { //tie breaker for minRemaining
	  if(c1 == null && c2 == null) return null;
	  if(c1 == null) return c2;
	  if(c2 == null) return c1;
	  int[] conflictsC1 = new int[c1.timeSlotValues.length];
	  int[] conflictsC2 = new int[c1.timeSlotValues.length];
	  int locC1 = courses.indexOf(c1);
	  int locC2 = courses.indexOf(c2);
	  Course c;
	  for(int i=0;i<courses.size();i++) {
		  c = courses.get(i);
		  if((courses.indexOf(c1) != i) && (i != locC2)) {
			  for(int j=0;j<conflictsC1.length;j++) {
				  if((c.timeSlotValues[j]>0) && (c1.timeSlotValues[j]>0)) conflictsC1[j]++;
			  }
		  }
		  if((courses.indexOf(c2) != i) && (i != locC1)) {
			  for(int j=0;j<conflictsC2.length;j++) {
				  if((c.timeSlotValues[j]>0) && (c2.timeSlotValues[j]>0)) conflictsC2[j]++;
			  }
		  }
	  }
	  int sumC1=0,sumC2=0;
	  for(int i=0;i<c1.timeSlotValues.length;i++) {
		  sumC1 += conflictsC1[i];
		  sumC2 += conflictsC2[i];
	  }
	  if(sumC2 > sumC1) return c2;
	  else return c1;
  }
  
  private boolean Solved(SchedulingProblem problem) {
	  for(int i=0;i<problem.courses.size();i++) {
		  Course c = problem.courses.get(i);
		  if(!c.scheduled) return false;
	  }
	  return true;
  }
  

  // This is a very naive baseline scheduling strategy
  // It should be easily beaten by any reasonable strategy
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
