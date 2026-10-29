package com.iekei.client.mixin;

import com.iekei.client.core.ModuleManager;
import com.iekei.client.modules.combat.HitBox;
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
		HitBox hitBox = ModuleManager.get(HitBox.class);
		Entity self = (Entity) (Object) this;

		if (hitBox == null || !hitBox.enabled() || !hitBox.appliesTo(self)) {
			return;
		}

		cir.setReturnValue(cir.getReturnValue().inflate(hitBox.size()));
	}
}