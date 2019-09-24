package com.laton95.pyramidplunder.config;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Config {
	
	public static final CommonConfig COMMON;
	
	public static final ForgeConfigSpec COMMON_SPEC;
	
	static {
		final Pair<CommonConfig, ForgeConfigSpec> specPair = new Builder().configure(CommonConfig::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}
	
	public static boolean generateUrns = true;
	
	public static double urnChance = 0.15D;
	
	public static int maxHeight = 40;
	
	public static int minHeight = 1;
	
	public static double snakeChance = 0.3;
	
	public static List<? extends Integer> dimensionBlacklist = getDimensionBlacklistDefault();
	
	public static List<? extends String> biomeBlacklist = new ArrayList<>();
	
	public static void load() {
		generateUrns = COMMON.generateUrns.get();
		urnChance = COMMON.urnChance.get();
		maxHeight = COMMON.maxHeight.get();
		minHeight = COMMON.minHeight.get();
		snakeChance = COMMON.snakeChance.get();
		dimensionBlacklist = COMMON.dimensionBlacklist.get();
		biomeBlacklist = COMMON.biomeBlacklist.get();
		
		if(maxHeight < minHeight) {
			throw new IllegalArgumentException(String.format("Max height cannot be larger than min height: %d > %d", minHeight, maxHeight));
		}
	}
	
	public static class CommonConfig {
		
		public ForgeConfigSpec.BooleanValue generateUrns;
		
		public ForgeConfigSpec.DoubleValue urnChance;
		
		public ForgeConfigSpec.IntValue maxHeight;
		
		public ForgeConfigSpec.IntValue minHeight;
		
		public ForgeConfigSpec.DoubleValue snakeChance;
		
		public ForgeConfigSpec.ConfigValue<List<? extends Integer>> dimensionBlacklist;
		
		public ForgeConfigSpec.ConfigValue<List<? extends String>> biomeBlacklist;
		
		CommonConfig(Builder builder) {
			builder.push("general");
			
			generateUrns = builder
					.comment("Generate urns in world")
					.translation("text.pyramidplunder.config.generate_urns")
					.define("generate_urns", true);
			
			urnChance = builder
					.comment("Percentage chance to attempt to place an urn in a chunk. Actual frequency of urns will be lower as attempts are not always successful")
					.translation("text.pyramidplunder.config.urn_chance")
					.defineInRange("urn_chance", 0.15D, 0, 1);
			
			maxHeight = builder
					.comment("Maximum height urns will generate")
					.translation("text.pyramidplunder.config.max_height")
					.defineInRange("max_height", 40, 0, 255);
			
			minHeight = builder
					.comment("Minimum height urns will generate")
					.translation("text.pyramidplunder.config.min_height")
					.defineInRange("min_height", 1, 0, 255);
			
			snakeChance = builder
					.comment("Chance an urn will contain a snake")
					.translation("text.pyramidplunder.config.snake_chance")
					.defineInRange("snake_chance", 0.3D, 0, 1);
			
			dimensionBlacklist = builder
					.comment("Dimension ids of dimensions that urns will not generate in")
					.translation("text.pyramidplunder.config.dimension_blacklist")
					.defineList("dimension_blacklist", getDimensionBlacklistDefault(), i -> i instanceof Integer);
			
			biomeBlacklist = builder
					.comment("Biomes to not generate urns in, in format \"mod_id:biome_id\". E.g. \"minecraft:desert\"")
					.translation("text.pyramidplunder.config.biome_blacklist")
					.defineList("biome_blacklist", new ArrayList<>(), s -> s instanceof String);
			
			builder.pop();
		}
	}
	
	private static List<Integer> getDimensionBlacklistDefault() {
		ArrayList<Integer> output = new ArrayList<>();
		
		output.add(1);
		output.add(-1);
		
		return output;
	}
	
	public static boolean isBiomeBlacklisted(Biome biome) {
		return biomeBlacklist.contains(biome.getRegistryName().toString());
	}
}
