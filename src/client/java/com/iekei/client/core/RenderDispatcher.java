package com.iekei.client.core;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Registry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;

/** Fans the world render event out to the enabled render modules. */
public final class RenderDispatcher {
	private RenderDispatcher() {
	}

	public static void onWorldRender(WorldRenderContext context) {
		if (context.consumers() == null || context.camera() == null) {
			return;
		}

		for (var category : Registry.categories()) {
			if (!category.id().equals("render") && !category.id().equals("basefind")) {
				continue;
			}

			for (Module m : category.modules()) {
				if (!m.enabled()) {
					continue;
				}

				try {
					m.onRenderWorld(context);
				} catch (Exception e) {
					IekeiLog.error(m.name(), e);
				}
			}
		}
	}
}