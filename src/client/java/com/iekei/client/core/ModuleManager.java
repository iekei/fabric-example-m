package com.iekei.client.core;

import com.iekei.client.gui.Category;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Registry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import com.iekei.client.modules.settings.SoundEffects;
import net.minecraft.sounds.SoundEvents;

/** Owns the live module list: tick dispatch, keybinds and config wiring. */
public final class ModuleManager {
	private static final List<Module> MODULES = new ArrayList<>();
	private static final Set<Integer> HELD = new HashSet<>();
	private static boolean built;

	private ModuleManager() {
	}

	private static void build() {
		if (built) {
			return;
		}

		built = true;

		for (Category c : Registry.categories()) {
			MODULES.addAll(c.modules());
		}

		Config.load();
	}

	public static List<Module> all() {
		build();
		return MODULES;
	}

	public static Module byId(String id) {
		for (Module m : all()) {
			if (m.id().equals(id)) {
				return m;
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Module> T get(Class<T> type) {
		for (Module m : all()) {
			if (type.isInstance(m)) {
				return (T) m;
			}
		}

		return null;
	}

	/** True when the module exists and is switched on. */
	public static boolean active(Class<? extends Module> type) {
		Module m = get(type);
		return m != null && m.enabled();
	}

public static void onModuleEnabled(Module m) {
		Notify.toast(m.name() + " ON", true);
		click();
	}

	public static void onModuleDisabled(Module m) {
		Notify.toast(m.name() + " OFF", false);
		click();
	}

	private static void click() {
		SoundEffects sound = get(SoundEffects.class);
		var player = Minecraft.getInstance().player;

		if (sound == null || !sound.audible() || player == null) {
			return;
		}

		player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), sound.gain(), 1.4F);
	}
	/* ---------------- tick ---------------- */

	public static void tick(Minecraft mc) {
		if (mc.player == null || mc.level == null) {
			return;
		}

		handleKeybinds(mc);

		for (Module m : all()) {
			if (!m.enabled()) {
				continue;
			}

			try {
				m.onTick();
			} catch (Exception e) {
				IekeiLog.error(m.name(), e);
			}
		}
	}

	private static void handleKeybinds(Minecraft mc) {
		if (mc.gui.screen() instanceof ChatScreen) {
			HELD.clear();
			return;
		}

		long handle = mc.getWindow().getWindow();

		for (Module m : all()) {
			int key = m.key();

			if (key <= 0) {
				continue;
			}

			boolean down = Input.isDown(handle, key);

			if (down && HELD.add(key)) {
				m.toggleEnabled();
			} else if (!down) {
				HELD.remove(key);
			}
		}
	}
}