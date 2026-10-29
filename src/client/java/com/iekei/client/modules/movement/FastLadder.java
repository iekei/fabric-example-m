package com.iekei.client.modules.movement;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.player.LocalPlayer;

public final class FastLadder extends Module {
	private final Setting speed = Setting.slider("Speed", 2.0, 1.0, 5.0, 0.1, "x");

	public FastLadder() {
		super("fastladder", "FastLadder", "Climb ladders at high speed.", false, speed);
	}

	@Override
	public void onTick() {
		LocalPlayer p = mc.player;

		if (p == null || !p.onClimbable()) {
			return;
		}

		p.setDeltaMovement(p.getDeltaMovement().x, 0.12 * num("Speed"), p.getDeltaMovement().z);
	}
}