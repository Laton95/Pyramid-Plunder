package com.laton95.pyramidplunder.item;

import com.laton95.pyramidplunder.PyramidPlunder;
import com.laton95.pyramidplunder.block.BlockUrn;
import com.laton95.pyramidplunder.tileentity.TileEntityUrn;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemUrnPlacer extends ItemBlock {
	
	public ItemUrnPlacer() {
		super(PyramidPlunder.URN, new Properties().rarity(EnumRarity.EPIC));
	}
	
	@Override
	protected boolean placeBlock(BlockItemUseContext context, IBlockState state) {
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return context.getWorld().setBlockState(context.getPos(), state.with(BlockUrn.OPEN, false).with(BlockUrn.WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER), 11);
	}
	
	@Override
	protected boolean onBlockPlaced(BlockPos pos, World worldIn, @Nullable EntityPlayer player, ItemStack stack, IBlockState state) {
		MinecraftServer minecraftserver = worldIn.getServer();
		if(minecraftserver != null) {
			TileEntityUrn urn = (TileEntityUrn) worldIn.getTileEntity(pos);
			if(urn != null) {
				urn.setLootTable(TileEntityUrn.URN_LOOT, worldIn.rand.nextLong());
				urn.putSnake(worldIn.rand);
			}
		}
		
		return super.onBlockPlaced(pos, worldIn, player, stack, state);
	}
	
	@Override
	public String getTranslationKey() {
		return "item.pyramidplunder.treasure_urn";
	}
}
