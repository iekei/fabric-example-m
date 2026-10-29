package com.iekei.client.modules.movement;

import com.iekei.client.core.Timer;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.player.LocalPlayer;

public final class AirJump extends Module {
	private static final Setting height = Setting.slider("Jump Height", 0.9, 0.5, 2.0, 0.1, "b");
	private static final Setting maxJumps = Setting.slider("Max Jumps", 3, 1, 10, 1, "x");
	private final Timer cooldown = new Timer();
	private int jumps;

	public AirJump() {
		super("airjump", "AirJump", "Jump again while already airborne.", false, height, maxJumps);
	}

	@Override
	protected void onEnable() {
		jumps = 0;
	}

	@Override
	public void onTick() {
		LocalPlayer p = mc.player;

		if (p == null) {
			return;
		}

		if (p.onGround()) {
			jumps = 0;
			return;
		}

		if (!mc.options.keyJump.isDown() || jumps >= numInt("Max Jumps") || !cooldown.every(120)) {
			return;
		}

		p.setDeltaMovement(p.getDeltaMovement().x, num("Jump Height") * 0.42, p.getDeltaMovement().z);
		p.fallDistance = 0.0F;
		jumps++;
	}
}