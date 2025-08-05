package de.raphicraft.grenzzeichen.block.entity.client;

import net.minecraft.util.Identifier;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class HauptsignalblockModel<T extends GeoAnimatable> extends GeoModel<T> {


    @Override
    public Identifier getModelResource(GeoAnimatable animatable) {
        return new Identifier("grenzzeichen", "geo/hauptsignal.geo.json");
    }

    @Override
    public Identifier getTextureResource(GeoAnimatable animatable) {
        if (animatable instanceof de.raphicraft.grenzzeichen.block.entity.HauptsignalblockEntity entity) {
            boolean powered = entity.getCachedState()
                    .getOrEmpty(de.raphicraft.grenzzeichen.block.custom.hauptsignalblock.POWERED)
                    .orElse(false);

            return powered
                    ? new Identifier("grenzzeichen", "textures/block/hauptsignal_red.png")
                    : new Identifier("grenzzeichen", "textures/block/hauptsignal_green.png");
        }
        return new Identifier("grenzzeichen", "textures/block/hauptsignal_green.png");
    }

    @Override
    public Identifier getAnimationResource(GeoAnimatable animatable) {
        return new Identifier("grenzzeichen", "animations/default1.json") ;
    }
}
