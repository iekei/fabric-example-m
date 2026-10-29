package com.iekei.client.modules.utility;

import com.iekei.client.core.Inv;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class AutoTotem extends Module {
	private static final Setting below = Setting.slider("Swap Below", 10, 1, 20, 1, "hp");

	public AutoTotem() {
		super("autototem", "AutoTotem", "Keeps a totem in your offhand.", true, below);
	}

	@Override
	public void onTick() {
		if (mc.player == null || mc.player.getHealth() > num("Below")) {
			return;
		}

		ItemStack offhand = mc.player.getOffhandItem();

		if (offhand.is(Items.TOTEM_OF_UNDYING)) {
			return;
		}

		int slot = Inv.find(stack -> stack.is(Items.TOTEM_OF_UNDYING));

		if (slot >= 0) {
			Inv.click(slot, 40, net.minecraft.world.inventory.ClickType.SWAP);
		}
	}
}