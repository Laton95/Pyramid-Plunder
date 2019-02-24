package com.laton95.pyramidplunder.block;

import com.laton95.pyramidplunder.reference.ModReference;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

public abstract class ModBlockContainer extends BlockContainer
{
	public final boolean hasItem;
	
	public ModBlockContainer(String name, Material material, float hardness, Float resistance, String toolClass, int harvestLevel, boolean hasItem)
	{
		super(material);
		setUnlocalizedName(ModReference.MOD_ID + ":" + name);
		setRegistryName(ModReference.MOD_ID, name.toLowerCase());
		setHardness(hardness);
		setResistance(resistance);
		setHarvestLevel(toolClass, harvestLevel);
		this.hasItem = hasItem;
	}
}
