package com.laton95.pyramidplunder.world.gen.feature;

import com.laton95.pyramidplunder.PyramidPlunder;
import com.laton95.pyramidplunder.block.UrnBlock;
import com.laton95.pyramidplunder.config.Config;
import com.laton95.pyramidplunder.tileentity.UrnTileEntity;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;
import java.util.function.Function;

public class UrnFeature extends Feature<NoFeatureConfig> {
	
	public UrnFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> p_i49878_1_) {
		super(p_i49878_1_);
	}
	
	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> chunkGenerator, Random random, BlockPos pos, NoFeatureConfig config) {
		if(!Config.isDimensionBlacklisted(world)) {
			IFluidState ifluidstate = world.getFluidState(pos);
			world.setBlockState(pos, PyramidPlunder.URN.getDefaultState().with(UrnBlock.OPEN, false).with(UrnBlock.WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER), 11);
			
			if(!world.getBlockState(pos.down()).getMaterial().blocksMovement()) {
				world.setBlockState(pos.down(), Blocks.STONE.getDefaultState(), 11);
			}
			
			UrnTileEntity urn = (UrnTileEntity) world.getTileEntity(pos);
			if(urn != null) {
				urn.setLootTable(UrnTileEntity.URN_LOOT, random.nextLong());
				urn.putSnake(random);
			}
			
			return true;
		}
		
		return false;
	}
}
