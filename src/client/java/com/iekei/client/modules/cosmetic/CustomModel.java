package com.iekei.client.modules.cosmetic;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class CustomModel extends Module {
	private static final Setting model = Setting.choice("Model", "Vanilla", "Slim 1.7", "Glow");

	public CustomModel() {
		super("custommodel", "CustomModel", "Swaps the model used by held tools.", false, model);
	}

	public float scale() {
		return switch (mode("Model")) {
			case "Slim 1.7" -> 0.85F;
			case "Glow" -> 1.15F;
			default -> 1.0F;
		};
	}
}