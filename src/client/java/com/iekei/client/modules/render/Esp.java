package com.iekei.client.modules.render;

import com.iekei.client.core.Colors;
import com.iekei.client.core.Render3D;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public final class Esp extends Module {
	private static final Setting style = Setting.choice("Style", "Outline", "Skeleton", "Box");
	private static final Setting color = Setting.choice("Color", "Accent", "Red", "Cyan", "Gold");
	private static final Setting range = Setting.slider("Range", 64, 8, 256, 8, "b");

	public Esp() {
		super("esp", "ESP", "Highlights entities through walls.", true, style, color, range);
	}

	@Override
	public void onRenderWorld(WorldRenderContext ctx) {
		if (mc.player == null || mc.level == null) {
			return;
		}

		var buf = Render3D.lines(ctx);
		int rgb = Colors.of(mode("Color"));

		Render3D.throughWalls(buf, () -> {
			for (Entity e : mc.level.entitiesForRendering()) {
				if (e == mc.player || !(e instanceof LivingEntity)) {
					continue;
				}

				if (mc.player.distanceTo(e) > num("Range")) {
					continue;
				}

				Render3D.box(ctx.matrixStack(), buf, e.getBoundingBox(), rgb, 0.85F);
			}
		});

		ctx.consumers().endBatch(net.minecraft.client.renderer.RenderType.lines());
	}
}