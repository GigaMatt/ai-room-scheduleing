public class Main {

  public static void main(String[] args) {

    int nBuildings = 0;
    int nRooms = 0;
    int nCourses = 0;
    int TIME_LIMIT_SECONDS = 0;
    int algorithm = 0;
    long seed = 0;

    if (args.length == 6) {
      try {
        nBuildings = Integer.parseInt(args[0]);
        nRooms = Integer.parseInt(args[1]);
        nCourses = Integer.parseInt(args[2]);
        TIME_LIMIT_SECONDS = Integer.parseInt(args[3]);
        algorithm = Integer.parseInt(args[4]);
        seed = Long.parseLong(args[5]);
      } catch (NumberFormatException e) {
        System.out.println("Number format exception reading arguments");
        System.exit(1);
      }
    } else {
      System.out.println("ERROR: Incorrect number of arguments (should have six).");
      System.exit(1);
    }

    System.out.println("Number of Buildings: " + nBuildings);
    System.out.println("Number of Rooms: " + nRooms);
    System.out.println("Number of Courses: " + nCourses);
    System.out.println("Time limit (s): " + TIME_LIMIT_SECONDS);
    System.out.println("Algorithm number: " + algorithm);
    System.out.println("Random seed: " + seed);


    SchedulingProblem test1 = new SchedulingProblem(seed);
    test1.createRandomInstance(nBuildings, nRooms, nCourses);

    SearchAlgorithm search = new SearchAlgorithm();

    long deadline = System.currentTimeMillis() + (1000 * TIME_LIMIT_SECONDS);

    // Add your seach algorithms here, each with a unique number
    Schedule solution = null;
    if (algorithm == 0) {
      solution = search.naiveBaseline(test1, deadline);
    } else {
      System.out.println("ERROR: Given algorithm number does not exist!");
      System.exit(1);
    }

    System.out.println("Deadline: " + deadline);
    System.out.println("Current: " + System.currentTimeMillis());
    System.out.println("Time remaining: " + (deadline - System.currentTimeMillis()));
    if (System.currentTimeMillis() > deadline) {
      System.out.println("EXCEEDED DEADLINE");
    }

    double score = test1.evaluateSchedule(solution);
    System.out.println();
    System.out.println("Score: " + score);
    System.out.println();
  }  
}