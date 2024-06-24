/*
 * https://www.geeksforgeeks.org/estimating-value-pi-using-monte-carlo/
 */

package solutions.easy;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.concurrent.TimeUnit.SECONDS;

import core.Solution;

class PiMonteCarloTask implements Callable<Integer> {
  private int start;
  private int end;

  public PiMonteCarloTask(int start, int end) {
    this.start = start;
    this.end = end;
  }

  @Override
  public Integer call() {
    int count = 0;
    for (int i = start; i < end; i++) {
      double x = Math.random() * 2 - 1;
      double y = Math.random() * 2 - 1;
      if (x * x + y * y <= 1) {
        count++;
      }
    }
    return count;
  }
}

public class PiMonteCarlo implements Solution {
  private double piEstimation(int size, int numThreads) {
    ExecutorService es = Executors.newFixedThreadPool(numThreads);
    List<Future<Integer>> ftrList = new ArrayList<>();
    for (int i = 0; i < numThreads; i++) {
      int start = i * size / numThreads;
      int end = (i + 1) * size / numThreads;
      PiMonteCarloTask task = new PiMonteCarloTask(start, end);
      Future<Integer> ftrVal = es.submit(task);
      ftrList.add(ftrVal);
    }

    es.shutdown();
    try {
      es.awaitTermination(10, SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    int count = 0;
    for (Future<Integer> ftr : ftrList) {
      try {
        count += ftr.get();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return 4.0 * count / size;
  }

  @Override
  public void solve() {
    int size = 20000000;
    int numThreads = 6;
    System.out.println(piEstimation(size, numThreads));
  }
}
