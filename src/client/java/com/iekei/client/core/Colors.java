package com.iekei.client.core;

/** Named colour choices to packed RGB. */
public final class Colors {
	private Colors() {
	}

	public static int of(String name) {
		return switch (name.toLowerCase()) {
			case "red" -> 0xFF4444;
			case "cyan" -> 0x44E0FF;
			case "gold", "amber" -> 0xFFC144;
			case "blue" -> 0x4488FF;
			case "lime", "emerald" -> 0x55FF66;
			case "white" -> 0xFFFFFF;
			case "orange" -> 0xFF8A33;
			case "accent" -> 0xC792EA;
			default -> 0xFFFFFF;
		};
	}
}