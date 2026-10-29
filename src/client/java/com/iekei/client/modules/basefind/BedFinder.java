package com.iekei.client.modules.basefind;

import com.iekei.client.core.Alerts;
import com.iekei.client.core.ScanModule;
import com.iekei.client.gui.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class BedFinder extends ScanModule {
	private static final Setting enabledToggle = Setting.toggle("Enabled", true);

	public BedFinder() {
		super("bedfinder", "BedFinder", "Locates beds for respawn anchors.", false, enabledToggle);
	}

	@Override
	protected void onBlock(BlockPos pos, BlockState state) {
		if (state.getBlock() instanceof BedBlock && state.getValue(BedBlock.PART) == net.minecraft.world.level.block.state.properties.BedPart.HEAD) {
			Alerts.found("Bed", pos);
		}
	}
}