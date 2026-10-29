package com.iekei.client.modules.pvp;

import com.iekei.client.core.Alerts;
import com.iekei.client.core.Render3D;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class PearlPredictor extends Module {
	private static final Setting showBox = Setting.toggle("Show Box", true);
	private static final Setting alert = Setting.toggle("Alert", false);
	private final Map<UUID, Vec3> landings = new HashMap<>();

	public PearlPredictor() {
		super("pearlpredictor", "PearlPredictor", "Predicts ender pearl landing spots.", false,
				showBox, alert);
	}

	@Override
	public void onTick() {
		if (mc.level == null) {
			return;
		}

		for (var entity : mc.level.entitiesForRendering()) {
			if (!(entity instanceof ThrownEnderpearl pearl) || landings.containsKey(pearl.getUUID())) {
				continue;
			}

			Vec3 landing = simulate(pearl);

			if (landing != null) {
				landings.put(pearl.getUUID(), landing);

				if (bool("Alert")) {
					Alerts.found("Pearl landing", (int) landing.x + ", " + (int) landing.y + ", " + (int) landing.z);
				}
			}
		}
	}

	private Vec3 simulate(ThrownEnderpearl pearl) {
		Vec3 pos = pearl.position();
		Vec3 velocity = pearl.getDeltaMovement();

		for (int i = 0; i < 200; i++) {
			pos = pos.add(velocity);
			velocity = velocity.scale(0.99).add(0.0, -0.03, 0.0);

			if (mc.level == null || mc.level.getBlockState(net.minecraft.core.BlockPos.containing(pos)).isSolid()) {
				return pos;
			}
		}

		return null;
	}

	@Override
	public void onRenderWorld(WorldRenderContext ctx) {
		if (landings.isEmpty() || !bool("Show Box")) {
			return;
		}

		var buf = Render3D.lines(ctx);

		Render3D.throughWalls(buf, () -> {
			for (Vec3 pos : landings.values()) {
				Render3D.box(ctx.matrixStack(), buf,
						new AABB(pos.x - 0.4, pos.y - 0.1, pos.z - 0.4, pos.x + 0.4, pos.y + 1.6, pos.z + 0.4),
						0xC792EA, 0.9F);
			}
		});

		ctx.consumers().endBatch(net.minecraft.client.renderer.RenderType.lines());
	}
}