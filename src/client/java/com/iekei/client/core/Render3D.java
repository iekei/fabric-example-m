package com.iekei.client.core;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

/** Immediate line drawing into the shared level buffer. */
public final class Render3D {
	private Render3D() {
	}

	public static void box(PoseStack ps, VertexConsumer buf, AABB b, int rgb, float alpha) {
		float x1 = (float) b.minX;
		float y1 = (float) b.minY;
		float z1 = (float) b.minZ;
		float x2 = (float) b.maxX;
		float y2 = (float) b.maxY;
		float z2 = (float) b.maxZ;

		edge(ps, buf, x1, y1, z1, x2, y1, z1, rgb, alpha);
		edge(ps, buf, x2, y1, z1, x2, y1, z2, rgb, alpha);
		edge(ps, buf, x2, y1, z2, x1, y1, z2, rgb, alpha);
		edge(ps, buf, x1, y1, z2, x1, y1, z1, rgb, alpha);
		edge(ps, buf, x1, y2, z1, x2, y2, z1, rgb, alpha);
		edge(ps, buf, x2, y2, z1, x2, y2, z2, rgb, alpha);
		edge(ps, buf, x2, y2, z2, x1, y2, z2, rgb, alpha);
		edge(ps, buf, x1, y2, z2, x1, y2, z1, rgb, alpha);
		edge(ps, buf, x1, y1, z1, x1, y2, z1, rgb, alpha);
		edge(ps, buf, x2, y1, z1, x2, y2, z1, rgb, alpha);
		edge(ps, buf, x2, y1, z2, x2, y2, z2, rgb, alpha);
		edge(ps, buf, x1, y1, z2, x1, y2, z2, rgb, alpha);
	}

	public static void line(PoseStack ps, VertexConsumer buf, Vec3 from, Vec3 to, int rgb, float alpha) {
		edge(ps, buf, (float) from.x, (float) from.y, (float) from.z,
				(float) to.x, (float) to.y, (float) to.z, rgb, alpha);
	}

	private static void edge(PoseStack ps, VertexConsumer buf, float x1, float y1, float z1,
			float x2, float y2, float z2, int rgb, float alpha) {
		float r = ((rgb >> 16) & 0xFF) / 255.0F;
		float g = ((rgb >> 8) & 0xFF) / 255.0F;
		float b = (rgb & 0xFF) / 255.0F;
		Matrix4f m = ps.last().pose();
		buf.addVertex(m, x1, y1, z1).setColor(r, g, b, alpha).setNormal(0.0F, 1.0F, 0.0F);
		buf.addVertex(m, x2, y2, z2).setColor(r, g, b, alpha).setNormal(0.0F, 1.0F, 0.0F);
	}

	/** Draws the given runnable with depth testing off, flushing the line batch. */
	public static void throughWalls(VertexConsumer buf, Runnable runnable) {
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		runnable.run();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
	}

	public static VertexConsumer lines(net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext context) {
		return context.consumers().getBuffer(RenderType.lines());
	}
}