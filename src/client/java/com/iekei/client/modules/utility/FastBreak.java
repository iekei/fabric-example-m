package com.iekei.client.modules.utility;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class FastBreak extends Module {
	private static final Setting speed = Setting.slider("Speed", 2.0, 1.0, 5.0, 0.1, "x");
	private static final Setting packetMode = Setting.toggle("Packet Mode", true);

	public FastBreak() {
		super("fastbreak", "FastBreak", "Mines blocks faster.", true, speed, packetMode);
	}

	public double multiplier() {
		return num("Speed");
	}
}