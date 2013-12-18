package rr.scr.test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
	public static void main(String[] args) {
		final Lock lock = new ReentrantLock();
		final Lock lock1 = new ReentrantLock();
		final Lock lock2 = new ReentrantLock();
		
		Thread t1 = new Thread() {
			public void run() {
				System.out.println("in 1");
				lock.lock();
				lock1.lock();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				lock2.lock();
				System.out.println("1");
				lock2.unlock();
				lock1.unlock();
				lock.unlock();
			}
		};
		
		Thread t2 = new Thread() {
			public void run() {
				System.out.println("in 2");
				lock.lock();
				lock2.lock();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				lock1.lock();
				System.out.println("2");
				lock1.unlock();
				lock2.unlock();
				lock.unlock();
			}
		};
		
		t1.start();
		t2.start();
	}
}
