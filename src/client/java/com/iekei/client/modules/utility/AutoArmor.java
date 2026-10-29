package com.iekei.client.modules.utility;

import com.iekei.client.core.Inv;
import com.iekei.client.core.Timer;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

public final class AutoArmor extends Module {
	private static final Setting allowHotbar = Setting.toggle("Allow Hotbar", false);
	private static final String[] PIECES = {"helmet", "chestplate", "leggings", "boots"};
	private final Timer cooldown = new Timer();

	public AutoArmor() {
		super("autoarmor", "AutoArmor", "Equips the best armour automatically.", true, allowHotbar);
	}

	@Override
	public void onTick() {
		if (mc.player == null || mc.gui.screen() != null || !cooldown.every(250)) {
			return;
		}

		for (int piece = 0; piece < 4; piece++) {
			int armorSlot = 5 + piece;
			int current = score(mc.player.getInventory().getItem(armorSlot));
			int bestSlot = -1;
			int bestScore = current;
			int limit = bool("Allow Hotbar") ? 36 : 9;

			for (int i = 0; i < limit; i++) {
				ItemStack stack = mc.player.getInventory().getItem(i);

				if (!isPiece(stack, piece)) {
					continue;
				}

				int value = score(stack);

				if (value > bestScore) {
					bestScore = value;
					bestSlot = i;
				}
			}

			if (bestSlot >= 0) {
				Inv.click(bestSlot, 0, net.minecraft.world.inventory.ClickType.QUICK_MOVE);
				return;
			}
		}
	}

	private boolean isPiece(ItemStack stack, int piece) {
		return !stack.isEmpty() && id(stack).endsWith(PIECES[piece]);
	}

	private int score(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		}

		String id = id(stack);

		if (id.contains("netherite")) {
			return 6;
		}

		if (id.contains("diamond")) {
			return 5;
		}

		if (id.contains("iron")) {
			return 4;
		}

		if (id.contains("chain")) {
			return 3;
		}

		if (id.contains("golden")) {
			return 2;
		}

		return 1;
	}

	private String id(ItemStack stack) {
		return BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
	}
}