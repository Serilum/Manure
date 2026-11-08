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
import java.util.List;

public class Util {
	public static List<EntityType<?>> manureAnimals = new ArrayList<EntityType<?>>();

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
			Entity entity = entityType.create(level);
			if (entity instanceof Animal) {
				animals.add(entityType);
			}
		}

		manureAnimals = animals;
	}
}
