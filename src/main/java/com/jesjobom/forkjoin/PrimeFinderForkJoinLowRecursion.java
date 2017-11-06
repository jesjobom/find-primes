package com.jesjobom.forkjoin;

import com.jesjobom.AbstractPrimeFinder;
import com.jesjobom.groups.PrimeFinderSequencialGroup;
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
 * In this class I am configuring a high range for the Recursive Task to actually 
 * do the work of finding primes. That means that it'll create a small recursion 
 * pile consuming less memory, but that also means that some working threads might
 * stop earlier than others, reducing the overal performance.
 * 
 * I expect it to have a similar result to the Algorithm with Manual Sequencial 
 * Groups ({@link PrimeFinderSequencialGroup}).
 *
 * @author jesjobom
 */
public class PrimeFinderForkJoinLowRecursion extends AbstractPrimeFinder {

	public PrimeFinderForkJoinLowRecursion(int numbers, int threads) {
		super(numbers, threads);
	}

	@Override
	public void run() {
		FindPrimesTask.setMinimunRange(numbers/threads);
		super.initTest("ALGORITHM FORK JOIN 2 - FORKING UNTIL GROUPS OF " + numbers/threads);
		
		ForkJoinPool pool = new ForkJoinPool(threads);
		FindPrimesTask task = new FindPrimesTask(0, numbers);
		super.incrementPrimes(pool.invoke(task));
		pool.shutdown();
		
		super.finish();
	}
	
}
