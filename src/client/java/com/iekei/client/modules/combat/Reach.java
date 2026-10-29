package com.iekei.client.modules.combat;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class Reach extends Module {
	private static final Setting distance = Setting.slider("Distance", 3.6, 3.0, 6.0, 0.1, "b");

	public Reach() {
		super("reach", "Reach", "Extends your attack range.", false, distance);
	}

	/** Extra blocks added on top of the vanilla base range. */
	public double extra() {
		return num("Distance") - 3.0;
	}
}