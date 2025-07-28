package de.raphicraft.grenzzeichen.block.entity.client;

import net.minecraft.util.Identifier;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

import static de.raphicraft.grenzzeichen.block.custom.hauptsignalbruecke.POWERED;

public class hauptsignalbrueckeModel<T extends GeoAnimatable> extends GeoModel<T> {


    @Override
    public Identifier getModelResource(GeoAnimatable animatable) {
        return new Identifier("grenzzeichen", "geo/hauptsignalbruecke.geo.json");
    }

    @Override
    public Identifier getTextureResource(GeoAnimatable animatable) {
        if (animatable instanceof de.raphicraft.grenzzeichen.block.entity.hauptsignalbrueckeEntity entity) {
            boolean powered = entity.getCachedState()
                    .getOrEmpty(de.raphicraft.grenzzeichen.block.custom.hauptsignalbruecke.POWERED)
                    .orElse(false);

            return powered
                    ? new Identifier("grenzzeichen", "textures/block/hauptsignalbruecke_off.png")
                    : new Identifier("grenzzeichen", "textures/block/hauptsignalbruecke.png");
        }
        return new Identifier("grenzzeichen", "textures/block/hauptsignalbruecke.png");
    }

    @Override
    public Identifier getAnimationResource(GeoAnimatable animatable) {
        return new Identifier("grenzzeichen", "animations/defaultanimation.json") ;
    }
}
