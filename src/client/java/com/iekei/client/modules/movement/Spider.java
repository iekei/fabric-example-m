package com.iekei.client.modules.movement;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.player.LocalPlayer;

public final class Spider extends Module {
	private static final Setting speed = Setting.slider("Climb Speed", 1.0, 0.2, 3.0, 0.1, "x");
	private static final Setting lookUpOnly = Setting.toggle("Only When Looking Up", true);

	public Spider() {
		super("spider", "Spider", "Climb walls like a spider.", false, speed, lookUpOnly);
	}

	@Override
	public void onTick() {
		LocalPlayer p = mc.player;

		if (p == null || !p.horizontalCollision) {
			return;
		}

		if (bool("Only When Looking Up") && p.getXRot() > -20.0F) {
			return;
		}

		p.setDeltaMovement(p.getDeltaMovement().x, 0.2 * num("Climb Speed"), p.getDeltaMovement().z);
		p.fallDistance = 0.0F;
	}
}