package com.iekei.client.modules.render;

import com.iekei.client.core.BlockUtil;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;

public final class Xray extends Module {
	private static final Setting transparency = Setting.slider("Transparency", 70, 0, 100, 1, "%");
	private static final Setting oresOnly = Setting.toggle("Ores Only", true);

	public Xray() {
		super("xray", "Xray", "Reveals ores through solid blocks.", false, transparency, oresOnly);
	}

	@Override
	protected void onEnable() {
		reload();
	}

	@Override
	protected void onDisable() {
		reload();
	}

	private void reload() {
		if (mc.levelRenderer != null) {
			mc.levelRenderer.allChanged();
		}
	}

	/** Called from the block render-shape mixin. */
	public boolean hidden(net.minecraft.world.level.block.state.BlockState state) {
		if (!enabled() || state.isAir()) {
			return false;
		}

		if (BlockUtil.isOre(state)) {
			return false;
		}

		return bool("Ores Only");
	}
}