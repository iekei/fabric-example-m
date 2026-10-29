package com.iekei.client.modules.movement;

import com.iekei.client.core.MoveUtil;
import com.iekei.client.gui.ClickGuiScreen;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public final class InventoryMove extends Module {
	private static final Setting allowInGuis = Setting.toggle("Allow in GUIs", true);

	public InventoryMove() {
		super("inventorymove", "InventoryMove", "Move while an inventory screen is open.", false, allowInGuis);
	}

	@Override
	public void onTick() {
		if (mc.player == null) {
			return;
		}

		var screen = mc.gui.screen();

		if (screen == null || screen instanceof ChatScreen || screen instanceof ClickGuiScreen) {
			return;
		}

		if (!bool("Allow in GUIs") && !(screen instanceof AbstractContainerScreen<?>)) {
			return;
		}

		MoveUtil.applyKeyboardInput();
	}
}