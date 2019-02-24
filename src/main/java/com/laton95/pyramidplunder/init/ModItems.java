package com.laton95.pyramidplunder.init;

import com.laton95.pyramidplunder.PyramidPlunder;
import com.laton95.pyramidplunder.item.ItemSnakeCharm;
import com.laton95.pyramidplunder.item.ItemUrnPlacer;
import com.laton95.pyramidplunder.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class ModItems
{
	
	public static ItemUrnPlacer URN_PLACER = new ItemUrnPlacer();
	
	public static ItemSnakeCharm SNAKE_CHARM = new ItemSnakeCharm();
	
	private static Item[] items = new Item[]
			{
				URN_PLACER,
				SNAKE_CHARM
			};
	
	private static ArrayList<Block> blocks = new ArrayList<>();
	
	private static ArrayList<ItemBlock> itemBlocks = new ArrayList<>();
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		LogHelper.info("Registering items");
		for(Item item : items)
		{
			event.getRegistry().register(item);
		}
		
		for(Block block : blocks)
		{
			ItemBlock itemBlock = new ItemBlock(block);
			itemBlock.setRegistryName(block.getRegistryName());
			event.getRegistry().register(itemBlock);
			itemBlocks.add(itemBlock);
		}
		
		PyramidPlunder.proxy.registerRenders();
	}
	
	public static void registerRenders()
	{
		for(Item item : items)
		{
			registerItemRender(item);
		}
		
		for(ItemBlock itemBlock : itemBlocks)
		{
			registerBlockRender(itemBlock, "normal");
		}
	}
	
	private static void registerItemRender(Item item)
	{
		final ModelResourceLocation fullModelLocation = new ModelResourceLocation(item.getRegistryName(), "inventory");
		ModelBakery.registerItemVariants(item, fullModelLocation);
		ModelLoader.setCustomMeshDefinition(item, stack -> fullModelLocation);
	}
	
	private static void registerBlockRender(ItemBlock block, String variant)
	{
		ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation(block.getRegistryName(), variant));
	}
	
	public static void addBlock(Block block)
	{
		blocks.add(block);
	}
}
