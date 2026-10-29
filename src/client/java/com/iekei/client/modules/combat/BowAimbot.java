package com.iekei.client.modules.combat;

import com.iekei.client.core.Rotation;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class BowAimbot extends Module {
	private final Setting prediction = Setting.slider("Prediction", 0.8, 0.0, 2.0, 0.05, "x");

	public BowAimbot() {
		super("bowaimbot", "BowAimbot", "Predicts and aims bow shots for you.", false, prediction);
	}

	@Override
	public void onTick() {
		LocalPlayer p = mc.player;

		if (p == null || mc.level == null || !p.isUsingItem()) {
			return;
		}

		Entity best = mc.level.getEntities(p, p.getBoundingBox().inflate(40.0)).stream()
				.filter(e -> e instanceof LivingEntity && e != p && e.isAlive())
				.min((a, b) -> Double.compare(p.distanceToSqr(a), p.distanceToSqr(b)))
				.orElse(null);

		if (best == null) {
			return;
		}

		double ticks = p.distanceTo(best) * num("Prediction");
		Vec3 aim = best.position()
				.add(best.getDeltaMovement().scale(ticks))
				.add(0.0, best.getBbHeight() * 0.6, 0.0);

		// crude gravity compensation for the arrow drop
		double dist = p.getEyePosition().distanceTo(aim);
		aim = aim.add(0.0, dist * 0.06, 0.0);

		Rotation.snap(aim);
	}
}