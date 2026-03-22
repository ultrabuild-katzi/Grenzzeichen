package de.raphicraft.grenzzeichen.compat.create.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import de.raphicraft.grenzzeichen.block.ModBlocks;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CarriageContraptionEntity.class)
public class CarriageContraptionEntityMixin {

    @Redirect(
            method = "control",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;rotateYCounterclockwise()Lnet/minecraft/util/math/Direction;"),
            require = 0
    )
    public Direction control(Direction instance, @Local(name = "info") StructureTemplate.StructureBlockInfo info) {
        return info.state().getBlock() == ModBlocks.FUEHRERSTAND ? instance.rotateYClockwise() : instance.rotateYCounterclockwise();
    }

}

