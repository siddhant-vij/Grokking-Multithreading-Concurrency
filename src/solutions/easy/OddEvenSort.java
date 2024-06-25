package solutions.easy;

import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

import core.Solution;

public class OddEvenSort implements Solution {
  private void threadSort(int[] arr, int threadId, int numThreads, CyclicBarrier barrier) {
    int n = arr.length;
    for (int phase = 0; phase < n; phase++) {
      int begin = (threadId % 2 == phase % 2) ? threadId : threadId + 1;
      for (int i = begin; i + 1 < n; i += numThreads) {
        if (arr[i] > arr[i + 1]) {
          int temp = arr[i];
          arr[i] = arr[i + 1];
          arr[i + 1] = temp;
        }
      }
      try {
        barrier.await();
      } catch (InterruptedException | BrokenBarrierException e) {
        e.printStackTrace();
      }
    }
  }

  private void oddEvenSort(CyclicBarrier barrier, int[] arr, int numThreads) {
    Thread[] threads = new Thread[numThreads];
    for (int i = 0; i < numThreads; i++) {
      final int threadId = i;
      threads[i] = new Thread(() -> threadSort(arr, threadId, numThreads, barrier));
      threads[i].start();
    }

    for (Thread thread : threads) {
      try {
        thread.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void solve() {
    int size = 20;
    int numThreads = 4;
    CyclicBarrier barrier = new CyclicBarrier(numThreads);

    int[] arr = new int[size];
    for (int i = 0; i < size; i++) {
      arr[i] = Math.abs((int) (Math.random() * 100));
    }

    System.out.println(Arrays.toString(arr));
    oddEvenSort(barrier, arr, numThreads);
    System.out.println(Arrays.toString(arr));
  }
}
