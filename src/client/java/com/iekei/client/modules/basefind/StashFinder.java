package com.iekei.client.modules.basefind;

import com.iekei.client.core.Alerts;
import com.iekei.client.core.ScanModule;
import com.iekei.client.gui.Setting;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class StashFinder extends ScanModule {
	private static final Setting minContainers = Setting.slider("Min Containers", 12, 2, 64, 1, "");
	private final Map<Long, Integer> counts = new HashMap<>();
	private final Set<Long> reported = new HashSet<>();

	public StashFinder() {
		super("stashfinder", "StashFinder", "Identifies high-volume storage stashes.", false, minContainers);
	}

	@Override
	protected void onBlock(BlockPos pos, BlockState state) {
		boolean container = state.is(Blocks.CHEST) || state.is(Blocks.TRAPPED_CHEST)
				|| state.is(Blocks.BARREL) || state.getBlock() instanceof ShulkerBoxBlock;

		if (!container) {
			return;
		}

		long chunk = ((long) (pos.getX() >> 4) << 32) | ((pos.getZ() >> 4) & 0xFFFFFFFFL);
		int count = counts.merge(chunk, 1, Integer::sum);

		if (count >= numInt("Min Containers") && reported.add(chunk)) {
			Alerts.found("Stash (" + count + " containers)", new BlockPos(pos.getX() >> 4 << 4, pos.getY(),
					pos.getZ() >> 4 << 4));
		}
	}
}