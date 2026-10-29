package com.iekei.client.modules.render;

import com.iekei.client.core.Colors;
import com.iekei.client.core.Render3D;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.world.entity.LivingEntity;

public final class Nametags extends Module {
	private static final Setting size = Setting.slider("Size", 1.4, 0.5, 3.0, 0.1, "x");
	private static final Setting showHealth = Setting.toggle("Show Health", true);

	public Nametags() {
		super("nametags", "Nametags", "Enlarges and restyles nametags.", false, size, showHealth);
	}

	@Override
	public void onRenderWorld(WorldRenderContext ctx) {
		if (mc.player == null || mc.level == null) {
			return;
		}

		var buf = Render3D.lines(ctx);
		double scale = num("Size");

		Render3D.throughWalls(buf, () -> {
			for (var e : mc.level.entitiesForRendering()) {
				if (e == mc.player || !(e instanceof LivingEntity living) || mc.player.distanceTo(e) > 64.0) {
					continue;
				}

				var box = e.getBoundingBox();
				double height = box.maxY + 0.35;
				double half = 0.35 * scale;
				int rgb = bool("ShowHealth")
						? (living.getHealth() / living.getMaxHealth() > 0.5F ? 0x55FF66 : 0xFF5555)
						: Colors.of("White");

				Render3D.line(ctx.matrixStack(), buf,
						new net.minecraft.world.phys.Vec3(e.getX() - half, height, e.getZ()),
						new net.minecraft.world.phys.Vec3(e.getX() + half, height, e.getZ()), rgb, 0.9F);
			}
		});

		ctx.consumers().endBatch(net.minecraft.client.renderer.RenderType.lines());
	}
}