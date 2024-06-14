package com.natamus.manure;

import com.natamus.collective.check.RegisterMod;
import com.natamus.manure.dispenser.RecipeManager;
import com.natamus.manure.forge.config.IntegrateForgeConfig;
import com.natamus.manure.forge.events.ForgeManureDropEvent;
import com.natamus.manure.items.ManureItems;
import com.natamus.manure.util.Reference;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(Reference.MOD_ID)
public class ModForge {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

	public static final RegistryObject<Item> MANURE_ITEM_OBJECT = ITEMS.register("manure", () -> new BoneMealItem((new Item.Properties())));
	
	public ModForge() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::loadComplete);
		modEventBus.addListener(this::buildContents);

		ITEMS.register(modEventBus);

		setGlobalConstants();
		ModCommon.init();

		IntegrateForgeConfig.registerScreen(ModLoadingContext.get());

		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}

	private void loadComplete(final FMLLoadCompleteEvent event) {
		ManureItems.MANURE = MANURE_ITEM_OBJECT.get();

		RecipeManager.initDispenserBehavior();

		MinecraftForge.EVENT_BUS.register(new ForgeManureDropEvent());
	}

    private void buildContents(BuildCreativeModeTabContentsEvent e) {
        if (e.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            e.accept(ManureItems.MANURE);
        }
    }

	private static void setGlobalConstants() {

	}
}