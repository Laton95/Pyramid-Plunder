package com.laton95.pyramidplunder.world;

import com.laton95.pyramidplunder.PyramidPlunder;
import com.laton95.pyramidplunder.block.BlockUrn;
import com.laton95.pyramidplunder.config.ModConfig;
import com.laton95.pyramidplunder.tileentity.TileEntityUrn;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class UrnGenerator implements IWorldGenerator
{
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
	{
		if(isSpawnableDimension(world.provider.getDimension()) && isSpawnableBiome(world, chunkX, chunkZ) && world.rand.nextFloat() > 1 - ModConfig.urnChance)
		{
			Random rand = new Random(world.getSeed() * chunkX * chunkZ);
			int xPos = rand.nextInt(16);
			int zPos = rand.nextInt(16);
			
			BlockPos urnPos = findCave(world, chunkX * 16 + xPos + 8, chunkZ * 16 + zPos + 8);
			
			if(urnPos != null)
			{
				world.setBlockState(urnPos, PyramidPlunder.URN.getDefaultState().withProperty(BlockUrn.OPEN, false), 2);
				
				TileEntityUrn urn = (TileEntityUrn) world.getTileEntity(urnPos);
				if(urn != null)
				{
					urn.setUnopened();
				}
			}
		}
	}
	
	private boolean isSpawnableBiome(World world, int chunkX, int chunkZ)
	{
		Biome biome = world.getBiome(new BlockPos(chunkX * 16, 0, chunkZ * 16));
		String biomeName = biome.getRegistryName().toString().substring(biome.getRegistryName().toString().indexOf(':') + 1);
		for(String name : ModConfig.biomeBlackList)
		{
			if(biomeName.equals(name))
			{
				return false;
			}
		}
		
		return true;
	}
	
	private boolean isSpawnableDimension(int dimId)
	{
		for(int id: ModConfig.dimensionBlacklist)
		{
			if(id == dimId)
			{
				return false;
			}
		}
		
		return true;
	}
	
	private BlockPos findCave(World world, int x, int z)
	{
		for(int y = ModConfig.minHeight; y < Math.min(ModConfig.maxHeight, world.provider.getHorizon()); y++)
		{
			BlockPos pos = new BlockPos(x, y, z);
			if((world.getBlockState(pos).getMaterial() == Material.AIR || world.getBlockState(pos).getMaterial() == Material.WATER) && world.getBlockState(pos.down()).isBlockNormalCube())
			{
				return pos;
			}
		}
		
		return null;
	}
}
