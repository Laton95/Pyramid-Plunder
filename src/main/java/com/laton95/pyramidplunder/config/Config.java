package com.laton95.pyramidplunder.config;

import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class Config {

    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue GENERATE_URNS;
    public static ForgeConfigSpec.DoubleValue URN_CHANCE;
    public static ForgeConfigSpec.IntValue MAX_HEIGHT;
    public static ForgeConfigSpec.IntValue MIN_HEIGHT;
    public static ForgeConfigSpec.DoubleValue SNAKE_CHANCE;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> DIMENSION_BLACKLIST;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BIOME_BLACKLIST;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        COMMON_BUILDER.comment("General settings")
                .push("general");

        GENERATE_URNS = COMMON_BUILDER.comment("Generate urns in world")
                .define("generate", true);
        URN_CHANCE = COMMON_BUILDER.comment("Percentage chance to attempt to place an urn in a chunk. Actual frequency of urns will be lower as attempts are not always successful")
                .defineInRange("urn_chance", 0.15, 0, 1);
        MAX_HEIGHT = COMMON_BUILDER.comment("Maximum height urns will generate")
                .defineInRange("max_height", 40, 0, 255);
        MIN_HEIGHT = COMMON_BUILDER.comment("Minimum height urns will generate")
                .defineInRange("min_height", 40, 0, 255);
        SNAKE_CHANCE = COMMON_BUILDER.comment("Chance an urn will contain a snake")
                .defineInRange("snake_chance", 0.3, 0, 1);
        DIMENSION_BLACKLIST = COMMON_BUILDER.comment("Dimension ids of dimensions that urns will not generate in")
                .defineList("dimension_blacklist", getDimensionBlacklistDefault(), i -> i instanceof String);
        BIOME_BLACKLIST = COMMON_BUILDER.comment("Biomes to not generate urns in, in format \"mod_id:biome_id\". E.g. \"minecraft:desert\"")
                .defineList("biome_blacklist", new ArrayList<>(), s -> s instanceof String);

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
        if (MAX_HEIGHT.get() < MIN_HEIGHT.get()) {
            throw new IllegalArgumentException(String.format("Max height cannot be larger than min height: %d > %d", MIN_HEIGHT.get(), MAX_HEIGHT.get()));
        }
    }

    private static List<String> getDimensionBlacklistDefault() {
        ArrayList<String> output = new ArrayList<>();

        output.add("minecraft:the_nether");
        output.add("minecraft:the_end");

        return output;
    }

    public static boolean isDimensionBlacklisted(ISeedReader world) {
        if (world instanceof WorldGenRegion) {
            return DIMENSION_BLACKLIST.get().contains(((WorldGenRegion) world).world.func_234923_W_().func_240901_a_().toString());
        }

        return false;
    }

    public static boolean isBiomeBlacklisted(Biome biome) {
        return DIMENSION_BLACKLIST.get().contains(biome.getRegistryName().toString());
    }
}
