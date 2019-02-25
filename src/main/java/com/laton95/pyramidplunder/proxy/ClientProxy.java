package com.laton95.pyramidplunder.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class ClientProxy extends ServerProxy {
	
	@Override
	public EntityPlayerSP getClientPlayer() {
		return Minecraft.getInstance().player;
	}
}
