package com.iekei.client.modules.notify;

import com.iekei.client.core.Notify;
import com.iekei.client.core.Timer;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.world.entity.player.Player;

public final class AutoLogOnAlert extends Module {
	private static final Setting health = Setting.slider("Health Threshold", 8, 1, 20, 1, "hp");
	private static final Setting distance = Setting.slider("Distance Threshold", 32, 8, 128, 8, "b");
	private final Timer cooldown = new Timer();

	public AutoLogOnAlert() {
		super("autologalert", "AutoLogOnAlert", "Disconnects when an alert fires.", false, health, distance);
	}

	@Override
	public void onTick() {
		if (mc.player == null || mc.level == null || !cooldown.every(500)) {
			return;
		}

		if (mc.player.getHealth() <= num("Health")) {
			Notify.autoLog("Low health");
			return;
		}

		for (Player p : mc.level.players()) {
			if (p != mc.player && mc.player.distanceTo(p) <= num("Distance")) {
				Notify.autoLog("Player nearby");
				return;
			}
		}
	}
}