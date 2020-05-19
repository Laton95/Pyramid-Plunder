package com.laton95.pyramidplunder;

import com.laton95.pyramidplunder.block.UrnBlock;
import com.laton95.pyramidplunder.client.gui.screen.inventory.UrnScreen;
import com.laton95.pyramidplunder.config.Config;
import com.laton95.pyramidplunder.inventory.container.UrnContainer;
import com.laton95.pyramidplunder.item.LootUrnItem;
import com.laton95.pyramidplunder.proxy.ClientProxy;
import com.laton95.pyramidplunder.proxy.ServerProxy;
import com.laton95.pyramidplunder.tileentity.UrnTileEntity;
import com.laton95.pyramidplunder.world.gen.feature.UrnFeature;
import com.laton95.pyramidplunder.world.gen.placement.InCaveWithChance;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.laton95.pyramidplunder.PyramidPlunder.MOD_ID;

@Mod(MOD_ID)
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PyramidPlunder {
	
	public static final String MOD_ID = "pyramidplunder";
	
	public static ServerProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> ServerProxy::new);
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	@ObjectHolder(PyramidPlunder.MOD_ID + ":urn")
	public static final UrnBlock URN = null;
	
	@ObjectHolder(PyramidPlunder.MOD_ID + ":treasure_urn")
	public static final LootUrnItem TREASURE_URN = null;
	
	@ObjectHolder(PyramidPlunder.MOD_ID + ":snake_charm")
	public static final Item SNAKE_CHARM = null;
	
	@ObjectHolder(PyramidPlunder.MOD_ID + ":urn")
	public static final TileEntityType<UrnTileEntity> URN_TILE = null;
	
	@ObjectHolder(PyramidPlunder.MOD_ID + ":urn")
	public static final ContainerType<UrnContainer> URN_CONTAINER_TYPE = null;
	
	@ObjectHolder(PyramidPlunder.MOD_ID + ":urn")
	public static final Feature<NoFeatureConfig> URN_FEATURE = null;
	
	public PyramidPlunder() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonConfig);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
	}
	
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		ScreenManager.registerFactory(URN_CONTAINER_TYPE, UrnScreen::new);
	}
	
	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		event.getRegistry().register(new UrnBlock().setRegistryName(PyramidPlunder.MOD_ID, "urn"));
	}
	
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		event.getRegistry().register(new LootUrnItem().setRegistryName(PyramidPlunder.MOD_ID, "treasure_urn"));
		
		event.getRegistry().register(new BlockItem(URN, new Item.Properties().group(ItemGroup.DECORATIONS)).setRegistryName(URN.getRegistryName()));
		
		event.getRegistry().register(new Item( new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS)).setRegistryName(PyramidPlunder.MOD_ID, "snake_charm"));
	}
	
	@SubscribeEvent
	public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
		event.getRegistry().register(TileEntityType.Builder.create(UrnTileEntity::new).build(null).setRegistryName(PyramidPlunder.MOD_ID, "urn"));
	}
	
	@SubscribeEvent
	public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
		event.getRegistry().register(IForgeContainerType.create(UrnContainer::new).setRegistryName(PyramidPlunder.MOD_ID, "urn"));
	}
	
	@SubscribeEvent
	public static void registerFeatures(final RegistryEvent.Register<Feature<?>> event) {
		event.getRegistry().register(new UrnFeature(NoFeatureConfig::deserialize).setRegistryName(PyramidPlunder.MOD_ID, "urn"));
	}
	
	@SubscribeEvent
	public static void applyFeatures(FMLCommonSetupEvent event) {
		if(Config.generateUrns) {
			Placement<ChanceConfig> inCave = new InCaveWithChance(ChanceConfig::deserialize);
			
			for(Biome biome : ForgeRegistries.BIOMES.getValues()) {
				if(!Config.isBiomeBlacklisted(biome)) {
					biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, URN_FEATURE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(inCave.configure(new ChanceConfig((int) (1 / Config.urnChance)))));
				}
			}
		}
	}
	
	public void commonConfig(ModConfig.ModConfigEvent event) {
		if(event.getConfig().getSpec() == Config.COMMON_SPEC) {
			Config.load();
		}
	}
}
