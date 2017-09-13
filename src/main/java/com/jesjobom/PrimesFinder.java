package com.jesjobom;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jesjobom
 */
public class PrimesFinder {
	
	private static final int THREADS = 10;
	
	private static final int NUMBERS = 300_000_000;
	
	public static void main(String[] args) {
		Runtime.getRuntime().gc();
		alg1(THREADS, NUMBERS);
		
		Runtime.getRuntime().gc();
		alg2(THREADS, NUMBERS);
	}
	
	/**
	 * This method will look for primes from 1 to <code>numbers</code> distribuiting 
	 * the processing among <code>thread</code> threads.
	 * 
	 * Each thread will receive the same amount of numbers to check, following the order:
	 * <ul>
	 * <li><strong>thread 1:</strong> 0*(numbers/thread)+1 to 1*(numbers/thread)</li>
	 * <li><strong>thread 2:</strong> 1*(numbers/thread)+1 to 2*(numbers/thread)</li>
	 * <li><strong>thread 3:</strong> 2*(numbers/thread)+1 to 3*(numbers/thread)</li>
	 * </ul>
	 * 
	 * And so on... Example with 100_000 numbers and 10 threads:
	 * <ul>
	 * <li><strong>thread 1:</strong> 1 to 10_000</li>
	 * <li><strong>thread 2:</strong> 10_001 to 20_000</li>
	 * <li><strong>thread 3:</strong> 20_001 to 30_000</li>
	 * ...
	 * <li><strong>thread 10:</strong> 90_001 to 100_000</li>
	 * </ul>
	 * 
	 * Like this, theoretically, the initial threads should have an easier job 
	 * and should end earlier, leaving the last thead with the heaviest task.
	 * 
	 * @param threads
	 * @param numbers 
	 */
	private static void alg1(int threads, int numbers) {
		final AtomicInteger primes = new AtomicInteger(0);
		long time = System.currentTimeMillis();

		System.out.println("ALGORITHM 1 - THREADS WITH SEQUENCIAL GROUPS");
		
		ExecutorService poolCount = Executors.newFixedThreadPool(1);
		ExecutorService poolProcessing = Executors.newFixedThreadPool(threads);
		MemoryCounter memory = new MemoryCounter();
		memory.start();
		
		for (int i = 0; i < threads; i++) {
			
			final Integer value = i;
			
			Future<Integer> future = poolProcessing.submit(() -> {
				int range = numbers / threads;
				int localPrimes = 0;
				
				for (int j = value * range +1; j <= (value+1) * range; j++) {
					if (PrimeTester.isPrime(j)) {
						localPrimes++;
					}
				}
				return localPrimes;
			});
			
			poolCount.submit(() -> {
				try {
					primes.getAndAdd(future.get());
				} catch (InterruptedException ex) {
					Logger.getLogger(PrimesFinder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (ExecutionException ex) {
					Logger.getLogger(PrimesFinder.class.getName()).log(Level.SEVERE, null, ex);
				}
			});
		}
		poolProcessing.shutdown();
		poolCount.shutdown();
		try {
			poolCount.awaitTermination(5, TimeUnit.MINUTES);
		} catch (InterruptedException ex) {
			Logger.getLogger(PrimesFinder.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		memory.shutdown();
		System.out.println(String.format("Found %,d primes from 1 to %.0e in %d seconds using %d MB of memory.", primes.get(), (double)numbers, (System.currentTimeMillis() - time) / 1000, memory.getFreeMemory() / (1024 * 1024)));
		
	}
	
	/**
	 * This method will look for primes from 1 to <code>numbers</code> distribuiting 
	 * the processing among <code>thread</code> threads.
	 * 
	 * Each thread will receive the same amount of numbers to check, following the order:
	 * <ul>
	 * <li><strong>thread 1:</strong> 0*(thread)+1, 1*(thread)+1, 2*(thread)+1, ..., (numbers/thread -1)*(thread)+1</li>
	 * <li><strong>thread 2:</strong> 0*(thread)+2, 1*(thread)+2, 2*(thread)+2, ..., (numbers/thread -1)*(thread)+2</li>
	 * <li><strong>thread 3:</strong> 0*(thread)+3, 1*(thread)+3, 2*(thread)+3, ..., (numbers/thread -1)*(thread)+3</li>
	 * </ul>
	 * 
	 * And so on... Example with 100_000 numbers and 10 threads:
	 * <ul>
	 * <li><strong>thread 1:</strong> 1, 11, 21, 31, ..., 99_991</li>
	 * <li><strong>thread 2:</strong> 2, 12, 22, 32, ..., 99_992</li>
	 * <li><strong>thread 3:</strong> 3, 13, 23, 33, ..., 99_993</li>
	 * ...
	 * <li><strong>thread 10:</strong> 10, 20, 30, 40, ..., 100_000</li>
	 * </ul>
	 * 
	 * Like this, theoretically, the burden to find primes should be equally
	 * distribuited, with all the  threads ending at the same time.
	 * 
	 * @param threads
	 * @param numbers 
	 */
	private static void alg2(int threads, int numbers) {
		final AtomicInteger primes = new AtomicInteger(0);
		long time = System.currentTimeMillis();

		System.out.println("ALGORITHM 2 - THREADS WITH BALANCED GROUPS");
		
		ExecutorService poolCount = Executors.newFixedThreadPool(1);
		ExecutorService poolProcessing = Executors.newFixedThreadPool(threads);
		
		MemoryCounter memory = new MemoryCounter();
		memory.start();
		
		for (int i = 0; i < threads; i++) {
			
			final Integer value = i;
			
			Future<Integer> future = poolProcessing.submit(() -> {
				int localPrimes = 0;
				
				for (int j = value +1; j < numbers; j+=threads) {
					if (PrimeTester.isPrime(j)) {
						localPrimes++;
					}
				}
				return localPrimes;
			});
			
			poolCount.submit(() -> {
				try {
					primes.getAndAdd(future.get());
				} catch (InterruptedException ex) {
					Logger.getLogger(PrimesFinder.class.getName()).log(Level.SEVERE, null, ex);
				} catch (ExecutionException ex) {
					Logger.getLogger(PrimesFinder.class.getName()).log(Level.SEVERE, null, ex);
				}
			});
		}
		poolProcessing.shutdown();
		poolCount.shutdown();
		try {
			poolCount.awaitTermination(5, TimeUnit.MINUTES);
		} catch (InterruptedException ex) {
			Logger.getLogger(PrimesFinder.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		memory.shutdown();
		System.out.println(String.format("Found %,d primes from 1 to %.0e in %d seconds using %d MB of memory.", primes.get(), (double)numbers, (System.currentTimeMillis() - time) / 1000, memory.getFreeMemory() / (1024 * 1024)));
		
	}
}
