package com.iekei.client.modules.basefind;

import com.iekei.client.core.Alerts;
import com.iekei.client.core.ScanModule;
import com.iekei.client.gui.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class SpawnerFinder extends ScanModule {
	private static final Setting enabledToggle = Setting.toggle("Enabled", true);

	public SpawnerFinder() {
		super("spawnerfinder", "SpawnerFinder", "Finds mob spawners underground.", false, enabledToggle);
	}

	@Override
	protected void onBlock(BlockPos pos, BlockState state) {
		if (state.is(Blocks.SPAWNER)) {
			Alerts.found("Spawner", pos);
		}
	}
}