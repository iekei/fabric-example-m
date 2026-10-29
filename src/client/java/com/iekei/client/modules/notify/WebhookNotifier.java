package com.iekei.client.modules.notify;

import com.iekei.client.core.Notify;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class WebhookNotifier extends Module {
	private static final Setting target = Setting.choice("Target", "Discord", "Custom URL");

	public WebhookNotifier() {
		super("webhook", "WebhookNotifier", "Sends detections to a Discord webhook.", false, target);
	}

	@Override
	protected void onEnable() {
		Notify.chat("Set the webhook with .iekai webhook <url>");
	}
}