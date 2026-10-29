package com.iekei.client.modules.basefind;

import com.iekei.client.core.Alerts;
import com.iekei.client.core.ScanModule;
import com.iekei.client.gui.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class ChestFinder extends ScanModule {
	private static final Setting range = Setting.slider("Range", 96, 16, 256, 16, "b");
	private static final Setting logToChat = Setting.toggle("Log to Chat", true);

	public ChestFinder() {
		super("chestfinder", "ChestFinder", "Scans for containers and logs coordinates.", false,
				range, logToChat);
	}

	@Override
	protected int radius() {
		return numInt("Range");
	}

	@Override
	protected void onBlock(BlockPos pos, BlockState state) {
		boolean container = state.is(Blocks.CHEST) || state.is(Blocks.TRAPPED_CHEST)
				|| state.is(Blocks.BARREL) || state.getBlock() instanceof ShulkerBoxBlock;

		if (!container) {
			return;
		}

		if (bool("Log to Chat")) {
			Alerts.found("Container", pos);
		} else {
			Alerts.found("Container", pos);
		}
	}
}