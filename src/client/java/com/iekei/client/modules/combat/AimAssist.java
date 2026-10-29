package com.iekei.client.modules.combat;

import com.iekei.client.core.Rotation;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public final class AimAssist extends Module {
	private static final Setting smoothness = Setting.slider("Smoothness", 4.5, 1.0, 10.0, 0.1, "");
	private static final Setting fov = Setting.slider("FOV", 90, 30, 180, 5, "°");
	private static final Setting vertical = Setting.toggle("Vertical Assist", false);

	public AimAssist() {
		super("aimassist", "AimAssist", "Gently nudges your aim towards targets.", true,
				smoothness, fov, vertical);
	}

	@Override
	public void onTick() {
		LocalPlayer p = mc.player;

		if (p == null || mc.level == null) {
			return;
		}

		Entity best = mc.level.getEntities(p, p.getBoundingBox().inflate(6.0)).stream()
				.filter(e -> e instanceof LivingEntity && e != p && e.isAlive())
				.filter(e -> Rotation.inFov(e.position(), num("FOV")))
				.min((a, b) -> Double.compare(p.distanceToSqr(a), p.distanceToSqr(b)))
				.orElse(null);

		if (best == null) {
			return;
		}

		var aim = best.position().add(0.0, best.getBbHeight() * 0.7, 0.0);
		float keepPitch = p.getXRot();
		Rotation.smooth(aim, num("Smoothness"));

		if (!bool("Vertical Assist")) {
			p.setXRot(keepPitch);
		}
	}
}