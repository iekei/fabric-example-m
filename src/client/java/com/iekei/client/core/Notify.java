package com.iekei.client.core;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

public final class Notify {
	private static final Minecraft mc = Minecraft.getInstance();
	private static final List<Entry> ALERTS = new ArrayList<>();
	private static final HttpClient HTTP = HttpClient.newHttpClient();

	private Notify() {
	}

	/* ---------------- chat / actionbar ---------------- */

	public static void toast(String text, boolean on) {
		if (mc.player == null) {
			return;
		}

		mc.player.displayClientMessage(
				Component.literal("[Iekei] ").withStyle(ChatFormatting.DARK_GRAY)
						.append(Component.literal(text).withStyle(on ? ChatFormatting.GREEN : ChatFormatting.GRAY)),
				true);
	}

	public static void chat(String text) {
		if (mc.player == null) {
			return;
		}

		mc.player.displayClientMessage(Component.literal("[Iekei] " + text), false);
	}

	public static void sound() {
		if (mc.player == null) {
			return;
		}

		var module = ModuleManager.byId("soundalert");
		float gain = module instanceof com.iekei.client.modules.notify.SoundAlert alert ? alert.gain() : 1.0F;
		mc.player.playSound(SoundEvents.NOTE_BLOCK_PLING.value(), gain, 1.6F);
	}

	/* ---------------- HUD alerts ---------------- */

	public static void hud(String text) {
		ALERTS.add(0, new Entry(text, System.currentTimeMillis()));

		while (ALERTS.size() > 6) {
			ALERTS.remove(ALERTS.size() - 1);
		}
	}

	/** Live alert stack, newest first, oldest expired entries pruned. */
	public static List<Entry> alerts() {
		long now = System.currentTimeMillis();
		ALERTS.removeIf(e -> now - e.createdAt() > 6000);
		return ALERTS;
	}

	public static final class Entry {
		private final String text;
		private final long createdAt;

		Entry(String text, long createdAt) {
			this.text = text;
			this.createdAt = createdAt;
		}

		public String text() {
			return text;
		}

		public long createdAt() {
			return createdAt;
		}

		/** 0-255, fades in fast and out over the last second. */
		public int alpha() {
			long age = System.currentTimeMillis() - createdAt;

			if (age < 120) {
				return (int) (255 * age / 120.0);
			}

			if (age > 5000) {
				return (int) Math.max(0, 255 * (6000 - age) / 1000.0);
			}

			return 255;
		}
	}

	/* ---------------- webhook ---------------- */

	public static void webhook(String content) {
		String url = Webhook.url();

		if (url.isBlank()) {
			return;
		}

		HttpRequest request = HttpRequest.newBuilder(URI.create(url))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(
						"{\"content\":\"" + content.replace("\"", "'") + "\"}"))
				.build();

		HTTP.sendAsync(request, HttpResponse.BodyHandlers.discarding());
	}

	/* ---------------- routing ---------------- */

	/** Fires every alert channel that has its module switched on. */
	public static void alert(String text) {
		hud(text);
		chat(text);

		if (active("soundalert")) {
			sound();
		}

		if (active("webhook")) {
			webhook("**[Iekei]** " + text);
		}

		if (active("autologalert")) {
			autoLog(text);
		}
	}

	private static boolean active(String id) {
		var module = ModuleManager.byId(id);
		return module != null && module.enabled();
	}

	public static void autoLog(String reason) {
		if (mc.getConnection() == null) {
			return;
		}

		chat("AutoLog: " + reason);
		mc.getConnection().getConnection().disconnect(Component.literal("[Iekei] " + reason));
	}
}