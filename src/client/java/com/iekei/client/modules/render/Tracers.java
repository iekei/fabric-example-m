package com.iekei.client.modules.render;

import com.iekei.client.core.Colors;
import com.iekei.client.core.Render3D;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class Tracers extends Module {
	private static final Setting color = Setting.choice("Color", "Accent", "Blue", "Lime", "White");
	private static final Setting thickness = Setting.slider("Thickness", 2, 1, 5, 0.5, "px");

	public Tracers() {
		super("tracers", "Tracers", "Draws lines from you to targets.", false, color, thickness);
	}

	@Override
	public void onRenderWorld(WorldRenderContext ctx) {
		if (mc.player == null || mc.level == null) {
			return;
		}

		var buf = Render3D.lines(ctx);
		int rgb = Colors.of(mode("Color"));
		Vec3 from = mc.player.getEyePosition();

		Render3D.throughWalls(buf, () -> {
			for (Entity e : mc.level.entitiesForRendering()) {
				if (e == mc.player || !(e instanceof LivingEntity) || mc.player.distanceTo(e) > 128.0) {
					continue;
				}

				Render3D.line(ctx.matrixStack(), buf, from,
						e.position().add(0.0, e.getBbHeight() / 2.0, 0.0), rgb, 0.9F);
			}
		});

		ctx.consumers().endBatch(net.minecraft.client.renderer.RenderType.lines());
	}
}