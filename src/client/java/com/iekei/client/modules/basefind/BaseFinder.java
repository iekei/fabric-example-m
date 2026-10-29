package com.iekei.client.modules.basefind;

import com.iekei.client.core.Alerts;
import com.iekei.client.core.ScanModule;
import com.iekei.client.gui.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class BaseFinder extends ScanModule {
	private static final Setting sensitivity = Setting.slider("Sensitivity", 6, 1, 10, 1, "");
	private static final Setting range = Setting.slider("Scan Range", 128, 32, 512, 32, "b");

	public BaseFinder() {
		super("basefinder", "BaseFinder", "Detects man-made block clusters.", true, sensitivity, range);
	}

	@Override
	protected int radius() {
		return numInt("Scan Range");
	}

	@Override
	protected int budget() {
		return 4000;
	}

	@Override
	protected void onBlock(BlockPos pos, BlockState state) {
		if (!state.is(Blocks.CRAFTING_TABLE) && !state.is(Blocks.FURNACE) && !state.is(Blocks.CHEST)
				&& !state.is(Blocks.ENCHANTING_TABLE) && !state.is(Blocks.ANVIL)
				&& !state.is(Blocks.BLAST_FURNACE) && !state.is(Blocks.SMOKER)) {
			return;
		}

		if (pos.getY() > 62 && num("Sensitivity") < 8) {
			return;
		}

		Alerts.found("Base block " + state.getBlock().getName().getString(), pos);
	}
}