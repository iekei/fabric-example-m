package com.iekei.client.modules.notify;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class SoundAlert extends Module {
	private static final Setting volume = Setting.slider("Volume", 60, 0, 100, 5, "%");

	public SoundAlert() {
		super("soundalert", "SoundAlert", "Plays a detection sound alert.", false, volume);
	}

	public float gain() {
		return (float) (num("Volume") / 100.0);
	}
}