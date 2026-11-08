package com.serilum.manure.config;

import com.natamus.collective.config.DuskConfig;
import com.serilum.manure.util.Reference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler extends DuskConfig {
	public static HashMap<String, List<String>> configMetaData = new HashMap<String, List<String>>();

	@Entry(min = 0, max = 10000000) public static int manureDropDelayTicks = 12000;
	@Entry(registry = "entity_type", filter = "animals") public static String animalBlacklist = "minecraft:axolotl,minecraft:bee,minecraft:frog";

	public static void initConfig() {
		configMetaData.put("manureDropDelayTicks", Arrays.asList(
			"How long the delay in ticks is in between loaded animals dropping manure. 20 ticks = 1 second. By default twice a day, every 12000 ticks."
		));
		configMetaData.put("animalBlacklist", Arrays.asList(
			"A comma separated list of animal ids that never drop manure. Use * as a wildcard."
		));

		DuskConfig.init(Reference.NAME, Reference.MOD_ID, ConfigHandler.class);
	}
}