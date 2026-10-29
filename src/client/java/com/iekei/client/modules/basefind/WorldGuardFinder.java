package com.iekei.client.modules.basefind;

import com.iekei.client.core.Alerts;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.core.BlockPos;

public final class WorldGuardFinder extends Module {
	private static final Setting enabledToggle = Setting.toggle("Enabled", true);

	public WorldGuardFinder() {
		super("worldguardfinder", "WorldGuardFinder", "Detects claims and protected regions.", false,
				enabledToggle);
	}

	/**
	 * A block that reappears right after the client predicted a break means the
	 * server rejected it - a decent proxy for a protected region.
	 */
	public void onBlockReverted(BlockPos pos) {
		if (enabled()) {
			Alerts.found("Protected region", pos);
		}
	}
}