package com.iekei.client.modules.movement;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;

public final class Blink extends Module {
	private final Setting hold = Setting.slider("Hold Duration", 800, 100, 3000, 50, "ms");
	private final Setting autoSend = Setting.toggle("Auto Send", true);
	private final List<Packet<?>> queue = new ArrayList<>();
	private long startedAt;

	public Blink() {
		super("blink", "Blink", "Suspends outgoing packets, then teleports.", false, hold, autoSend);
	}

	@Override
	protected void onEnable() {
		queue.clear();
		startedAt = System.currentTimeMillis();
	}

	@Override
	protected void onDisable() {
		flush();
	}

	@Override
	public void onTick() {
		if (bool("Auto Send") && System.currentTimeMillis() - startedAt > num("Hold Duration")) {
			flush();
			startedAt = System.currentTimeMillis();
		}
	}

	public boolean shouldHold(Packet<?> packet) {
		if (!enabled() || mc.player == null) {
			return false;
		}

		if (packet instanceof ServerboundMovePlayerPacket || packet instanceof ServerboundPlayerActionPacket) {
			queue.add(packet);
			return true;
		}

		return false;
	}

	private void flush() {
		if (mc.getConnection() == null) {
			queue.clear();
			return;
		}

		for (Packet<?> packet : queue) {
			mc.getConnection().send(packet);
		}

		queue.clear();
	}
}