package com.iekei.client.modules.chunkutil;

import com.iekei.client.core.Alerts;
import com.iekei.client.core.ScanModule;
import com.iekei.client.core.Waypoints;
import com.iekei.client.gui.Setting;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class VaultFinder extends ScanModule {
	private static final Setting minContainers = Setting.slider("Min Containers", 6, 2, 32, 1, "");
	private static final Setting radiusSetting = Setting.slider("Cluster Radius", 6, 2, 16, 1, "b");
	private final Map<Long, Integer> counts = new HashMap<>();
	private final Set<Long> reported = new HashSet<>();

	public VaultFinder() {
		super("vaultfinder", "VaultFinder", "Finds dense container clusters (vaults).", false,
				minContainers, radiusSetting);
	}

	@Override
	protected void onBlock(BlockPos pos, BlockState state) {
		boolean container = state.is(Blocks.BARREL) || state.is(Blocks.CHEST)
				|| state.getBlock() instanceof ShulkerBoxBlock;

		if (!container) {
			return;
		}

		int cell = numInt("Cluster Radius");
		long key = ((long) (pos.getX() / cell) << 32) | ((pos.getZ() / cell) & 0xFFFFFFFFL);
		int count = counts.merge(key, 1, Integer::sum);

		if (count >= numInt("Min Containers") && reported.add(key)) {
			Waypoints.add("vault", pos.immutable());
			Alerts.found("Vault (" + count + " containers)", pos);
		}
	}
}