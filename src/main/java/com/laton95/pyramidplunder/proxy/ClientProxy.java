package com.laton95.pyramidplunder.proxy;

import com.laton95.pyramidplunder.PyramidPlunder;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.*;

public class ClientProxy implements IProxy
{
	@Override
	public void registerRenders()
	{
		PyramidPlunder.registerRenders();
	}
}
