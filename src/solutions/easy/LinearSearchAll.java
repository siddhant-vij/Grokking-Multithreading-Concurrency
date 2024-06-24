package solutions.easy;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.concurrent.TimeUnit.SECONDS;

import core.Solution;

class SolutionTaskAll implements Callable<List<Integer>> {
  private int[] arr;
  private int target;
  private int start;
  private int end;

  public SolutionTaskAll(int[] arr, int target, int start, int end) {
    this.arr = arr;
    this.target = target;
    this.start = start;
    this.end = end;
  }

  @Override
  public List<Integer> call() {
    List<Integer> res = new ArrayList<>();
    for (int i = start; i < end; i++) {
      if (arr[i] == target) {
        res.add(i);
      }
    }
    return res;
  }
}

public class LinearSearchAll implements Solution {
  private List<Integer> linearSearchAll(int[] arr, int target, int numThreads) {
    List<Integer> indices = new ArrayList<>();
    ExecutorService es = Executors.newFixedThreadPool(numThreads);
    List<Future<List<Integer>>> idxFtrList = null;
    for (int i = 0; i < numThreads; i++) {
      int start = i * arr.length / numThreads;
      int end = (i + 1) * arr.length / numThreads;
      SolutionTaskAll st = new SolutionTaskAll(arr, target, start, end);
      Future<List<Integer>> idxFtr = es.submit(st);
      if (idxFtrList == null) {
        idxFtrList = new ArrayList<>();
      }
      idxFtrList.add(idxFtr);
    }
    
    try {
      es.shutdown();
      es.awaitTermination(10, SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    for (Future<List<Integer>> idxFtr : idxFtrList) {
      try {
        List<Integer> idxList = idxFtr.get();
        if (!idxList.isEmpty()) {
          indices.addAll(idxList);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return indices;
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
    System.out.println(linearSearchAll(arr, target, numThreads));
  }
}
