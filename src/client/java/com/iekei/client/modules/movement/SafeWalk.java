package com.iekei.client.modules.movement;

import com.iekei.client.core.BlockUtil;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;

public final class SafeWalk extends Module {
	private static final Setting forceSneak = Setting.toggle("Force Sneak", true);
	private static final Setting includeAir = Setting.toggle("Include Air Blocks", false);

	public SafeWalk() {
		super("safewalk", "SafeWalk", "Stops you from walking off edges.", true, forceSneak, includeAir);
	}

	@Override
	public void onTick() {
		LocalPlayer p = mc.player;

		if (p == null || !p.onGround() || p.isPassenger()) {
			return;
		}

		BlockPos ahead = BlockPos.containing(
				p.getX() + Math.sin(Math.toRadians(-p.getYRot())) * 0.4,
				p.getY() - 0.1,
				p.getZ() + Math.cos(Math.toRadians(-p.getYRot())) * 0.4);

		boolean empty = bool("Include Air Blocks") ? BlockUtil.isAir(ahead) : !BlockUtil.isSolid(ahead);

		if (empty && bool("Force Sneak")) {
			p.setShiftKeyDown(true);
		}
	}
}