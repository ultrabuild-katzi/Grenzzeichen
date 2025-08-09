package de.raphicraft.grenzzeichen.block.entity.client;

import de.raphicraft.grenzzeichen.block.custom.hauptsignalblock;
import de.raphicraft.grenzzeichen.block.entity.HauptsignalblockEntity;
import de.raphicraft.grenzzeichen.compat.CreateCompat;
import de.raphicraft.grenzzeichen.util.Result;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class HauptsignalblockModel<T extends GeoAnimatable> extends GeoModel<T> {

    @Override
    public Identifier getModelResource(GeoAnimatable animatable) {
        return new Identifier("grenzzeichen", "geo/hauptsignal.geo.json");
    }

    @Override
    public Identifier getTextureResource(GeoAnimatable animatable) {
        if (animatable instanceof HauptsignalblockEntity entity) {

            var world = entity.getWorld();
            if (world == null)
                return new Identifier("grenzzeichen", "textures/block/hauptsignal_invalid.png");

            BlockPos pos = entity.getPos();
            boolean powered = entity.getCachedState()
                    .getOrEmpty(hauptsignalblock.POWERED)
                    .orElse(false);

            // Attempt to get texture from Create
            Result<CreateCompat.GrenzzeichenSignalStates, CreateCompat.SignalTextureResult> signalTextureResult = CreateCompat.getSignalTexture(world, entity.lastSignalPosition);
            if(signalTextureResult.error() == CreateCompat.SignalTextureResult.BLOCK_BROKEN && entity.nextSignalSearchTime <= System.currentTimeMillis()) {
                entity.lastSignalPosition = CreateCompat.tryFindSignal(world, pos).orElse(null);
                entity.nextSignalSearchTime = System.currentTimeMillis() + 1000;
                signalTextureResult = CreateCompat.getSignalTexture(world, entity.lastSignalPosition);
            }

            if (signalTextureResult.data() != null) {
                return signalTextureResult.data().getHauptSignalTexture();
            }

            // Fallback redstone logic
            return powered
                    ? new Identifier("grenzzeichen", "textures/block/hauptsignal_red.png")
                    : new Identifier("grenzzeichen", "textures/block/hauptsignal_green.png");
        }

        // Fallback for item in hand or invalid animatable
        return new Identifier("grenzzeichen", "textures/block/hauptsignal_green.png");
    }

    @Override
    public Identifier getAnimationResource(GeoAnimatable animatable) {
        return new Identifier("grenzzeichen", "animations/default1.json");
    }
}
