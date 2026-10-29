package com.iekei.client.modules.nav;

import com.iekei.client.core.Nav;
import com.iekei.client.core.Waypoints;
import com.iekei.client.gui.Draw;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import java.util.Comparator;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class ChunkTrailTracer extends Module {
	private static final Setting radius = Setting.slider("Radius", 60, 20, 120, 5, "px");
	private static final Setting maxArrows = Setting.slider("Arrows", 3, 1, 8, 1, "");

	public ChunkTrailTracer() {
		super("chunktrailtracer", "ChunkTrailTracer", "Points along recent player trails.", true,
				radius, maxArrows);
	}

	@Override
	public void onRenderHud(GuiGraphicsExtractor g, float delta) {
		if (mc.player == null) {
			return;
		}

		var points = Waypoints.ofKind("broken").stream()
				.sorted(Comparator.comparingLong(p -> -p.time()))
				.limit(numInt("MaxArrows"))
				.toList();

		if (points.isEmpty()) {
			return;
		}

		int cx = mc.getWindow().getGuiScaledWidth() / 2;
		int cy = mc.getWindow().getGuiScaledHeight() - 90;

		for (var point : points) {
			double bearing = Nav.bearing(point.pos().getCenter());
			Nav.arrow(g, cx, cy, bearing, numInt("Radius"), 0x66CCFF);
			Draw.textCenter(g, (int) Nav.distance(point.pos().getCenter()) + "m", cx, cy + 8, 0xFF66CCFF);
			cy -= 22;
		}
	}
}