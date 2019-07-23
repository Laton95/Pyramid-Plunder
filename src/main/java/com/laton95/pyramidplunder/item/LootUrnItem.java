package com.laton95.pyramidplunder.item;

import com.laton95.pyramidplunder.PyramidPlunder;
import com.laton95.pyramidplunder.block.UrnBlock;
import com.laton95.pyramidplunder.tileentity.UrnTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class LootUrnItem extends BlockItem {
	
	public LootUrnItem() {
		super(PyramidPlunder.URN, new Properties().rarity(Rarity.EPIC));
	}
	
	@Override
	protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return context.getWorld().setBlockState(context.getPos(), state.with(UrnBlock.OPEN, false).with(UrnBlock.WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER), 11);
	}
	
	@Override
	protected boolean onBlockPlaced(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
		MinecraftServer minecraftserver = world.getServer();
		if(minecraftserver != null) {
			UrnTileEntity urn = (UrnTileEntity) world.getTileEntity(pos);
			if(urn != null) {
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
