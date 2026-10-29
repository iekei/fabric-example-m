package com.iekei.client.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

/** Persists module states and setting values to config/iekeiclient.json. */
public final class Config {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("iekeiclient.json");
	private static long dirtyAt;

	private Config() {
	}

	public static void requestSave() {
		dirtyAt = System.currentTimeMillis();
	}

	/** Called every client tick; writes at most once per second after a change. */
	public static void tickAutosave() {
		if (dirtyAt == 0 || System.currentTimeMillis() - dirtyAt < 800) {
			return;
		}

		dirtyAt = 0;
		save();
	}

	public static void save() {
		JsonObject root = new JsonObject();

		for (Module m : ModuleManager.all()) {
			JsonObject entry = new JsonObject();
			entry.addProperty("enabled", m.enabled());

			JsonObject settings = new JsonObject();

			for (Setting s : m.settings()) {
				switch (s.type()) {
					case TOGGLE -> settings.addProperty(s.label(), s.getBool());
					case SLIDER -> settings.addProperty(s.label(), s.getNumber());
					case CHOICE -> settings.addProperty(s.label(), s.getChoice());
					case KEYBIND -> settings.addProperty(s.label(), s.getKey());
				}
			}

			entry.add("settings", settings);
			root.add(m.id(), entry);
		}

		try {
			Files.createDirectories(FILE.getParent());
			Files.writeString(FILE, GSON.toJson(root));
		} catch (Exception e) {
			IekeiLog.error("config save", e);
		}
	}

	public static void load() {
		if (!Files.exists(FILE)) {
			return;
		}

		try {
			JsonObject root = JsonParser.parseString(Files.readString(FILE)).getAsJsonObject();

			for (Module m : ModuleManager.all()) {
				if (!root.has(m.id())) {
					continue;
				}

				JsonObject entry = root.getAsJsonObject(m.id());
				JsonObject settings = entry.getAsJsonObject("settings");

				for (Setting s : m.settings()) {
					if (!settings.has(s.label())) {
						continue;
					}

					switch (s.type()) {
						case TOGGLE -> s.setBool(settings.get(s.label()).getAsBoolean());
						case SLIDER -> s.setNumber(settings.get(s.label()).getAsDouble());
						case CHOICE -> s.setChoice(settings.get(s.label()).getAsInt());
						case KEYBIND -> s.setKey(settings.get(s.label()).getAsInt());
					}
				}

				// setEnabled fires onEnable, which some modules need for their initial state
				m.setEnabled(entry.get("enabled").getAsBoolean());
			}
		} catch (Exception e) {
			IekeiLog.error("config load", e);
		}
	}
}