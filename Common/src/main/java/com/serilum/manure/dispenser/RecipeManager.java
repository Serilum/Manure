package com.serilum.manure.dispenser;

import com.serilum.manure.items.ManureItems;
import net.minecraft.world.level.block.DispenserBlock;

public class RecipeManager {
	public static void initDispenserBehavior() {
		try {
			DispenserBlock.registerBehavior(ManureItems.MANURE, new BehaviourManureDispenser(ManureItems.MANURE));
		}
		catch (ArrayIndexOutOfBoundsException ignored) { }
	}
}
