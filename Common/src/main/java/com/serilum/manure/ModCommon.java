package com.serilum.manure;

import com.natamus.collective.functions.CreativeModeTabFunctions;
import com.natamus.collective.services.Services;
import com.serilum.manure.config.ConfigHandler;
import com.serilum.manure.dispenser.RecipeManager;
import com.serilum.manure.items.ManureItems;
import com.serilum.manure.util.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;

public class ModCommon {

	public static void init() {
		ConfigHandler.initConfig();
		load();
	}

	private static void load() {

	}

	public static void registerAssets(Object modEventBusObject) {
		Services.REGISTERITEM.registerItem(modEventBusObject, new ResourceLocation(Reference.MOD_ID, "manure"), () -> new BoneMealItem(new Item.Properties()), CreativeModeTabFunctions.getCreativeModeTabResourceKey("tools_and_utilities"), true);
	}

	public static void setAssets() {
		ManureItems.MANURE = Services.REGISTERITEM.getRegisteredItem(new ResourceLocation(Reference.MOD_ID, "manure"));

		RecipeManager.initDispenserBehavior();
	}
}