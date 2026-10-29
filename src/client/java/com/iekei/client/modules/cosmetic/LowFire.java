package com.iekei.client.modules.cosmetic;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class LowFire extends Module {
	private static final Setting height = Setting.slider("Overlay Height", 30, 0, 100, 1, "%");
	public static float offset;

	public LowFire() {
		super("lowfire", "LowFire", "Lowers the fire overlay while burning.", true, height);
	}

	@Override
	public void onTick() {
		offset = (float) ((1.0 - num("Overlay Height") / 100.0) * 0.5);
	}
}