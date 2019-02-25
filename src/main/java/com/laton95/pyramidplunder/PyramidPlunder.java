package com.laton95.pyramidplunder;

import com.laton95.pyramidplunder.block.BlockUrn;
import com.laton95.pyramidplunder.config.Config;
import com.laton95.pyramidplunder.item.ItemUrnPlacer;
import com.laton95.pyramidplunder.proxy.ClientProxy;
import com.laton95.pyramidplunder.proxy.ServerProxy;
import com.laton95.pyramidplunder.tileentity.TileEntityUrn;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.laton95.pyramidplunder.PyramidPlunder.MOD_ID;

@Mod(MOD_ID)
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PyramidPlunder {
	
	public static final String MOD_ID = "pyramidplunder";
	
	public static ServerProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> ServerProxy::new);
	
	public static Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	@ObjectHolder(PyramidPlunder.MOD_ID + ":urn")
	public static BlockUrn URN;
	
	@ObjectHolder(PyramidPlunder.MOD_ID + ":treasure_urn")
	public static ItemUrnPlacer TREASURE_URN;
	
	@ObjectHolder(PyramidPlunder.MOD_ID + ":snake_charm")
	public static Item SNAKE_CHARM;
	
	@ObjectHolder(PyramidPlunder.MOD_ID + "urn")
	public static TileEntityType<TileEntityUrn> URN_TILE;
	
	@ObjectHolder(PyramidPlunder.MOD_ID + ":snake")
	public static SoundEvent SNAKE;
	
	public PyramidPlunder() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverConfig);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
	}
	
	@SubscribeEvent
	public static void setup(FMLCommonSetupEvent event) {
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> GuiHandler::openGui);
	}
	
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		URN = new BlockUrn();
		URN.setRegistryName(PyramidPlunder.MOD_ID, "urn");
		event.getRegistry().register(URN);
	}
	
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		Item.Properties builder = new Item.Properties().group(ItemGroup.DECORATIONS);
		ItemBlock itemBlock = new ItemBlock(URN, builder);
		itemBlock.setRegistryName(URN.getRegistryName());
		event.getRegistry().register(itemBlock);
		
		TREASURE_URN = new ItemUrnPlacer();
		TREASURE_URN.setRegistryName("treasure_urn");
		event.getRegistry().register(TREASURE_URN);
		
		Item.Properties properties = new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS);
		SNAKE_CHARM = new Item(properties);
		SNAKE_CHARM.setRegistryName("snake_charm");
		event.getRegistry().register(SNAKE_CHARM);
	}
	
	@SubscribeEvent
	public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
		URN_TILE = TileEntityType.Builder.create(TileEntityUrn::new).build(null);
		URN_TILE.setRegistryName("urn");
		event.getRegistry().register(URN_TILE);
	}
	
	@SubscribeEvent
	public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().register(new SoundEvent(new ResourceLocation(MOD_ID, "snake")).setRegistryName("snake"));
	}
	
	public void serverConfig(ModConfig.ModConfigEvent event) {
		if(event.getConfig().getSpec() == Config.SERVER_SPEC) {
			Config.load();
		}
	}
}
