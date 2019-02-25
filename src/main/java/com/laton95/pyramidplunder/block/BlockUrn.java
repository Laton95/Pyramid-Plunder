package com.laton95.pyramidplunder.block;

import com.laton95.pyramidplunder.PyramidPlunder;
import com.laton95.pyramidplunder.advancements.ModCriteriaTriggers;
import com.laton95.pyramidplunder.tileentity.TileEntityUrn;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Fluids;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockUrn extends BlockContainer implements IBucketPickupHandler, ILiquidContainer {
	
	private static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);
	
	public static final BooleanProperty OPEN = BooleanProperty.create("open");
	
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	public BlockUrn() {
		super(Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 10.0f));
		this.setDefaultState(this.stateContainer.getBaseState().with(OPEN, true).with(WATERLOGGED, false));
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(OPEN, WATERLOGGED);
	}
	
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
		return SHAPE;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
	
	@Nullable
	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext context) {
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return getDefaultState().with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new TileEntityUrn();
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}
	
	@Override
	public IBlockState updatePostPlacement(IBlockState stateIn, EnumFacing facing, IBlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if(stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}
		
		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
	
	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(worldIn.isRemote) {
			return true;
		}
		else {
			TileEntityUrn urn = (TileEntityUrn) worldIn.getTileEntity(pos);
			
			if(urn != null) {
				if(state.get(OPEN)) {
					if(!player.isSneaking()) {
						NetworkHooks.openGui((EntityPlayerMP) player, urn, buf -> buf.writeBlockPos(pos));
					}
					else if(player.getHeldItem(hand).isEmpty()) {
						worldIn.setBlockState(pos, state.with(OPEN, false), 4);
					}
				}
				else {
					if(urn.hasSnake()) {
						if(player.getHeldItem(hand).getItem() == PyramidPlunder.SNAKE_CHARM) {
							ModCriteriaTriggers.CHARM_SNAKE.trigger((EntityPlayerMP) player);
							player.sendMessage(new TextComponentTranslation(getCharmMessage(state), player.getDisplayName()));
						}
						else {
							ModCriteriaTriggers.SNAKE_BITE.trigger((EntityPlayerMP) player);
							int duration = 15;
							player.addPotionEffect(new PotionEffect(Potion.getPotionById(19), duration * 20));
							player.sendMessage(new TextComponentTranslation(getPoisonMessage(state), player.getDisplayName()));
							player.attackEntityFrom(new DamageSource(getDamageType(state)), 3);
						}
						
						urn.removeSnake();
					}
					
					if(urn.hasLoot()) {
						ModCriteriaTriggers.LOOT_URN.trigger((EntityPlayerMP) player);
					}
					
					worldIn.setBlockState(pos, state.with(OPEN, true), 4);
				}
			}
			else {
				worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
			}
			
			return true;
		}
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, @Nullable EntityLivingBase placer, ItemStack stack) {
		if(stack.hasDisplayName()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if(tileentity instanceof TileEntityUrn) {
				((TileEntityUrn) tileentity).setCustomName(stack.getDisplayName());
			}
		}
	}
	
	@Override
	public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving) {
		if(state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if(tileentity instanceof TileEntityUrn) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityUrn) tileentity);
				worldIn.updateComparatorOutputLevel(pos, this);
				
				if(((TileEntityUrn) tileentity).hasSnake()) {
					EntityPlayer player = worldIn.getNearestAttackablePlayer(pos, 10, 10);
					if(player != null) {
						ModCriteriaTriggers.SNAKE_BITE.trigger((EntityPlayerMP) player);
						int duration = 15;
						player.addPotionEffect(new PotionEffect(Potion.getPotionById(19), duration * 20));
						player.sendMessage(new TextComponentTranslation(getBrokenPoisonMessage(state), player.getDisplayName()));
						player.attackEntityFrom(new DamageSource(getDamageType(state)), 3);
					}
				}
			}
			
			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
	
	public Fluid pickupFluid(IWorld worldIn, BlockPos pos, IBlockState state) {
		if(state.get(WATERLOGGED)) {
			worldIn.setBlockState(pos, state.with(WATERLOGGED, Boolean.valueOf(false)), 3);
			return Fluids.WATER;
		}
		else {
			return Fluids.EMPTY;
		}
	}
	
	public IFluidState getFluidState(IBlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}
	
	public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, IBlockState state, Fluid fluidIn) {
		return !state.get(WATERLOGGED) && fluidIn == Fluids.WATER;
	}
	
	public boolean receiveFluid(IWorld worldIn, BlockPos pos, IBlockState state, IFluidState fluidStateIn) {
		if(!state.get(WATERLOGGED) && fluidStateIn.getFluid() == Fluids.WATER) {
			if(!worldIn.isRemote()) {
				worldIn.setBlockState(pos, state.with(WATERLOGGED, Boolean.TRUE), 3);
				worldIn.getPendingFluidTicks().scheduleTick(pos, fluidStateIn.getFluid(), fluidStateIn.getFluid().getTickRate(worldIn));
			}
			
			return true;
		}
		else {
			return false;
		}
	}
	
	private String getCharmMessage(IBlockState state) {
		return state.get(WATERLOGGED) ? "tile.pyramidplunder.urn.charm_underwater" : "tile.pyramidplunder.urn.charm";
	}
	
	private String getPoisonMessage(IBlockState state) {
		return state.get(WATERLOGGED) ? "tile.pyramidplunder.urn.poison_underwater" : "tile.pyramidplunder.urn.poison";
	}
	
	private String getBrokenPoisonMessage(IBlockState state) {
		return state.get(WATERLOGGED) ? "tile.pyramidplunder.urn.poison_broken_underwater" : "tile.pyramidplunder.urn.poison_broken";
	}
	
	private String getDamageType(IBlockState state) {
		return state.get(WATERLOGGED) ? "eelbite" : "snakebite";
	}
}
