package com.laton95.pyramidplunder.tileentity;

import com.laton95.pyramidplunder.PyramidPlunder;
import com.laton95.pyramidplunder.config.Config;
import com.laton95.pyramidplunder.inventory.container.UrnContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Random;

public class UrnTileEntity extends LockableLootTileEntity {

    public static final ResourceLocation URN_LOOT = new ResourceLocation(PyramidPlunder.MOD_ID, "urn");

    private ItemStackHandler inventory = new ItemStackHandler(10) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };

    private final LazyOptional<IItemHandler> inventoryOptional = LazyOptional.of(() -> inventory);

    private boolean hasSnake = false;

    public UrnTileEntity() {
        super(PyramidPlunder.URN_TILE.get());
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSlots();
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new UrnContainer(id, playerInventory, this.world, this.pos);
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

    @Override
    public void read(BlockState stateIn, CompoundNBT nbtIn) {
        super.read(stateIn, nbtIn);
        if (!this.checkLootAndRead(nbtIn)) {
            //Make sure items are transferred from old nbt tag, in case someone keeps their world between versions
            if (nbtIn.contains("Items")) {
                NonNullList<ItemStack> oldInventory = NonNullList.withSize(10, ItemStack.EMPTY);
                ItemStackHelper.loadAllItems(nbtIn, oldInventory);
                inventory = new ItemStackHandler(oldInventory);
            } else {
                inventory.deserializeNBT(getUpdateTag().getCompound("Inventory"));
            }
        }

        if (nbtIn.contains("HasSnake")) {
            hasSnake = nbtIn.getBoolean("HasSnake");
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbtOut) {
        super.write(nbtOut);
        if (!this.checkLootAndWrite(nbtOut)) {
            nbtOut.put("Inventory", inventory.serializeNBT());
        }

        nbtOut.putBoolean("HasSnake", hasSnake);

        return nbtOut;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return inventoryOptional.cast();
        }
        return super.getCapability(capability, side);
    }

    //Overridden methods from LockableLootTileEntity to be compadible with ItemStackHandler
    @Override
    protected NonNullList<ItemStack> getItems() {
        return null;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        inventory = new ItemStackHandler(items);
        markDirty();
    }

    @Override
    public boolean isEmpty() {
        this.fillWithLoot(null);
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        this.fillWithLoot(null);
        return inventory.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        this.fillWithLoot(null);
        ItemStack itemstack = index >= 0 && index < inventory.getSlots() && !inventory.getStackInSlot(index).isEmpty() && count > 0 ? inventory.getStackInSlot(index).split(count) : ItemStack.EMPTY;
        if (!itemstack.isEmpty()) {
            this.markDirty();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        this.fillWithLoot(null);
        return inventory.extractItem(index, inventory.getStackInSlot(index).getCount(), false);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.fillWithLoot(null);
        inventory.setStackInSlot(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        this.markDirty();
    }

    @Override
    public void clear() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public void putSnake(Random random) {
        hasSnake = random.nextFloat() < Config.SNAKE_CHANCE.get();
        markDirty();
    }

    public void removeSnake() {
        hasSnake = false;
        markDirty();
    }

    public boolean hasSnake() {
        return hasSnake;
    }
}
