package com.iekei.client.modules.combat;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class Velocity extends Module {
	private final Setting horizontal = Setting.slider("Horizontal", 35, 0, 100, 1, "%");
	private final Setting vertical = Setting.slider("Vertical", 60, 0, 100, 1, "%");

	public Velocity() {
		super("velocity", "Velocity", "Reduces knockback taken from hits.", true, horizontal, vertical);
	}

	public double horizontal() {
		return num("Horizontal") / 100.0;
	}

	public double vertical() {
		return num("Vertical") / 100.0;
	}
}