package com.iekei.client.modules.combat;

import com.iekei.client.core.ModuleManager;
import com.iekei.client.core.Rotation;
import com.iekei.client.core.Timer;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import java.util.Comparator;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;

public final class Killaura extends Module {
	private final Setting range = Setting.slider("Range", 4.2, 2.5, 7.0, 0.1, "b");
	private final Setting aps = Setting.slider("APS", 9.5, 1.0, 20.0, 0.5, "/s");
	private final Setting priority = Setting.choice("Priority", "Closest", "Lowest HP", "Angle");
	private final Setting targetPlayers = Setting.toggle("Target Players", true);
	private final Timer cooldown = new Timer();

	private Entity target;

	public Killaura() {
		super("killaura", "Killaura", "Attacks nearby entities automatically.", true,
				range, aps, priority, targetPlayers);
	}

	public Entity target() {
		return target;
	}

	@Override
	public void onTick() {
		LocalPlayer p = mc.player;

		if (p == null || mc.level == null) {
			return;
		}

		target = pick(p);

		if (target == null) {
			return;
		}

		Rotation.snap(target.position().add(0.0, target.getBbHeight() / 2.0, 0.0));

		if (!cooldown.every(1000.0 / Math.max(1.0, num("APS")))) {
			return;
		}

		Criticals criticals = ModuleManager.get(Criticals.class);

		if (criticals != null && criticals.enabled()) {
			criticals.prepareAttack();
		}

		mc.gameMode.attack(p, target);
		p.swing(InteractionHand.MAIN_HAND);
	}

	private Entity pick(LocalPlayer p) {
		double max = num("Range");

		return mc.level.getEntities(p, p.getBoundingBox().inflate(max, max, max)).stream()
				.filter(e -> e != p && e.isAlive() && valid(e))
				.filter(e -> p.distanceTo(e) <= max)
				.sorted(comparator(p))
				.findFirst()
				.orElse(null);
	}

	private boolean valid(Entity e) {
		if (e instanceof Player player) {
			return bool("Target Players") && !player.isCreative() && !player.isSpectator();
		}

		return e instanceof Enemy;
	}

	private Comparator<Entity> comparator(LocalPlayer p) {
		return switch (mode("Priority")) {
			case "Lowest HP" -> Comparator.comparingDouble(e -> e instanceof LivingEntity le ? le.getHealth() : 0.0);
			case "Angle" -> Comparator.comparingDouble(e -> {
				float[] rot = Rotation.toward(Rotation.eye(), e.position());
				return Math.abs(Rotation.wrap(rot[0] - p.getYRot()));
			});
			default -> Comparator.comparingDouble(p::distanceToSqr);
		};
	}
}