package net.pcfractal.createcomputers.block;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.pcfractal.createcomputers.blockentity.RedstoneLinkBridgeBlockEntity;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

public class RedstoneLinkBridgeBlock extends BaseEntityBlock implements IWrenchable {
    public static final MapCodec<RedstoneLinkBridgeBlock> CODEC = simpleCodec(RedstoneLinkBridgeBlock::new);

    public RedstoneLinkBridgeBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    protected @NonNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public BlockEntity newBlockEntity(@NonNull BlockPos pos, @NonNull BlockState state) {
        return new RedstoneLinkBridgeBlockEntity(pos, state);
    }

    public @NonNull RenderShape getRenderShape(@NonNull BlockState state) {
        return RenderShape.MODEL;
    }

    protected @NonNull VoxelShape getShape(@NonNull BlockState state, @NonNull BlockGetter worldIn, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        return Block.box(1.0F, 0.0F, 1.0F, 15.0F, 5.0F, 15.0F);
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NonNull Level level, @NonNull BlockState state, @NonNull BlockEntityType<T> type) {
        return null;
    }

    public boolean isSignalSource(@NonNull BlockState state) {
        return false;
    }

    public int getSignal(@NonNull BlockState state, @NonNull BlockGetter world, @NonNull BlockPos pos, @NonNull Direction side) {
        return 0;
    }

    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (!level.isClientSide) {
            ItemStack stack = new ItemStack(this);
            level.destroyBlock(pos, false, player);
            if (player == null || !player.getInventory().add(stack)) {
                Block.popResource(level, pos, stack);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction playerFacing = context.getHorizontalDirection();
        return this.defaultBlockState().setValue(FACING, playerFacing.getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
