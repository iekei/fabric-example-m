package com.iekei.client.modules.pvp;

import com.iekei.client.core.BlockUtil;
import com.iekei.client.core.Inv;
import com.iekei.client.core.Timer;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class AutoCobweb extends Module {
	private static final Setting range = Setting.slider("Range", 4.0, 2.0, 8.0, 0.5, "b");
	private static final Setting delay = Setting.slider("Delay", 200, 50, 800, 10, "ms");
	private final Timer cooldown = new Timer();

	public AutoCobweb() {
		super("autocobweb", "AutoCobweb", "Drops cobwebs on approaching enemies.", false, range, delay);
	}

	@Override
	public void onTick() {
		if (mc.player == null || mc.level == null || !cooldown.every(num("Delay"))) {
			return;
		}

		Player target = mc.level.players().stream()
				.filter(p -> p != mc.player && mc.player.distanceTo(p) <= num("Range"))
				.min((a, b) -> Double.compare(mc.player.distanceTo(a), mc.player.distanceTo(b)))
				.orElse(null);

		if (target == null) {
			return;
		}

		BlockPos feet = target.blockPosition();

		if (!BlockUtil.isAir(feet)) {
			return;
		}

		int slot = Inv.find(stack -> stack.is(Items.COBWEB));

		if (slot < 0) {
			return;
		}

		if (slot < 9) {
			Inv.select(slot);
		} else {
			Inv.moveToHotbar(slot, Inv.selected());
			return;
		}

		BlockPos below = feet.below();
		Vec3 hit = Vec3.atCenterOf(below).add(0.0, 0.5, 0.0);
		mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND,
				new BlockHitResult(hit, Direction.UP, below, false));
	}
}