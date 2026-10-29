package com.iekei.client.modules.hud;

import com.iekei.client.core.ModuleManager;
import com.iekei.client.gui.Draw;
import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import com.iekei.client.modules.combat.Killaura;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;

public final class TargetHUD extends Module {
	private static final Setting showArmor = Setting.toggle("Show Armor", true);

	public TargetHUD() {
		super("targethud", "TargetHUD", "Shows the current target's stats.", false, showArmor);
	}

	@Override
	public void onRenderHud(GuiGraphicsExtractor g, float delta) {
		if (mc.player == null) {
			return;
		}

		LivingEntity target = null;
		Killaura aura = ModuleManager.get(Killaura.class);

		if (aura != null && aura.enabled() && aura.target() instanceof LivingEntity living) {
			target = living;
		} else if (mc.hitResult instanceof EntityHitResult hit
				&& hit.getEntity() instanceof LivingEntity living) {
			target = living;
		}

		if (target == null) {
			return;
		}

		int x = mc.getWindow().getGuiScaledWidth() - 130;
		int y = mc.getWindow().getGuiScaledHeight() - 70;
		String name = target.getName().getString();
		float ratio = target.getHealth() / target.getMaxHealth();

		g.fill(x, y, x + 124, y + 46, 0xC0101018);
		Draw.text(g, name, x + 4, y + 4, 0xFFFFFFFF);
		Draw.text(g, Math.round(target.getHealth()) + " / " + Math.round(target.getMaxHealth()), x + 4, y + 16,
				0xFFBBBBCC);
		g.fill(x + 4, y + 26, x + 120, y + 30, 0xFF22222A);
		g.fill(x + 4, y + 26, x + 4 + Math.round(116 * ratio), y + 30,
				ratio > 0.5F ? 0xFF55FF66 : ratio > 0.25F ? 0xFFFFC144 : 0xFFFF5555);

            if (bool("Show Armor") && target instanceof net.minecraft.world.entity.player.Player player) {
			int ax = x + 4;

			for (int i = 39; i >= 36; i--) {
				var stack = player.getInventory().getItem(i);

				if (stack.isEmpty()) {
					continue;
				}

				g.fill(ax, y + 34, ax + 8, y + 42, 0xFF2A2A34);
				Draw.text(g, stack.getCount() > 1 ? "" + stack.getCount() : "", ax + 1, y + 34, 0xFFFFFFFF);
				ax += 10;
			}
		}
	}
}