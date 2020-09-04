package com.laton95.pyramidplunder.world.gen.feature;

import com.laton95.pyramidplunder.PyramidPlunder;
import com.laton95.pyramidplunder.block.UrnBlock;
import com.laton95.pyramidplunder.config.Config;
import com.laton95.pyramidplunder.tileentity.UrnTileEntity;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;

import java.util.Random;

public class UrnFeature extends Feature<NoFeatureConfig> {

    public UrnFeature() {
        super(NoFeatureConfig.field_236558_a_);
    }

    @Override
    public boolean func_230362_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator chunkGenerator, Random random, BlockPos pos, NoFeatureConfig config) {
        if (!Config.isDimensionBlacklisted(world)) {
            FluidState fluidstate = world.getFluidState(pos);
            world.setBlockState(pos, PyramidPlunder.URN.get().getDefaultState().with(UrnBlock.OPEN, false).with(UrnBlock.WATERLOGGED, fluidstate.getFluid() == Fluids.WATER), 11);

            if (!world.getBlockState(pos.down()).getMaterial().blocksMovement()) {
                world.setBlockState(pos.down(), Blocks.STONE.getDefaultState(), 11);
            }

            TileEntity urn = world.getTileEntity(pos);
            if (urn instanceof UrnTileEntity) {
                ((UrnTileEntity) urn).setLootTable(UrnTileEntity.URN_LOOT, random.nextLong());
                ((UrnTileEntity) urn).putSnake(random);
            }

            return true;
        }

        return false;
    }
}
