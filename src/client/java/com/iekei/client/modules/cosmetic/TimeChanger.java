package com.iekei.client.modules.cosmetic;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class TimeChanger extends Module {
	private static final Setting time = Setting.slider("Time", 13000, 0, 24000, 100, "t");

	public TimeChanger() {
		super("timechanger", "TimeChanger", "Pins the world time client-side.", false, time);
	}

	public float fraction() {
		return (float) (num("Time") / 24000.0);
	}
}