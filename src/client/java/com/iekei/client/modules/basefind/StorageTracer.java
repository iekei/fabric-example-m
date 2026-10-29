package com.iekei.client.modules.chunkutil;

import com.iekei.client.core.Render3D;
import com.iekei.client.core.Waypoints;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.world.phys.Vec3;

public final class StorageTracer extends Module {
	private static final Setting maxLines = Setting.slider("Max Lines", 12, 1, 48, 1, "");
	private static final Setting range = Setting.slider("Range", 128, 16, 512, 16, "b");

	public StorageTracer() {
		super("storagetracer", "StorageTracer", "Draws lines to known containers.", false, maxLines, range);
	}

	@Override
	public void onRenderWorld(WorldRenderContext ctx) {
		if (mc.player == null) {
			return;
		}

		var buf = Render3D.lines(ctx);
		Vec3 from = mc.player.getEyePosition();
		var targets = Waypoints.ofKind("container").stream()
				.filter(p -> p.pos().getCenter().distanceTo(from) <= num("Range"))
				.sorted((a, b) -> Double.compare(a.pos().getCenter().distanceToSqr(from),
						b.pos().getCenter().distanceToSqr(from)))
				.limit(numInt("Max Lines"))
				.toList();

		Render3D.throughWalls(buf, () -> {
			for (var point : targets) {
				Render3D.line(ctx.matrixStack(), buf, from, point.pos().getCenter(), 0xFFC144, 0.7F);
			}
		});

		ctx.consumers().endBatch(net.minecraft.client.renderer.RenderType.lines());
	}
}