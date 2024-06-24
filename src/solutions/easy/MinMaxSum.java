package solutions.easy;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.concurrent.TimeUnit.SECONDS;

import core.Solution;

class Statistics {
  int min;
  int max;
  int sum;

  public Statistics(int min, int max, int sum) {
    this.min = min;
    this.max = max;
    this.sum = sum;
  }

  public int getMin() {
    return min;
  }

  public int getMax() {
    return max;
  }

  public int getSum() {
    return sum;
  }
}

class StatsTask implements Callable<Statistics> {
  private int[] arr;
  private int start;
  private int end;

  public StatsTask(int[] arr, int start, int end) {
    this.arr = arr;
    this.start = start;
    this.end = end;
  }

  @Override
  public Statistics call() {
    int min = Integer.MAX_VALUE;
    int max = Integer.MIN_VALUE;
    int sum = 0;
    for (int i = start; i < end; i++) {
      min = Math.min(min, arr[i]);
      max = Math.max(max, arr[i]);
      sum += arr[i];
    }
    return new Statistics(min, max, sum);
  }
}

public class MinMaxSum implements Solution {
  private List<Future<Statistics>> startTasks(ExecutorService es, int[] arr, int numThreads) {
    List<Future<Statistics>> statsFtrList = new ArrayList<>();
    for (int i = 0; i < numThreads; i++) {
      int start = i * arr.length / numThreads;
      int end = (i + 1) * arr.length / numThreads;
      StatsTask st = new StatsTask(arr, start, end);
      Future<Statistics> statsFtr = es.submit(st);
      statsFtrList.add(statsFtr);
    }
    return statsFtrList;
  }

  private void closeTasks(ExecutorService es) {
    es.shutdown();
    try {
      es.awaitTermination(10, SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private List<Statistics> getStatsData(int[] arr, int numThreads) {
    ExecutorService es = Executors.newFixedThreadPool(numThreads);
    List<Future<Statistics>> statsFtrList = startTasks(es, arr, numThreads);

    closeTasks(es);

    List<Statistics> statsList = new ArrayList<>();
    try {
      for (Future<Statistics> statsFtr : statsFtrList) {
        statsList.add(statsFtr.get());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return statsList;
  }

  private int threadedMin(int[] arr, int numThreads) {
    List<Statistics> statsList = getStatsData(arr, numThreads);
    int min = Integer.MAX_VALUE;
    for (Statistics stats : statsList) {
      min = Math.min(min, stats.getMin());
    }
    return min;
  }

  private int threadedMax(int[] arr, int numThreads) {
    List<Statistics> statsList = getStatsData(arr, numThreads);
    int max = Integer.MIN_VALUE;
    for (Statistics stats : statsList) {
      max = Math.max(max, stats.getMax());
    }
    return max;
  }

  private int threadedSum(int[] arr, int numThreads) {
    List<Statistics> statsList = getStatsData(arr, numThreads);
    int sum = 0;
    for (Statistics stats : statsList) {
      sum += stats.getSum();
    }
    return sum;
  }

  @Override
  public void solve() {
    int size = 20000;
    int numThreads = 6;

    int[] arr = new int[size];
    for (int i = 0; i < size; i++) {
      arr[i] = Math.abs((int) (Math.random() * 100));
    }

    System.out.println(threadedMin(arr, numThreads));
    System.out.println(threadedMax(arr, numThreads));
    System.out.println(threadedSum(arr, numThreads));
  }
}
