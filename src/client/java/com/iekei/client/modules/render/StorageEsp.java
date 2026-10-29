package com.iekei.client.modules.render;

import com.iekei.client.core.Colors;
import com.iekei.client.core.Render3D;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;

public final class StorageEsp extends Module {
	private static final Setting color = Setting.choice("Color", "Amber", "Accent", "White");
	private static final Setting range = Setting.slider("Range", 48, 8, 128, 8, "b");

	public StorageEsp() {
		super("storageesp", "StorageESP", "Highlights chests and shulkers.", false, color, range);
	}

	@Override
	public void onRenderWorld(WorldRenderContext ctx) {
		if (mc.player == null || mc.level == null) {
			return;
		}

		var buf = Render3D.lines(ctx);
		int rgb = Colors.of(mode("Color"));
		int chunkRadius = Math.max(1, (int) num("Range") / 16);

		Render3D.throughWalls(buf, () -> {
			var origin = mc.player.chunkPosition();

			for (int cx = -chunkRadius; cx <= chunkRadius; cx++) {
				for (int cz = -chunkRadius; cz <= chunkRadius; cz++) {
					var chunk = mc.level.getChunkSource().getChunk(origin.x + cx, origin.z + cz, false);

					if (chunk == null) {
						continue;
					}

					for (BlockEntity be : chunk.getBlockEntities().values()) {
						if (!(be instanceof ChestBlockEntity) && !(be instanceof ShulkerBoxBlockEntity)) {
							continue;
						}

						if (mc.player.distanceToSqr(be.getBlockPos().getCenter()) > Math.pow(num("Range"), 2)) {
							continue;
						}

						Render3D.box(ctx.matrixStack(), buf,
								new net.minecraft.world.phys.AABB(be.getBlockPos()).inflate(0.05), rgb, 0.8F);
					}
				}
			}
		});

		ctx.consumers().endBatch(net.minecraft.client.renderer.RenderType.lines());
	}
}