package de.raphicraft.grenzzeichen.block.settings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class db_sign extends Block {
    public db_sign(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        Direction dir = state.get(Properties.HORIZONTAL_FACING);
        return switch (dir) {
            case NORTH -> VoxelShapes.cuboid(-1.0f, 0.0f, 0.0f, 3.0f, 2.0f, 0.2f);
            case SOUTH -> VoxelShapes.cuboid(-1.0f, 0.0f, 0.2f, 3.0f, 2.0f, 1.0f);
            case EAST -> VoxelShapes.cuboid(0.2f, 0.0f, -1.0f, 1.0f, 2.0f, 3.0f);
            case WEST -> VoxelShapes.cuboid(0.0f, 0.0f, -1.0f, 0.2f, 2.0f, 3.0f);
            default -> VoxelShapes.fullCube();
        };
    }
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing());
    }

}
