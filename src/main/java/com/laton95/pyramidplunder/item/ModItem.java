package com.laton95.pyramidplunder.item;

import com.laton95.pyramidplunder.reference.ModReference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ModItem extends Item
{
	
	public ModItem(String name, boolean showInCreative)
	{
		this(name, showInCreative, 64);
	}
	
	public ModItem(String name, boolean showInCreative, int maxStackSize)
	{
		super();
		setUnlocalizedName(ModReference.MOD_ID + ":" + name);
		setRegistryName(ModReference.MOD_ID, name.toLowerCase());
		if(showInCreative)
		{
			setCreativeTab(CreativeTabs.TOOLS);
		}
		setMaxStackSize(maxStackSize);
	}
}
