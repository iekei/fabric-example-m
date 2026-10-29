package com.iekei.client.core;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

public final class Notify {
	private static final Minecraft mc = Minecraft.getInstance();
	private static final Deque<String> HUD = new ArrayDeque<>();
	private static final Deque<Long> HUD_TIME = new ArrayDeque<>();
	private static final HttpClient HTTP = HttpClient.newHttpClient();
	private static String webhookUrl = "";

	private Notify() {
	}

	public static void setWebhook(String url) {
		webhookUrl = url;
	}

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

		mc.player.playSound(SoundEvents.NOTE_BLOCK_PLING.value(), 1.0F, 1.6F);
	}

	public static void hud(String text) {
		HUD.addLast(text);
		HUD_TIME.addLast(System.currentTimeMillis());

		while (HUD.size() > 6) {
			HUD.removeFirst();
			HUD_TIME.removeFirst();
		}
	}

	public static void webhook(String content) {
		if (webhookUrl.isBlank()) {
			return;
		}

		HttpRequest request = HttpRequest.newBuilder(URI.create(webhookUrl))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(
						"{\"content\":\"" + content.replace("\"", "'") + "\"}"))
				.build();

		HTTP.sendAsync(request, HttpResponse.BodyHandlers.discarding());
	}

	public static Deque<String> hudLines() {
		long now = System.currentTimeMillis();

		while (!HUD_TIME.isEmpty() && now - HUD_TIME.peekFirst() > 4000) {
			HUD.removeFirst();
			HUD_TIME.removeFirst();
		}

		return HUD;
	}

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

	private static void autoLog(String reason) {
		if (mc.getConnection() == null) {
			return;
		}

		mc.getConnection().getConnection().disconnect(Component.literal("[Iekei] " + reason));
	}
}