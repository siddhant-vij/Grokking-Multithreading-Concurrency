package solutions.medium;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import core.Solution;

class LeapYearDetectorHelper {
  private int[] years;
  private int length;
  private int curIndex;

  private Lock lock;
  private Condition leapYearCondition;
  private Condition nonLeapYearCondition;

  public LeapYearDetectorHelper(int[] years, int length, int curIndex, Lock lock, Condition leapYearCondition,
      Condition nonLeapYearCondition) {
    this.years = years;
    this.length = length;
    this.curIndex = curIndex;

    this.lock = lock;
    this.leapYearCondition = leapYearCondition;
    this.nonLeapYearCondition = nonLeapYearCondition;
  }

  private boolean isLeapYear(int year) {
    return (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0);
  }

  public void leapYearPrinter() {
    while (curIndex < length) {
      lock.lock();
      try {
        if (isLeapYear(years[curIndex])) {
          System.out.println(years[curIndex] + " is a leap year");
          curIndex++;
          nonLeapYearCondition.signal();
        } else {
          leapYearCondition.await();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    }
  }

  public void nonLeapYearPrinter() {
    while (curIndex < length) {
      lock.lock();
      try {
        if (!isLeapYear(years[curIndex])) {
          System.out.println(years[curIndex] + " is not a leap year");
          curIndex++;
          leapYearCondition.signal();
        } else {
          nonLeapYearCondition.await();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    }
  }
}

public class LeapYearDetector implements Solution {
  @Override
  public void solve() {
    final int[] years = { 1990, 1991, 1992, 1993, 1994, 1995, 1996, 1997, 1998, 1999, 2000, 2001, 2002, 2003, 2004,
        2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022,
        2023, 2024 };
    int length = years.length;
    int curIndex = 0;

    Lock lock = new ReentrantLock();
    Condition leapCondition = lock.newCondition();
    Condition nonLeapCondition = lock.newCondition();

    LeapYearDetectorHelper leapYearHelper = new LeapYearDetectorHelper(years, length, curIndex, lock, leapCondition,
        nonLeapCondition);

    Thread leapThread = new Thread(() -> {
      leapYearHelper.leapYearPrinter();
    });

    Thread nonLeapThread = new Thread(() -> {
      leapYearHelper.nonLeapYearPrinter();
    });

    leapThread.start();
    nonLeapThread.start();

    try {
      leapThread.join();
      nonLeapThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
