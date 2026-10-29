package com.iekei.client.core;

import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;

public final class Inv {
	private static final Minecraft mc = Minecraft.getInstance();

	private Inv() {
	}

	public static int selected() {
		LocalPlayer p = mc.player;
		return p == null ? 0 : p.getInventory().getSelectedSlot();
	}

	public static void select(int slot) {
		LocalPlayer p = mc.player;

		if (p != null && slot >= 0 && slot < 9) {
			p.getInventory().setSelectedSlot(slot);
		}
	}

	public static int find(Predicate<ItemStack> filter) {
		LocalPlayer p = mc.player;

		if (p == null) {
			return -1;
		}

		for (int i = 0; i < 36; i++) {
			if (filter.test(p.getInventory().getItem(i))) {
				return i;
			}
		}

		return -1;
	}

	/** Clicks a slot in the currently open container (0-8 hotbar, 9-35 inventory). */
	public static void click(int slot, int button, ClickType type) {
		LocalPlayer p = mc.player;

		if (p == null || mc.gameMode == null || p.containerMenu == null) {
			return;
		}

		mc.gameMode.handleInventoryMouseClick(p.containerMenu.containerId, slot, button, type, p);
	}

	/** Moves an inventory slot into the hotbar slot {@code hotbar}. */
	public static void moveToHotbar(int slot, int hotbar) {
		click(slot, hotbar, ClickType.SWAP);
	}

	public static void drop(int slot) {
		click(slot, 1, ClickType.THROW);
	}
}