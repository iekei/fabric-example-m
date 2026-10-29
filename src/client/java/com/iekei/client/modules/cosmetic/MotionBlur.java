package com.iekei.client.modules.cosmetic;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class MotionBlur extends Module {
	private static final Setting strength = Setting.slider("Strength", 3.5, 0.0, 10.0, 0.1, "");

	public MotionBlur() {
		super("motionblur", "MotionBlur", "Adds a subtle trailing screen blur.", false, strength);
	}

	@Override
	public void onRenderHud(GuiGraphicsExtractor g, float delta) {
		int alpha = (int) Math.min(120, num("Strength") * 6.0);

		if (alpha <= 0) {
			return;
		}

		g.fill(0, 0, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight(),
				(alpha << 24));
	}
}