package com.laton95.pyramidplunder.item;

import com.laton95.pyramidplunder.block.BlockUrn;
import com.laton95.pyramidplunder.init.ModBlocks;
import com.laton95.pyramidplunder.tileentity.TileEntityUrn;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemUrnPlacer extends ModItem
{
	public ItemUrnPlacer()
	{
		super("debug_urn", false, 1);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!worldIn.isRemote)
		{
			BlockPos urnPos = pos.offset(facing);
			worldIn.setBlockState(urnPos, ModBlocks.URN.getDefaultState().withProperty(BlockUrn.OPEN, false), 2);
			
			TileEntityUrn tileEntity = (TileEntityUrn) worldIn.getTileEntity(urnPos);
			
			if(tileEntity != null)
			{
				tileEntity.setUnopened();
			}
		}
		
		return EnumActionResult.SUCCESS;
	}
}
