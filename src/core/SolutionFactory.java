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
      default:
        return null;
    }
  }
}