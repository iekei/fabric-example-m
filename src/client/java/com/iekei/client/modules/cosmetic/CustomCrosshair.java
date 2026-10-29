package com.iekei.client.modules.cosmetic;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class CustomCrosshair extends Module {
	private static final Setting size = Setting.slider("Size", 9, 2, 20, 1, "px");
	private static final Setting thickness = Setting.slider("Thickness", 2, 1, 4, 0.5, "px");

	public CustomCrosshair() {
		super("crosshair", "CustomCrosshair", "Reshapes the crosshair to your taste.", false, size, thickness);
	}

	@Override
	public void onRenderHud(GuiGraphicsExtractor g, float delta) {
		if (mc.player == null || mc.options.hideGui) {
			return;
		}

		int cx = mc.getWindow().getGuiScaledWidth() / 2;
		int cy = mc.getWindow().getGuiScaledHeight() / 2;
		int arm = numInt("Size") / 2;
		int t = Math.max(1, numInt("Thickness"));

		g.fill(cx - arm, cy - t / 2, cx + arm, cy + t - t / 2, 0xFFFFFFFF);
		g.fill(cx - t / 2, cy - arm, cx + t - t / 2, cy + arm, 0xFFFFFFFF);
	}
}