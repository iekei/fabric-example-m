package com.iekei.client.core;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class BlockUtil {
	private static final Minecraft mc = Minecraft.getInstance();

	private BlockUtil() {
	}

	public static BlockState at(BlockPos pos) {
		return mc.level == null ? Blocks.AIR.defaultBlockState() : mc.level.getBlockState(pos);
	}

	public static boolean isAir(BlockPos pos) {
		return at(pos).isAir();
	}

	public static boolean isLiquid(BlockPos pos) {
		return !at(pos).getFluidState().isEmpty();
	}

	public static boolean isSolid(BlockPos pos) {
		return at(pos).isSolidRender();
	}

	public static boolean isOre(BlockState state) {
		return state.is(BlockTags.COAL_ORES)
				|| state.is(BlockTags.IRON_ORES)
				|| state.is(BlockTags.GOLD_ORES)
				|| state.is(BlockTags.DIAMOND_ORES)
				|| state.is(BlockTags.EMERALD_ORES)
				|| state.is(BlockTags.REDSTONE_ORES)
				|| state.is(BlockTags.LAPIS_ORES)
				|| state.is(BlockTags.COPPER_ORES)
				|| state.is(Blocks.ANCIENT_DEBRIS);
	}
}