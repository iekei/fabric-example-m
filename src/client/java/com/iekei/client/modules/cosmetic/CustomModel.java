package com.iekei.client.modules.cosmetic;

import com.iekei.client.gui.Module;
import com.iekei.client.gui.Setting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Swaps the baked model used to render held tools and weapons.
 *
 * <p>The replacement stacks are created once and reused; the renderer mixin
 * asks {@link #replacement(ItemStack)} for a substitute model source.
 */
public final class CustomModel extends Module {
	private static final Setting model = Setting.choice("Model", "Vanilla", "Slim 1.7", "Glow");
	private static final Setting onlyTools = Setting.toggle("Only Tools", true);
	private static final ItemStack SLIM = new ItemStack(Items.IRON_SWORD);
	private static final ItemStack GLOW = new ItemStack(Items.NETHERITE_SWORD);

	/** Re-entrancy guard: the replacement lookup itself calls getModel. */
	public static boolean swapping;

	public CustomModel() {
		super("custommodel", "CustomModel", "Swaps the model used by held tools.", false, model, onlyTools);
	}

	/** Replacement model source, or {@code null} to keep vanilla. */
	public ItemStack replacement(ItemStack held) {
		if (held.isEmpty() || modeIs("Model", "Vanilla")) {
			return null;
		}

		if (bool("Only Tools") && !isTool(held)) {
			return null;
		}

		return switch (mode("Model")) {
			case "Slim 1.7" -> SLIM;
			case "Glow" -> GLOW;
			default -> null;
		};
	}

	private boolean isTool(ItemStack stack) {
		String id = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
		return id.endsWith("_sword") || id.endsWith("_pickaxe") || id.endsWith("_axe")
				|| id.endsWith("_shovel") || id.endsWith("_hoe");
	}

	/** Extra pose scale applied on top of the swapped model. */
	public float scale() {
		return modeIs("Model", "Glow") ? 1.12F : 1.0F;
	}
}