package com.iekei.client.modules.utility;

import com.iekei.client.core.Inv;
import com.iekei.client.core.Timer;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.core.registries.BuiltInRegistries;
import java.util.List;

public final class InventoryCleaner extends Module {
	private static final Setting autoDrop = Setting.toggle("Auto Drop", true);
	private static final Setting keepGapples = Setting.toggle("Keep Gapples", true);
	private static final List<String> JUNK = List.of(
			"rotten_flesh", "poisonous_potato", "spider_eye", "beetroot_seeds", "wheat_seeds",
			"pumpkin_seeds", "melon_seeds", "bowl", "stick", "flint", "gravel", "dirt", "cobblestone");
	private final Timer cooldown = new Timer();

	public InventoryCleaner() {
		super("inventorycleaner", "InventoryCleaner", "Drops junk items automatically.", false,
				autoDrop, keepGapples);
	}

	@Override
	public void onTick() {
		if (mc.player == null || !bool("Auto Drop") || !cooldown.every(150)) {
			return;
		}

		for (int i = 9; i < 36; i++) {
			var stack = mc.player.getInventory().getItem(i);

			if (stack.isEmpty()) {
				continue;
			}

			String id = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();

			if (bool("Keep Gapples") && (id.contains("golden_apple") || id.equals("enchanted_golden_apple"))) {
				continue;
			}

			if (JUNK.contains(id)) {
				Inv.drop(i);
				return;
			}
		}
	}
}