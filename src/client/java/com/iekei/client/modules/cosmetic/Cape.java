package com.iekei.client.modules.cosmetic;

import com.iekei.client.core.Render3D;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class Cape extends Module {
	private static final Setting showCape = Setting.toggle("Show Cape", true);
	private static final Setting showWings = Setting.toggle("Show Wings", false);

	public Cape() {
		super("cape", "Cape / Cosmetics", "Shows capes and cosmetic accessories.", false,
				showCape, showWings);
	}

	@Override
	public void onRenderWorld(WorldRenderContext ctx) {
		if (mc.player == null) {
			return;
		}

		var buf = Render3D.lines(ctx);
		var p = mc.player;
		double yaw = Math.toRadians(p.getYRot());
		double back = 0.28;
		double ox = -Math.sin(yaw) * back;
		double oz = Math.cos(yaw) * back;
		double half = 0.22;
		double top = p.getY() + 1.45;
		double bottom = p.getY() + 0.85;

		Render3D.throughWalls(buf, () -> {
			if (bool("Show Cape")) {
				Render3D.box(ctx.matrixStack(), buf,
						new AABB(p.getX() + ox - half, bottom, p.getZ() + oz - 0.02,
								p.getX() + ox + half, top, p.getZ() + oz + 0.02),
						0xE0E0E0, 0.9F);
			}

			if (bool("Show Wings")) {
				for (int side = -1; side <= 1; side += 2) {
					Render3D.line(ctx.matrixStack(), buf,
							new Vec3(p.getX(), top - 0.15, p.getZ()),
							new Vec3(p.getX() + ox * 2 + side * 0.55, top + 0.25, p.getZ() + oz * 2),
							0xFFFFFF, 0.9F);
				}
			}
		});

		ctx.consumers().endBatch(net.minecraft.client.renderer.RenderType.lines());
	}
}