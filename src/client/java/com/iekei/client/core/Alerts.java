package com.iekei.client.core;

import net.minecraft.core.BlockPos;

/** Routes a detection to every alert channel the user has switched on. */
public final class Alerts {
	private Alerts() {
	}

	public static void found(String kind, BlockPos pos) {
		found(kind, pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
	}

	public static void found(String kind, String where) {
		String text = kind + " @ " + where;

		if (active("visualalert")) {
			Notify.hud(text);
		}

		if (active("basealert")) {
			Notify.chat(text);

			if (active("soundalert")) {
				Notify.sound();
			}
		}

		if (active("webhook")) {
			Notify.webhook(text);
		}

		if (active("autologalert")) {
			Notify.autoLog(text);
		}
	}

	private static boolean active(String id) {
		var module = ModuleManager.byId(id);
		return module != null && module.enabled();
	}
}