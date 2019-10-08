package com.laton95.pyramidplunder;

import com.laton95.pyramidplunder.block.BlockUrn;
import com.laton95.pyramidplunder.config.ModConfig;
import com.laton95.pyramidplunder.init.ModAdvancements;
import com.laton95.pyramidplunder.item.ItemUrnPlacer;
import com.laton95.pyramidplunder.proxy.IProxy;
import com.laton95.pyramidplunder.tileentity.TileEntityUrn;
import com.laton95.pyramidplunder.world.UrnGenerator;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.laton95.pyramidplunder.PyramidPlunder.MOD_ID;

@Mod(modid = MOD_ID, version = "1.12.2-1.5")
@Mod.EventBusSubscriber
public class PyramidPlunder
{
	public static final String MOD_ID = "pyramidplunder";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	@Mod.Instance(MOD_ID)
	public static PyramidPlunder instance;
	
	@SidedProxy(clientSide = "com.laton95.pyramidplunder.proxy.ClientProxy", serverSide = "com.laton95.pyramidplunder.proxy.ServerProxy")
	public static IProxy proxy;
	
	@ObjectHolder(PyramidPlunder.MOD_ID + ":urn")
	public static final BlockUrn URN = null;
	
	@ObjectHolder(PyramidPlunder.MOD_ID + ":debug_urn")
	public static final ItemUrnPlacer URN_PLACER = null;
	
	@ObjectHolder(PyramidPlunder.MOD_ID + ":snake_charm")
	public static final Item SNAKE_CHARM = null;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		ModAdvancements.registerAdvancementTriggers();
		
		if(ModConfig.generateUrns)
		{
			UrnGenerator urnGenerator = new UrnGenerator();
			MinecraftForge.EVENT_BUS.register(urnGenerator);
			MinecraftForge.TERRAIN_GEN_BUS.register(urnGenerator);
			GameRegistry.registerWorldGenerator(urnGenerator, 0);
		}
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.registerRenders();
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		BlockUrn urn = new BlockUrn();
		urn.setUnlocalizedName(MOD_ID + ":" + "urn");
		urn.setRegistryName(MOD_ID, "urn");
		event.getRegistry().register(urn);
		
		GameRegistry.registerTileEntity(TileEntityUrn.class, PyramidPlunder.MOD_ID + ":urn");
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		Item urnPlacer = new ItemUrnPlacer();
		setupItem(urnPlacer, "debug_urn", 64, false);
		event.getRegistry().register(urnPlacer);
		
		Item snakeCharm = new Item();
		setupItem(snakeCharm, "snake_charm", 1, true);
		event.getRegistry().register(snakeCharm);
		
		ItemBlock itemBlock = new ItemBlock(URN);
		itemBlock.setRegistryName(URN.getRegistryName());
		event.getRegistry().register(itemBlock);
	}
	
	private static void setupItem(Item item, String name, int maxStackSize, boolean showInCreative) {
		item.setUnlocalizedName(PyramidPlunder.MOD_ID + ":" + name);
		item.setRegistryName(PyramidPlunder.MOD_ID, name.toLowerCase());
		if(showInCreative) {
			item.setCreativeTab(CreativeTabs.TOOLS);
		}
		item.setMaxStackSize(maxStackSize);
	}
	
	public static void registerRenders()
	{
		registerItemRender(URN_PLACER);
		registerItemRender(SNAKE_CHARM);
		
		registerBlockRender((ItemBlock) Item.getItemFromBlock(URN), "normal");
	}
	
	private static void registerItemRender(Item item)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	private static void registerBlockRender(ItemBlock block, String variant)
	{
		ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation(block.getRegistryName(), variant));
	}
}
