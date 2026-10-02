package com.serilum.manure;

import com.natamus.collective.functions.CreativeModeTabFunctions;
import com.natamus.collective.services.Services;
import com.serilum.manure.config.ConfigHandler;
import com.serilum.manure.dispenser.RecipeManager;
import com.serilum.manure.items.ManureItems;
import com.serilum.manure.util.Reference;
import net.minecraft.resources.Identifier;
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
		Services.REGISTERITEM.registerItem(modEventBusObject, Identifier.fromNamespaceAndPath(Reference.MOD_ID, "manure"), (properties) -> new BoneMealItem(properties), new Item.Properties(), CreativeModeTabFunctions.getCreativeModeTabResourceKey("tools_and_utilities"), true);
	}

	public static void setAssets() {
		ManureItems.MANURE = Services.REGISTERITEM.getRegisteredItem(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "manure"));

		RecipeManager.initDispenserBehavior();
	}
}