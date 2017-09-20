# Parallel Find Primes 
Given a computational problem, how well it is parallelizable and how well differents ideas of parallelization can perform?

I chose an algorithm to find if a given number is prime or not.
Since the higher the number is the heavier the operation is, a simple sequencial distribuition of the numbers among N threads is not ideal.

The idea is to test different methods of parallelization without changing the original algorithm.
