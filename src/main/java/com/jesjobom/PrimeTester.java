package com.jesjobom;

/**
 *
 * @author jairton
 */
public class PrimeTester {
	
	public static boolean isPrimeSlow(Integer number) {
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
		
		while (i * i <= number) {
			if(number % i == 0) {
				return false;
			}
			
			i += 2;
		}
		
		return true;
	}
	
	public static boolean isPrimeFast(Integer number) {
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
		
		while (i * i <= number) {
			if(number % i == 0 || number % (i + 2) == 0) {
				return false;
			}
			
			i += 6;
		}
		
		return true;
	}
}
