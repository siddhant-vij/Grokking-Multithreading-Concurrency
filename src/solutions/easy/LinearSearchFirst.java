package solutions.easy;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.concurrent.TimeUnit.SECONDS;

import core.Solution;

class SolutionTask implements Callable<Integer> {
  private int[] arr;
  private int target;
  private int start;
  private int end;

  public SolutionTask(int[] arr, int target, int start, int end) {
    this.arr = arr;
    this.target = target;
    this.start = start;
    this.end = end;
  }

  @Override
  public Integer call() {
    int index = -1;
    for (int i = start; i < end; i++) {
      if (arr[i] == target) {
        index = i;
        break;
      }
    }
    return index;
  }
}

public class LinearSearchFirst implements Solution {

  private int linearSearch(int[] arr, int target, int numThreads) {
    int index = -1;
    ExecutorService es = Executors.newFixedThreadPool(numThreads);
    for (int i = 0; i < numThreads; i++) {
      int start = i * arr.length / numThreads;
      int end = (i + 1) * arr.length / numThreads;
      SolutionTask st = new SolutionTask(arr, target, start, end);
      Future<Integer> idxFtr = es.submit(st);
      try {
        index = idxFtr.get();
        if (index != -1) {
          // Once any of the threads finds the target value, they will return the index of the target value.
          es.shutdownNow();
          break;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    try {
      es.shutdown();
      es.awaitTermination(10, SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return index;
  }

  @Override
  public void solve() {
    int size = 20000;
    int numThreads = 6;

    int[] arr = new int[size];
    for (int i = 0; i < size; i++) {
      arr[i] = Math.abs((int) (Math.random() * 100));
    }

    int target = Math.abs((int) (Math.random() * 100));
    System.out.println("Target: " + target);
    System.out.println(linearSearch(arr, target, numThreads));
  }
}
