package com.laton95.pyramidplunder.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;

public class ClientProxy extends ServerProxy {
	
	@Override
	public ClientPlayerEntity getClientPlayer() {
		return Minecraft.getInstance().player;
	}
}
