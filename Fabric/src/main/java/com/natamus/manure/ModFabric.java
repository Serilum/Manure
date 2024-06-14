package com.natamus.manure;

import com.natamus.collective.check.RegisterMod;
import com.natamus.manure.dispenser.RecipeManager;
import com.natamus.manure.events.ManureDropEvent;
import com.natamus.manure.items.ManureItems;
import com.natamus.manure.util.Reference;
import com.natamus.manure.util.Util;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public class ModFabric implements ModInitializer {
	
	@Override
	public void onInitialize() {
		setGlobalConstants();
		ModCommon.init();

		registerItems();
		loadEvents();

		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
	}

	@SuppressWarnings("UnstableApiUsage")
	private void registerItems() {
		ManureItems.MANURE = new BoneMealItem(new Item.Properties());

		Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "manure"), ManureItems.MANURE);
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> entries.prepend(ManureItems.MANURE));

		RecipeManager.initDispenserBehavior();
	}

	private void loadEvents() {
		ServerWorldEvents.LOAD.register((MinecraftServer server, ServerLevel level) -> {
			Util.attemptBlacklistProcessing(level);
		});

		ServerTickEvents.END_SERVER_TICK.register((MinecraftServer minecraftServer) -> {
			ManureDropEvent.onServerTick(minecraftServer);
		});

		ServerEntityEvents.ENTITY_LOAD.register((Entity entity, ServerLevel world) -> {
			ManureDropEvent.onEntityJoin(entity, world);
		});

		ServerEntityEvents.ENTITY_UNLOAD.register((Entity entity, ServerLevel world) -> {
			ManureDropEvent.onEntityLeave(entity, world);
		});
	}

	private static void setGlobalConstants() {

	}
}
