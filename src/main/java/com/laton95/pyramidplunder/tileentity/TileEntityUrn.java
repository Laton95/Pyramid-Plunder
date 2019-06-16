package com.laton95.pyramidplunder.tileentity;

import com.laton95.pyramidplunder.config.ModConfig;
import com.laton95.pyramidplunder.inventory.ContainerUrn;
import com.laton95.pyramidplunder.reference.ModReference;
import com.laton95.pyramidplunder.util.LogHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class TileEntityUrn extends TileEntityLockableLoot
{
	
	public static final ResourceLocation URN_LOOT = LootTableList.register(new ResourceLocation(ModReference.MOD_ID, "urn"));
	
	private NonNullList<ItemStack> stacks = NonNullList.withSize(10, ItemStack.EMPTY);
	
	private boolean unopened = false;
	
	private boolean hasSnake;
	
	public void setUnopened()
	{
		unopened = true;
		setLootTable(TileEntityUrn.URN_LOOT, world.rand.nextLong());
		hasSnake = world.rand.nextFloat() < ModConfig.snakeChance;
	}
	
	@Override
	protected NonNullList<ItemStack> getItems()
	{
		return stacks;
	}
	
	@Override
	public int getSizeInventory()
	{
		return 10;
	}
	
	@Override
	public boolean isEmpty()
	{
		for (ItemStack itemstack : stacks)
		{
			if (!itemstack.isEmpty())
			{
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		fillWithLoot(playerIn);
		return new ContainerUrn(playerInventory, this);
	}
	
	@Override
	public String getGuiID()
	{
		return "pyramidplunder:urn";
	}
	
	@Override
	public String getName()
	{
		return this.hasCustomName() ? customName : "container.urn";
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.stacks = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
		
		if (!checkLootAndRead(compound))
		{
			ItemStackHelper.loadAllItems(compound, stacks);
		}
		
		if (compound.hasKey("CustomName", 8))
		{
			customName = compound.getString("CustomName");
		}
		
		if (compound.hasKey("Unopened"))
		{
			unopened = compound.getBoolean("Unopened");
		}
		
		if (compound.hasKey("HasSnake"))
		{
			hasSnake = compound.getBoolean("HasSnake");
		}
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return oldState.getBlock() != newSate.getBlock();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		
		if (!this.checkLootAndWrite(compound))
		{
			ItemStackHelper.saveAllItems(compound, stacks);
		}
		
		if (this.hasCustomName())
		{
			compound.setString("CustomName", customName);
		}
		
		compound.setBoolean("Unopened", unopened);
		
		compound.setBoolean("HasSnake", hasSnake);
		
		return compound;
	}
	
	public void setOpened()
	{
		this.unopened = false;
	}
	
	public boolean isUnopened()
	{
		return unopened;
	}
	
	public void removeSnake()
	{
		hasSnake = false;
	}
	
	public boolean hasSnake()
	{
		return hasSnake;
	}
}
