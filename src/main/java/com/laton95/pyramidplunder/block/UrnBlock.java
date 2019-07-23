package com.laton95.pyramidplunder.block;

import com.laton95.pyramidplunder.PyramidPlunder;
import com.laton95.pyramidplunder.advancements.ModCriteriaTriggers;
import com.laton95.pyramidplunder.tileentity.UrnTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class UrnBlock extends ContainerBlock implements IWaterLoggable {
	
	private static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);
	
	public static final BooleanProperty OPEN = BooleanProperty.create("open");
	
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	public UrnBlock() {
		super(Properties.create(Material.ROCK).hardnessAndResistance(1.5f, 10.0f));
		this.setDefaultState(this.stateContainer.getBaseState().with(OPEN, true).with(WATERLOGGED, false));
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(OPEN, WATERLOGGED);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return getDefaultState().with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);
	}
	
	@Nullable
	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new UrnTileEntity();
	}
	
	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		return Container.calcRedstone(world.getTileEntity(pos));
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		if(state.get(WATERLOGGED)) {
			world.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		
		return super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		if(world.isRemote) {
			return true;
		}
		else {
			UrnTileEntity urn = (UrnTileEntity) world.getTileEntity(pos);
			
			if(urn != null) {
				if(state.get(OPEN)) {
					if(!player.isSneaking()) {
						NetworkHooks.openGui((ServerPlayerEntity) player, urn, buf -> buf.writeBlockPos(pos));
					}
					else if(player.getHeldItem(hand).isEmpty()) {
						world.setBlockState(pos, state.with(OPEN, false), 4);
					}
				}
				else {
					if(urn.hasSnake()) {
						if(player.getHeldItem(hand).getItem() == PyramidPlunder.SNAKE_CHARM) {
							ModCriteriaTriggers.CHARM_SNAKE.trigger((ServerPlayerEntity) player);
							player.sendMessage(new TranslationTextComponent(getCharmMessage(state), player.getDisplayName()));
						}
						else {
							ModCriteriaTriggers.SNAKE_BITE.trigger((ServerPlayerEntity) player);
							int duration = 15;
							player.addPotionEffect(new EffectInstance(Effects.POISON, duration * 20));
							player.sendMessage(new TranslationTextComponent(getPoisonMessage(state), player.getDisplayName()));
							player.attackEntityFrom(new DamageSource(getDamageType(state)), 3);
						}
						
						urn.removeSnake();
					}
					
					if(urn.hasLoot()) {
						ModCriteriaTriggers.LOOT_URN.trigger((ServerPlayerEntity) player);
					}
					
					world.setBlockState(pos, state.with(OPEN, true), 4);
				}
			}
			else {
				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
			}
			
			return true;
		}
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if(stack.hasDisplayName()) {
			TileEntity tileentity = world.getTileEntity(pos);
			if(tileentity instanceof UrnTileEntity) {
				((UrnTileEntity) tileentity).setCustomName(stack.getDisplayName());
			}
		}
	}
	
	@Override
	public void onReplaced(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if(oldState.getBlock() != newState.getBlock()) {
			TileEntity tileentity = world.getTileEntity(pos);
			if(tileentity instanceof UrnTileEntity) {
				InventoryHelper.dropInventoryItems(world, pos, (UrnTileEntity) tileentity);
				world.updateComparatorOutputLevel(pos, this);
				
				if(((UrnTileEntity) tileentity).hasSnake()) {
					PlayerEntity player = world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ());
					if(player != null) {
						ModCriteriaTriggers.SNAKE_BITE.trigger((ServerPlayerEntity) player);
						int duration = 15;
						player.addPotionEffect(new EffectInstance(Effects.POISON, duration * 20));
						player.sendMessage(new TranslationTextComponent(getBrokenPoisonMessage(oldState), player.getDisplayName()));
						player.attackEntityFrom(new DamageSource(getDamageType(oldState)), 3);
					}
				}
			}
			
			super.onReplaced(oldState, world, pos, newState, isMoving);
		}
	}
	
	private String getCharmMessage(BlockState state) {
		return state.get(WATERLOGGED) ? "tile.pyramidplunder.urn.charm_underwater" : "tile.pyramidplunder.urn.charm";
	}
	
	private String getPoisonMessage(BlockState state) {
		return state.get(WATERLOGGED) ? "tile.pyramidplunder.urn.poison_underwater" : "tile.pyramidplunder.urn.poison";
	}
	
	private String getBrokenPoisonMessage(BlockState state) {
		return state.get(WATERLOGGED) ? "tile.pyramidplunder.urn.poison_broken_underwater" : "tile.pyramidplunder.urn.poison_broken";
	}
	
	private String getDamageType(BlockState state) {
		return state.get(WATERLOGGED) ? "eelbite" : "snakebite";
	}
	
	@Override
	public IFluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}
}
