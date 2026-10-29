package com.iekei.client.core;

import com.iekei.client.gui.ClickGuiScreen;
import com.iekei.client.gui.Module;
import net.minecraft.client.Minecraft;

/** Dot-commands: ".iekai <sub>". Never reaches the server. */
public final class ChatCommands {
	private static final Minecraft mc = Minecraft.getInstance();

	private ChatCommands() {
	}

	public static boolean onChat(String message) {
		if (!message.startsWith(".")) {
			return true;
		}

		String[] parts = message.substring(1).trim().split("\\s+");
		String root = parts[0].toLowerCase();

		if (!root.equals("iekai") && !root.equals("ie") && !root.equals("i")) {
			return true;
		}

		if (parts.length < 2) {
			help();
			return false;
		}

		switch (parts[1].toLowerCase()) {
			case "webhook" -> webhook(parts);
			case "toggle" -> toggle(parts);
			case "bind" -> bind(parts);
			case "save" -> {
				Config.save();
				Notify.chat("Config saved");
			}
			case "reload" -> {
				Config.load();
				Notify.chat("Config reloaded");
			}
			case "gui" -> {
				if (mc.gui.screen() == null) {
					mc.gui.setScreen(new ClickGuiScreen());
				}
			}
			case "list" -> list();
			default -> help();
            
            case "home" -> {
				if (parts.length >= 3 && parts[2].equalsIgnoreCase("set") && mc.player != null) {
					com.iekei.client.modules.nav.ReturnNavigator.setHome(mc.player.blockPosition());
				} else {
					Notify.chat("Usage: .iekai home set");
				}
			}
			case "danger" -> {
				if (parts.length < 3) {
					Notify.chat("Listed: " + String.join(", ", DangerList.names()));
					return false;
				}

				if (parts[2].equalsIgnoreCase("clear")) {
					DangerList.names().clear();
					DangerList.save();
				} else {
					DangerList.add(parts[2]);
				}

				Notify.chat("Danger list updated");
			}
            
		}

		return false;
	}

	private static void help() {
		Notify.chat("Commands:");
		Notify.chat(" .iekai webhook <url|clear>");
		Notify.chat(" .iekai toggle <module id>");
		Notify.chat(" .iekai bind <module id> <scancode|clear>");
		Notify.chat(" .iekai list / save / reload / gui");
	}

	private static void webhook(String[] parts) {
		if (parts.length < 3) {
			Notify.chat("Current webhook: " + (Webhook.url().isBlank() ? "none" : Webhook.url()));
			return;
		}

		String value = parts[2].equalsIgnoreCase("clear") ? "" : parts[2];
		Webhook.save(value);
		Notify.chat(value.isBlank() ? "Webhook cleared" : "Webhook set");
	}

	private static void toggle(String[] parts) {
		if (parts.length < 3) {
			Notify.chat("Usage: .iekai toggle <module id>");
			return;
		}

		Module module = ModuleManager.byId(parts[2].toLowerCase());

		if (module == null) {
			Notify.chat("Unknown module: " + parts[2]);
			return;
		}

		module.toggleEnabled();
	}

	private static void bind(String[] parts) {
		if (parts.length < 4) {
			Notify.chat("Usage: .iekai bind <module id> <scancode|clear>");
			return;
		}

		Module module = ModuleManager.byId(parts[2].toLowerCase());

		if (module == null) {
			Notify.chat("Unknown module: " + parts[2]);
			return;
		}

		if (parts[3].equalsIgnoreCase("clear")) {
			module.keybind().setKey(0);
			Notify.chat(module.name() + " unbound");
			return;
		}

		try {
			module.keybind().setKey(Integer.parseInt(parts[3]));
			Notify.chat(module.name() + " bound to SC" + parts[3]);
		} catch (NumberFormatException e) {
			Notify.chat("Scancode must be a number");
		}
	}

	private static void list() {
		for (Module m : ModuleManager.all()) {
			Notify.chat((m.enabled() ? "[ON] " : "[--] ") + m.id());
		}
	}
}