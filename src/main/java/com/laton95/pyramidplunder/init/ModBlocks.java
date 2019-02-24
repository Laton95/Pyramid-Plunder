package com.laton95.pyramidplunder.init;

import com.laton95.pyramidplunder.block.BlockUrn;
import com.laton95.pyramidplunder.block.ModBlock;
import com.laton95.pyramidplunder.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ModBlocks
{
	public static final BlockUrn URN = (BlockUrn) new BlockUrn().setCreativeTab(CreativeTabs.DECORATIONS);
	
	private static Block[] blocks = new Block[]
			{
				URN
			};
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		LogHelper.info("Registering blocks");
		
		for(Block block : blocks)
		{
			if(block instanceof ModBlock && !((ModBlock) block).hasItem)
			{
				event.getRegistry().register(block);
			}
			else
			{
				ModItems.addBlock(block);
				event.getRegistry().register(block);
			}
		}
	}
}
