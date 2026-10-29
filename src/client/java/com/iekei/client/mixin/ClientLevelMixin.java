package com.iekei.client.mixin;

import com.iekei.client.core.ModuleManager;
import com.iekei.client.modules.cosmetic.CustomSky;
import com.iekei.client.modules.cosmetic.ParticleCustomizer;
import com.iekei.client.modules.cosmetic.TimeChanger;
import com.iekei.client.modules.cosmetic.WeatherChanger;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
	@Inject(method = "getTimeOfDay", at = @At("RETURN"), cancellable = true)
	private void iekei$time(float partialTick, CallbackInfoReturnable<Float> cir) {
		TimeChanger module = ModuleManager.get(TimeChanger.class);

		if (module != null && module.enabled()) {
			cir.setReturnValue(module.fraction());
		}
	}

	@Inject(method = "getRainLevel", at = @At("RETURN"), cancellable = true)
	private void iekei$rain(float partialTick, CallbackInfoReturnable<Float> cir) {
		WeatherChanger module = ModuleManager.get(WeatherChanger.class);

		if (module != null && module.enabled()) {
			cir.setReturnValue(module.rain());
		}
	}

	@Inject(method = "getThunderLevel", at = @At("RETURN"), cancellable = true)
	private void iekei$thunder(float partialTick, CallbackInfoReturnable<Float> cir) {
		WeatherChanger module = ModuleManager.get(WeatherChanger.class);

		if (module != null && module.enabled()) {
			cir.setReturnValue(module.thunder());
		}
	}

	@Inject(method = "getSkyColor", at = @At("RETURN"), cancellable = true)
	private void iekei$sky(Vec3 pos, float partialTick, CallbackInfoReturnable<Vec3> cir) {
		CustomSky module = ModuleManager.get(CustomSky.class);

		if (module != null && module.enabled()) {
			cir.setReturnValue(module.tint(cir.getReturnValue()));
		}
	}

	@Inject(method = "addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", at = @At("TAIL"))
	private void iekei$particles(ParticleOptions options, double x, double y, double z,
			double dx, double dy, double dz, CallbackInfo ci) {
		ParticleCustomizer module = ModuleManager.get(ParticleCustomizer.class);

		if (module == null || !module.enabled() || ParticleCustomizer.spawning) {
			return;
		}

		ParticleCustomizer.spawning = true;

		try {
			for (int i = 1; i < module.multiplier(); i++) {
				((ClientLevel) (Object) this).addParticle(options, x, y, z, dx, dy, dz);
			}
		} finally {
			ParticleCustomizer.spawning = false;
		}
	}
}