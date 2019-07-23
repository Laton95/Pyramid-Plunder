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
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Random;

public class UrnTileEntity extends LockableLootTileEntity {
	
	public static final ResourceLocation URN_LOOT = new ResourceLocation(PyramidPlunder.MOD_ID, "urn");
	
	public static int SIZE = 10;
	
	private NonNullList<ItemStack> stacks = NonNullList.withSize(SIZE, ItemStack.EMPTY);
	
	private boolean hasSnake = false;
	
	public UrnTileEntity() {
		super(PyramidPlunder.URN_TILE);
	}
	
	@Override
	protected NonNullList<ItemStack> getItems() {
		return stacks;
	}
	
	@Override
	protected void setItems(NonNullList<ItemStack> itemsIn) {
		stacks = itemsIn;
	}
	
	@Override
	public int getSizeInventory() {
		return SIZE;
	}
	
	@Override
	public boolean isEmpty() {
		for(ItemStack itemstack : this.stacks) {
			if(!itemstack.isEmpty()) {
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
	public void read(CompoundNBT compound) {
		super.read(compound);
		stacks = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
		if(!this.checkLootAndRead(compound)) {
			ItemStackHelper.loadAllItems(compound, stacks);
		}
		
		if(compound.contains("CustomName", 8)) {
			setCustomName(ITextComponent.Serializer.fromJson(compound.getString("CustomName")));
		}
		
		if(compound.contains(hasSnakeTag)) {
			hasSnake = compound.getBoolean(hasSnakeTag);
		}
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		if(!this.checkLootAndWrite(compound)) {
			ItemStackHelper.saveAllItems(compound, this.stacks);
		}
		
		ITextComponent itextcomponent = this.getCustomName();
		if(itextcomponent != null) {
			compound.putString("CustomName", ITextComponent.Serializer.toJson(itextcomponent));
		}
		
		compound.putBoolean(hasSnakeTag, hasSnake);
		
		return compound;
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
}
