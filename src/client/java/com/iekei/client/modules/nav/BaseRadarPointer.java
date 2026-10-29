package com.iekei.client.modules.nav;

import com.iekei.client.core.ModuleManager;
import com.iekei.client.core.Nav;
import com.iekei.client.core.Waypoints;
import com.iekei.client.gui.Draw;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import com.iekei.client.modules.chunkutil.BaseRadar;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class BaseRadarPointer extends Module {
	private static final Setting radius = Setting.slider("Radius", 60, 20, 120, 5, "px");

	public BaseRadarPointer() {
		super("baseradarpointer", "BaseRadarPointer", "Points at the likeliest base direction.", true, radius);
	}

	@Override
	public void onRenderHud(GuiGraphicsExtractor g, float delta) {
		if (mc.player == null) {
			return;
		}

		BaseRadar radar = ModuleManager.get(BaseRadar.class);
		var best = radar != null && radar.enabled() ? radar.best() : null;

		if (best == null) {
			var point = Waypoints.nearest("base", mc.player.position());
			best = point == null ? null : point.pos();
		}

		if (best == null) {
			return;
		}

		int cx = mc.getWindow().getGuiScaledWidth() / 2;
		int cy = mc.getWindow().getGuiScaledHeight() - 70;
		double bearing = Nav.bearing(best.getCenter());
		Nav.arrow(g, cx, cy, bearing, numInt("Radius"), 0xC792EA);
		Draw.textCenter(g, Nav.compass(bearing) + " " + (int) Nav.distance(best.getCenter()) + "m",
				cx, cy + 8, 0xFFC792EA);
	}
}