package com.laton95.pyramidplunder.block;

import com.laton95.pyramidplunder.reference.ModReference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class ModBlock extends Block
{
	public final boolean hasItem;
	
	public ModBlock(String name, Material material, float hardness, Float resistance, String toolClass, int harvestLevel, boolean hasItem)
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
