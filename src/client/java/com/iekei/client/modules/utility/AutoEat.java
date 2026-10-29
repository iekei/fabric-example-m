package com.iekei.client.modules.utility;

import com.iekei.client.core.Inv;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public final class AutoEat extends Module {
	private static final Setting hunger = Setting.slider("Hunger Threshold", 14, 2, 19, 1, "/20");

	public AutoEat() {
		super("autoeat", "AutoEat", "Eats when your hunger drops.", false, hunger);
	}

	@Override
	public void onTick() {
		if (mc.player == null || mc.gui.screen() != null || mc.player.isUsingItem()) {
			return;
		}

		if (mc.player.getFoodData().getFoodLevel() > num("Hunger Threshold")) {
			return;
		}

		ItemStack held = mc.player.getInventory().getItem(Inv.selected());

		if (!held.isEdible()) {
			int slot = Inv.find(ItemStack::isEdible);

			if (slot < 0) {
				return;
			}

			if (slot < 9) {
				Inv.select(slot);
			} else {
				Inv.moveToHotbar(slot, Inv.selected());
			}

			return;
		}

		mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
	}
}