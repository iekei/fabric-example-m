package com.iekei.client.modules.combat;

import com.iekei.client.core.ModuleManager;
import com.iekei.client.core.Timer;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;

public final class Triggerbot extends Module {
	private final Setting delay = Setting.slider("Delay", 60, 0, 500, 10, "ms");
	private final Timer cooldown = new Timer();

	public Triggerbot() {
		super("triggerbot", "Triggerbot", "Attacks whatever is under your crosshair.", false, delay);
	}

	@Override
	public void onTick() {
		LocalPlayer p = mc.player;

		if (p == null || !(mc.hitResult instanceof EntityHitResult hit)) {
			return;
		}

		Entity target = hit.getEntity();

		if (target == null || !target.isAlive() || !cooldown.every(num("Delay"))) {
			return;
		}

		Criticals criticals = ModuleManager.get(Criticals.class);

		if (criticals != null && criticals.enabled()) {
			criticals.prepareAttack();
		}

		mc.gameMode.attack(p, target);
		p.swing(InteractionHand.MAIN_HAND);
	}
}