package com.natamus.manure.util;

import com.natamus.collective.functions.DataFunctions;
import com.natamus.manure.data.Variables;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Util {
	private static final String dirpath = DataFunctions.getConfigDirectory() + File.separator + Reference.MOD_ID;
	private static final File dir = new File(dirpath);
	private static final File blacklistfile = new File(dirpath + File.separator + "blacklist.txt");

	private static final List<String> defaultBlacklistedNames = Arrays.asList("minecraft:axolotl", "minecraft:bee", "minecraft:frog");
	public static List<EntityType<?>> manureAnimals = new ArrayList<EntityType<?>>();

	public static void attemptBlacklistProcessing(Level level) {
		if (!Variables.processedBlacklist) {
			try {
				Util.loadManureBlacklist(level);
				Variables.processedBlacklist = true;
			} catch (Exception ex) {
				System.out.println("[" + Reference.NAME + "] Error: Unable to generate manure blacklist.");
			}
		}
	}

	public static void loadManureBlacklist(Level level) throws Exception {
		Registry<EntityType<?>> entityTypeRegistry = level.registryAccess().registryOrThrow(Registries.ENTITY_TYPE);
		List<String> blacklist = new ArrayList<String>();

		PrintWriter writer = null;
		if (!dir.isDirectory() || !blacklistfile.isFile()) {
			dir.mkdirs();

			if (!blacklistfile.isFile()) {
				writer = new PrintWriter(dirpath + File.separator + "blacklist.txt", StandardCharsets.UTF_8);
			}
		}
		else {
			String blcontent = new String(Files.readAllBytes(Paths.get(dirpath + File.separator + "blacklist.txt")));
			for (String entityrl : blcontent.split("," )) {
				String name = entityrl.replace("\n", "").trim();
				if (name.startsWith("!")) {
					blacklist.add(name.replace("!", ""));
				}
			}
		}

		List<String> namesToWrite = new ArrayList<String>();
		for (ResourceLocation loc : entityTypeRegistry.keySet()) {
			EntityType<?> entitytype = entityTypeRegistry.get(loc);
			Entity entity = entitytype.create(level);
			if (!(entity instanceof Animal)) {
				continue;
			}

			String name = loc.toString();

			if (writer != null) {
				namesToWrite.add(name);
			}

			if (!blacklist.contains(name)) {
				manureAnimals.add(entitytype);
			}
		}

		if (writer != null) {
			Collections.sort(namesToWrite);

			for (String name : namesToWrite) {
				String prefix = "";
				if (defaultBlacklistedNames.contains(name)) {
					blacklist.add(name);
					prefix = "!";
				}

				writer.println(prefix + name + ",");
			}

			writer.close();
		}
	}
}
