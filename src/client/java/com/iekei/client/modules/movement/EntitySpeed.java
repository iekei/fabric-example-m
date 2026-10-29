package com.iekei.client.modules.movement;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;

public final class EntitySpeed extends Module {
	private final Setting speed = Setting.slider("Speed", 2.5, 1.0, 8.0, 0.1, "x");

	public EntitySpeed() {
		super("entityspeed", "EntitySpeed", "Speeds up boats, horses and minecarts.", false, speed);
	}

	@Override
	public void onTick() {
		LocalPlayer p = mc.player;

		if (p == null) {
			return;
		}

		Entity vehicle = p.getVehicle();

		if (vehicle == null) {
			return;
		}

		double factor = num("Speed");

		if (factor <= 1.0) {
			return;
		}

		vehicle.setDeltaMovement(
				vehicle.getDeltaMovement().x * factor,
				vehicle.getDeltaMovement().y,
				vehicle.getDeltaMovement().z * factor);
	}
}