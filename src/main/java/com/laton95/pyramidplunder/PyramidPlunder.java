package com.laton95.pyramidplunder;

import com.laton95.pyramidplunder.config.ModConfig;
import com.laton95.pyramidplunder.init.ModAdvancements;
import com.laton95.pyramidplunder.init.ModItems;
import com.laton95.pyramidplunder.proxy.IProxy;
import com.laton95.pyramidplunder.reference.ModReference;
import com.laton95.pyramidplunder.world.UrnGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = ModReference.MOD_ID, version = "1.12.2-1.0")
public class PyramidPlunder
{
	@Mod.Instance(ModReference.MOD_ID)
	public static PyramidPlunder instance;
	
	@SidedProxy(clientSide = ModReference.CLIENT_PROXY_CLASS, serverSide = ModReference.SERVER_PROXY_CLASS)
	public static IProxy proxy;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
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
}
