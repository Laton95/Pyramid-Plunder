package com.laton95.pyramidplunder.world.gen.placement;

import com.laton95.pyramidplunder.config.Config;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.BasePlacement;
import net.minecraft.world.gen.placement.NoPlacementConfig;

import java.util.Random;

public class InCaveWithChance extends BasePlacement<NoPlacementConfig> {
	
	public <C extends IFeatureConfig> boolean generate(IWorld worldIn, IChunkGenerator<? extends IChunkGenSettings> chunkGenerator, Random random, BlockPos pos, NoPlacementConfig placementConfig, Feature<C> featureIn, C featureConfig) {
		if(Config.dimensionBlacklist.contains(worldIn.getDimension().getType().getId())) {
			return false;
		}
		if(random.nextFloat() < Config.urnChance) {
			int x = random.nextInt(16);
			int z = random.nextInt(16);
			BlockPos blockpos = getLowestCave(worldIn, new BlockPos(pos.getX() + x, 0, pos.getZ() + z));
			if(blockpos != null) {
				boolean generate = true;
				if(worldIn.getHeight(Heightmap.Type.OCEAN_FLOOR, blockpos).getY() == blockpos.getY()) {
					generate = random.nextFloat() > 0.9f;
				}
				
				if(generate) {
					featureIn.place(worldIn, chunkGenerator, random, blockpos, featureConfig);
				}
			}
		}
		
		return true;
	}
	
	private BlockPos getLowestCave(IWorld worldIn, BlockPos pos) {
		BlockPos result = new BlockPos(pos.getX(), Config.minHeight, pos.getZ());
		
		while(worldIn.getBlockState(result).getMaterial().blocksMovement() || worldIn.getBlockState(result).getBlock() == Blocks.LAVA) {
			if(result.getY() > Config.maxHeight || result.getY() > worldIn.getDimension().getHorizon()) {
				return null;
			}
			result = result.up();
		}
		
		return result;
	}
}
