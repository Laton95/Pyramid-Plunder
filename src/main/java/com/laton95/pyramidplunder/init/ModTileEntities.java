package com.laton95.pyramidplunder.init;

import com.laton95.pyramidplunder.reference.ModReference;
import com.laton95.pyramidplunder.tileentity.TileEntityUrn;
import com.laton95.pyramidplunder.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class ModTileEntities
{
	@SubscribeEvent
	public static void registerTileEntities(RegistryEvent.Register<Block> event)
	{
		LogHelper.info("Registering tile entities");
		
		GameRegistry.registerTileEntity(TileEntityUrn.class, ModReference.MOD_ID + ":urn");
	}
}
