package com.iekei.client.modules.nav;

import com.iekei.client.core.Nav;
import com.iekei.client.core.Notify;
import com.iekei.client.gui.Draw;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;

public final class ReturnNavigator extends Module {
	private static final Setting radius = Setting.slider("Radius", 60, 20, 120, 5, "px");
	private static BlockPos origin;

	public ReturnNavigator() {
		super("returnnavigator", "ReturnNavigator", "Points back to where you set home.", true, radius);
	}

	public static void setHome(BlockPos pos) {
		origin = pos.immutable();
		Notify.chat("Home set to " + pos.getX() + " " + pos.getY() + " " + pos.getZ());
	}

	public static BlockPos home() {
		return origin;
	}

	@Override
	protected void onEnable() {
		if (origin == null && mc.player != null) {
			origin = mc.player.blockPosition();
			Notify.chat("Home auto-set to your position");
		}
	}

	@Override
	public void onRenderHud(GuiGraphicsExtractor g, float delta) {
		if (mc.player == null || origin == null) {
			return;
		}

		int cx = mc.getWindow().getGuiScaledWidth() / 2;
		int cy = mc.getWindow().getGuiScaledHeight() - 110;
		double bearing = Nav.bearing(origin.getCenter());
		Nav.arrow(g, cx, cy, bearing, numInt("Radius"), 0x55FF99);
		Draw.textCenter(g, "HOME " + Nav.compass(bearing) + " " + (int) Nav.distance(origin.getCenter()) + "m",
				cx, cy + 8, 0xFF55FF99);
	}
}