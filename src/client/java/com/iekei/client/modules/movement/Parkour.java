package com.iekei.client.modules.movement;

import com.iekei.client.core.BlockUtil;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;

public final class Parkour extends Module {
	private final Setting autoJump = Setting.toggle("Auto Jump", true);
	private final Setting requireSprint = Setting.toggle("Require Sprint", false);

	public Parkour() {
		super("parkour", "Parkour", "Automatically jumps at ledge edges.", false, autoJump, requireSprint);
	}

	@Override
	public void onTick() {
		LocalPlayer p = mc.player;

		if (p == null || !p.onGround() || !bool("Auto Jump")) {
			return;
		}

		if (bool("Require Sprint") && !p.isSprinting()) {
			return;
		}

		BlockPos ahead = BlockPos.containing(
				p.getX() + Math.sin(Math.toRadians(-p.getYRot())) * 0.5,
				p.getY() - 0.1,
				p.getZ() + Math.cos(Math.toRadians(-p.getYRot())) * 0.5);

		if (BlockUtil.isSolid(ahead)) {
			return;
		}

		if (!BlockUtil.isSolid(BlockPos.containing(p.getX(), p.getY() - 0.1, p.getZ()))) {
			return;
		}

		p.jumpFromGround();
	}
}