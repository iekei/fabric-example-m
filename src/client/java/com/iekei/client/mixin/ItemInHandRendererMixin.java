package com.iekei.client.mixin;

import com.iekei.client.core.ModuleManager;
import com.iekei.client.modules.cosmetic.CustomModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ItemInHandRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
	@Inject(method = "renderItem", at = @At("HEAD"))
	private void iekei$modelHead(PoseStack poseStack, CallbackInfo ci) {
		CustomModel module = ModuleManager.get(CustomModel.class);

		if (module != null && module.enabled() && module.scale() != 1.0F) {
			poseStack.pushPose();
			poseStack.scale(module.scale(), module.scale(), module.scale());
		}
	}

	@Inject(method = "renderItem", at = @At("RETURN"))
	private void iekei$modelReturn(PoseStack poseStack, CallbackInfo ci) {
		CustomModel module = ModuleManager.get(CustomModel.class);

		if (module != null && module.enabled() && module.scale() != 1.0F) {
			poseStack.popPose();
		}
	}
}