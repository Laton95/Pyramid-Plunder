package com.laton95.pyramidplunder.config;

import com.laton95.pyramidplunder.PyramidPlunder;
import net.minecraftforge.common.config.Config;

@Config(modid = PyramidPlunder.MOD_ID)
@Config.LangKey("pyramidplunder.config.title")
public class ModConfig
{
	@Config.Name("Generate Urns")
	@Config.Comment("Generate urns in world.")
	public static boolean generateUrns = true;
	
	@Config.Name("Urn Chance")
	@Config.Comment("Percentage of chunks to attempt to generate an urn in.")
	@Config.RangeDouble(min = 0, max = 1)
	public static double urnChance = 0.10D;
	
	@Config.Name("Urn Max Height")
	@Config.Comment("Maximum height urns will generate")
	public static int maxHeight = 50;
	
	@Config.Name("Urn Min Height")
	@Config.Comment("Minimum height urns will generate")
	public static int minHeight = 1;
	
	@Config.Name("Dimension Blacklist")
	@Config.Comment("Dimension ids of dimensions that urns will not generate in")
	public static int[] dimensionBlacklist = {-1, 1};
	
	@Config.Name("Biome Blacklist")
	@Config.Comment("Biome names of biomes that urns will not generate in")
	public static String[] biomeBlackList = {"deep_ocean"};
	
	@Config.Name("Snake Chance")
	@Config.Comment("Chance for a generated urn to contain a snake")
	@Config.RangeDouble(min = 0, max = 1)
	public static double snakeChance = 0.2;
}
