package com.iekei.client.core;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Budgeted world scanner. Walks a box around the player with a persistent
 * cursor so large radii stay cheap, and reports each block once.
 */
public abstract class ScanModule extends Module {
	private final Set<Long> seen = new HashSet<>();
	private int cursor;

	protected ScanModule(String id, String name, String description, boolean enabled, Setting... settings) {
		super(id, name, description, enabled, settings);
	}

	protected abstract void onBlock(BlockPos pos, BlockState state);

	@Override
	public void onTick() {
		if (mc.player == null || mc.level == null) {
			return;
		}

		int radius = radius();
		int height = height();
		int budget = budget();
		int width = radius * 2 + 1;
		int depth = height * 2 + 1;
		int total = width * width * depth;
		BlockPos base = mc.player.blockPosition();

		for (int i = 0; i < budget; i++) {
			cursor = (cursor + 1) % total;

			int x = cursor % width - radius;
			int z = (cursor / width) % width - radius;
			int y = (cursor / (width * width)) % depth - height;
			BlockPos pos = base.offset(x, y, z);

			if (!mc.level.isLoaded(pos)) {
				continue;
			}

			if (!seen.add(pos.asLong())) {
				continue;
			}

			onBlock(pos, mc.level.getBlockState(pos));
		}

		if (seen.size() > 400_000) {
			seen.clear();
		}
	}

	protected int radius() {
		return 64;
	}

	protected int height() {
		return 24;
	}

	protected int budget() {
		return 3000;
	}
}