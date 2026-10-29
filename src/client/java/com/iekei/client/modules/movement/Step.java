package com.iekei.client.modules.movement;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class Step extends Module {
	private final Setting height = Setting.slider("Height", 1.0, 0.5, 2.5, 0.1, "b");
	private final Setting instant = Setting.toggle("Instant", true);

	public Step() {
		super("step", "Step", "Instantly climbs up blocks.", true, height, instant);
	}

	@Override
	protected void onEnable() {
		apply();
	}

	@Override
	protected void onDisable() {
		if (mc.player != null) {
			mc.player.setMaxUpStep(0.6F);
		}
	}

	@Override
	public void onTick() {
		apply();
	}

	private void apply() {
		if (mc.player != null) {
			mc.player.setMaxUpStep((float) num("Height"));
		}
	}
}