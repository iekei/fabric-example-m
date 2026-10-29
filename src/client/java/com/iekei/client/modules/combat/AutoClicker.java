package com.iekei.client.modules.combat;

import com.iekei.client.core.Timer;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import java.util.Random;

public final class AutoClicker extends Module {
	private final Setting cps = Setting.slider("CPS", 12, 1, 20, 0.5, "/s");
	private final Setting button = Setting.choice("Button", "Left", "Right", "Both");
	private final Setting jitter = Setting.toggle("Jitter", true);
	private final Timer cooldown = new Timer();
	private final Random random = new Random();

	public AutoClicker() {
		super("autoclicker", "AutoClicker", "Clicks for you at a set CPS.", false, cps, button, jitter);
	}

	@Override
	public void onTick() {
		if (mc.player == null || mc.gui.screen() != null || mc.gameMode == null) {
			return;
		}

		double rate = num("CPS");
		double wait = 1000.0 / rate * (bool("Jitter") ? 0.7 + random.nextDouble() * 0.6 : 1.0);

		if (!cooldown.every(wait)) {
			return;
		}

		String which = mode("Button");

		if (!which.equals("Right")) {
			mc.options.keyAttack.setDown(true);
			mc.options.keyAttack.setDown(false);
		}

		if (!which.equals("Left")) {
			mc.options.keyUse.setDown(true);
			mc.options.keyUse.setDown(false);
		}
	}
}