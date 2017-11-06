/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jesjobom.forkjoin;

import com.jesjobom.PrimeTester;
import java.util.concurrent.RecursiveTask;

/**
 *
 * @author jairton
 */
public class FindPrimesTask extends RecursiveTask<Integer> {

	private static final long serialVersionUID = -1091662610347422257L;
	
	private static int minimumRange = 10;
	
	private final int firstNumber;
	
	private final int range;

	public FindPrimesTask(int firstNumber, int range) {
		this.firstNumber = firstNumber;
		this.range = range;
	}

	@Override
	protected Integer compute() {
		if(range > minimumRange) {
			int newRange = range/2;
			int modRange = range%2;
			
			FindPrimesTask secondHalf = new FindPrimesTask(firstNumber + newRange , newRange + modRange);
			secondHalf.fork();
			
			FindPrimesTask firstHalf = new FindPrimesTask(firstNumber, newRange);
			int firstResult = firstHalf.compute();
			return firstResult + secondHalf.join();
		} else {
			return findPrimes();
		}
	}
	
	private int findPrimes() {
		int primes = 0;
		for (int i = firstNumber; i < firstNumber + range; i++) {
			if(PrimeTester.isPrime(i)) {
				primes++;
			}
		}
		return primes;
	}
	
	public static void setMinimunRange(int range) {
		minimumRange = range;
	}
}
