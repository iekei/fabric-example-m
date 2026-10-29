package com.iekei.client.modules.pvp;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public final class HitBoxPlus extends Module {
	private static final Setting size = Setting.slider("Size", 0.9, 0.1, 3.0, 0.05, "b");
	private static final Setting playersOnly = Setting.toggle("Players Only", true);

	public HitBoxPlus() {
		super("hitboxplus", "HitBoxPlus", "Extra hitbox expansion for PvP.", false, size, playersOnly);
	}

	public double size() {
		return num("Size");
	}

	public boolean appliesTo(Entity entity) {
		return !bool("Players Only") || entity instanceof Player;
	}
}