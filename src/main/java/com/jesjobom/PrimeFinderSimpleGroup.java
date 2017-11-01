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
 * the processing among <code>thread</code> balanced threads.
 *
 * Each thread will receive the same amount of numbers to check, following the
 * order:
 * <ul>
 * <li><strong>thread 1:</strong> 0*(threads)+1, 1*(threads)+1, 2*(threads)+1, ..., (numbers/threads -1)*(threads)+1</li>
 * <li><strong>thread 2:</strong> 0*(threads)+2, 1*(threads)+2, 2*(threads)+2, ..., (numbers/threads -1)*(threads)+2</li>
 * <li><strong>thread 3:</strong> 0*(threads)+3, 1*(threads)+3, 2*(threads)+3, ..., (numbers/threads -1)*(threads)+3</li>
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
 * distribuited, with all the threads ending at the same time.
 *
 * This would be true if the operation wasn't so dependent of the entry. While
 * finding primes, numbers ending in 2,4,5,6,8,0 are easily solved. So this
 * distribution is leaving only 40% of threads doing the heavy work.
 *
 * @author jesjobom
 */
public class PrimeFinderSimpleGroup extends AbstractPrimeFinder {

	public PrimeFinderSimpleGroup(int numbers, int threads) {
		super(numbers, threads);
	}

	@Override
	public void run() {
		super.initTest("ALGORITHM 2 - THREADS WITH BALANCED GROUPS");

		ExecutorService poolCount = Executors.newFixedThreadPool(1);
		ExecutorService poolProcessing = Executors.newFixedThreadPool(threads);

		for (int i = 0; i < threads; i++) {

			final Integer step = i;

			Future<Integer> future = poolProcessing.submit(() -> {
				int localPrimes = 0;

				for (int j = step + 1; j < numbers; j += threads) {
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
			poolCount.awaitTermination(5, TimeUnit.MINUTES);
		} catch (InterruptedException ex) {
			Logger.getLogger(PrimesFinder.class.getName()).log(Level.SEVERE, null, ex);
		}

		super.finish();
	}

}
