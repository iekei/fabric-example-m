package com.iekei.client.modules.movement;

import com.iekei.client.core.MoveUtil;
import com.iekei.client.core.Packet;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

public final class Fly extends Module {
	private static final Setting speed = Setting.slider("Speed", 1.6, 0.1, 5.0, 0.1, "x");
	private static final Setting mode = Setting.choice("Mode", "Vanilla", "Creative", "Glide");
	private static final Setting antiKick = Setting.toggle("Anti-Kick", true);

	public Fly() {
		super("fly", "Fly", "Allows sustained flight in survival.", true, speed, mode, antiKick);
	}

	@Override
	protected void onDisable() {
		LocalPlayer p = mc.player;

		if (p != null) {
			p.getAbilities().flying = false;
			p.getAbilities().mayfly = false;
		}
	}

	@Override
	public void onTick() {
		LocalPlayer p = mc.player;

		if (p == null) {
			return;
		}

		if (modeIs("Mode", "Creative")) {
			p.getAbilities().mayfly = true;
			p.getAbilities().flying = true;
			return;
		}

		double velocity = num("Speed") * 0.32;
		Vec3 dir = MoveUtil.input(velocity);
		double vertical = 0.0;

		if (mc.options.keyJump.isDown()) {
			vertical = velocity;
		} else if (mc.options.keyShift.isDown()) {
			vertical = -velocity;
		} else if (modeIs("Mode", "Glide")) {
			vertical = -0.03;
		}

		p.setDeltaMovement(dir.x, vertical, dir.z);
		p.fallDistance = 0.0F;
		p.setOnGround(true);

		if (bool("Anti-Kick") && p.tickCount % 20 == 0) {
			Packet.ground(true);
		}
	}
}