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


                                    // check direction SignalBlockEntity From Create
                                    // NotStevy NotStevy NotStevy NotStevy NotStevy NotStevy NotStevy NotStevy NotStevy NotStevy
public class WeichensignalBlock extends Block{
    public WeichensignalBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }
                                        public static enum SignalState {
                                            RED, YELLOW, GREEN, INVALID;

                                            public boolean isRedLight(float renderTime) {
                                                return this == RED || this == INVALID && renderTime % 40 < 3;
                                            }

                                            public boolean isYellowLight(float renderTime) {
                                                return this == YELLOW;
                                            }

                                            public boolean isGreenLight(float renderTime) {
                                                return this == GREEN;
                                            }
                                        }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        return VoxelShapes.cuboid(0.25f, 0.0f, 0.25f, 0.75f, 1.0f, 0.75f);


    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing());
    }
}