package de.raphicraft.grenzzeichen.compat.create.mixin;

import com.simibubi.create.content.trains.entity.CarriageContraption;
import de.raphicraft.grenzzeichen.block.ModBlocks;
import de.raphicraft.grenzzeichen.block.settings.FUEHRERSTAND;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CarriageContraption.class, remap = false)
public class CarriageContraptionMixin {

    @Shadow
    private boolean sidewaysControls;

    @Shadow
    private boolean forwardControls;

    @Shadow
    private boolean backwardControls;

    @Shadow
    private Direction assemblyDirection;

    @Inject(
            method = "capture",
            at = @At("HEAD"),
            require = 0
    )
    public void allowUsageOfFuehrerstand(World world, BlockPos pos, CallbackInfoReturnable<Pair<StructureTemplate.StructureBlockInfo, BlockEntity>> cir) {
        BlockState blockState = world.getBlockState(pos);

        if (blockState.isOf(ModBlocks.FUEHRERSTAND)) {
            if (!blockState.contains(FUEHRERSTAND.FACING)) {
                return;
            }

            Direction facing = blockState.get(FUEHRERSTAND.FACING);
            if (facing.getAxis() != this.assemblyDirection.getAxis()) {
//                this.sidewaysControls = true;
            } else {
                boolean forwards = facing.getOpposite() == this.assemblyDirection;
                if (forwards)
                    this.forwardControls = true;
                else
                    this.backwardControls = true;
            }
        }
    }

}
