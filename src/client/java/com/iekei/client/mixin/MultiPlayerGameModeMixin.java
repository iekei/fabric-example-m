package com.iekei.client.mixin;

import com.iekei.client.core.ModuleManager;
import com.iekei.client.modules.exploit.InventoryDesyncGuard;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.inventory.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {
	@Inject(method = "handleInventoryMouseClick", at = @At("HEAD"), cancellable = true)
	private void iekei$desyncGuard(int containerId, int slot, int button, ClickType type, CallbackInfo ci) {
		InventoryDesyncGuard guard = ModuleManager.get(InventoryDesyncGuard.class);

		if (guard != null && guard.intercept(slot, button, type)) {
			ci.cancel();
		}
	}
}