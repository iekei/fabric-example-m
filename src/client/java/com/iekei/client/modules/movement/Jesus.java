package com.iekei.client.modules.movement;

import com.iekei.client.core.BlockUtil;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;

public final class Jesus extends Module {
	private static final Setting liquid = Setting.choice("Liquid", "Water", "Lava", "Both");
	private static final Setting sneakToSink = Setting.toggle("Sneak to Sink", true);

	public Jesus() {
		super("jesus", "Jesus", "Walk on water, lava and other liquids.", false, liquid, sneakToSink);
	}

	@Override
	public void onTick() {
		LocalPlayer p = mc.player;

		if (p == null || !p.isInWater() && !p.isInLava()) {
			return;
		}

		if (bool("Sneak to Sink") && mc.options.keyShift.isDown()) {
			return;
		}

		if (!BlockUtil.isLiquid(p.blockPosition().below())) {
			return;
		}

		if (modeIs("Liquid", "Water") && !p.isInWater()) {
			return;
		}

		if (modeIs("Liquid", "Lava") && !p.isInLava()) {
			return;
		}

		p.setDeltaMovement(p.getDeltaMovement().x, Math.max(0.0, p.getDeltaMovement().y), p.getDeltaMovement().z);
		p.fallDistance = 0.0F;
		p.setOnGround(true);
	}
}