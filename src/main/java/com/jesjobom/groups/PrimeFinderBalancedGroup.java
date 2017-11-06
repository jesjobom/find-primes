package com.jesjobom.groups;

import com.jesjobom.AbstractPrimeFinder;
import com.jesjobom.PrimeTester;
import com.jesjobom.PrimesFinder;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This method will look for primes from 1 to <code>numbers</code> distribuiting
 * the processing among <code>thread</code> truly balanced threads.
 *
 * Each thread will receive the same amount of numbers to check, following the
 * order:
 * <ul>
 * <li><strong>thread 1:</strong> 0*(threads^2)+(0*threads)+1 to 0*(threads^2)+(0*threads)+threads, 1*(threads^2)+(0*threads)+1 to 1*(threads^2)+(0*threads)+threads, ..., (theads-1)*(threads^2)+(0*threads)+1 to (theads-1)*(threads^2)+(0*threads)+threads</li>
 * <li><strong>thread 2:</strong> 0*(threads^2)+(1*threads)+1 to 0*(threads^2)+(1*threads)+threads, 1*(threads^2)+(1*threads)+1 to 1*(threads^2)+(1*threads)+threads, ..., (theads-1)*(threads^2)+(1*threads)+1 to (theads-1)*(threads^2)+(1*threads)+threads</li>
 * <li><strong>thread 3:</strong> 0*(threads^2)+(2*threads)+1 to 0*(threads^2)+(2*threads)+threads, 1*(threads^2)+(2*threads)+1 to 1*(threads^2)+(2*threads)+threads, ..., (theads-1)*(threads^2)+(2*threads)+1 to (theads-1)*(threads^2)+(2*threads)+threads</li>
 * </ul>
 *
 * And so on... Example with 100_000 numbers and 10 threads:
 * <ul>
 * <li><strong>thread 1:</strong> 01 to 10, 101 to 110, 201 to 210, ..., 99_901 to 99_910</li>
 * <li><strong>thread 2:</strong> 11 to 20, 111 to 120, 211 to 220, ..., 99_911 to 99_920</li>
 * <li><strong>thread 3:</strong> 21 to 30, 121 to 130, 221 to 230, ..., 99_921 to 99_930</li>
 * ...
 * <li><strong>thread 10:</strong> 91 to 100, 191 to 200, 291 to 300, ..., 99_991 to 100_00</li>
 * </ul>
 *
 * Like this, theoretically, the burden to find primes should be equally
 * distribuited, for real, with all the threads ending at the same time.
 *
 * @author jesjobom
 */
public class PrimeFinderBalancedGroup extends AbstractPrimeFinder {

	public PrimeFinderBalancedGroup(int numbers, int threads) {
		super(numbers, threads);
	}

	@Override
	public void run() {
		super.initTest("ALGORITHM MANUAL GROUPING 3 - THREADS WITH TRULY BALANCED GROUPS");

		ExecutorService poolCount = Executors.newFixedThreadPool(1);
		ExecutorService poolProcessing = Executors.newFixedThreadPool(threads);

		for (int i = 0; i < threads; i++) {

			final Integer step = i;

			Future<Integer> future = poolProcessing.submit(() -> {
				int localPrimes = 0;

				for (int j = step*threads +1; j < numbers; j+=threads*threads) {
					//new Integer is being used to make it easier to GC
					for (int k = j; k < j + threads; k++) {
						//new Integer is being used to make it easier to GC
						if (PrimeTester.isPrime(new Integer(k))) {
							localPrimes++;
						}
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
