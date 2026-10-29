package com.iekei.client.modules.combat;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;

public final class FastBow extends Module {
	private final Setting instant = Setting.toggle("Instant Release", true);

	public FastBow() {
		super("fastbow", "FastBow", "Shoots arrows with almost no cooldown.", false, instant);
	}

	@Override
	public void onTick() {
		LocalPlayer p = mc.player;

		if (p == null || !p.isUsingItem()) {
			return;
		}

		if (!(p.getUseItem().getItem() instanceof BowItem) && !(p.getUseItem().getItem() instanceof CrossbowItem)) {
			return;
		}

		if (p.getUseItemRemainingTicks() <= (bool("Instant Release") ? 1 : 3)) {
			mc.gameMode.releaseUsingItem(p);
		}
	}
}