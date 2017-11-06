package com.jesjobom.forkjoin;

import com.jesjobom.AbstractPrimeFinder;
import com.jesjobom.groups.PrimeFinderBalancedGroup;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * This method will look for primes from 1 to <code>numbers</code> distribuiting
 * the processing through a FORK-JOIN algorithm using <code>threads</code> threads.
 * 
 * Since I am using the Java 7 {@link ForkJoinPool} and {@link RecursiveTask}, the 
 * code becomes really simple once the process of Forking and Joining the tasks
 * to find primes is undertood.
 * 
 * In this class I am configuring a low range for the Recursive Task to actually 
 * do the work of finding primes. That means that it'll create a long recursion 
 * pile consuming more memory, but that also means that the working threads will
 * have more work to keep them 100% occupied.
 * 
 * I expect it to have a similar result to the Algorithm with Manual Truly 
 * Balanced Groups ({@link PrimeFinderBalancedGroup}).
 *
 * @author jesjobom
 */
public class PrimeFinderForkJoinHighRecursion extends AbstractPrimeFinder {

	public PrimeFinderForkJoinHighRecursion(int numbers, int threads) {
		super(numbers, threads);
	}

	@Override
	public void run() {
		FindPrimesTask.setMinimunRange(10);
		super.initTest("ALGORITHM FORK JOIN 1 - FORKING UNTIL GROUPS OF 10");
		
		ForkJoinPool pool = new ForkJoinPool(threads);
		FindPrimesTask task = new FindPrimesTask(0, numbers);
		super.incrementPrimes(pool.invoke(task));
		pool.shutdown();
		
		super.finish();
	}
	
}
