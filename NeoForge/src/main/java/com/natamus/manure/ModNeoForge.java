package com.natamus.manure;

import com.natamus.collective.check.RegisterMod;
import com.natamus.manure.dispenser.RecipeManager;
import com.natamus.manure.items.ManureItems;
import com.natamus.manure.neoforge.config.IntegrateNeoForgeConfig;
import com.natamus.manure.neoforge.events.NeoForgeManureDropEvent;
import com.natamus.manure.util.Reference;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Reference.MOD_ID)
public class ModNeoForge {

	private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MOD_ID);

	public static final DeferredItem<Item> MANURE_ITEM_OBJECT = ITEMS.register("manure", () -> new BoneMealItem((new Item.Properties())));
	
	public ModNeoForge(IEventBus modEventBus) {
		modEventBus.addListener(this::loadComplete);
		modEventBus.addListener(this::buildContents);

		ITEMS.register(modEventBus);

		setGlobalConstants();
		ModCommon.init();

		IntegrateNeoForgeConfig.registerScreen(ModLoadingContext.get());

		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}

	private void loadComplete(final FMLLoadCompleteEvent event) {
		ManureItems.MANURE = MANURE_ITEM_OBJECT.get();

		RecipeManager.initDispenserBehavior();

		NeoForge.EVENT_BUS.register(NeoForgeManureDropEvent.class);
	}

	private void buildContents(BuildCreativeModeTabContentsEvent e) {
		if (e.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
			e.accept(ManureItems.MANURE);
		}
	}

	private static void setGlobalConstants() {

	}
}