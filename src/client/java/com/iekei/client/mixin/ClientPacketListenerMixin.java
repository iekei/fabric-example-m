package com.iekei.client.mixin;

import com.iekei.client.core.ModuleManager;
import com.iekei.client.modules.basefind.NewChunks;
import com.iekei.client.modules.basefind.TrailFinder;
import com.iekei.client.modules.basefind.WorldGuardFinder;
import com.iekei.client.modules.chunkutil.TrailTracker;
import com.iekei.client.modules.combat.Velocity;
import com.iekei.client.modules.exploit.FakeLagSwitch;
import com.iekei.client.modules.exploit.PacketCanceller;
import com.iekei.client.modules.exploit.PayloadBypass;
import com.iekei.client.modules.exploit.PingDisguiser;
import com.iekei.client.modules.movement.Blink;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
	@Inject(method = "handleSetEntityMotion", at = @At("TAIL"))
	private void iekei$velocity(ClientboundSetEntityMotionPacket packet, CallbackInfo ci) {
		Velocity velocity = ModuleManager.get(Velocity.class);

		if (velocity == null || !velocity.enabled()) {
			return;
		}

		ClientPacketListener self = (ClientPacketListener) (Object) this;
		var entity = self.getLevel().getEntity(packet.getId());

		if (entity == null) {
			return;
		}

		Vec3 motion = packet.getMovement();
		entity.setDeltaMovement(
				motion.x * velocity.horizontal(),
				motion.y * velocity.vertical(),
				motion.z * velocity.horizontal());
	}

	@Inject(method = "send", at = @At("HEAD"), cancellable = true)
	private void iekei$outgoing(Packet<?> packet, CallbackInfo ci) {
		Blink blink = ModuleManager.get(Blink.class);

		if (blink != null && blink.shouldHold(packet)) {
			ci.cancel();
			return;
		}

		FakeLagSwitch lag = ModuleManager.get(FakeLagSwitch.class);

		if (lag != null && lag.hold(packet)) {
			ci.cancel();
			return;
		}

		PacketCanceller canceller = ModuleManager.get(PacketCanceller.class);

		if (canceller != null && canceller.cancels(packet)) {
			ci.cancel();
			return;
		}

		PayloadBypass bypass = ModuleManager.get(PayloadBypass.class);

		if (bypass != null && bypass.enabled()) {
			Packet<?> rewritten = bypass.rewrite(packet);

			if (rewritten != packet) {
				((ClientPacketListener) (Object) this).send(rewritten);
				ci.cancel();
			}
		}
	}

	@Inject(method = "handleKeepAlive", at = @At("HEAD"), cancellable = true)
	private void iekei$ping(ClientboundKeepAlivePacket packet, CallbackInfo ci) {
		PingDisguiser ping = ModuleManager.get(PingDisguiser.class);

		if (ping != null && ping.defer(packet.getId())) {
			ci.cancel();
		}
	}

	@Inject(method = "handleLevelChunkWithLight", at = @At("TAIL"))
	private void iekei$newChunk(ClientboundLevelChunkWithLightPacket packet, CallbackInfo ci) {
		NewChunks module = ModuleManager.get(NewChunks.class);

		if (module != null && module.enabled()) {
			NewChunks.onChunk(((long) packet.getX() << 32) | (packet.getZ() & 0xFFFFFFFFL));
		}
	}

	@Inject(method = "handleBlockUpdate", at = @At("TAIL"))
	private void iekei$blockUpdate(ClientboundBlockUpdatePacket packet, CallbackInfo ci) {
		ClientPacketListener self = (ClientPacketListener) (Object) this;
		boolean air = packet.getBlockState().isAir();
		var pos = packet.getPos();

		TrailFinder trail = ModuleManager.get(TrailFinder.class);

		if (trail != null && trail.enabled()) {
			trail.onBlockUpdate(pos, air);
		}

		TrailTracker tracker = ModuleManager.get(TrailTracker.class);

		if (tracker != null) {
			tracker.onBlockUpdate(pos, air);
		}

		WorldGuardFinder guard = ModuleManager.get(WorldGuardFinder.class);

		if (guard != null && guard.enabled() && !air && !self.getLevel().getBlockState(pos).isAir()) {
			guard.onBlockReverted(pos);
		}
	}
}