package solutions.medium;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

import core.Solution;

class ZeroEvenOddHelper {
  private int num;
  private Lock lock;
  private Condition zeroCondition;
  private Condition evenCondition;
  private Condition oddCondition;
  private boolean zeroTurn;
  private boolean oddTurn;

  public ZeroEvenOddHelper(int num) {
    this.num = num;
    this.lock = new ReentrantLock();
    this.zeroCondition = lock.newCondition();
    this.evenCondition = lock.newCondition();
    this.oddCondition = lock.newCondition();
    this.zeroTurn = true;
    this.oddTurn = true;
  }

  public void zeroPrinter() {
    for (int i = 0; i < num; i++) {
      lock.lock();
      try {
        while (!zeroTurn) {
          zeroCondition.await();
        }
        System.out.print(0);
        zeroTurn = false;
        if (oddTurn) {
          oddCondition.signal();
        } else {
          evenCondition.signal();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    }
  }

  public void evenPrinter() {
    for (int i = 2; i <= num; i += 2) {
      lock.lock();
      try {
        while (zeroTurn || oddTurn) {
          evenCondition.await();
        }
        System.out.print(i);
        zeroTurn = true;
        oddTurn = true;
        zeroCondition.signal();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    }
  }

  public void oddPrinter() {
    for (int i = 1; i <= num; i += 2) {
      lock.lock();
      try {
        while (zeroTurn || !oddTurn) {
          oddCondition.await();
        }
        System.out.print(i);
        zeroTurn = true;
        oddTurn = false;
        zeroCondition.signal();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    }
  }
}

public class ZeroEvenOdd implements Solution {
  @Override
  public void solve() {
    int n = 9;
    ZeroEvenOddHelper helper = new ZeroEvenOddHelper(n);
    Thread zero = new Thread(() -> helper.zeroPrinter());
    Thread even = new Thread(() -> helper.evenPrinter());
    Thread odd = new Thread(() -> helper.oddPrinter());
    zero.start();
    even.start();
    odd.start();
    try {
      zero.join();
      even.join();
      odd.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println();
  }
}
