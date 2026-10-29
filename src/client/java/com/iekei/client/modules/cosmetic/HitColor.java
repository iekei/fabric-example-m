package com.iekei.client.modules.cosmetic;

import com.iekei.client.core.Colors;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class HitColor extends Module {
	private static final Setting tint = Setting.choice("Tint", "Red", "Accent", "Orange");

	public HitColor() {
		super("hitcolor", "HitColor", "Tints the screen when you take a hit.", false, tint);
	}

	@Override
	public void onRenderHud(GuiGraphicsExtractor g, float delta) {
		if (mc.player == null || mc.player.hurtTime <= 0) {
			return;
		}

		int rgb = Colors.of(mode("Tint"));
		int color = (0x55 << 24) | rgb;
		g.fill(0, 0, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight(), color);
	}
}