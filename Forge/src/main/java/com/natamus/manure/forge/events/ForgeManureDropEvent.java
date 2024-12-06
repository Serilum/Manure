package com.natamus.manure.forge.events;

import com.natamus.collective.functions.WorldFunctions;
import com.natamus.manure.events.ManureDropEvent;
import com.natamus.manure.util.Util;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ForgeManureDropEvent {
    @SubscribeEvent
    public void onWorldLoad(LevelEvent.Load e) {
        Level level = WorldFunctions.getWorldIfInstanceOfAndNotRemote(e.getLevel());
        if (level == null) {
            return;
        }

        Util.attemptBlacklistProcessing(level);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent e) {
        if (!e.phase.equals(TickEvent.Phase.END)) {
            return;
        }

        ManureDropEvent.onServerTick(e.getServer());
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinLevelEvent e) {
        Level level = e.getLevel();
        if (level.isClientSide) {
            return;
        }

        ManureDropEvent.onEntityJoin(e.getEntity(), (ServerLevel)level);
    }

    @SubscribeEvent
    public void onEntityLeave(EntityLeaveLevelEvent e) {
        Level level = e.getLevel();
        if (level.isClientSide) {
            return;
        }

        ManureDropEvent.onEntityLeave(e.getEntity(), (ServerLevel)level);
    }
}