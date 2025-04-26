package de.raphicraft.grenzzeichen.block.settings;

import de.raphicraft.grenzzeichen.block.entity.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class HauptsignalBlockEntity extends BlockEntity implements GeoBlockEntity {
    public HauptsignalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.HAUPTSIGNAL_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return null;
    }
}
