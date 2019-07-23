package com.laton95.pyramidplunder;

import com.laton95.pyramidplunder.block.UrnBlock;
import com.laton95.pyramidplunder.client.gui.screen.inventory.UrnScreen;
import com.laton95.pyramidplunder.config.Config;
import com.laton95.pyramidplunder.inventory.container.UrnContainer;
import com.laton95.pyramidplunder.item.LootUrnItem;
import com.laton95.pyramidplunder.proxy.ClientProxy;
import com.laton95.pyramidplunder.proxy.ServerProxy;
import com.laton95.pyramidplunder.tileentity.UrnTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
	public static UrnBlock URN;
	
	@ObjectHolder(PyramidPlunder.MOD_ID + ":treasure_urn")
	public static LootUrnItem TREASURE_URN;
	
	@ObjectHolder(PyramidPlunder.MOD_ID + ":snake_charm")
	public static Item SNAKE_CHARM;
	
	@ObjectHolder(PyramidPlunder.MOD_ID + ":urn")
	public static TileEntityType<UrnTileEntity> URN_TILE;
	
	@ObjectHolder(PyramidPlunder.MOD_ID + ":urn")
	public static ContainerType<UrnContainer> URN_CONTAINER_TYPE;
	
	public PyramidPlunder() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverConfig);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
	}
	
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		ScreenManager.registerFactory(URN_CONTAINER_TYPE, UrnScreen::new);
	}
	
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		URN = new UrnBlock();
		URN.setRegistryName(PyramidPlunder.MOD_ID, "urn");
		event.getRegistry().register(URN);
	}
	
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		TREASURE_URN = new LootUrnItem();
		TREASURE_URN.setRegistryName("treasure_urn");
		event.getRegistry().register(TREASURE_URN);
		
		Item.Properties builder = new Item.Properties().group(ItemGroup.DECORATIONS);
		BlockItem itemBlock = new BlockItem(URN, builder);
		itemBlock.setRegistryName(URN.getRegistryName());
		event.getRegistry().register(itemBlock);
		
		Item.Properties properties = new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS);
		SNAKE_CHARM = new Item(properties);
		SNAKE_CHARM.setRegistryName("snake_charm");
		event.getRegistry().register(SNAKE_CHARM);
	}
	
	@SubscribeEvent
	public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
		URN_TILE = TileEntityType.Builder.create(UrnTileEntity::new).build(null);
		URN_TILE.setRegistryName("urn");
		event.getRegistry().register(URN_TILE);
	}
	
	@SubscribeEvent
	public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
		event.getRegistry().register(IForgeContainerType.create(UrnContainer::new).setRegistryName("urn"));
	}
	
	public void serverConfig(ModConfig.ModConfigEvent event) {
		if(event.getConfig().getSpec() == Config.SERVER_SPEC) {
			Config.load();
		}
	}
}
