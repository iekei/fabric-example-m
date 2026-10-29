package com.iekei.client.modules.utility;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;

public final class AutoRespawn extends Module {
	private static final Setting enabledToggle = Setting.toggle("Enabled", true);

	public AutoRespawn() {
		super("autorespawn", "AutoRespawn", "Respawns instantly after dying.", false, enabledToggle);
	}

	@Override
	public void onTick() {
		if (mc.player == null || !mc.player.isDeadOrDying() || !bool("Enabled")) {
			return;
		}

		if (mc.getConnection() != null) {
			mc.getConnection().send(new ServerboundClientCommandPacket(
					ServerboundClientCommandPacket.Action.PERFORM_RESPAWN));
		}
	}
}