package com.laton95.pyramidplunder.world.gen.feature;

import com.laton95.pyramidplunder.PyramidPlunder;
import com.laton95.pyramidplunder.block.BlockUrn;
import com.laton95.pyramidplunder.tileentity.TileEntityUrn;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class UrnFeature extends Feature<NoFeatureConfig> {
	
	@Override
	public boolean place(IWorld world, IChunkGenerator<? extends IChunkGenSettings> chunkGenerator, Random rand, BlockPos pos, NoFeatureConfig config) {
		IFluidState ifluidstate = world.getFluidState(pos);
		world.setBlockState(pos, PyramidPlunder.URN.getDefaultState().with(BlockUrn.OPEN, false).with(BlockUrn.WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER), 11);
		
		if(!world.getBlockState(pos.down()).getMaterial().blocksMovement()) {
			world.setBlockState(pos.down(), Blocks.STONE.getDefaultState(), 11);
		}
		
		TileEntityUrn urn = (TileEntityUrn) world.getTileEntity(pos);
		if(urn != null) {
			urn.setLootTable(TileEntityUrn.URN_LOOT, rand.nextLong());
			urn.putSnake(rand);
		}
		
		return true;
	}
}
