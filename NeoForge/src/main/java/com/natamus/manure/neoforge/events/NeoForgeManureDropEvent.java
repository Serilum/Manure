package com.natamus.manure.neoforge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.manure.events.ManureDropEvent;
import com.natamus.manure.util.Util;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class NeoForgeManureDropEvent {
	@SubscribeEvent
	public static void onWorldLoad(LevelEvent.Load e) {
		Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
		if (level == null) {
			return;
		}

		Util.attemptBlacklistProcessing(level);
	}

	@SubscribeEvent
	public static void onServerTick(ServerTickEvent.Post e) {
		ManureDropEvent.onServerTick(e.getServer());
	}

	@SubscribeEvent
	public static void onEntityJoin(EntityJoinLevelEvent e) {
		Level level = e.getLevel();
		if (level.isClientSide) {
			return;
		}

		ManureDropEvent.onEntityJoin(e.getEntity(), (ServerLevel)level);
	}

	@SubscribeEvent
	public static void onEntityLeave(EntityLeaveLevelEvent e) {
		Level level = e.getLevel();
		if (level.isClientSide) {
			return;
		}

		ManureDropEvent.onEntityLeave(e.getEntity(), (ServerLevel)level);
	}
}