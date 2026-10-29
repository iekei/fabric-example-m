package com.iekei.client.modules.utility;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class FastPlace extends Module {
	private static final Setting delay = Setting.slider("Delay", 0, 0, 4, 1, "t");

	public FastPlace() {
		super("fastplace", "FastPlace", "Removes the block placement delay.", false, delay);
	}

	@Override
	public void onTick() {
		if (mc.player == null) {
			return;
		}

		mc.rightClickDelay = numInt("Delay");
	}
}