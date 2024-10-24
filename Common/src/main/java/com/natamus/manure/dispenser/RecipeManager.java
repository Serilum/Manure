package com.natamus.manure.dispenser;

import com.natamus.manure.items.ManureItems;
import net.minecraft.world.level.block.DispenserBlock;

public class RecipeManager {
	public static void initDispenserBehavior() {
		try {
			DispenserBlock.registerBehavior(ManureItems.MANURE, new BehaviourManureDispenser(ManureItems.MANURE));
		}
		catch (ArrayIndexOutOfBoundsException ignored) { }
	}
}
