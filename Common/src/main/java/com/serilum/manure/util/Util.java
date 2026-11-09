package com.serilum.manure.util;

import com.natamus.collective.config.DuskConfig;
import com.natamus.collective.config.DuskConfigMigration;
import com.natamus.collective.functions.ConfigFunctions;
import com.natamus.collective.functions.EntityFunctions;
import com.serilum.manure.config.ConfigHandler;
import com.serilum.manure.data.Constants;
import com.serilum.manure.data.Variables;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Util {
	public static List<EntityType<?>> manureAnimals = new ArrayList<EntityType<?>>();
	public static HashMap<Animal, Integer> earliestDropTicks = new HashMap<>();

	public static void attemptBlacklistProcessing(Level level) {
		if (!Variables.processedBlacklist) {
			try {
				Util.loadManureAnimals(level);
				Variables.processedBlacklist = true;
			} catch (Exception ex) {
				Constants.logger.warn("[" + Reference.NAME + "] Unable to generate the list of manure animals.");
			}
		}
	}

	public static boolean isDueToDropManure(Animal animal, int currentTick, boolean isSharedDropTick) {
		if (!ConfigHandler.randomizeDropTimingPerAnimal) {
			return isSharedDropTick;
		}

		Integer earliestDropTick = earliestDropTicks.get(animal);
		if (earliestDropTick != null && currentTick < earliestDropTick) {
			return false;
		}

		return animal.getRandom().nextInt(getRandomDropTicks()) == 0;
	}

	public static void startDropCooldown(Animal animal, int currentTick) {
		earliestDropTicks.put(animal, currentTick + ConfigHandler.minimumTicksBetweenDrops);
	}

	public static int getRandomDropTicks() {
		int randomDropTicks = ConfigHandler.manureDropDelayTicks - ConfigHandler.minimumTicksBetweenDrops;
		return Math.max(1, randomDropTicks);
	}

	public static boolean hasEatenSinceLastDrop(Animal animal) {
		return animal.getTags().contains(Reference.MOD_ID + ".ate");
	}

	public static boolean isBlacklistedAnimal(Animal animal) {
		return ConfigFunctions.isInIdList(ConfigHandler.animalBlacklist, EntityFunctions.getEntityTypeId(animal.level(), animal.getType()));
	}

	public static void moveBlacklistFileIntoConfig() {
		String disabledAnimals = DuskConfigMigration.takeDisabledIdsFromFile(Reference.MOD_ID, "blacklist.txt");
		if (disabledAnimals == null) {
			return;
		}

		ConfigHandler.animalBlacklist = disabledAnimals;
		DuskConfig.write(Reference.MOD_ID);
	}

	public static void loadManureAnimals(Level level) {
		Registry<EntityType<?>> entityTypeRegistry = level.registryAccess().registryOrThrow(Registries.ENTITY_TYPE);

		List<EntityType<?>> animals = new ArrayList<>();
		for (ResourceLocation loc : entityTypeRegistry.keySet()) {
			EntityType<?> entityType = entityTypeRegistry.get(loc);
			if (isAnimalEntityType(level, entityType)) {
				animals.add(entityType);
			}
		}

		manureAnimals = animals;
	}

	public static boolean isAnimalEntityType(Level level, EntityType<?> entityType) {
		try {
			Entity entity = entityType.create(level);
			return entity instanceof Animal;
		}
		catch (Exception ignored) {
			return false;
		}
	}
}
