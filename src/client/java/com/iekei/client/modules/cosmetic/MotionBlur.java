package com.iekei.client.modules.cosmetic;

import com.iekei.client.core.Rotation;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class MotionBlur extends Module {
	private static final Setting strength = Setting.slider("Strength", 3.5, 0.0, 10.0, 0.1, "");
	private static final Setting samples = Setting.slider("Samples", 6, 2, 12, 1, "");
	private static final Setting vertical = Setting.toggle("Vertical", true);
	private float lastYaw;
	private float lastPitch;

	public MotionBlur() {
		super("motionblur", "MotionBlur", "Adds a subtle trailing screen blur.", false,
				strength, samples, vertical);
	}

	@Override
	protected void onEnable() {
		if (mc.player != null) {
			lastYaw = mc.player.getYRot();
			lastPitch = mc.player.getXRot();
		}
	}

	@Override
	public void onRenderHud(GuiGraphicsExtractor g, float delta) {
		if (mc.player == null) {
			return;
		}

		float yaw = mc.player.getYRot();
		float pitch = mc.player.getXRot();
		float dYaw = Rotation.wrap(yaw - lastYaw);
		float dPitch = pitch - lastPitch;
		lastYaw = yaw;
		lastPitch = pitch;

		float s = (float) num("Strength");

		if (s <= 0.0F || Math.abs(dYaw) < 0.05F && Math.abs(dPitch) < 0.05F) {
			return;
		}

		int width = mc.getWindow().getGuiScaledWidth();
		int height = mc.getWindow().getGuiScaledHeight();
		int steps = numInt("Samples");
		int slice = Math.max(2, width / 48);
		int band = Math.max(2, height / 48);

		for (int i = steps; i >= 1; i--) {
			float falloff = (float) (steps - i + 1) / steps;
			int alpha = (int) Math.min(90, s * 4.0F * falloff);

			if (alpha <= 0) {
				continue;
			}

			int color = alpha << 24;
			int offset = (int) (-dYaw * s * i * 1.5F);

			g.fill(offset, 0, offset + slice, height, color);
			g.fill(width + offset - slice, 0, width + offset, height, color);

			if (bool("Vertical")) {
				int vOffset = (int) (dPitch * s * i * 1.5F);
				g.fill(0, vOffset, width, vOffset + band, color);
				g.fill(0, height + vOffset - band, width, height + vOffset, color);
			}
		}
	}
}