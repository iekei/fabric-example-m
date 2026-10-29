package com.iekei.client.core;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import net.fabricmc.loader.api.FabricLoader;

/** Persistent name list used by AutoLogOnDanger. */
public final class DangerList {
	private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("iekeiclient-danger.txt");
	private static final Set<String> NAMES = new HashSet<>();

	private DangerList() {
	}

	public static void load() {
		try {
			if (!Files.exists(FILE)) {
				return;
			}

			for (String line : Files.readAllLines(FILE)) {
				if (!line.isBlank()) {
					NAMES.add(line.trim().toLowerCase(Locale.ROOT));
				}
			}
		} catch (Exception e) {
			IekeiLog.error("danger load", e);
		}
	}

	public static void save() {
		try {
			Files.createDirectories(FILE.getParent());
			Files.write(FILE, NAMES);
		} catch (Exception e) {
			IekeiLog.error("danger save", e);
		}
	}

	public static void add(String name) {
		NAMES.add(name.toLowerCase(Locale.ROOT));
		save();
	}

	public static void remove(String name) {
		NAMES.remove(name.toLowerCase(Locale.ROOT));
		save();
	}

	public static Set<String> names() {
		return NAMES;
	}

	public static boolean matches(String name) {
		return NAMES.contains(name.toLowerCase(Locale.ROOT));
	}
}