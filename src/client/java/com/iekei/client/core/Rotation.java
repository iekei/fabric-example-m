package com.iekei.client.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

public final class Rotation {
	private static final Minecraft mc = Minecraft.getInstance();

	private Rotation() {
	}

	public static float[] toward(Vec3 from, Vec3 to) {
		double dx = to.x - from.x;
		double dy = to.y - from.y;
		double dz = to.z - from.z;
		double dist = Math.sqrt(dx * dx + dz * dz);
		float yaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
		float pitch = (float) -Math.toDegrees(Math.atan2(dy, dist));
		return new float[] {yaw, pitch};
	}

	public static Vec3 eye() {
		LocalPlayer p = mc.player;
		return p == null ? Vec3.ZERO : p.getEyePosition();
	}

	/** Snaps the player's rotation to look at the given point. */
	public static void snap(Vec3 target) {
		LocalPlayer p = mc.player;

		if (p == null) {
			return;
		}

		float[] rot = toward(eye(), target);
		p.setYRot(rot[0]);
		p.setXRot(rot[1]);
	}

	/** Eases the player's rotation towards the target (higher = faster). */
	public static void smooth(Vec3 target, double speed) {
		LocalPlayer p = mc.player;

		if (p == null) {
			return;
		}

		float[] rot = toward(eye(), target);
		float factor = (float) Math.min(1.0, 0.15 * speed);
		p.setYRot(p.getYRot() + wrap(rot[0] - p.getYRot()) * factor);
		p.setXRot(p.getXRot() + (rot[1] - p.getXRot()) * factor);
	}

	public static float wrap(float degrees) {
		float d = degrees % 360.0F;

		if (d >= 180.0F) {
			d -= 360.0F;
		}

		if (d < -180.0F) {
			d += 360.0F;
		}

		return d;
	}

	public static boolean inFov(Vec3 target, double fov) {
		LocalPlayer p = mc.player;

		if (p == null) {
			return false;
		}

		float[] rot = toward(eye(), target);
		return Math.abs(wrap(rot[0] - p.getYRot())) <= fov / 2.0 && Math.abs(rot[1] - p.getXRot()) <= fov / 2.0;
	}
}