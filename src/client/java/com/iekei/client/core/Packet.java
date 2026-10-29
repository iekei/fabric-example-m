package com.iekei.client.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public final class Packet {
	private static final Minecraft mc = Minecraft.getInstance();

	private Packet() {
	}

	public static void ground(boolean onGround) {
		LocalPlayer p = mc.player;

		if (p == null || mc.getConnection() == null) {
			return;
		}

		mc.getConnection().send(new ServerboundMovePlayerPacket.StatusOnly(onGround, p.horizontalCollision));
	}

	/** The classic 0.11 upward nudge that makes the server accept a critical hit. */
	public static void critNudge() {
		LocalPlayer p = mc.player;

		if (p == null || mc.getConnection() == null) {
			return;
		}

		mc.getConnection().send(new ServerboundMovePlayerPacket.Pos(
				p.getX(), p.getY() + 0.11, p.getZ(), false, p.horizontalCollision));
		mc.getConnection().send(new ServerboundMovePlayerPacket.Pos(
				p.getX(), p.getY(), p.getZ(), false, p.horizontalCollision));
	}
}