package com.iekei.client.modules.movement;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.player.LocalPlayer;

public final class HighJump extends Module {
	private static final Setting multiplier = Setting.slider("Multiplier", 2.0, 1.0, 8.0, 0.1, "x");
	private static final Setting glide = Setting.toggle("Glide Down", false);

	public HighJump() {
		super("highjump", "HighJump", "Jump several blocks into the air.", false, multiplier, glide);
	}

	@Override
	public void onTick() {
		LocalPlayer p = mc.player;

		if (p == null) {
			return;
		}

		if (p.onGround() && mc.options.keyJump.isDown()) {
			p.jumpFromGround();
			p.setDeltaMovement(p.getDeltaMovement().x, 0.42 * num("Multiplier"), p.getDeltaMovement().z);
		} else if (bool("Glide Down") && p.getDeltaMovement().y < -0.1) {
			p.setDeltaMovement(p.getDeltaMovement().x, -0.1, p.getDeltaMovement().z);
		}
	}
}