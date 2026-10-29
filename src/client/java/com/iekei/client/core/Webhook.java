package com.iekei.client.core;

import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

/** Persists the Discord webhook URL next to the config file. */
public final class Webhook {
	private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("iekeiclient-webhook.txt");
	private static String url = "";

	private Webhook() {
	}

	public static String url() {
		return url;
	}

	public static void load() {
		try {
			if (Files.exists(FILE)) {
				url = Files.readString(FILE).trim();
			}
		} catch (Exception e) {
			IekeiLog.error("webhook load", e);
		}
	}

	public static void save(String value) {
		url = value == null ? "" : value.trim();

		try {
			Files.createDirectories(FILE.getParent());

			if (url.isBlank()) {
				Files.deleteIfExists(FILE);
			} else {
				Files.writeString(FILE, url);
			}
		} catch (Exception e) {
			IekeiLog.error("webhook save", e);
		}
	}
}