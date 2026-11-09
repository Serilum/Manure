package com.serilum.manure.events;

import com.serilum.manure.config.ConfigHandler;
import com.serilum.manure.items.ManureItems;
import com.serilum.manure.util.Reference;
import com.serilum.manure.util.Util;
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
		int currentTick = server.getTickCount();
		boolean isSharedDropTick = currentTick % ConfigHandler.manureDropDelayTicks == 0;
		if (!isSharedDropTick && !ConfigHandler.randomizeDropTimingPerAnimal) {
			return;
		}

		for (Animal animal : loadedAnimals) {
			if (!animal.isAlive()) {
				loadedAnimals.remove(animal);
				Util.earliestDropTicks.remove(animal);
				continue;
			}
			if (!Util.isDueToDropManure(animal, currentTick, isSharedDropTick)) {
				continue;
			}
			if (Util.isBlacklistedAnimal(animal)) {
				continue;
			}
			if (ConfigHandler.onlyDropManureAfterEating && !Util.hasEatenSinceLastDrop(animal)) {
				continue;
			}

			animal.removeTag(Reference.MOD_ID + ".ate");
			Util.startDropCooldown(animal, currentTick);
			animal.level().addFreshEntity(new ItemEntity(animal.level(), animal.getX(), animal.getY()+0.5, animal.getZ(), new ItemStack(ManureItems.MANURE, 1)));
		}
	}

	public static void onAnimalEat(Animal animal) {
		if (!ConfigHandler.onlyDropManureAfterEating) {
			return;
		}
		if (animal.level().isClientSide()) {
			return;
		}

		animal.addTag(Reference.MOD_ID + ".ate");
	}

	public static void onEntityJoin(Entity entity, ServerLevel world) {
		if (entity instanceof Animal) {
			if (Util.manureAnimals.contains(entity.getType())) {
				loadedAnimals.add((Animal) entity);
				Util.startDropCooldown((Animal) entity, world.getServer().getTickCount());
			}
		}
	}

	public static void onEntityLeave(Entity entity, ServerLevel world) {
		if (entity instanceof Animal) {
			loadedAnimals.remove((Animal)entity);
			Util.earliestDropTicks.remove((Animal)entity);
		}
	}
}