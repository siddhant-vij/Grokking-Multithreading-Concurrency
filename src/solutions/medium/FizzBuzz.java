package solutions.medium;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import core.Solution;

class FizzBuzzHelper {
  private int currentNum;
  private int num;
  private Lock lock;
  private Condition condition;

  public FizzBuzzHelper(int currentNum, int num, Lock lock, Condition condition) {
    this.currentNum = currentNum;
    this.num = num;
    this.lock = lock;
    this.condition = condition;
  }

  public int getCurrentNum() {
    return currentNum;
  }

  public void fizzPrinter(Runnable printer) {
    while (currentNum <= num) {
      lock.lock();
      try {
        while (currentNum % 3 != 0 || currentNum % 5 == 0 && currentNum <= num) {
          condition.await();
        }
        if (currentNum > num)
          return;
        printer.run();
        currentNum++;
        condition.signalAll();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    }

  }

  public void buzzPrinter(Runnable printer) {
    while (currentNum <= num) {
      lock.lock();
      try {
        while (currentNum % 5 != 0 || currentNum % 3 == 0 && currentNum <= num) {
          condition.await();
        }
        if (currentNum > num)
          return;
        printer.run();
        currentNum++;
        condition.signalAll();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    }
  }

  public void fizzBuzzPrinter(Runnable printer) {
    while (currentNum <= num) {
      lock.lock();
      try {
        while (currentNum % 15 != 0 && currentNum <= num) {
          condition.await();
        }
        if (currentNum > num)
          return;
        printer.run();
        currentNum++;
        condition.signalAll();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    }
  }

  public void numberPrinter(Runnable printer) {
    while (currentNum <= num) {
      lock.lock();
      try {
        while ((currentNum % 3 == 0 || currentNum % 5 == 0) && currentNum <= num) {
          condition.await();
        }
        if (currentNum > num)
          return;
        printer.run();
        currentNum++;
        condition.signalAll();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    }
  }
}

public class FizzBuzz implements Solution {
  @Override
  public void solve() {
    int num = 20;
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    FizzBuzzHelper fizzBuzz = new FizzBuzzHelper(1, num, lock, condition);

    Thread fizzThread = new Thread(() -> {
      fizzBuzz.fizzPrinter(() -> System.out.println(fizzBuzz.getCurrentNum() + " - fizz"));
    });

    Thread buzzThread = new Thread(() -> {
      fizzBuzz.buzzPrinter(() -> System.out.println(fizzBuzz.getCurrentNum() + " - buzz"));
    });

    Thread fizzBuzzThread = new Thread(() -> {
      fizzBuzz.fizzBuzzPrinter(() -> System.out.println(fizzBuzz.getCurrentNum() + " - fizzbuzz"));
    });

    Thread numberThread = new Thread(() -> {
      fizzBuzz.numberPrinter(() -> System.out.println(fizzBuzz.getCurrentNum()));
    });

    fizzThread.start();
    buzzThread.start();
    fizzBuzzThread.start();
    numberThread.start();
  }
}
