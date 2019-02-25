package com.laton95.pyramidplunder.config;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class Config {
	
	public static final ServerConfig SERVER;
	
	public static final ForgeConfigSpec SERVER_SPEC;
	
	static {
		final Pair<ServerConfig, ForgeConfigSpec> specPair = new Builder().configure(ServerConfig::new);
		SERVER_SPEC = specPair.getRight();
		SERVER = specPair.getLeft();
	}
	
	public static boolean generateUrns = true;
	
	public static double urnChance = 0.15D;
	
	public static int maxHeight = 40;
	
	public static int minHeight = 1;
	
	public static double snakeChance = 0.3;
	
	public static List<? extends Integer> dimensionBlacklist = getDimensionBlacklistDefault();
	
	public static List<? extends String> biomeBlacklist = new ArrayList<>();
	
	public static void load() {
		generateUrns = SERVER.generateUrns.get();
		urnChance = SERVER.urnChance.get();
		maxHeight = SERVER.maxHeight.get();
		minHeight = SERVER.minHeight.get();
		snakeChance = SERVER.snakeChance.get();
		dimensionBlacklist = SERVER.dimensionBlacklist.get();
		biomeBlacklist = SERVER.biomeBlacklist.get();
		
		if(maxHeight < minHeight) {
			throw new IllegalArgumentException(String.format("Max height cannot be larger than min height: %d > %d", minHeight, maxHeight));
		}
	}
	
	public static class ServerConfig {
		
		public ForgeConfigSpec.BooleanValue generateUrns;
		
		public ForgeConfigSpec.DoubleValue urnChance;
		
		public ForgeConfigSpec.IntValue maxHeight;
		
		public ForgeConfigSpec.IntValue minHeight;
		
		public ForgeConfigSpec.DoubleValue snakeChance;
		
		public ForgeConfigSpec.ConfigValue<List<? extends Integer>> dimensionBlacklist;
		
		public ForgeConfigSpec.ConfigValue<List<? extends String>> biomeBlacklist;
		
		ServerConfig(Builder builder) {
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
