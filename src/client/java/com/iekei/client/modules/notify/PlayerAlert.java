package com.iekei.client.modules.notify;

import com.iekei.client.core.Alerts;
import com.iekei.client.core.Timer;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.world.entity.player.Player;

public final class PlayerAlert extends Module {
	private static final Setting distance = Setting.slider("Trigger Distance", 64, 8, 256, 8, "b");
	private final Set<UUID> seen = new HashSet<>();
	private final Timer cooldown = new Timer();

	public PlayerAlert() {
		super("playeralert", "PlayerAlert", "Warns when players come close.", false, distance);
	}

	@Override
	public void onTick() {
		if (mc.player == null || mc.level == null || !cooldown.every(2000)) {
			return;
		}

		double max = num("Distance");
		seen.retainAll(mc.level.players().stream().map(Player::getUUID).toList());

		for (Player p : mc.level.players()) {
			if (p == mc.player || mc.player.distanceTo(p) > max) {
				continue;
			}

			if (seen.add(p.getUUID())) {
				Alerts.found("Player " + p.getName().getString(), p.blockPosition());
			}
		}
	}

	@Override
	protected void onEnable() {
		seen.clear();
	}
}