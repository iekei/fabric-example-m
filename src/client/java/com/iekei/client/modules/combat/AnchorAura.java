package com.iekei.client.modules.pvp;

import com.iekei.client.core.Inv;
import com.iekei.client.core.Timer;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class AnchorAura extends Module {
	private static final Setting range = Setting.slider("Range", 4.0, 2.0, 8.0, 0.5, "b");
	private static final Setting delay = Setting.slider("Delay", 150, 50, 600, 10, "ms");
	private static final Setting placeOwn = Setting.toggle("Place Anchor", true);
	private final Timer cooldown = new Timer();

	public AnchorAura() {
		super("anchoraura", "AnchorAura", "Charges and detonates respawn anchors.", false,
				range, delay, placeOwn);
	}

	@Override
	public void onTick() {
		if (mc.player == null || mc.level == null || !cooldown.every(num("Delay"))) {
			return;
		}

		BlockPos anchor = findAnchor();

		if (anchor == null) {
			if (bool("Place Anchor")) {
				placeAnchor();
			}

			return;
		}

		int charge = mc.level.getBlockState(anchor).getValue(RespawnAnchorBlock.CHARGE);
		int glowstone = Inv.find(stack -> stack.is(Items.GLOWSTONE));

		if (charge == 0) {
			if (glowstone >= 0) {
				select(glowstone);
				use(anchor);
			}

			return;
		}

		if (mc.player.getMainHandItem().is(Items.GLOWSTONE) && glowstone >= 0) {
			use(anchor);
			return;
		}

		use(anchor);
	}

	private BlockPos findAnchor() {
		int r = numInt("Range");
		BlockPos origin = mc.player.blockPosition();

		for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-r, -2, -r), origin.offset(r, 2, r))) {
			if (mc.level.getBlockState(pos).is(Blocks.RESPAWN_ANCHOR)) {
				return pos.immutable();
			}
		}

		return null;
	}

	private void placeAnchor() {
		int slot = Inv.find(stack -> stack.is(Items.RESPAWN_ANCHOR));

		if (slot < 0) {
			return;
		}

		select(slot);
		BlockPos below = mc.player.blockPosition().below();
		use(below);
	}

	private void select(int slot) {
		if (slot < 9) {
			Inv.select(slot);
		} else {
			Inv.moveToHotbar(slot, Inv.selected());
		}
	}

	private void use(BlockPos pos) {
		Vec3 hit = Vec3.atCenterOf(pos).add(0.0, 0.5, 0.0);
		mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND,
				new BlockHitResult(hit, Direction.UP, pos, false));
	}
}