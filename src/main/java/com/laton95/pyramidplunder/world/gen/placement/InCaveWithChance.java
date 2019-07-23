package com.laton95.pyramidplunder.world.gen.placement;

import com.laton95.pyramidplunder.config.Config;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

public class InCaveWithChance extends Placement<NoPlacementConfig> {
	
	public InCaveWithChance(Function<Dynamic<?>, ? extends NoPlacementConfig> p_i51371_1_) {
		super(p_i51371_1_);
	}
	
	@Override
	public Stream<BlockPos> getPositions(IWorld world, ChunkGenerator<? extends GenerationSettings> chunkGenerator, Random random, NoPlacementConfig noPlacementConfig, BlockPos pos) {
		if(!Config.dimensionBlacklist.contains(world.getDimension().getType().getId()) && random.nextFloat() <= Config.urnChance) {
			int x = random.nextInt(16);
			int z = random.nextInt(16);
			BlockPos placement = getLowestCave(world, new BlockPos(pos.getX() + x, 0, pos.getZ() + z));
			if(placement != null) {
				
				if(world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, placement.getX(), placement.getZ()) != placement.getY()) {
					return Stream.of(new BlockPos(placement.getX(), placement.getY(), placement.getZ()));
				}
			}
		}
		
		return Stream.empty();
	}
	
	private BlockPos getLowestCave(IWorld world, BlockPos pos) {
		BlockPos result = new BlockPos(pos.getX(), Config.minHeight, pos.getZ());
		
		while(world.getBlockState(result).getMaterial().blocksMovement() || world.getBlockState(result).getBlock() == Blocks.LAVA) {
			if(result.getY() > Config.maxHeight || result.getY() > world.getDimension().getHorizon()) {
				return null;
			}
			result = result.up();
		}
		
		return result;
	}
}
