package com.iekei.client.mixin;

import com.iekei.client.core.ModuleManager;
import com.iekei.client.modules.combat.FastUse;
import com.iekei.client.modules.cosmetic.CustomAnimations;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Inject(method = "getUseItemRemainingTicks", at = @At("RETURN"), cancellable = true)
	private void iekei$fastUse(CallbackInfoReturnable<Integer> cir) {
		FastUse fastUse = ModuleManager.get(FastUse.class);

		if (fastUse != null && fastUse.enabled()) {
			cir.setReturnValue((int) (cir.getReturnValue() / fastUse.speed()));
		}
	}

	@Inject(method = "getCurrentSwingDuration", at = @At("RETURN"), cancellable = true)
	private void iekei$swingDuration(CallbackInfoReturnable<Integer> cir) {
		CustomAnimations module = ModuleManager.get(CustomAnimations.class);

		if (module != null && module.enabled()) {
			cir.setReturnValue(module.swingDuration());
		}
	}
}