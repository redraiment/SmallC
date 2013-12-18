package rr.scr.test;

import java.util.Scanner;

public class MutilThread {
	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread() {
			public void run() {
				new Thread() {
					public void run() {
						Scanner cin = new Scanner(System.in);
						while (cin.hasNext()) {
							int i = cin.nextInt();
							if (i == 0) {
								break;
							}
							System.out.println("i = " + i);
						}
					}
				}.start();
			}
		};
		t.start();
		System.out.println("Start");
		t.join();
		System.out.println("Join");
		System.out.println(t.isAlive());
	}
}
