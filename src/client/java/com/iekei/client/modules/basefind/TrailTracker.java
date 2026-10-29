package com.iekei.client.modules.chunkutil;

import com.iekei.client.core.Nav;
import com.iekei.client.core.Waypoints;
import com.iekei.client.gui.Draw;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;

public final class TrailTracker extends Module {
	private static final Setting rows = Setting.slider("Rows", 8, 3, 20, 1, "");
	private static final Setting history = Setting.slider("History", 30, 5, 120, 5, "s");

	public TrailTracker() {
		super("trailtracker", "TrailTracker", "Logs block changes made by players.", false, rows, history);
	}

	/** Called from the block update mixin. */
	public void onBlockUpdate(BlockPos pos, boolean nowAir) {
		if (!enabled()) {
			return;
		}

		Waypoints.add(nowAir ? "broken" : "placed", pos);
	}

	@Override
	public void onRenderHud(GuiGraphicsExtractor g, float delta) {
		if (mc.player == null) {
			return;
		}

		long cutoff = System.currentTimeMillis() - (long) (num("History") * 1000.0);
		var recent = Waypoints.all().stream()
				.filter(p -> (p.kind().equals("broken") || p.kind().equals("placed")) && p.time() >= cutoff)
				.sorted((a, b) -> Long.compare(b.time(), a.time()))
				.limit(numInt("Rows"))
				.toList();

		int y = 40;

		for (var point : recent) {
			double bearing = Nav.bearing(point.pos().getCenter());
			Draw.text(g, (point.kind().equals("broken") ? "- " : "+ ") + Nav.compass(bearing) + "  "
					+ (int) Nav.distance(point.pos().getCenter()) + "m", 6, y,
					point.kind().equals("broken") ? 0xFFFF7777 : 0xFF77FF99);
			y += 10;
		}
	}
}