package com.iekei.client.modules.movement;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

public final class NoSlowdown extends Module {
	private static final Setting itemsOnly = Setting.toggle("Items Only", false);
	private static final Setting ignoreWebs = Setting.toggle("Ignore Webs", true);

	public NoSlowdown() {
		super("noslowdown", "NoSlowdown", "Removes the slowdown from using items.", true, itemsOnly, ignoreWebs);
	}

	public boolean blocksSpeedFactor() {
		return enabled() && bool("Ignore Webs") && !bool("Items Only");
	}

	@Override
	public void onTick() {
		LocalPlayer p = mc.player;

		if (p == null || !p.isUsingItem()) {
			return;
		}

		// vanilla multiplies the horizontal velocity by 0.2 while using an item
		Vec3 motion = p.getDeltaMovement();

		if (motion.horizontalDistanceSqr() > 1.0E-4) {
			p.setDeltaMovement(motion.x / 0.2, motion.y, motion.z / 0.2);
		}
	}
}