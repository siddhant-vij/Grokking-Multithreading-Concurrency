import core.SolutionExecutor;

public class Main {
  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("Please provide a solution number");
      return;
    }

    int solutionNumber = Integer.parseInt(args[0]);
    if (solutionNumber < 0 || solutionNumber > 17) {
      System.out.println("Invalid solution number");
      return;
    }

    SolutionExecutor solutionExecutor = new SolutionExecutor();
    solutionExecutor.executeSolution(solutionNumber);
  }
}