package com.iekei.client.mixin;

import com.iekei.client.core.ModuleManager;
import com.iekei.client.modules.cosmetic.CustomModel;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
	@Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
	private void iekei$customModel(ItemStack stack, Level level, Entity entity, int seed,
			CallbackInfoReturnable<BakedModel> cir) {
		if (CustomModel.swapping) {
			return;
		}

		CustomModel module = ModuleManager.get(CustomModel.class);

		if (module == null || !module.enabled()) {
			return;
		}

		ItemStack replacement = module.replacement(stack);

		if (replacement == null) {
			return;
		}

		CustomModel.swapping = true;

		try {
			cir.setReturnValue(((ItemRenderer) (Object) this).getModel(replacement, level, entity, seed));
		} finally {
			CustomModel.swapping = false;
		}
	}
}