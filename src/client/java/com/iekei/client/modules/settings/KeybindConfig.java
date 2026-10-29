package com.iekei.client.modules.settings;

import com.iekei.client.IekeiClientClient;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.sdl.SDLScancode;

public final class KeybindConfig extends Module {
	private static final Setting toggleKey = Setting.choice("Menu Toggle Key",
			"Right Shift", "Right Ctrl", "Grave");
	private int applied = -1;

	public KeybindConfig() {
		super("keybind", "Keybind Config", "The key that opens and closes this menu.", true, toggleKey);
	}

	@Override
	public void onTick() {
		int choice = toggleKey.getChoice();

		if (choice == applied || IekeiClientClient.openGuiKey == null) {
			return;
		}

		applied = choice;
		int scancode = switch (choice) {
			case 1 -> SDLScancode.SDL_SCANCODE_RCTRL;
			case 2 -> SDLScancode.SDL_SCANCODE_GRAVE;
			default -> SDLScancode.SDL_SCANCODE_RSHIFT;
		};

		IekeiClientClient.openGuiKey.setKey(InputConstants.Type.KEYBOARD.getOrCreate(scancode));
	}
}