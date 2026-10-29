package com.iekei.client.modules.utility;

import com.iekei.client.core.BlockUtil;
import com.iekei.client.core.Inv;
import com.iekei.client.core.Timer;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class Scaffold extends Module {
	private static final Setting mode = Setting.choice("Mode", "Normal", "Tower", "Legit");
	private static final Setting silentRotate = Setting.toggle("Silent Rotate", true);
	private final Timer cooldown = new Timer();

	public Scaffold() {
		super("scaffold", "Scaffold", "Places blocks beneath you as you walk.", false, mode, silentRotate);
	}

	@Override
	public void onTick() {
		if (mc.player == null || mc.level == null || !cooldown.every(60)) {
			return;
		}

		BlockPos feet = mc.player.blockPosition();

		if (!BlockUtil.isAir(feet.below())) {
			return;
		}

		if (modeIs("Mode", "Tower") && !mc.options.keyJump.isDown()) {
			return;
		}

		int slot = Inv.find(stack -> stack.getItem() instanceof BlockItem);

		if (slot < 0) {
			return;
		}

		if (slot < 9) {
			Inv.select(slot);
		} else {
			Inv.moveToHotbar(slot, Inv.selected());
			return;
		}

		BlockPos target = feet.below();
		BlockPos anchor = target.below();
		Direction face = Direction.UP;

		if (BlockUtil.isAir(anchor)) {
			anchor = target;
			face = Direction.UP;
			target = target.above();
		}

		Vec3 hitVec = Vec3.atCenterOf(anchor).add(face.getStepX() * 0.5, face.getStepY() * 0.5, face.getStepZ() * 0.5);
		mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND,
				new BlockHitResult(hitVec, face, anchor, false));
	}
}