package com.iekei.client.modules.cosmetic;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.world.phys.Vec3;

public final class CustomSky extends Module {
	private static final Setting skybox = Setting.choice("Skybox", "Galaxy", "Sunset", "Nebula");

	public CustomSky() {
		super("customsky", "CustomSky", "Replaces the vanilla skybox texture.", false, skybox);
	}

	public Vec3 tint(Vec3 original) {
		return switch (mode("Skybox")) {
			case "Sunset" -> new Vec3(1.0, 0.55, 0.32);
			case "Nebula" -> new Vec3(0.55, 0.35, 1.0);
			default -> new Vec3(0.12, 0.12, 0.30);
		};
	}
}