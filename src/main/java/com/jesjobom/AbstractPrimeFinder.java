package com.jesjobom;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author jairton
 */
public abstract class AbstractPrimeFinder {
	
	private AtomicInteger primesCounter;
	
	private long startTime;
	
	private MemoryCounter memory;
	
	protected final int numbers;
	
	protected final int threads;

	public AbstractPrimeFinder(int numbers, int threads) {
		this.numbers = numbers;
		this.threads = threads;
	}
	
	public abstract void run();
	
	protected final void initTest(String title) {
		System.out.println(title);
		primesCounter = new AtomicInteger(0);
		
		Runtime.getRuntime().gc();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException ex) {
		}
		
		startTime = System.currentTimeMillis();
		
		memory = new MemoryCounter();
		memory.start();
	}
	
	protected final void incrementPrimes(Integer increment) {
		this.primesCounter.addAndGet(increment);
	}
	
	protected final void finish() {
		memory.shutdown();
		System.out.println(String.format("Found %,d primes from 1 to %.0e in %d seconds using %d MB of memory and %d threads.", primesCounter.get(), (double)numbers, (System.currentTimeMillis() - startTime) / 1000, memory.getFreeMemory() / (1024 * 1024), threads));
	}
}
