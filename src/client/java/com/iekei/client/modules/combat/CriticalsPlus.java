package com.iekei.client.modules.pvp;

import com.iekei.client.core.Packet;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.player.LocalPlayer;

public final class CriticalsPlus extends Module {
	private static final Setting mode = Setting.choice("Mode", "Nudge", "Sneak", "Step");

	public CriticalsPlus() {
		super("criticalsplus", "CriticalsPlus", "Guarantees crits while keeping movement state.", false, mode);
	}

	public void prepareAttack() {
		LocalPlayer p = mc.player;

		if (p == null || !enabled()) {
			return;
		}

		switch (mode("Mode")) {
			case "Sneak" -> {
				p.setShiftKeyDown(true);
				Packet.critNudge();
			}
			case "Step" -> {
				p.setDeltaMovement(p.getDeltaMovement().x, 0.2, p.getDeltaMovement().z);
				Packet.critNudge();
			}
			default -> Packet.critNudge();
		}
	}
}