package de.raphicraft.grenzzeichen.block.settings;

import de.raphicraft.grenzzeichen.block.custom.SignalRotation;
import de.raphicraft.grenzzeichen.block.entity.RotatedSignalBlockEntity;
import de.raphicraft.grenzzeichen.network.PrecisePlacementNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Base for model-based signal blocks that can be placed at 22.5 degree steps (or 1 degree with Ctrl).
 * Blocks sitting exactly on their cardinal facing render as a normal block model. Any other angle sets
 * ROTATED, which hides the chunk model and lets a block entity renderer draw the same model rotated.
 * Subclasses must add ROTATED in appendProperties.
 */
public class RotatableSignalBlock extends Block implements BlockEntityProvider {
    public static final BooleanProperty ROTATED = BooleanProperty.of("rotated");

    public RotatableSignalBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(ROTATED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ROTATED);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return state.get(ROTATED) ? new RotatedSignalBlockEntity(pos, state) : null;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return state.get(ROTATED) ? BlockRenderType.INVISIBLE : BlockRenderType.MODEL;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState base = super.getPlacementState(ctx);
        if (base == null) return null;
        float yaw = SignalRotation.placementYaw(ctx.getPlayerYaw(), isPrecise(ctx.getPlayer()));
        float cardinal = hasFacing(base)
                ? Direction.fromRotation(ctx.getPlayerYaw()).asRotation()
                : 0f;
        return base.with(ROTATED, Math.abs(MathHelper.wrapDegrees(yaw - cardinal)) > 0.01f);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (world.isClient() || placer == null || !state.get(ROTATED)) return;
        if (world.getBlockEntity(pos) instanceof RotatedSignalBlockEntity be) {
            be.setYaw(SignalRotation.placementYaw(placer.getYaw(), isPrecise(placer)));
        }
    }

    private static boolean isPrecise(@Nullable LivingEntity entity) {
        return entity instanceof PlayerEntity player && PrecisePlacementNetworking.isPrecise(player);
    }

    private static boolean hasFacing(BlockState state) {
        return state.contains(Properties.HORIZONTAL_FACING);
    }

    /** Yaw of the cardinal facing the block model is already rotated to (0 for blocks without facing). */
    public static float facingYaw(BlockState state) {
        return hasFacing(state) ? state.get(Properties.HORIZONTAL_FACING).asRotation() : 0f;
    }
}
