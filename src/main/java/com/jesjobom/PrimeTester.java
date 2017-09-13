package com.jesjobom;

/**
 * 
 * @see https://en.wikipedia.org/wiki/Primality_test
 * @author jesjobom
 */
public class PrimeTester {
	
	/**
	 * Check if a number is prime through a fast algorithm.
	 * 
	 * @param number
	 * @return 
	 */
	public static boolean isPrime(Integer number) {
		if (number <= 1) {
			return false;
		}
		
		if (number <= 3) {
			return true;
		}
		
		if (number % 2 == 0 || number % 3 == 0) {
			return false;
		}
		
		int i = 5;
		
		//it will look for dividers until (square root of number)
		while (i * i <= number) {
			if(number % i == 0 || number % (i + 2) == 0) {
				return false;
			}
			
			i += 6;
		}
		
		return true;
	}
}
