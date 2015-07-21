package rr.scr.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ThreadSet {
	private final Collection<Thread> ts = new ArrayList<Thread>();

	public boolean add(Thread t) {
		return ts.add(t);
	}

	public void start() {
		Iterator<Thread> it = ts.iterator();
		while (it.hasNext()) {
			it.next().start();
		}
	}

	public void join() {
		Iterator<Thread> it = ts.iterator();
		while (it.hasNext()) {
			Thread t = it.next();
			if (t.isAlive()) {
				try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
