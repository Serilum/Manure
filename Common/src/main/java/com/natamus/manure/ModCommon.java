package com.natamus.manure;

import com.natamus.collective.services.Services;
import com.natamus.manure.config.ConfigHandler;
import com.natamus.manure.dispenser.RecipeManager;
import com.natamus.manure.items.ManureItems;
import com.natamus.manure.util.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class ModCommon {

	public static void init() {
		ConfigHandler.initConfig();
		load();
	}

	private static void load() {

	}

	public static void registerAssets(Object modEventBusObject) {
		Services.REGISTERITEM.registerItem(modEventBusObject, new ResourceLocation(Reference.MOD_ID, "manure"), () -> new BoneMealItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)),null, true);
	}

	public static void setAssets() {
		ManureItems.MANURE = Services.REGISTERITEM.getRegisteredItem(new ResourceLocation(Reference.MOD_ID, "manure"));

		RecipeManager.initDispenserBehavior();
	}
}