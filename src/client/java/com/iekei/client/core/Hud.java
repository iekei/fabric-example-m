package com.iekei.client.core;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Registry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** HUD layer: module overlays plus the alert feed in the top-right corner. */
public final class Hud {
	private static final Minecraft mc = Minecraft.getInstance();

	private Hud() {
	}

	public static void render(GuiGraphicsExtractor g) {
		if (mc.player == null || mc.options.hideGui) {
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

		int width = mc.getWindow().getGuiScaledWidth();
		int y = 6;

		for (Notify.Entry entry : Notify.alerts()) {
			int alpha = entry.alpha();

			if (alpha <= 0) {
				continue;
			}

			int textWidth = mc.font.width(entry.text());
			int x = width - textWidth - 8;
			g.fill(x - 5, y - 2, width - 3, y + 10, (Math.min(alpha, 160) / 3) << 24);
			g.drawString(mc.font, entry.text(), x, y, (alpha << 24) | 0xFFFFFF);
			y += 12;
		}
	}
}