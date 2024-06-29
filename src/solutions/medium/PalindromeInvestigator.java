package solutions.medium;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import core.Solution;

class PalindromeInvestigatorHelper {
  private int[] numbers;
  private int length;
  private int curIndex;

  private Lock lock;
  private Condition isPalindrome;
  private Condition notPalindrome;

  public PalindromeInvestigatorHelper(int[] numbers, int length, int curIndex, Lock lock, Condition isPalindrome,
      Condition notPalindrome) {
    this.numbers = numbers;
    this.length = length;
    this.curIndex = curIndex;

    this.lock = lock;
    this.isPalindrome = isPalindrome;
    this.notPalindrome = notPalindrome;
  }

  private boolean isPalindrome(int number) {
    String numberString = String.valueOf(number);
    String reversedString = new StringBuilder(numberString).reverse().toString();
    return numberString.equals(reversedString);
  }

  public void palindromePrinter() {
    while (curIndex < length) {
      lock.lock();
      try {
        if (isPalindrome(numbers[curIndex])) {
          System.out.println(numbers[curIndex] + " is a palindrome");
          curIndex++;
          notPalindrome.signal();
        } else {
          isPalindrome.await();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    }
  }

  public void notPalindromePrinter() {
    while (curIndex < length) {
      lock.lock();
      try {
        if (!isPalindrome(numbers[curIndex])) {
          System.out.println(numbers[curIndex] + " is not a palindrome");
          curIndex++;
          isPalindrome.signal();
        } else {
          notPalindrome.await();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    }
  }
}

public class PalindromeInvestigator implements Solution {
  @Override
  public void solve() {
    int[] numbers = { 123, 454, 1567, 1771, 89098, 12321, 45654 };
    int length = numbers.length;
    int curIndex = 0;

    Lock lock = new ReentrantLock();
    Condition isPalindrome = lock.newCondition();
    Condition notPalindrome = lock.newCondition();

    PalindromeInvestigatorHelper helper = new PalindromeInvestigatorHelper(numbers, length, curIndex, lock,
        isPalindrome, notPalindrome);

    Thread palindrome = new Thread(() -> helper.palindromePrinter());
    Thread notPalindromeThread = new Thread(() -> helper.notPalindromePrinter());

    palindrome.start();
    notPalindromeThread.start();

    try {
      palindrome.join();
      notPalindromeThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
