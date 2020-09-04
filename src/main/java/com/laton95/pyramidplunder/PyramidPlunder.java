package com.laton95.pyramidplunder;

import com.laton95.pyramidplunder.block.UrnBlock;
import com.laton95.pyramidplunder.client.gui.screen.inventory.UrnScreen;
import com.laton95.pyramidplunder.config.Config;
import com.laton95.pyramidplunder.inventory.container.UrnContainer;
import com.laton95.pyramidplunder.item.LootUrnItem;
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
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.laton95.pyramidplunder.PyramidPlunder.MOD_ID;

@Mod(MOD_ID)
@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PyramidPlunder {

    public static final String MOD_ID = "pyramidplunder";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, MOD_ID);

    public static final RegistryObject<UrnBlock> URN = BLOCKS.register("urn", UrnBlock::new);

    public static final RegistryObject<LootUrnItem> TREASURE_URN = ITEMS.register("treasure_urn", LootUrnItem::new);
    public static final RegistryObject<Item> URN_ITEM = ITEMS.register("urn", () -> new BlockItem(URN.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));
    public static final RegistryObject<Item> SNAKE_CHARM = ITEMS.register("snake_charm", () -> new Item(new Item.Properties().maxStackSize(1).group(ItemGroup.TOOLS)));

    public static final RegistryObject<TileEntityType<UrnTileEntity>> URN_TILE = TILE_ENTITIES.register("urn", () -> TileEntityType.Builder.create(UrnTileEntity::new, URN.get()).build(null));

    public static final RegistryObject<ContainerType<UrnContainer>> URN_CONTAINER = CONTAINERS.register("urn", () -> IForgeContainerType.create((id, playerInventory, data) -> new UrnContainer(id, playerInventory, playerInventory.player.getEntityWorld(), data.readBlockPos())));

    public static final RegistryObject<Feature<NoFeatureConfig>> URN_FEATURE = FEATURES.register("urn", UrnFeature::new);

    public PyramidPlunder() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);
    }

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(URN_CONTAINER.get(), UrnScreen::new);
    }

    @SubscribeEvent
    public static void applyFeatures(final ModConfig.Loading configEvent) {
        if (Config.GENERATE_URNS.get()) {
            for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
                if (!Config.isBiomeBlacklisted(biome)) {
                    biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, URN_FEATURE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(new InCaveWithChance().configure(new ChanceConfig((int) (1 / Config.URN_CHANCE.get())))));
                }
            }
        }
    }
}
