package com.iekei.client.modules.cosmetic;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class ParticleCustomizer extends Module {
	private static final Setting amount = Setting.slider("Amount", 120, 0, 300, 10, "%");
	public static boolean spawning;

	public ParticleCustomizer() {
		super("particles", "ParticleCustomizer", "Amplifies hit and critical particles.", false, amount);
	}

	public int multiplier() {
		return Math.max(1, numInt("Amount") / 100);
	}
}