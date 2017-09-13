package com.jesjobom;

import static java.lang.Thread.sleep;

/**
 * Once started monitors the memory keeping the highest memory usage.
 * Memory Used = Total Memory set (i.e. Xms) - Free Memory
 *
 * @author jesjobom
 */
public class MemoryCounter extends Thread {

	private long freeMemory = 0;
	private boolean shutdown = false;

	@Override
	public void run() {
		while (!shutdown) {
			try {
				sleep(500);
			} catch (InterruptedException ex) {
			}
			long l = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			if (l > freeMemory) {
				freeMemory = l;
			}
		}
	}

	public void shutdown() {
		this.shutdown = true;
	}

	public long getFreeMemory() {
		return freeMemory;
	}
}
