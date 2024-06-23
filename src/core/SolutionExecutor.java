package core;

public class SolutionExecutor {
  public void executeSolution(int solutionNumber) {
    Solution solution = SolutionFactory.getSolution(solutionNumber);
    if (solution != null) {
      solution.solve();
    } else {
      System.out.println("Solution not found");
    }
  }
}