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
 * @author jairton
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
	
	private static void alg1(int threads, int numbers) {
		final AtomicInteger primes = new AtomicInteger(0);
		long time = System.currentTimeMillis();

		System.out.println("ALGORITMO MULTI THREAD COM AGRUPAMENTO");
		
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
					if (PrimeTester.isPrimeFast(j)) {
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
	
	private static void alg2(int threads, int numbers) {
		final AtomicInteger primes = new AtomicInteger(0);
		long time = System.currentTimeMillis();

		System.out.println("ALGORITMO MULTI THREAD COM AGRUPAMENTO BALANCEADO");
		
		ExecutorService poolCount = Executors.newFixedThreadPool(1);
		ExecutorService poolProcessing = Executors.newFixedThreadPool(threads);
		
		MemoryCounter memory = new MemoryCounter();
		memory.start();
		
		for (int i = 0; i < threads; i++) {
			
			final Integer value = i;
			
			Future<Integer> future = poolProcessing.submit(() -> {
				int localPrimes = 0;
				
				for (int j = value +1; j < numbers; j+=threads) {
					if (PrimeTester.isPrimeFast(j)) {
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
