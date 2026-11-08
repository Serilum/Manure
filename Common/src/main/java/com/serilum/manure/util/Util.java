package com.serilum.manure.util;

import com.natamus.collective.config.DuskConfig;
import com.natamus.collective.config.DuskConfigMigration;
import com.natamus.collective.functions.ConfigFunctions;
import com.natamus.collective.functions.EntityFunctions;
import com.serilum.manure.config.ConfigHandler;
import com.serilum.manure.data.Constants;
import com.serilum.manure.data.Variables;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

import java.util.*;

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
		Registry<EntityType<?>> entityTypeRegistry = level.registryAccess().lookupOrThrow(Registries.ENTITY_TYPE);

		List<EntityType<?>> animals = new ArrayList<>();
		for (Identifier loc : entityTypeRegistry.keySet()) {
			Optional<Holder.Reference<EntityType<?>>> entityTypeOptionalReference = entityTypeRegistry.get(loc);
			if (entityTypeOptionalReference.isEmpty()) {
				continue;
			}

			EntityType<?> entityType = entityTypeOptionalReference.get().value();
			Entity entity = entityType.create(level, EntitySpawnReason.COMMAND);
			if (entity instanceof Animal) {
				animals.add(entityType);
			}
		}

		manureAnimals = animals;
	}
}
