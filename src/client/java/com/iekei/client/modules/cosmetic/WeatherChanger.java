package com.iekei.client.modules.cosmetic;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class WeatherChanger extends Module {
	private static final Setting weather = Setting.choice("Weather", "Clear", "Rain", "Thunder");

	public WeatherChanger() {
		super("weatherchanger", "WeatherChanger", "Forces a weather state client-side.", false, weather);
	}

	public float rain() {
		return modeIs("Weather", "Clear") ? 0.0F : 1.0F;
	}

	public float thunder() {
		return modeIs("Weather", "Thunder") ? 1.0F : 0.0F;
	}
}