package com.iekei.client.modules.combat;

import com.iekei.client.core.Packet;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.player.LocalPlayer;

public final class Criticals extends Module {
	private final Setting mode = Setting.choice("Mode", "Packet", "Jump", "MiniJump");

	public Criticals() {
		super("criticals", "Criticals", "Forces every hit to be a critical hit.", false, mode);
	}

	/** Called by Killaura / Triggerbot right before the attack. */
	public void prepareAttack() {
		LocalPlayer p = mc.player;

		if (p == null || !enabled() || !p.onGround()) {
			return;
		}

		switch (mode("Mode")) {
			case "Jump" -> p.jumpFromGround();
			case "MiniJump" -> p.setDeltaMovement(p.getDeltaMovement().x, 0.2, p.getDeltaMovement().z);
			default -> Packet.critNudge();
		}
	}
}