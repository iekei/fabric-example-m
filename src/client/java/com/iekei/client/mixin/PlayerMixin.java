package com.iekei.client.mixin;

import com.iekei.client.core.ModuleManager;
import com.iekei.client.modules.combat.Reach;
import com.iekei.client.modules.utility.FastBreak;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {
	@Inject(method = "entityInteractionRange", at = @At("RETURN"), cancellable = true)
	private void iekei$reachEntity(CallbackInfoReturnable<Double> cir) {
		Reach reach = ModuleManager.get(Reach.class);

		if (reach != null && reach.enabled()) {
			cir.setReturnValue(cir.getReturnValue() + reach.extra());
		}
	}

	@Inject(method = "blockInteractionRange", at = @At("RETURN"), cancellable = true)
	private void iekei$reachBlock(CallbackInfoReturnable<Double> cir) {
		Reach reach = ModuleManager.get(Reach.class);

		if (reach != null && reach.enabled()) {
			cir.setReturnValue(cir.getReturnValue() + reach.extra());
		}
	}

	@Inject(method = "getDestroySpeed", at = @At("RETURN"), cancellable = true)
	private void iekei$destroySpeed(BlockState state, BlockPos pos, CallbackInfoReturnable<Float> cir) {
		FastBreak fastBreak = ModuleManager.get(FastBreak.class);

		if (fastBreak != null && fastBreak.enabled()) {
			cir.setReturnValue((float) (cir.getReturnValue() * fastBreak.multiplier()));
		}
	}
}