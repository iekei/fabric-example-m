package com.iekei.client.modules.combat;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;

public final class HitBox extends Module {
	private final Setting size = Setting.slider("Size", 0.6, 0.1, 2.0, 0.05, "b");

	public HitBox() {
		super("hitbox", "HitBox", "Enlarges entity hitboxes.", false, size);
	}

	public double size() {
		return num("Size");
	}

	public boolean appliesTo(Entity entity) {
		return entity instanceof LivingEntity || entity instanceof ItemEntity;
	}
}