package com.laton95.pyramidplunder;

import com.laton95.pyramidplunder.client.gui.inventory.GuiUrn;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class GuiHandler {
	
	public static GuiScreen openGui(FMLPlayMessages.OpenContainer openContainer) {
		if(openContainer.getId().equals(GuiUrn.URN_GUI_ID)) {
			BlockPos pos = openContainer.getAdditionalData().readBlockPos();
			return new GuiUrn(Minecraft.getInstance().player.inventory, (IInventory) Minecraft.getInstance().world.getTileEntity(pos));
		}
		
		return null;
	}
}
