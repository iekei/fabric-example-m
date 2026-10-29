package com.iekei.client.modules.basefind;

import com.iekei.client.core.Alerts;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.core.BlockPos;

public final class TrailFinder extends Module {
	private static final Setting showLines = Setting.toggle("Show Lines", true);

	public TrailFinder() {
		super("trailfinder", "TrailFinder", "Traces movement paths of players.", false, showLines);
	}

	/** Called from the block update mixin. */
	public void onBlockUpdate(BlockPos pos, boolean nowAir) {
		if (!enabled()) {
			return;
		}

		if (!showLines.getBool()) {
			return;
		}

		Alerts.found(nowAir ? "Block broken" : "Block placed", pos);
	}
}