package com.iekei.client.modules.pvp;

import com.iekei.client.core.Inv;
import com.iekei.client.core.Timer;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Items;

public final class AutoTotemRefill extends Module {
	private static final Setting cooldownMs = Setting.slider("Cooldown", 80, 0, 400, 10, "ms");
	private final Timer cooldown = new Timer();

	public AutoTotemRefill() {
		super("autototemrefill", "AutoTotemRefill", "Refills the offhand totem the instant it pops.", false,
				cooldownMs);
	}

	@Override
	public void onTick() {
		if (mc.player == null || !cooldown.every(num("Cooldown"))) {
			return;
		}

		if (mc.player.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) {
			return;
		}

		int slot = Inv.find(stack -> stack.is(Items.TOTEM_OF_UNDYING));

		if (slot >= 0) {
			Inv.click(slot, 40, ClickType.SWAP);
		}
	}
}