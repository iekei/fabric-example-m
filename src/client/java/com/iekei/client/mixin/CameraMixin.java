package com.iekei.client.mixin;

import com.iekei.client.core.ModuleManager;
import com.iekei.client.modules.render.Freecam;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
	@Inject(method = "setup", at = @At("TAIL"))
	private void iekei$freecam(CallbackInfo ci) {
		Freecam freecam = ModuleManager.get(Freecam.class);

		if (freecam == null || !freecam.enabled() || !freecam.active) {
			return;
		}

		Camera self = (Camera) (Object) this;
		self.setPosition(freecam.pos.x, freecam.pos.y, freecam.pos.z);
		self.setRotation(freecam.yaw, freecam.pitch);
	}
}