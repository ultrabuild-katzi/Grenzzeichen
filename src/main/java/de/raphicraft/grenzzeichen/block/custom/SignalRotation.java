package de.raphicraft.grenzzeichen.block.custom;

import de.raphicraft.grenzzeichen.block.entity.PreciseRotationBlockEntity;
import de.raphicraft.grenzzeichen.network.PrecisePlacementNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Adds 22.5 degree rotation on top of the 4 horizontal facings.
 * FACING keeps the 90 degree step (so existing worlds are unaffected),
 * FINE_ROTATION adds 0-3 extra 22.5 degree steps (clockwise, seen from above).
 * Holding Ctrl while placing overrides this with an exact 1 degree yaw stored in the block entity.
 */
public final class SignalRotation {
    public static final IntProperty FINE_ROTATION = IntProperty.of("fine_rotation", 0, 3);
    public static final float STEP_DEGREES = 22.5f;

    private SignalRotation() {}

    public static BlockState applyPlacement(BlockState state, DirectionProperty facing, ItemPlacementContext ctx) {
        int step = MathHelper.floor(ctx.getPlayerYaw() * 16.0f / 360.0f + 0.5f) & 15;
        return state
                .with(facing, Direction.fromHorizontal(step >> 2))
                .with(FINE_ROTATION, step & 3);
    }

    // Extra Y rotation applied after GeckoLib's rotation for the cardinal facing
    public static float extraRotationDegrees(BlockState state) {
        if (!state.contains(FINE_ROTATION)) return 0f;
        return -state.get(FINE_ROTATION) * STEP_DEGREES;
    }

    // Holding Ctrl while placing stores the exact yaw (1 degree steps) in the block entity
    public static void applyPrecisePlacement(World world, BlockPos pos, @Nullable LivingEntity placer) {
        if (world.isClient()) return;
        if (!(world.getBlockEntity(pos) instanceof PreciseRotationBlockEntity be)) return;
        boolean precise = placer instanceof PlayerEntity player && PrecisePlacementNetworking.isPrecise(player);
        be.setPreciseYaw(precise ? Math.round(placer.getYaw()) : PreciseRotationBlockEntity.NO_PRECISE_YAW);
    }

    // Y rotation (as used by GeckoLib's rotateBlock) for an exact placement yaw
    public static float preciseRotationDegrees(int preciseYaw) {
        return 180f - preciseYaw;
    }

    // Absolute placement yaw: exact 1 degree with Ctrl, otherwise snapped to 22.5 degree steps
    public static float placementYaw(float playerYaw, boolean precise) {
        return precise ? Math.round(playerYaw) : Math.round(playerYaw / STEP_DEGREES) * STEP_DEGREES;
    }
}
