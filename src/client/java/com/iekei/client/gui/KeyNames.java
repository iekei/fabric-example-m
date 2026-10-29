package com.iekei.client.gui;

/** Human readable names for the handful of scancodes people actually bind. */
public final class KeyNames {
	private KeyNames() {
	}

	public static String of(int scancode) {
		return switch (scancode) {
			case 0 -> "NONE";
			case 41 -> "ESCAPE";
			case 43 -> "TAB";
			case 44 -> "SPACE";
			case 224 -> "LCTRL";
			case 225 -> "LSHIFT";
			case 226 -> "LALT";
			case 228 -> "RCTRL";
			case 229 -> "RSHIFT";
			default -> {
				if (scancode >= 4 && scancode <= 29) {
					yield String.valueOf((char) ('A' + scancode - 4));
				}

				if (scancode >= 30 && scancode <= 38) {
					yield String.valueOf((char) ('1' + scancode - 30));
				}

				yield "SC" + scancode;
			}
		};
	}
}