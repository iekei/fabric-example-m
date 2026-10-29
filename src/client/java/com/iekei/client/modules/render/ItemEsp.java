package com.iekei.client.modules.render;

import com.iekei.client.core.Colors;
import com.iekei.client.core.Render3D;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.world.entity.item.ItemEntity;

public final class ItemEsp extends Module {
	private static final Setting enabledToggle = Setting.toggle("Enabled", true);

	public ItemEsp() {
		super("itemesp", "ItemESP", "Highlights dropped items on the ground.", false, enabledToggle);
	}

	@Override
	public void onRenderWorld(WorldRenderContext ctx) {
		if (mc.player == null || mc.level == null || !bool("Enabled")) {
			return;
		}

		var buf = Render3D.lines(ctx);
		int rgb = Colors.of("Gold");

		Render3D.throughWalls(buf, () -> {
			for (var e : mc.level.entitiesForRendering()) {
				if (!(e instanceof ItemEntity) || mc.player.distanceTo(e) > 48.0) {
					continue;
				}

				Render3D.box(ctx.matrixStack(), buf, e.getBoundingBox(), rgb, 0.8F);
			}
		});

		ctx.consumers().endBatch(net.minecraft.client.renderer.RenderType.lines());
	}
}