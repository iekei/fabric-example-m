package com.iekei.client.modules.utility;

import com.iekei.client.core.Notify;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.network.chat.Component;

public final class AutoDisconnect extends Module {
	private static final Setting health = Setting.slider("Health", 6, 1, 20, 1, "hp");

	public AutoDisconnect() {
		super("autodisconnect", "AutoDisconnect", "Leaves the server at low health.", false, health);
	}

	@Override
	public void onTick() {
		if (mc.player == null || mc.player.getHealth() > num("Health") || mc.getConnection() == null) {
			return;
		}

		Notify.chat("AutoDisconnect: " + Math.round(mc.player.getHealth()) + " hp");
		mc.getConnection().getConnection().disconnect(Component.literal("[Iekei] AutoDisconnect"));
		setEnabled(false);
	}
}