package com.iekei.client.modules.chunkutil;

import com.iekei.client.core.Nav;
import com.iekei.client.core.ScanModule;
import com.iekei.client.gui.Draw;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class ChestRadar extends ScanModule {
	private static final Setting range = Setting.slider("Range", 64, 16, 192, 16, "b");
	private static final Setting maxRows = Setting.slider("Rows", 8, 3, 20, 1, "");
	private final List<BlockPos> found = new ArrayList<>();

	public ChestRadar() {
		super("chestradar", "ChestRadar", "Lists nearby containers on screen.", false, range, maxRows);
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

		found.add(pos.immutable());
		com.iekei.client.core.Waypoints.add("container", pos);

		while (found.size() > 200) {
			found.remove(0);
		}
	}

	@Override
	public void onRenderHud(GuiGraphicsExtractor g, float delta) {
		if (mc.player == null || found.isEmpty()) {
			return;
		}

		List<BlockPos> sorted = found.stream()
				.filter(p -> mc.player.distanceToSqr(p.getCenter()) <= Math.pow(num("Range"), 2))
				.sorted(Comparator.comparingDouble(p -> mc.player.distanceToSqr(p.getCenter())))
				.limit(numInt("Rows"))
				.toList();

		int y = 40;

		for (BlockPos pos : sorted) {
			double bearing = Nav.bearing(pos.getCenter());
			int distance = (int) Nav.distance(pos.getCenter());
			String row = Nav.compass(bearing) + "  " + distance + "m  "
					+ pos.getX() + " " + pos.getY() + " " + pos.getZ();
			Draw.text(g, row, 6, y, 0xFFFFC144);
			y += 10;
		}
	}
}