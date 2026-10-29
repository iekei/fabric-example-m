package com.iekei.client.modules.cosmetic;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class CustomAnimations extends Module {
	private static final Setting animation = Setting.choice("Animation", "1.7", "Smooth", "Modern");

	public CustomAnimations() {
		super("customanimations", "CustomAnimations", "Changes the first-person hand motion.", true, animation);
	}

	/** Swing duration in ticks. */
	public int swingDuration() {
		return switch (mode("Animation")) {
			case "Smooth" -> 8;
			case "Modern" -> 5;
			default -> 4;
		};
	}
}