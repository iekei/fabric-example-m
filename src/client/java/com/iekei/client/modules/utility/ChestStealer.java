package com.iekei.client.modules.utility;

import com.iekei.client.core.Inv;
import com.iekei.client.core.Timer;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ClickType;

public final class ChestStealer extends Module {
	private static final Setting delay = Setting.slider("Delay", 60, 0, 500, 10, "ms");
	private final Timer cooldown = new Timer();

	public ChestStealer() {
		super("cheststealer", "ChestStealer", "Empties chests into your inventory.", false, delay);
	}

	@Override
	public void onTick() {
		if (mc.player == null || !(mc.gui.screen() instanceof AbstractContainerScreen<?>)) {
			return;
		}

		var menu = mc.player.containerMenu;

		if (menu == mc.player.inventoryMenu || !cooldown.every(num("Delay"))) {
			return;
		}

		for (int i = 0; i < menu.slots.size(); i++) {
			var slot = menu.getSlot(i);

			if (slot.container == mc.player.getInventory()) {
				continue;
			}

			if (slot.hasItem()) {
				Inv.click(i, 0, ClickType.QUICK_MOVE);
				return;
			}
		}
	}
}