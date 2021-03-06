package com.laton95.pyramidplunder.world.gen.placement;

import com.laton95.pyramidplunder.config.Config;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.stream.Stream;

public class InCaveWithChance extends Placement<ChanceConfig> {

    public InCaveWithChance() {
        super(ChanceConfig.field_236950_a_);
    }

    @Override
    public Stream<BlockPos> getPositions(IWorld world, ChunkGenerator chunkGenerator, Random random, ChanceConfig config, BlockPos pos) {
        if (random.nextFloat() < 1.0F / (float) config.chance) {
            int x = random.nextInt(16);
            int z = random.nextInt(16);
            BlockPos blockpos = getLowestCave(world, new BlockPos(pos.getX() + x, 0, pos.getZ() + z));
            if (blockpos != null && world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, blockpos.getX(), blockpos.getZ()) != blockpos.getY()) {
                return Stream.of(blockpos);
            }
        }

        return Stream.empty();
    }

    private BlockPos getLowestCave(IWorld world, BlockPos pos) {
        BlockPos result = new BlockPos(pos.getX(), Config.MIN_HEIGHT.get(), pos.getZ());
        while (world.getBlockState(result).getMaterial().blocksMovement() || world.getBlockState(result).getBlock() == Blocks.LAVA) {
            if (result.getY() > Config.MAX_HEIGHT.get() || result.getY() > world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos).getY()) {
                return null;
            }
            result = result.up();
        }

        return result;
    }
}
