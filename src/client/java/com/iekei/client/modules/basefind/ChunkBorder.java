package com.iekei.client.modules.chunkutil;

import com.iekei.client.core.Render3D;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.world.phys.Vec3;

public final class ChunkBorder extends Module {
	private static final Setting radius = Setting.slider("Radius", 4, 1, 12, 1, "c");
	private static final Setting height = Setting.slider("Height", 40, 8, 128, 8, "b");

	public ChunkBorder() {
		super("chunkborder", "ChunkBorder", "Draws chunk boundary lines.", false, radius, height);
	}

	@Override
	public void onRenderWorld(WorldRenderContext ctx) {
		if (mc.player == null) {
			return;
		}

		var buf = Render3D.lines(ctx);
		int chunkX = mc.player.chunkPosition().x;
		int chunkZ = mc.player.chunkPosition().z;
		int r = numInt("Radius");
		int yTop = (int) (mc.player.getY() + num("Height"));
		int yBottom = (int) (mc.player.getY() - num("Height"));

		Render3D.throughWalls(buf, () -> {
			for (int i = -r; i <= r; i++) {
				float x = (chunkX + i) * 16.0F;
				float z = (chunkZ + i) * 16.0F;
				Render3D.line(ctx.matrixStack(), buf, new Vec3(x, yBottom, (chunkZ - r) * 16.0),
						new Vec3(x, yTop, (chunkZ + r + 1) * 16.0), 0x66CCFF, 0.4F);
				Render3D.line(ctx.matrixStack(), buf, new Vec3((chunkX - r) * 16.0, yBottom, z),
						new Vec3((chunkX + r + 1) * 16.0, yTop, z), 0x66CCFF, 0.4F);
			}
		});

		ctx.consumers().endBatch(net.minecraft.client.renderer.RenderType.lines());
	}
}