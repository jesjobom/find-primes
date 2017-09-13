package com.jesjobom.test;

import com.jesjobom.PrimeTester;
import junit.framework.TestCase;
import org.junit.Test;


/**
 *
 * @author jairton
 */
public class PrimeTesterTest extends TestCase {
	
	private Integer[] primes = new Integer[]{2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 105337, 105341, 15487469 };
	
	private Integer[] notPrimes = new Integer[]{1, 4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 22, 24, 25, 26, 27, 28, 30, 105349, 15487467 };
	
	@Test
	public void testPrimes() {
		for (Integer prime : primes) {
			assertTrue(PrimeTester.isPrimeFast(prime));
		}
	}
	
	@Test
	public void testNotPrimes() {
		for (Integer prime : notPrimes) {
			assertFalse(PrimeTester.isPrimeFast(prime));
		}
	}
}
