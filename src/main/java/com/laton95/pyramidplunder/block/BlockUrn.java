package com.laton95.pyramidplunder.block;

import com.laton95.pyramidplunder.GuiHandler;
import com.laton95.pyramidplunder.PyramidPlunder;
import com.laton95.pyramidplunder.advancement.Triggers;
import com.laton95.pyramidplunder.tileentity.TileEntityUrn;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockUrn extends BlockContainer
{
	
	private static final AxisAlignedBB OPEN_BB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.9375D, 0.9375D);
	
	public static final PropertyBool OPEN = PropertyBool.create("open");
	
	public BlockUrn()
	{
		super(Material.ROCK);
		setCreativeTab(CreativeTabs.DECORATIONS);
		setHardness(1.5f);
		setResistance(10.0f);
		setHarvestLevel("pickaxe", 1);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, OPEN);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(OPEN) ? 0 : 1;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(OPEN, meta == 0);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return OPEN_BB;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
		{
			return true;
		}
		else
		{
			TileEntityUrn tileEntity = (TileEntityUrn) worldIn.getTileEntity(pos);
			
			if(state.getValue(OPEN))
			{
				if(playerIn.isSneaking() && playerIn.getHeldItem(hand).isEmpty())
				{
					worldIn.setBlockState(pos, state.withProperty(OPEN, false), 2);
				}
				else
				{
					if(tileEntity != null)
					{
						playerIn.openGui(PyramidPlunder.instance, GuiHandler.EnumGUI.URN.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
					}
				}
			}
			else
			{
				if(tileEntity != null && tileEntity.hasSnake() && playerIn.getHeldItem(hand).getItem() == PyramidPlunder.SNAKE_CHARM)
				{
					Triggers.CHARM_SNAKE.trigger((EntityPlayerMP) playerIn);
					tileEntity.removeSnake();
					playerIn.sendMessage(new TextComponentTranslation("tile.pyramidplunder:urn.charm", playerIn.getDisplayName()));
				}
				else
				{
					worldIn.setBlockState(pos, state.withProperty(OPEN, true), 2);
					if(tileEntity != null)
					{
						if(tileEntity.isUnopened())
						{
							Triggers.LOOT_URN.trigger((EntityPlayerMP) playerIn);
							
							if(tileEntity.hasSnake())
							{
								Triggers.SNAKE_BITE.trigger((EntityPlayerMP) playerIn);
								tileEntity.removeSnake();
								int duration = 15;
								playerIn.addPotionEffect(new PotionEffect(Potion.getPotionById(19), duration*20));
								playerIn.sendMessage(new TextComponentTranslation("tile.pyramidplunder:urn.poison", playerIn.getDisplayName()));
								playerIn.attackEntityFrom(new DamageSource("snakebite"), 3);
							}
							
							tileEntity.fillWithLoot(playerIn);
						}
						
						tileEntity.setOpened();
					}
				}
			}
			
			return true;
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		worldIn.setBlockState(pos, state, 2);
		
		if (stack.hasDisplayName())
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);
			
			if (tileentity instanceof TileEntityUrn)
			{
				((TileEntityUrn)tileentity).setCustomName(stack.getDisplayName());
			}
		}
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntityUrn tileentity = (TileEntityUrn) worldIn.getTileEntity(pos);
		
		if (tileentity != null)
		{
			InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityUrn)tileentity);
			worldIn.updateComparatorOutputLevel(pos, this);
			
			if(tileentity.hasSnake())
			{
				EntityPlayer player = worldIn.getNearestAttackablePlayer(pos, 10, 10);
				if(player != null)
				{
					Triggers.SNAKE_BITE.trigger((EntityPlayerMP) player);
					int duration = 15;
					player.addPotionEffect(new PotionEffect(Potion.getPotionById(19), duration * 20));
					player.sendMessage(new TextComponentTranslation("tile.pyramidplunder:urn.poison_broken", player.getDisplayName()));
					player.attackEntityFrom(new DamageSource("snakebite"), 3);
				}
			}
		}
		
		super.breakBlock(worldIn, pos, state);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean hasComparatorInputOverride(IBlockState state)
	{
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	{
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityUrn();
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
}
