package com.iekei.client.modules.notify;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class BaseAlert extends Module {
	private static final Setting systemSound = Setting.toggle("System Sound", true);

	public BaseAlert() {
		super("basealert", "BaseAlert", "Notifies when a base is detected.", true, systemSound);
	}
}