package com.iekei.client.modules.settings;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import com.iekei.client.gui.Theme;

public final class ThemeSelector extends Module {
	private static final Setting accent = Setting.choice("Accent",
			"Violet", "Red", "Blue", "Green", "Amber");
	private static final Setting glow = Setting.slider("Glow Intensity", 70, 0, 100, 5, "%");
	private int applied = -1;

	public ThemeSelector() {
		super("theme", "Theme Selector", "Accent colour used across the whole client.", true, accent, glow);
	}

	@Override
	public void onTick() {
		int choice = accent.getChoice();

		if (choice != applied) {
			applied = choice;
			Theme.setPreset(choice);
		}
	}

	public double glow() {
		return num("Glow Intensity");
	}
}