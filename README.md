# Parallel Find Primes 
Given a computational problem, how well it is parallelizable and how well differents ideas of parallelization can perform?

I chose an algorithm to find if a given number is prime or not.
Since the higher the number is the heavier the operation is, a simple sequencial distribuition of the numbers among N threads is not ideal.

The idea is to test different methods of parallelization without changing the original algorithm, prioritizing memory usage and completion time.

### Example 
```
$ mvn exec:java
[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building find-primes 1.0-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- exec-maven-plugin:1.6.0:java (default-cli) @ find-primes ---
ALGORITHM 1 - THREADS WITH SEQUENCIAL GROUPS
Found 16.252.325 primes from 1 to 3e+08 in 49 seconds using 14 MB of memory and 10 threads.
ALGORITHM 2 - THREADS WITH BALANCED GROUPS
Found 16.252.325 primes from 1 to 3e+08 in 60 seconds using 16 MB of memory and 10 threads.
ALGORITHM 3 - THREADS WITH TRULY BALANCED GROUPS
Found 16.252.325 primes from 1 to 3e+08 in 47 seconds using 14 MB of memory and 10 threads.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 02:41 min
[INFO] Finished at: 2017-11-01T15:08:25-02:00
[INFO] Final Memory: 6M/150M
[INFO] ------------------------------------------------------------------------

```