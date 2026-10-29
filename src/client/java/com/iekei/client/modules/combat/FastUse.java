package com.iekei.client.modules.combat;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class FastUse extends Module {
	private final Setting speed = Setting.slider("Speed", 2.5, 1.0, 5.0, 0.1, "x");

	public FastUse() {
		super("fastuse", "FastUse", "Uses items such as food far faster.", false, speed);
	}

	public double speed() {
		return num("Speed");
	}
}