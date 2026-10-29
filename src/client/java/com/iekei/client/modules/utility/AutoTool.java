package com.iekei.client.modules.utility;

import com.iekei.client.core.Inv;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;

public final class AutoTool extends Module {
	private static final Setting respectSilk = Setting.toggle("Respect Silk Touch", false);
	private static final Setting stopLowDurability = Setting.toggle("Stop Low Durability", true);

	public AutoTool() {
		super("autotool", "AutoTool", "Selects the fastest tool when mining.", false,
				respectSilk, stopLowDurability);
	}

	@Override
	public void onTick() {
		if (mc.player == null || !mc.options.keyAttack.isDown() || mc.level == null) {
			return;
		}

		if (!(mc.hitResult instanceof BlockHitResult hit)) {
			return;
		}

		var state = mc.level.getBlockState(hit.getBlockPos());
		int best = -1;
		float bestSpeed = 1.0F;

		for (int i = 0; i < 9; i++) {
			ItemStack stack = mc.player.getInventory().getItem(i);

			if (stack.isEmpty()) {
				continue;
			}

			if (bool("Stop Low Durability") && stack.isDamageableItem()
					&& stack.getMaxDamage() - stack.getDamageValue() < 8) {
				continue;
			}

			float speed = stack.getDestroySpeed(state);

			if (speed > bestSpeed) {
				bestSpeed = speed;
				best = i;
			}
		}

		if (best >= 0 && best != Inv.selected()) {
			Inv.select(best);
		}
	}
}