package com.iekei.client.modules.utility;

import com.iekei.client.core.Inv;
import com.iekei.client.core.Timer;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.world.inventory.ClickType;

public final class FastDrop extends Module {
	private static final Setting speed = Setting.slider("Speed", 3, 1, 5, 1, "x");
	private final Timer cooldown = new Timer();

	public FastDrop() {
		super("fastdrop", "FastDrop", "Drops items without the delay.", false, speed);
	}

	@Override
	public void onTick() {
		if (mc.player == null || !mc.options.keyDrop.isDown() || mc.gui.screen() != null) {
			return;
		}

		if (cooldown.every(300.0 / num("Speed"))) {
			Inv.click(Inv.selected(), 1, ClickType.THROW);
		}
	}
}