package com.laton95.pyramidplunder;

import com.laton95.pyramidplunder.client.gui.GuiUrn;
import com.laton95.pyramidplunder.inventory.ContainerUrn;
import com.laton95.pyramidplunder.tileentity.TileEntityUrn;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler
{
	@Nullable
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch(EnumGUI.values()[ID])
		{
			case URN:
				TileEntityUrn urn = (TileEntityUrn) world.getTileEntity(new BlockPos(x, y, z));
				ContainerUrn urnContainer = (ContainerUrn) urn.createContainer(player.inventory, player);
				return new ContainerUrn(player.inventory, urnContainer.getUrnInventory());
		}
		
		throw new IllegalArgumentException("No guid with id " + ID);
	}
	
	@Nullable
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch(EnumGUI.values()[ID])
		{
			case URN:
				return new GuiUrn(player.inventory, ((TileEntityUrn)world.getTileEntity(new BlockPos(x, y, z))).createContainer(player.inventory, player));
		}
		
		throw new IllegalArgumentException("No guid with id " + ID);
	}
	
	public enum EnumGUI
	{
		URN
	}
}
