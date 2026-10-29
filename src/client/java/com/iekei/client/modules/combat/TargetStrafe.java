package com.iekei.client.modules.combat;

import com.iekei.client.core.ModuleManager;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import java.util.Random;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public final class TargetStrafe extends Module {
	private final Setting radius = Setting.slider("Radius", 3.0, 1.0, 6.0, 0.1, "b");
	private final Setting direction = Setting.choice("Direction", "Clockwise", "Counter", "Random");
	private final Random random = new Random();
	private int sign = 1;

	public TargetStrafe() {
		super("targetstrafe", "TargetStrafe", "Circles around your current target.", false, radius, direction);
	}

	@Override
	public void onTick() {
		LocalPlayer p = mc.player;
		Killaura aura = ModuleManager.get(Killaura.class);

		if (p == null || aura == null || !aura.enabled()) {
			return;
		}

		Entity target = aura.target();

		if (target == null) {
			return;
		}

		double r = num("Radius");
		Vec3 offset = p.position().subtract(target.position());

		if (offset.lengthSqr() < 0.01) {
			offset = new Vec3(1.0, 0.0, 0.0);
		}

		if (offset.length() > r + 0.4) {
			Vec3 approach = offset.normalize().scale(0.2);
			p.setDeltaMovement(approach.x, p.getDeltaMovement().y, approach.z);
			return;
		}

		if (modeIs("Direction", "Random") && random.nextInt(40) == 0) {
			sign = -sign;
		} else if (modeIs("Direction", "Counter")) {
			sign = -1;
		} else if (modeIs("Direction", "Clockwise")) {
			sign = 1;
		}

		Vec3 tangent = new Vec3(-offset.z, 0.0, offset.x).normalize().scale(0.24 * sign);
		p.setDeltaMovement(tangent.x, p.getDeltaMovement().y, tangent.z);
	}
}