package com.iekei.client.core;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Registry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** HUD layer: module overlays plus the notification feed. */
public final class Hud {
	private static final Minecraft mc = Minecraft.getInstance();

	private Hud() {
	}

	public static void render(GuiGraphicsExtractor g) {
		if (mc.player == null) {
			return;
		}

		for (var category : Registry.categories()) {
			for (Module m : category.modules()) {
				if (!m.enabled()) {
					continue;
				}

				try {
					m.onRenderHud(g, 1.0F);
				} catch (Exception e) {
					IekeiLog.error(m.name(), e);
				}
			}
		}

		int y = 6;

		for (String line : Notify.hudLines()) {
			g.drawString(mc.font, line, 6, y, 0xFFFFFFFF);
			y += 10;
		}
	}
}