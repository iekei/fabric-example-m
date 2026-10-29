package com.iekei.client.modules.hud;

import com.iekei.client.gui.Draw;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

public final class ArmorHUD extends Module {
	private static final Setting showEmpty = Setting.toggle("Show Empty", true);

	public ArmorHUD() {
		super("armorhud", "ArmorHUD", "Shows armour durability as percentages.", false, showEmpty);
	}

	@Override
	public void onRenderHud(GuiGraphicsExtractor g, float delta) {
		if (mc.player == null) {
			return;
		}

		int y = mc.getWindow().getGuiScaledHeight() - 60;

		for (int i = 39; i >= 36; i--) {
			ItemStack stack = mc.player.getInventory().getItem(i);

			if (stack.isEmpty() && !bool("Show Empty")) {
				continue;
			}

			String label = stack.isEmpty() ? "---" : Math.round(percent(stack)) + "%";
			int color = stack.isEmpty() ? 0xFF777777 : (percent(stack) > 30 ? 0xFF77FF99 : 0xFFFF6666);
			Draw.text(g, label, 6, y, color);
			y += 10;
		}
	}

	private float percent(ItemStack stack) {
		if (stack.getMaxDamage() == 0) {
			return 100.0F;
		}

		return (stack.getMaxDamage() - stack.getDamageValue()) * 100.0F / stack.getMaxDamage();
	}
}