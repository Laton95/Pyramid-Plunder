package com.laton95.pyramidplunder.world.gen;

import com.laton95.pyramidplunder.config.Config;
import com.laton95.pyramidplunder.world.gen.feature.ModFeature;
import com.laton95.pyramidplunder.world.gen.placement.InCaveWithChance;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.CompositeFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.registries.ForgeRegistries;

import static com.laton95.pyramidplunder.PyramidPlunder.MOD_ID;
import static net.minecraft.world.biome.Biome.createCompositeFeature;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FeatureInit {
	
	private static CompositeFeature feature = createCompositeFeature(ModFeature.URNS, new NoFeatureConfig(), new InCaveWithChance(), new NoPlacementConfig());
	
	@SubscribeEvent
	public static void setup(FMLServerStartingEvent event) {
		for(Biome biome : ForgeRegistries.BIOMES.getValues()) {
			if(!Config.isBiomeBlacklisted(biome)) {
				if(!biome.getFeatures(GenerationStage.Decoration.UNDERGROUND_STRUCTURES).contains(feature)) {
					biome.addFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, feature);
				}
			}
			else {
				biome.getFeatures(GenerationStage.Decoration.UNDERGROUND_STRUCTURES).remove(feature);
			}
		}
	}
}
