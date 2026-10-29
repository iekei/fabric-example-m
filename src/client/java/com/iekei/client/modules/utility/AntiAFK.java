package com.iekei.client.modules.utility;

import com.iekei.client.core.Timer;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import java.util.Random;
import net.minecraft.world.InteractionHand;

public final class AntiAFK extends Module {
	private static final Setting mode = Setting.choice("Mode", "Swing", "Walk", "Rotate");
	private static final Setting interval = Setting.slider("Interval", 30, 5, 120, 5, "s");
	private final Timer cooldown = new Timer();
	private final Random random = new Random();

	public AntiAFK() {
		super("antiafk", "AntiAFK", "Avoids AFK kicks with small moves.", false, mode, interval);
	}

	@Override
	public void onTick() {
		if (mc.player == null || !cooldown.every(num("Interval") * 1000.0)) {
			return;
		}

		switch (mode("Mode")) {
			case "Walk" -> {
				mc.options.keyUp.setDown(true);
				mc.player.setDeltaMovement(
						Math.sin(Math.toRadians(-mc.player.getYRot())) * 0.12,
						mc.player.getDeltaMovement().y,
						Math.cos(Math.toRadians(-mc.player.getYRot())) * 0.12);
			}
			case "Rotate" -> {
				mc.player.setYRot(mc.player.getYRot() + (random.nextBoolean() ? 35.0F : -35.0F));
				mc.player.setXRot(-15.0F + random.nextFloat() * 30.0F);
			}
			default -> mc.player.swing(InteractionHand.MAIN_HAND);
		}
	}

	@Override
	protected void onDisable() {
		if (mc.options != null) {
			mc.options.keyUp.setDown(false);
		}
	}
}