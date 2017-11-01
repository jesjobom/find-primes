package com.jesjobom;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This method will look for primes from 1 to <code>numbers</code> distribuiting
 * the processing among <code>threads</code> sequencial threads.
 *
 * Each thread will receive the same amount of numbers to check, following the
 * order:
 * <ul>
 * <li><strong>thread 1:</strong> 0*(numbers/threads)+1 to 1*(numbers/threads)</li>
 * <li><strong>thread 2:</strong> 1*(numbers/threads)+1 to 2*(numbers/threads)</li>
 * <li><strong>thread 3:</strong> 2*(numbers/threads)+1 to 3*(numbers/threads)</li>
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
 * Like this, theoretically, the initial threads should have an easier job and
 * should end earlier, leaving the last thead with the heaviest task.
 *
 * @author jesjobom
 */
public class PrimeFinderSequencialThreads extends AbstractPrimeFinder {

	public PrimeFinderSequencialThreads(int numbers, int threads) {
		super(numbers, threads);
	}

	@Override
	public void run() {
		super.initTest("ALGORITHM 1 - THREADS WITH SEQUENCIAL GROUPS");

		ExecutorService poolCount = Executors.newFixedThreadPool(1);
		ExecutorService poolProcessing = Executors.newFixedThreadPool(threads);

		for (int i = 0; i < threads; i++) {

			final Integer step = i;

			Future<Integer> future = poolProcessing.submit(() -> {
				int range = numbers / threads;
				int localPrimes = 0;

				for (int j = step * range + 1; j <= (step + 1) * range; j++) {
					//new Integer is being used to make it easier to GC
					if (PrimeTester.isPrime(new Integer(j))) {
						localPrimes++;
					}
				}
				return localPrimes;
			});

			poolCount.submit(() -> {
				try {
					super.incrementPrimes(future.get());
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
			poolCount.awaitTermination(2, TimeUnit.MINUTES);
		} catch (InterruptedException ex) {
			Logger.getLogger(PrimesFinder.class.getName()).log(Level.SEVERE, null, ex);
		}

		super.finish();
	}

}
