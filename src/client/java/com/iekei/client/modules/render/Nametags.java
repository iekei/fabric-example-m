package com.iekei.client.modules.render;

import com.iekei.client.gui.Draw;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public final class Nametags extends Module {
	private static final Setting size = Setting.slider("Size", 1.4, 0.5, 3.0, 0.1, "x");
	private static final Setting showHealth = Setting.toggle("Show Health", true);
	private static final Setting range = Setting.slider("Range", 64, 8, 256, 8, "b");
	private static final Setting scaleWithDistance = Setting.toggle("Scale With Distance", true);
	private final List<Tag> tags = new ArrayList<>();

	public Nametags() {
		super("nametags", "Nametags", "Enlarges and restyles nametags.", false,
				size, showHealth, range, scaleWithDistance);
	}

	@Override
	public void onRenderWorld(WorldRenderContext ctx) {
		tags.clear();

		if (mc.player == null || mc.level == null) {
			return;
		}

		Matrix4f view = ctx.matrixStack().last().pose();
		Matrix4f projection = mc.gameRenderer.getProjectionMatrix(mc.options.fov().get().floatValue());
		int width = mc.getWindow().getGuiScaledWidth();
		int height = mc.getWindow().getGuiScaledHeight();

		for (var entity : mc.level.entitiesForRendering()) {
			if (entity == mc.player || !(entity instanceof LivingEntity living)) {
				continue;
			}

			double distance = mc.player.distanceTo(entity);

			if (distance > num("Range")) {
				continue;
			}

			Vector4f point = new Vector4f(
					(float) entity.getX(),
					(float) (entity.getY() + entity.getBbHeight() + 0.55),
					(float) entity.getZ(),
					1.0F);
			view.transform(point);
			projection.transform(point);

			if (point.w <= 0.001F) {
				continue;
			}

			float ndcX = point.x / point.w;
			float ndcY = point.y / point.w;

			if (Math.abs(ndcX) > 1.25F || Math.abs(ndcY) > 1.25F) {
				continue;
			}

			int x = Math.round((ndcX * 0.5F + 0.5F) * width);
			int y = Math.round((1.0F - (ndcY * 0.5F + 0.5F)) * height);
			tags.add(new Tag(living, x, y, (float) distance));
		}

		tags.sort(Comparator.comparingDouble((Tag t) -> t.distance).reversed());
	}

	@Override
	public void onRenderHud(GuiGraphicsExtractor g, float delta) {
		if (tags.isEmpty()) {
			return;
		}

		for (Tag tag : tags) {
			float shrink = bool("Scale With Distance")
					? (float) Math.max(0.55, 1.0 - tag.distance / (num("Range") * 1.6))
					: 1.0F;
			float scale = (float) num("Size") * shrink;
			String name = tag.entity.getName().getString();
			String health = Math.round(tag.entity.getHealth()) + " hp";

			int nameWidth = Draw.width(name);
			int boxWidth = Math.round(Math.max(nameWidth, Draw.width(health)) * scale) + 6;
			int boxHeight = Math.round((bool("Show Health") ? 18 : 10) * scale);
			int x = tag.x - boxWidth / 2;
			int y = tag.y - boxHeight;

			g.fill(x, y, x + boxWidth, y + boxHeight, 0xB0101018);
			Draw.text(g, name, x + 3, y + 2, 0xFFFFFFFF);

			if (bool("Show Health")) {
				float ratio = tag.entity.getHealth() / tag.entity.getMaxHealth();
				int barColor = ratio > 0.5F ? 0xFF55FF66 : (ratio > 0.25F ? 0xFFFFC144 : 0xFFFF5555);
				Draw.text(g, health, x + 3, y + 11, 0xFFBBBBCC);
				g.fill(x + 3, y + boxHeight - 2, x + boxWidth - 3, y + boxHeight - 1, barColor);
			}
		}
	}

	private static final class Tag {
		private final LivingEntity entity;
		private final int x;
		private final int y;
		private final float distance;

		private Tag(LivingEntity entity, int x, int y, float distance) {
			this.entity = entity;
			this.x = x;
			this.y = y;
			this.distance = distance;
		}
	}
}