package com.iekei.client.mixin;

import com.iekei.client.core.ModuleManager;
import com.iekei.client.modules.combat.HitBox;
import com.iekei.client.modules.pvp.HitBoxPlus;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Inject(method = "getBoundingBox", at = @At("RETURN"), cancellable = true)
	private void iekei$hitbox(CallbackInfoReturnable<AABB> cir) {
		Entity self = (Entity) (Object) this;
		double inflate = 0.0;
		HitBox base = ModuleManager.get(HitBox.class);

		if (base != null && base.enabled() && base.appliesTo(self)) {
			inflate = Math.max(inflate, base.size());
		}

		HitBoxPlus plus = ModuleManager.get(HitBoxPlus.class);

		if (plus != null && plus.enabled() && plus.appliesTo(self)) {
			inflate = Math.max(inflate, plus.size());
		}

		if (inflate > 0.0) {
			cir.setReturnValue(cir.getReturnValue().inflate(inflate));
		}
	}
}