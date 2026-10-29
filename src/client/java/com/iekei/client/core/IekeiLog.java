package com.iekei.client.core;

import com.iekei.IekeiClient;

public final class IekeiLog {
	private IekeiLog() {
	}

	public static void error(String where, Throwable t) {
		IekeiClient.LOGGER.error("[Iekei Client] {} failed", where, t);
	}
}