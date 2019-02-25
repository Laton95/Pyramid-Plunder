package com.laton95.pyramidplunder.tileentity;

import com.laton95.pyramidplunder.PyramidPlunder;
import com.laton95.pyramidplunder.client.gui.inventory.GuiUrn;
import com.laton95.pyramidplunder.config.Config;
import com.laton95.pyramidplunder.inventory.ContainerUrn;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.Random;

public class TileEntityUrn extends TileEntityLockableLoot implements IInteractionObject {
	
	public static final ResourceLocation URN_LOOT = LootTableList.register(new ResourceLocation(PyramidPlunder.MOD_ID, "urn"));
	
	public static int SIZE = 10;
	
	private NonNullList<ItemStack> stacks = NonNullList.withSize(SIZE, ItemStack.EMPTY);
	
	private boolean hasSnake = false;
	
	public TileEntityUrn() {
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
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		this.fillWithLoot(playerIn);
		return new ContainerUrn(playerInventory, this);
	}
	
	@Override
	public String getGuiID() {
		return GuiUrn.URN_GUI_ID.toString();
	}
	
	@Override
	public ITextComponent getName() {
		ITextComponent customName = getCustomName();
		return (customName != null ? customName : new TextComponentTranslation(PyramidPlunder.MOD_ID + ".container.urn"));
	}
	
	private static String hasSnakeTag = "HasSnake";
	
	@Override
	public void read(NBTTagCompound compound) {
		super.read(compound);
		stacks = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
		if(!this.checkLootAndRead(compound)) {
			ItemStackHelper.loadAllItems(compound, stacks);
		}
		
		if(compound.contains("CustomName", 8)) {
			customName = ITextComponent.Serializer.fromJson(compound.getString("CustomName"));
		}
		
		if(compound.contains(hasSnakeTag)) {
			hasSnake = compound.getBoolean(hasSnakeTag);
		}
	}
	
	@Override
	public NBTTagCompound write(NBTTagCompound compound) {
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
