package com.laton95.pyramidplunder.item;

import com.laton95.pyramidplunder.PyramidPlunder;
import com.laton95.pyramidplunder.block.UrnBlock;
import com.laton95.pyramidplunder.tileentity.UrnTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class LootUrnItem extends BlockItem {

    public LootUrnItem() {
        super(PyramidPlunder.URN.get(), new Properties().rarity(Rarity.EPIC));
    }

    @Override
    protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
        return super.placeBlock(context, state.with(UrnBlock.OPEN, false));
    }

    @Override
    protected boolean onBlockPlaced(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        if (!world.isRemote) {
            UrnTileEntity urn = (UrnTileEntity) world.getTileEntity(pos);
            if (urn != null) {
                urn.setLootTable(UrnTileEntity.URN_LOOT, world.rand.nextLong());
                urn.putSnake(world.rand);
            }
        }

        return super.onBlockPlaced(pos, world, player, stack, state);
    }

    @Override
    public String getTranslationKey() {
        return "item.pyramidplunder.treasure_urn";
    }
}
