package com.iekei.client.modules.utility;

import com.iekei.client.core.Packet;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class NoFall extends Module {
	private static final Setting mode = Setting.choice("Mode", "Packet", "NoGround", "Spoof");

	public NoFall() {
		super("nofall", "NoFall", "Cancels fall damage.", true, mode);
	}

	@Override
	public void onTick() {
		if (mc.player == null) {
			return;
		}

		if (mc.player.fallDistance < 2.5F) {
			return;
		}

		Packet.ground(true);

		if (modeIs("Mode", "NoGround")) {
			mc.player.setOnGround(true);
			mc.player.fallDistance = 0.0F;
		}
	}
}