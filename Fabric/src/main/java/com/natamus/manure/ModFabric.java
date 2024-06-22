package com.natamus.manure;

import com.natamus.collective.check.RegisterMod;
import com.natamus.manure.events.ManureDropEvent;
import com.natamus.manure.util.Reference;
import com.natamus.manure.util.Util;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class ModFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		setGlobalConstants();
		ModCommon.init();
		ModCommon.registerAssets(null);
		ModCommon.setAssets();

		loadEvents();

		RegisterMod.register(Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
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
