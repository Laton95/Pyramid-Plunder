package com.laton95.pyramidplunder.proxy;

import com.laton95.pyramidplunder.init.ModItems;
import net.minecraftforge.fml.common.event.*;

public class ClientProxy implements IProxy
{
	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
	
	}
	
	@Override
	public void init(FMLInitializationEvent event)
	{
	
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
	
	}
	
	@Override
	public void serverStarting(FMLServerStartingEvent event)
	{
	
	}
	
	@Override
	public void serverStopping(FMLServerStoppingEvent event)
	{
	
	}
	
	@Override
	public void registerRenders()
	{
		ModItems.registerRenders();
	}
}
