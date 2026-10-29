package com.iekei.client.mixin;

import com.iekei.client.core.ModuleManager;
import com.iekei.client.modules.movement.NoSlowdown;
import com.iekei.client.modules.render.Xray;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
	@Inject(method = "getSpeedFactor", at = @At("RETURN"), cancellable = true)
	private void iekei$speedFactor(CallbackInfoReturnable<Float> cir) {
		NoSlowdown module = ModuleManager.get(NoSlowdown.class);

		if (module != null && module.enabled() && module.blocksSpeedFactor()) {
			cir.setReturnValue(1.0F);
		}
	}

	@Inject(method = "getRenderShape", at = @At("RETURN"), cancellable = true)
	private void iekei$xray(BlockState state, CallbackInfoReturnable<RenderShape> cir) {
		Xray xray = ModuleManager.get(Xray.class);

		if (xray != null && xray.hidden(state)) {
			cir.setReturnValue(RenderShape.INVISIBLE);
		}
	}
}