package com.vaani.java.conc.oddeven.atomicinteger;

import java.util.concurrent.atomic.AtomicInteger;

public class OddEvenPrinterAtomic {
	public static boolean print = false;

	public static void main(String args[]) throws Exception {
		AtomicInteger atomicInt = new AtomicInteger(0);
		Thread t;
		int runs = 10;
		long time = System.currentTimeMillis();
		new Thread(new OddAtomicRunnable(atomicInt, runs)).start();
		t = new Thread(new EvenAtomicRunnable(atomicInt, runs));
		t.start();
		t.join();
		System.out.println("Runs -" + runs);
		System.out.println("Time using Atomic "
				+ (System.currentTimeMillis() - time) + "ms for runs " + runs);
	}
}

class OddAtomicRunnable implements Runnable {
	AtomicInteger atomicInt;
	int runs;

	public OddAtomicRunnable(AtomicInteger atomicInt, int runs) {
		this.atomicInt = atomicInt;
		this.runs = runs;
	}

	public void run() {
		for (int i = 1, counter = 0; counter < runs; i += 2, counter++) {
			while (!atomicInt.compareAndSet(0, 1)) {
			}
			if (OddEvenPrinterAtomic.print) {
				System.out.println(i);
			}
			while (!atomicInt.compareAndSet(1, 2)) {
			}
		}
	}
}

class EvenAtomicRunnable implements Runnable {
	AtomicInteger atomicInt;
	int runs;

	public EvenAtomicRunnable(AtomicInteger atomicInt, int runs) {
		this.atomicInt = atomicInt;
		this.runs = runs;
	}

	public void run() {
		for (int i = 2, counter = 0; counter < runs; i += 2, counter++) {
			while (!atomicInt.compareAndSet(2, 3)) {
			}
			if (OddEvenPrinterAtomic.print) {
				System.out.println(i);
			}
			while (!atomicInt.compareAndSet(3, 0)) {
			}
		}
	}
}
