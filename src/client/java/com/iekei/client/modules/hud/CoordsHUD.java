package com.iekei.client.modules.hud;

import com.iekei.client.gui.Draw;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class CoordsHUD extends Module {
	private static final Setting showBiome = Setting.toggle("Show Biome", true);

	public CoordsHUD() {
		super("coordshud", "CoordsHUD", "Shows coordinates, biome and facing.", false, showBiome);
	}

	@Override
	public void onRenderHud(GuiGraphicsExtractor g, float delta) {
		if (mc.player == null || mc.level == null) {
			return;
		}

		int x = mc.getWindow().getGuiScaledWidth() - 6;
		int y = mc.getWindow().getGuiScaledHeight() - 40;
		var pos = mc.player.blockPosition();
		String coords = pos.getX() + " / " + pos.getY() + " / " + pos.getZ();
		Draw.textRight(g, coords, x, y, 0xFFFFFFFF);
		Draw.textRight(g, facing(), x, y + 10, 0xFFC792EA);

		if (bool("Show Biome")) {
			String biome = mc.level.getBiome(pos).unwrapKey()
					.map(k -> k.location().getPath()).orElse("?");
			Draw.textRight(g, biome, x, y + 20, 0xFFAAAAAA);
		}
	}

	private String facing() {
		float yaw = ((mc.player.getYRot() % 360.0F) + 360.0F) % 360.0F;

		if (yaw < 45.0F || yaw >= 315.0F) {
			return "S";
		}

		if (yaw < 135.0F) {
			return "W";
		}

		if (yaw < 225.0F) {
			return "N";
		}

		return "E";
	}
}