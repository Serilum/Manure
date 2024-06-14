package com.natamus.manure.events;

import com.natamus.manure.config.ConfigHandler;
import com.natamus.manure.items.ManureItems;
import com.natamus.manure.util.Util;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.CopyOnWriteArrayList;

public class ManureDropEvent {
	private static final CopyOnWriteArrayList<Animal> loadedAnimals = new CopyOnWriteArrayList<Animal>();

	public static void onServerTick(MinecraftServer server) {
		if (server.getTickCount() % ConfigHandler.manureDropDelayTicks != 0) {
			return;
		}

		for (Animal animal : loadedAnimals) {
			if (!animal.isAlive()) {
				loadedAnimals.remove(animal);
				continue;
			}

			animal.level().addFreshEntity(new ItemEntity(animal.level(), animal.getX(), animal.getY()+0.5, animal.getZ(), new ItemStack(ManureItems.MANURE, 1)));
		}
	}

	public static void onEntityJoin(Entity entity, ServerLevel world) {
		if (entity instanceof Animal) {
			if (Util.manureAnimals.contains(entity.getType())) {
				loadedAnimals.add((Animal) entity);
			}
		}
	}

	public static void onEntityLeave(Entity entity, ServerLevel world) {
		if (entity instanceof Animal) {
			loadedAnimals.remove((Animal)entity);
		}
	}
}