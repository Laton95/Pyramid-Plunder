package com.laton95.pyramidplunder.tileentity;

import com.laton95.pyramidplunder.PyramidPlunder;
import com.laton95.pyramidplunder.config.Config;
import com.laton95.pyramidplunder.inventory.container.UrnContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;
import java.util.Random;

public class UrnTileEntity extends LockableLootTileEntity {
	
	public static final ResourceLocation URN_LOOT = new ResourceLocation(PyramidPlunder.MOD_ID, "urn");
	
	private NonNullList<ItemStack> inventory = NonNullList.withSize(10, ItemStack.EMPTY);
	
	private LazyOptional<IItemHandler> inventoryHandler = LazyOptional.of(this::createInventory);
	
	private boolean hasSnake = false;
	
	public UrnTileEntity() {
		super(PyramidPlunder.URN_TILE);
	}
	
	@Override
	protected NonNullList<ItemStack> getItems() {
		return inventory;
	}
	
	@Override
	protected void setItems(NonNullList<ItemStack> items) {
		inventory = items;
	}
	
	@Override
	public int getSizeInventory() {
		return inventory.size();
	}
	
	@Override
	public boolean isEmpty() {
		for(ItemStack itemstack : inventory) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	protected Container createMenu(int id, PlayerInventory playerInventory) {
		this.fillWithLoot(playerInventory.player);
		return new UrnContainer(id, playerInventory, this);
	}
	
	@Override
	public ITextComponent getName() {
		ITextComponent customName = getCustomName();
		return (customName != null ? customName : getDefaultName());
	}
	
	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent(PyramidPlunder.MOD_ID + ".container.urn");
	}
	
	private static String hasSnakeTag = "HasSnake";
	
	@Override
	public void read(CompoundNBT nbt) {
		super.read(nbt);
		inventory = NonNullList.withSize(10, ItemStack.EMPTY);
		if (!this.checkLootAndRead(nbt)) {
			ItemStackHelper.loadAllItems(nbt, inventory);
		}
		
		if(nbt.contains(hasSnakeTag)) {
			hasSnake = nbt.getBoolean(hasSnakeTag);
		}
	}
	
	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		super.write(nbt);
		if (!this.checkLootAndWrite(nbt)) {
			ItemStackHelper.saveAllItems(nbt, inventory);
		}
		
		nbt.putBoolean(hasSnakeTag, hasSnake);
		
		return nbt;
	}
	
	public boolean hasLoot() {
		return lootTable != null;
	}
	
	public void putSnake(Random random) {
		hasSnake = random.nextFloat() < Config.snakeChance;
	}
	
	public void removeSnake() {
		hasSnake = false;
		markDirty();
	}
	
	public boolean hasSnake() {
		return hasSnake;
	}
	
	private IItemHandler createInventory() {
		return new InvWrapper(this);
	}
	
	@Nullable
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return inventoryHandler.cast();
		}
		return super.getCapability(capability, side);
	}
}
