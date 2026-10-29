package com.iekei.client.modules.pvp;

import com.iekei.client.core.Timer;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.ClickType;

public final class AutoArmorSwap extends Module {
	private static final Setting crystalRange = Setting.slider("Crystal Range", 8.0, 2.0, 16.0, 0.5, "b");
	private static final Setting cooldownMs = Setting.slider("Cooldown", 100, 0, 500, 10, "ms");
	private final Timer cooldown = new Timer();

	public AutoArmorSwap() {
		super("autoarmorswap", "AutoArmorSwap", "Re-equips armour instantly under threat.", false,
				crystalRange, cooldownMs);
	}

	@Override
	public void onTick() {
		if (mc.player == null || mc.level == null || !threatened() || !cooldown.every(num("Cooldown"))) {
			return;
		}

		for (int piece = 0; piece < 4; piece++) {
			int slot = 5 + piece;
			int best = -1;
			int bestScore = score(mc.player.getInventory().getItem(slot));

			for (int i = 0; i < 36; i++) {
				ItemStack stack = mc.player.getInventory().getItem(i);

				if (stack.isEmpty() || !fits(stack, piece)) {
					continue;
				}

				int value = score(stack);

				if (value > bestScore) {
					bestScore = value;
					best = i;
				}
			}

			if (best >= 0) {
				com.iekei.client.core.Inv.click(best, 0, ClickType.QUICK_MOVE);
				return;
			}
		}
	}

	private boolean threatened() {
		if (mc.player.hurtTime > 0) {
			return true;
		}

		double r = num("Crystal Range");

		for (Entity e : mc.level.entitiesForRendering()) {
			if (e instanceof EndCrystal && mc.player.distanceTo(e) <= r) {
				return true;
			}
		}

		return false;
	}

	private boolean fits(ItemStack stack, int piece) {
		String[] pieces = {"helmet", "chestplate", "leggings", "boots"};
		return BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath().endsWith(pieces[piece]);
	}

	private int score(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		}

		String id = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
		int base = id.contains("netherite") ? 6 : id.contains("diamond") ? 5 : id.contains("iron") ? 4
				: id.contains("chain") ? 3 : id.contains("golden") ? 2 : 1;
		return base;
	}
}