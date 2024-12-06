package com.natamus.manure.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BehaviourManureDispenser implements DispenseItemBehavior {
	protected final Item MANURE;

	public BehaviourManureDispenser(Item itemIn){
		MANURE = itemIn;
	}

	@Override
	public @NotNull ItemStack dispense(BlockSource source, @NotNull ItemStack itemstack) {
		Level world = source.level();
		if (world.isClientSide) {
			return itemstack;
		}
		
		BlockPos pos = source.pos();
		BlockState state = source.state();
		Direction facing = state.getValue(DispenserBlock.FACING);
		BlockPos facepos = pos.relative(facing);
		
		if (BoneMealItem.growCrop(itemstack, world, facepos)) {
			world.levelEvent(2005, facepos, 0);
		}
		
		return itemstack;
	}
}
