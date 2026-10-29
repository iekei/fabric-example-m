package com.iekei.client.modules.hud;

import com.iekei.client.gui.Draw;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.effect.MobEffectInstance;

public final class PotionStatus extends Module {
	private static final Setting maxRows = Setting.slider("Rows", 6, 1, 16, 1, "");

	public PotionStatus() {
		super("potionstatus", "PotionStatus", "Lists active effects and their timers.", false, maxRows);
	}

	@Override
	public void onRenderHud(GuiGraphicsExtractor g, float delta) {
		if (mc.player == null || mc.player.getActiveEffects().isEmpty()) {
			return;
		}

		int y = 40;

		for (MobEffectInstance effect : mc.player.getActiveEffects().stream()
				.limit(numInt("Rows")).toList()) {
			String name = effect.getEffect().value().getDisplayName().getString();
			int seconds = effect.getDuration() / 20;
			int color = effect.getEffect().value().isBeneficial() ? 0xFF77FF99 : 0xFFFF7777;
			Draw.text(g, name + "  " + seconds + "s", 6, y, color);
			y += 10;
		}
	}
}