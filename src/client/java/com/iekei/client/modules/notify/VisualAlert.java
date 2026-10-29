package com.iekei.client.modules.notify;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class VisualAlert extends Module {
	private static final Setting enabledToggle = Setting.toggle("Enabled", true);

	public VisualAlert() {
		super("visualalert", "VisualAlert", "Shows on-screen popup warnings.", false, enabledToggle);
	}
}