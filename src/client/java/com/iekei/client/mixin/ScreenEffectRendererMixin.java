package com.iekei.client.mixin;

import com.iekei.client.core.ModuleManager;
import com.iekei.client.modules.cosmetic.LowFire;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {
	@Inject(method = "renderFire", at = @At("HEAD"))
	private void iekei$lowFireHead(PoseStack poseStack, CallbackInfo ci) {
		LowFire module = ModuleManager.get(LowFire.class);

		if (module != null && module.enabled() && LowFire.offset > 0.0F) {
			poseStack.pushPose();
			poseStack.translate(0.0F, -LowFire.offset, 0.0F);
		}
	}

	@Inject(method = "renderFire", at = @At("RETURN"))
	private void iekei$lowFireReturn(PoseStack poseStack, CallbackInfo ci) {
		LowFire module = ModuleManager.get(LowFire.class);

		if (module != null && module.enabled() && LowFire.offset > 0.0F) {
			poseStack.popPose();
		}
	}
}