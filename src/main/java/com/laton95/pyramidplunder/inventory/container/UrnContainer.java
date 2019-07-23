package com.laton95.pyramidplunder.inventory.container;

import com.laton95.pyramidplunder.PyramidPlunder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class UrnContainer extends Container {
	
	private final IInventory urnInventory;
	
	public UrnContainer(int id, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
		this(id, playerInventory, new Inventory(10));
	}
	
	public UrnContainer(int id, IInventory playerInventory, IInventory urnInventory) {
		super(PyramidPlunder.URN_CONTAINER_TYPE, id);
		this.urnInventory = urnInventory;
		
		int xPosUrn = 44;
		int yPosUrn = 33;
		
		for(int i = 0; i < 2; ++i) {
			for(int j = 0; j < 5; ++j) {
				addSlot(new Slot(urnInventory, j + i * 5, xPosUrn + j * 18, yPosUrn + i * 18));
			}
		}
		
		int xPosPlayer = 8;
		int yPosPlayer = 81;
		
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				addSlot(new Slot(playerInventory, j + i * 9 + 9, xPosPlayer + j * 18, yPosPlayer + i * 18));
			}
		}
		
		for(int i = 0; i < 9; ++i) {
			addSlot(new Slot(playerInventory, i, xPosPlayer + i * 18, yPosPlayer + 58));
		}
	}
	
	@Override
	public boolean canInteractWith(PlayerEntity player) {
		return urnInventory.isUsableByPlayer(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if(index < 10) {
				if(!this.mergeItemStack(itemstack1, 10, 46, true)) {
					return ItemStack.EMPTY;
				}
			}
			else if(!this.mergeItemStack(itemstack1, 0, 10, false)) {
				return ItemStack.EMPTY;
			}
			
			if(itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}
			else {
				slot.onSlotChanged();
			}
			
			if(itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			
			slot.onTake(player, itemstack1);
		}
		
		return itemstack;
	}
}
