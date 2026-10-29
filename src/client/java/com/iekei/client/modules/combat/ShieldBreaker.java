package com.iekei.client.modules.pvp;

import com.iekei.client.core.Inv;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;

public final class ShieldBreaker extends Module {
	private static final Setting axeOnly = Setting.toggle("Axe Only", true);

	public ShieldBreaker() {
		super("shieldbreaker", "ShieldBreaker", "Swaps to an axe when the target blocks.", false, axeOnly);
	}

	@Override
	public void onTick() {
		if (mc.player == null || mc.hitResult == null) {
			return;
		}

		if (!(mc.hitResult instanceof net.minecraft.world.phys.EntityHitResult hit)) {
			return;
		}

		if (!(hit.getEntity() instanceof LivingEntity target) || !target.isBlocking()) {
			return;
		}

		int slot = Inv.find(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath().endsWith("_axe"));

		if (slot >= 0 && slot < 9) {
			Inv.select(slot);
		}
	}
}