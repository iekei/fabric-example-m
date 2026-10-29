package com.iekei.client.modules.settings;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class SoundEffects extends Module {
	private static final Setting enabledToggle = Setting.toggle("Enabled", true);
	private static final Setting volume = Setting.slider("Volume", 60, 0, 100, 5, "%");

	public SoundEffects() {
		super("sound", "Sound Effects", "Subtle UI clicks and toggle blips.", true, enabledToggle, volume);
	}

	public float gain() {
		return (float) (num("Volume") / 100.0 * 0.8);
	}

	public boolean audible() {
		return enabled() && bool("Enabled") && num("Volume") > 0;
	}
}