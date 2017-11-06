# Parallel Find Primes 
Given a computational problem, how well it is parallelizable and how well differents ideas of parallelization can perform?

I chose an algorithm to find if a given number is prime or not.
Since the higher the number is the heavier the operation is, a simple sequencial distribuition of the numbers among N threads is not ideal.

The idea here is to test different methods of parallelization without changing the original algorithm, prioritizing memory usage and completion time.

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
ALGORITHM MANUAL GROUPING 1 - THREADS WITH SEQUENCIAL GROUPS
Found 16.252.325 primes from 1 to 3e+08 in 48 seconds using 14 MB of memory and 10 threads.
ALGORITHM MANUAL GROUPING 2 - THREADS WITH BALANCED GROUPS
Found 16.252.325 primes from 1 to 3e+08 in 52 seconds using 14 MB of memory and 10 threads.
ALGORITHM MANUAL GROUPING 3 - THREADS WITH TRULY BALANCED GROUPS
Found 16.252.325 primes from 1 to 3e+08 in 44 seconds using 14 MB of memory and 10 threads.
ALGORITHM FORK JOIN 1 - FORKING UNTIL GROUPS OF 10
Found 16.252.325 primes from 1 to 3e+08 in 45 seconds using 231 MB of memory and 10 threads.
ALGORITHM FORK JOIN 2 - FORKING UNTIL GROUPS OF 30000000
Found 16.252.325 primes from 1 to 3e+08 in 49 seconds using 36 MB of memory and 10 threads.
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 04:26 min
[INFO] Finished at: 2017-11-06T15:14:45-02:00
[INFO] Final Memory: 6M/115M
[INFO] ------------------------------------------------------------------------

```

### The Algorithm for Finding Primes 
Since I am testing differents strategies of parallelization of the same work and not the algorithm itself, the algorithm for testing primes surely isn't the best.
```
function is_prime(n)
    if n ≤ 1
        return false
    else if n ≤ 3
        return true
    else if n mod 2 = 0 or n mod 3 = 0
        return false
    let i ← 5
    while i * i ≤ n
        if n mod i = 0 or n mod (i + 2) = 0
            return false
        i ← i + 6
    return true
```
from Wikipedia ([Primality test](https://en.wikipedia.org/wiki/Primality_test)).