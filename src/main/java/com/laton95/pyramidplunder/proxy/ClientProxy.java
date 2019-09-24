package com.laton95.pyramidplunder.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.world.IWorld;

public class ClientProxy extends ServerProxy {
	
	@Override
	public IWorld getClientWorld() {
		return Minecraft.getInstance().world;
	}
}
