package de.raphicraft.grenzzeichen.block.entity.client;

import de.raphicraft.grenzzeichen.block.entity.OrbyEntity;
import de.raphicraft.grenzzeichen.block.entity.hauptsignalbrueckeEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class hauptsignalbrueckeRenderer extends GeoBlockRenderer<hauptsignalbrueckeEntity> {
    public hauptsignalbrueckeRenderer(BlockEntityRendererFactory.Context context) {
        super(new hauptsignalbrueckeModel<>());
    }
}
