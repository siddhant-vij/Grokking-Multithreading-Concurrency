package solutions.medium;

import core.Solution;
import java.util.concurrent.CountDownLatch;

class PrintInOrderHelper {
  private final CountDownLatch firstDone;
  private final CountDownLatch secondDone;

  public PrintInOrderHelper(CountDownLatch firstDone, CountDownLatch secondDone) {
    this.firstDone = firstDone;
    this.secondDone = secondDone;
  }

  public void printFirst() {
    System.out.println("first");
    firstDone.countDown();
  }

  public void printSecond() {
    try {
      firstDone.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("second");
    secondDone.countDown();
  }

  public void printThird() {
    try {
      secondDone.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("third");
  }
}

public class PrintInOrder implements Solution {
  @Override
  public void solve() {
    CountDownLatch firstDone = new CountDownLatch(1);
    CountDownLatch secondDone = new CountDownLatch(1);
    PrintInOrderHelper helper = new PrintInOrderHelper(firstDone, secondDone);
    Thread first = new Thread(() -> helper.printFirst());
    Thread second = new Thread(() -> helper.printSecond());
    Thread third = new Thread(() -> helper.printThird());
    first.start();
    second.start();
    third.start();
  }
}
