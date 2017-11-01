package com.jesjobom;

/**
 *
 * @author jesjobom
 */
public class PrimesFinder {
	
	/**
	 * Modifying the number of threads shouldn't be a problem, but it is since 
	 * many operations that I do to calculate groups of numbers consider only 
	 * exact division.
	 */
	private static final int THREADS = 10;
	
	/**
	 * Max number to test if it is prime.
	 */
	private static final int NUMBERS = 300_000_000;
	
	public static void main(String[] args) {
		
		AbstractPrimeFinder finder = new PrimeFinderSequencialThreads(NUMBERS, THREADS);
		finder.run();
		
		finder = new PrimeFinderSimpleGroup(NUMBERS, THREADS);
		finder.run();
		
		finder = new PrimeFinderBalancedGroup(NUMBERS, THREADS);
		finder.run();
	}
}
