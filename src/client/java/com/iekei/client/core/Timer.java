package com.iekei.client.core;

/** Millisecond cooldown helper shared by the timed modules. */
public final class Timer {
	private long last;

	public boolean every(double millis) {
		long now = System.currentTimeMillis();

		if (now - last < millis) {
			return false;
		}

		last = now;
		return true;
	}

	public void reset() {
		last = System.currentTimeMillis();
	}
}