package com.laton95.pyramidplunder.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerUrn extends Container
{
	private final IInventory urnInventory;
	
	public ContainerUrn(IInventory playerInventory, IInventory urnInventory)
	{
		this.urnInventory = urnInventory;
		
		int xPosUrn = 44;
		int yPosUrn = 33;
		
		for(int i = 0; i < 2; ++i)
		{
			for(int j = 0; j < 5; ++j)
			{
				addSlotToContainer(new Slot(urnInventory, j + i * 5, xPosUrn + j * 18, yPosUrn + i * 18));
			}
		}
		
		int xPosPlayer = 8;
		int yPosPlayer = 81;
		
		for(int i = 0; i < 3; ++i)
		{
			for(int j = 0; j < 9; ++j)
			{
				addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, xPosPlayer + j * 18, yPosPlayer + i * 18));
			}
		}
		
		for(int i = 0; i < 9; ++i)
		{
			addSlotToContainer(new Slot(playerInventory, i, xPosPlayer + i * 18, yPosPlayer + 58));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return urnInventory.isUsableByPlayer(playerIn);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (index < 10)
			{
				if (!this.mergeItemStack(itemstack1, 10, 46, true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 0, 10, false))
			{
				return ItemStack.EMPTY;
			}
			
			if (itemstack1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}
			
			if (itemstack1.getCount() == itemstack.getCount())
			{
				return ItemStack.EMPTY;
			}
			
			slot.onTake(playerIn, itemstack1);
		}
		
		return itemstack;
	}
	
	public IInventory getUrnInventory()
	{
		return urnInventory;
	}
}
