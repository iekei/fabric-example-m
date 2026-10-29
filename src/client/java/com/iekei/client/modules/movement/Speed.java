package com.iekei.client.modules.movement;

import com.iekei.client.core.MoveUtil;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

public final class Speed extends Module {
	private static final Setting multiplier = Setting.slider("Multiplier", 2.4, 1.0, 10.0, 0.1, "x");
	private static final Setting mode = Setting.choice("Mode", "Strafe", "Omni", "Legacy");
	private static final Setting autoJump = Setting.toggle("Auto Jump", true);

	public Speed() {
		super("speed", "Speed", "Multiplies your ground movement speed.", true, multiplier, mode, autoJump);
	}

	@Override
	public void onTick() {
		LocalPlayer p = mc.player;

		if (p == null || p.isPassenger() || p.getAbilities().flying) {
			return;
		}

		Vec3 dir = MoveUtil.input(0.29 * num("Multiplier"));

		if (dir.lengthSqr() == 0.0) {
			return;
		}

		if (modeIs("Mode", "Omni")) {
			p.setDeltaMovement(dir.x, p.getDeltaMovement().y, dir.z);
		} else if (p.onGround() || modeIs("Mode", "Legacy")) {
			p.setDeltaMovement(dir.x, p.getDeltaMovement().y, dir.z);
		}

		if (bool("Auto Jump") && p.onGround() && p.horizontalCollision) {
			p.jumpFromGround();
		}
	}
}