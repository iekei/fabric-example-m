package com.iekei.client.core;

/** Smoothed client-side tick rate estimate. */
public final class Tps {
	private static long last;
	private static float value = 20.0F;

	private Tps() {
	}

	public static void tick() {
		long now = System.currentTimeMillis();

		if (last != 0) {
			float delta = now - last;

			if (delta > 0.0F && delta < 500.0F) {
				value += (1000.0F / delta - value) * 0.05F;
			}
		}

		last = now;
		value = Math.max(0.0F, Math.min(20.0F, value));
	}

	public static float value() {
		return value;
	}
}