package com.iekei.client.modules.chunkutil;

import com.iekei.client.core.Nav;
import com.iekei.client.core.ScanModule;
import com.iekei.client.core.Waypoints;
import com.iekei.client.gui.Draw;
import com.iekei.client.gui.Setting;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class BaseRadar extends ScanModule {
	private static final Setting range = Setting.slider("Range", 192, 64, 512, 32, "b");
	private static final Setting threshold = Setting.slider("Threshold", 12, 3, 64, 1, "");
	private final Map<Long, Integer> scores = new HashMap<>();

	public BaseRadar() {
		super("baseradar", "BaseRadar", "Scores man-made block density per chunk.", false, range, threshold);
	}

	@Override
	protected int radius() {
		return numInt("Range");
	}

	@Override
	protected int budget() {
		return 6000;
	}

	@Override
	protected void onBlock(BlockPos pos, BlockState state) {
		boolean manMade = state.is(Blocks.CHEST) || state.is(Blocks.CRAFTING_TABLE) || state.is(Blocks.FURNACE)
				|| state.is(Blocks.ENCHANTING_TABLE) || state.is(Blocks.BARREL) || state.is(Blocks.HOPPER)
				|| state.is(Blocks.OBSIDIAN) || state.is(Blocks.ANVIL);

		if (!manMade) {
			return;
		}

		long chunk = ((long) (pos.getX() >> 4) << 32) | ((pos.getZ() >> 4) & 0xFFFFFFFFL);
		int score = scores.merge(chunk, 1, Integer::sum);

		if (score == numInt("Threshold")) {
			Waypoints.add("base", new BlockPos(pos.getX() >> 4 << 4, pos.getY(), pos.getZ() >> 4 << 4));
		}
	}

	/** Highest scoring chunk centre, or null. */
	public BlockPos best() {
		return scores.entrySet().stream()
				.filter(e -> e.getValue() >= numInt("Threshold"))
				.max(Map.Entry.comparingByValue())
				.map(e -> new BlockPos((int) (e.getKey() >> 32) * 16 + 8, (int) mc.player.getY(),
						(int) (long) e.getKey() * 16 + 8))
				.orElse(null);
	}

	@Override
	public void onRenderHud(GuiGraphicsExtractor g, float delta) {
		if (mc.player == null || scores.isEmpty()) {
			return;
		}

		List<Map.Entry<Long, Integer>> top = new ArrayList<>(scores.entrySet());
		top.sort(Map.Entry.<Long, Integer>comparingByValue().reversed());

		int y = 40;

		for (int i = 0; i < Math.min(5, top.size()); i++) {
			var entry = top.get(i);
			var pos = new BlockPos((int) (entry.getKey() >> 32) * 16 + 8, (int) mc.player.getY(),
					(int) (long) entry.getKey() * 16 + 8);
			double bearing = Nav.bearing(pos.getCenter());
			Draw.text(g, Nav.compass(bearing) + "  " + (int) Nav.distance(pos.getCenter()) + "m  score "
					+ entry.getValue(), 6, y, 0xFFC792EA);
			y += 10;
		}
	}
}