package com.iekei.client.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.phys.Vec3;

/** Bearing maths and on-screen arrow drawing for the navigation modules. */
public final class Nav {
	private static final Minecraft mc = Minecraft.getInstance();

	private Nav() {
	}

	/** Degrees from straight ahead, positive to the right. */
	public static double bearing(Vec3 target) {
		if (mc.player == null) {
			return 0.0;
		}

		Vec3 eye = mc.player.getEyePosition();
		float[] rot = Rotation.toward(eye, target);
		return Rotation.wrap(rot[0] - mc.player.getYRot());
	}

	public static double distance(Vec3 target) {
		return mc.player == null ? 0.0 : mc.player.position().distanceTo(target);
	}

	/** Draws an arrow from (cx, cy) pointing along the bearing. */
	public static void arrow(GuiGraphicsExtractor g, int cx, int cy, double bearing, int length, int rgb) {
		double rad = Math.toRadians(bearing);
		double dx = Math.sin(rad);
		double dy = -Math.cos(rad);
		int color = 0xFF000000 | rgb;

		for (int i = 2; i <= length; i++) {
			int x = cx + (int) Math.round(dx * i);
			int y = cy + (int) Math.round(dy * i);
			g.fill(x - 1, y - 1, x + 2, y + 2, color);
		}

		int headX = cx + (int) Math.round(dx * length);
		int headY = cy + (int) Math.round(dy * length);
		int sideX = (int) Math.round(-dy * 4);
		int sideY = (int) Math.round(dx * 4);
		g.fill(headX - 3, headY - 3, headX + 4, headY + 4, color);
		g.fill(headX + sideX - 2, headY + sideY - 2, headX + sideX + 3, headY + sideY + 3, color);
		g.fill(headX - sideX - 2, headY - sideY - 2, headX - sideX + 3, headY - sideY + 3, color);
	}

	/** Compass rose letters for the arrow label. */
	public static String compass(double bearing) {
		double b = ((bearing % 360.0) + 360.0) % 360.0;

		if (b < 22.5 || b >= 337.5) {
			return "N";
		}

		if (b < 67.5) {
			return "NE";
		}

		if (b < 112.5) {
			return "E";
		}

		if (b < 157.5) {
			return "SE";
		}

		if (b < 202.5) {
			return "S";
		}

		if (b < 247.5) {
			return "SW";
		}

		if (b < 292.5) {
			return "W";
		}

		return "NW";
	}
}