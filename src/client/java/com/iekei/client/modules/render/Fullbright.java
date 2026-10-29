package com.iekei.client.modules.render;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class Fullbright extends Module {
	private static final Setting brightness = Setting.slider("Brightness", 12, 1, 15, 1, "lvl");
	private double previous = 1.0;

	public Fullbright() {
		super("fullbright", "Fullbright", "Removes darkness client-side.", true, brightness);
	}

	@Override
	protected void onEnable() {
		if (mc.options != null) {
			previous = mc.options.gamma().get();
			mc.options.gamma().set(num("Brightness"));
		}
	}

	@Override
	protected void onDisable() {
		if (mc.options != null) {
			mc.options.gamma().set(previous);
		}
	}

	@Override
	public void onTick() {
		if (mc.options != null) {
			mc.options.gamma().set(num("Brightness"));
		}
	}
}