package com.iekei.client.modules.movement;

import com.iekei.client.core.Notify;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;

public final class Blink extends Module {
	private static final Setting hold = Setting.slider("Hold Duration", 800, 100, 3000, 50, "ms");
	private static final Setting maxQueue = Setting.slider("Max Queue", 400, 20, 2000, 20, "p");
	private static final Setting autoSend = Setting.toggle("Auto Send", true);
	private static final Setting holdActions = Setting.toggle("Hold Actions", true);
	private final Deque<Packet<?>> queue = new ArrayDeque<>();
	private long startedAt;

	public Blink() {
		super("blink", "Blink", "Suspends outgoing packets, then teleports.", false,
				hold, maxQueue, autoSend, holdActions);
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
		if (mc.player == null || mc.getConnection() == null) {
			queue.clear();
			return;
		}

		if (bool("Auto Send") && System.currentTimeMillis() - startedAt > num("Hold Duration")) {
			flush();
			startedAt = System.currentTimeMillis();
		}
	}

	/** Called from the packet mixin; true means the packet was swallowed. */
	public boolean shouldHold(Packet<?> packet) {
		if (!enabled() || mc.player == null || mc.getConnection() == null) {
			return false;
		}

		boolean movement = packet instanceof ServerboundMovePlayerPacket;
		boolean action = packet instanceof ServerboundPlayerActionPacket
				|| packet instanceof ServerboundUseItemOnPacket
				|| packet instanceof ServerboundUseItemPacket;

		if (!movement && !(action && bool("Hold Actions"))) {
			return false;
		}

		queue.addLast(packet);

		if (queue.size() > numInt("Max Queue")) {
			flush();
		}

		return true;
	}

	private void flush() {
		if (queue.isEmpty()) {
			return;
		}

		var connection = mc.getConnection();

		if (connection == null) {
			queue.clear();
			return;
		}

		int sent = 0;

		while (!queue.isEmpty()) {
			connection.send(queue.pollFirst());
			sent++;
		}

		if (sent > 20) {
			Notify.toast("Blink flushed " + sent, true);
		}
	}
}