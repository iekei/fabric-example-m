package com.iekei.client.modules.basefind;

import com.iekei.client.core.Alerts;
import com.iekei.client.core.ScanModule;
import com.iekei.client.gui.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class IllegalBlockFinder extends ScanModule {
	private static final Setting enabledToggle = Setting.toggle("Enabled", true);

	public IllegalBlockFinder() {
		super("illegalblockfinder", "IllegalBlockFinder", "Flags blocks that should not exist.", false,
				enabledToggle);
	}

	@Override
	protected void onBlock(BlockPos pos, BlockState state) {
		if (state.is(Blocks.BARRIER) || state.is(Blocks.COMMAND_BLOCK) || state.is(Blocks.CHAIN_COMMAND_BLOCK)
				|| state.is(Blocks.REPEATING_COMMAND_BLOCK) || state.is(Blocks.STRUCTURE_BLOCK)
				|| state.is(Blocks.STRUCTURE_VOID) || state.is(Blocks.LIGHT)) {
			Alerts.found("Illegal block", pos);
		}
	}
}