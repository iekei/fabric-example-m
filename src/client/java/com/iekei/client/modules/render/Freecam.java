package com.iekei.client.modules.render;

import com.iekei.client.core.MoveUtil;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.world.phys.Vec3;

public final class Freecam extends Module {
	private static final Setting speed = Setting.slider("Fly Speed", 1.5, 0.5, 5.0, 0.1, "x");

	public Vec3 pos = Vec3.ZERO;
	public float yaw;
	public float pitch;
	public boolean active;

	public Freecam() {
		super("freecam", "Freecam", "Detaches the camera from your body.", false, speed);
	}

	@Override
	protected void onEnable() {
		if (mc.player != null) {
			pos = mc.player.getEyePosition();
			yaw = mc.player.getYRot();
			pitch = mc.player.getXRot();
		}

		active = true;
	}

	@Override
	protected void onDisable() {
		active = false;
	}

	@Override
	public void onTick() {
		if (!active || mc.player == null) {
			return;
		}

		double v = num("Speed") * 0.35;
		Vec3 dir = MoveUtil.input(v);
		double vertical = 0.0;

		if (mc.options.keyJump.isDown()) {
			vertical = v;
		} else if (mc.options.keyShift.isDown()) {
			vertical = -v;
		}

		pos = pos.add(dir.x, vertical, dir.z);

		if (mc.mouseHandler.isMouseGrabbed() == false) {
			return;
		}

		yaw += mc.mouseHandler.getXVelocity() * 0.15F;
		pitch = Math.max(-90.0F, Math.min(90.0F, pitch + mc.mouseHandler.getYVelocity() * 0.15F));
	}
}