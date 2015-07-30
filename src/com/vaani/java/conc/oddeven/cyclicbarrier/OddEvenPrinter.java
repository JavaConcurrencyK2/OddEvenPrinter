package com.vaani.java.conc.oddeven.cyclicbarrier;

import java.util.concurrent.CyclicBarrier;

public class OddEvenPrinter {
	public static boolean print = false;

	public static void main(String args[]) throws Exception {
		Thread t;
		int runs = 10000000;
		CyclicBarrier barrier = new CyclicBarrier(2);

		long time = System.currentTimeMillis();
		t = new Thread(new OddBarrierRunnable(runs, barrier), "OddThread");
		t.start();
		t = new Thread(new EvenCyclicRunnable(runs, barrier), "EvenThread");
		t.start();
		t.join();
		System.out.println("Time using cyclic barrier "
				+ (System.currentTimeMillis() - time) + "ms for runs " + runs);

	}

}

class OddBarrierRunnable implements Runnable {
	CyclicBarrier barrier;
	int runs;

	public OddBarrierRunnable(int runs, CyclicBarrier barrier) {
		this.runs = runs;
		this.barrier = barrier;
	}

	public void run() {
		try {
			for (int i = 1, counter = 0; counter < runs; i += 2, counter++) {
				if (OddEvenPrinter.print) {
					System.out.println(i);
				}
				barrier.await();
				barrier.await();

			}
		} catch (Exception e) {
			System.out.println("Opps. I was not expecting any exception here");
		}
	}
}

class EvenCyclicRunnable implements Runnable {
	CyclicBarrier barrier;
	int runs;

	public EvenCyclicRunnable(int runs, CyclicBarrier barrier) {
		this.runs = runs;
		this.barrier = barrier;
	}

	public void run() {
		try {
			for (int i = 2, counter = 0; counter < runs; i += 2, counter++) {
				barrier.await();
				if (OddEvenPrinter.print) {
					System.out.println(i);
				}
				barrier.await();
			}
		} catch (Exception e) {
			System.out.println("Opps. I was not expecting any exception here");
		}
	}
}
