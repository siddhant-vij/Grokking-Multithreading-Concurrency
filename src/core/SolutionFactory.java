package core;

import solutions.*;
import solutions.easy.*;

public class SolutionFactory {
  public static Solution getSolution(int solutionNumber) {
    switch (solutionNumber) {
      case 0:
        return new HelloWorld();
      case 1:
        return new LinearSearchFirst();
      case 2:
        return new LinearSearchAll();
      case 3:
        return new MinMaxSum();
      case 4:
        return new PiMonteCarlo();
      case 5:
        return new OddEvenSort();
      default:
        return null;
    }
  }
}