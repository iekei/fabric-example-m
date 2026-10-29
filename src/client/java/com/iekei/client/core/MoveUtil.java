package com.iekei.client.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

public final class MoveUtil {
	private static final Minecraft mc = Minecraft.getInstance();

	private MoveUtil() {
	}

	/** Horizontal input direction, normalised and scaled to {@code speed}. */
	public static Vec3 input(double speed) {
		LocalPlayer p = mc.player;

		if (p == null) {
			return Vec3.ZERO;
		}

		float forward = p.input.forwardImpulse;
		float strafe = p.input.leftImpulse;

		if (forward == 0.0F && strafe == 0.0F) {
			return Vec3.ZERO;
		}

		float yaw = (float) Math.toRadians(p.getYRot());
		double sin = Math.sin(yaw);
		double cos = Math.cos(yaw);
		double x = forward * -sin + strafe * -cos;
		double z = forward * cos + strafe * -sin;
		double len = Math.sqrt(x * x + z * z);
		return new Vec3(x / len * speed, 0.0, z / len * speed);
	}

	/** Feeds the WASD state into the player while a screen is open. */
	public static void applyKeyboardInput() {
		LocalPlayer p = mc.player;

		if (p == null) {
			return;
		}

		p.input.forwardImpulse = axis(mc.options.keyUp.isDown(), mc.options.keyDown.isDown());
		p.input.leftImpulse = axis(mc.options.keyLeft.isDown(), mc.options.keyRight.isDown());
		p.input.jumping = mc.options.keyJump.isDown();
		p.input.shiftKeyDown = mc.options.keyShift.isDown();
	}

	private static float axis(boolean positive, boolean negative) {
		return (positive ? 1.0F : 0.0F) - (negative ? 1.0F : 0.0F);
	}
}