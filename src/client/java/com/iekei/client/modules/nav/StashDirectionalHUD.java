package com.iekei.client.modules.nav;

import com.iekei.client.core.Nav;
import com.iekei.client.core.Waypoints;
import com.iekei.client.gui.Draw;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class StashDirectionalHUD extends Module {
	private static final Setting radius = Setting.slider("Radius", 60, 20, 120, 5, "px");

	public StashDirectionalHUD() {
		super("stashdirectionalhud", "StashDirectionalHUD", "Points at the nearest detected stash.", true,
				radius);
	}

	@Override
	public void onRenderHud(GuiGraphicsExtractor g, float delta) {
		if (mc.player == null) {
			return;
		}

		var vault = Waypoints.nearest("vault", mc.player.position());
		var container = Waypoints.nearest("container", mc.player.position());
		var target = vault != null ? vault : container;

		if (target == null) {
			return;
		}

		int cx = mc.getWindow().getGuiScaledWidth() / 2;
		int cy = mc.getWindow().getGuiScaledHeight() - 130;
		double bearing = Nav.bearing(target.pos().getCenter());
		Nav.arrow(g, cx, cy, bearing, numInt("Radius"), 0xFFC144);
		Draw.textCenter(g, "STASH " + Nav.compass(bearing) + " " + (int) Nav.distance(target.pos().getCenter())
				+ "m", cx, cy + 8, 0xFFFFC144);
	}
}