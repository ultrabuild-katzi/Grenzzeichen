package de.raphicraft.grenzzeichen.block.entity.client;

import de.raphicraft.grenzzeichen.block.entity.hauptsignalbrueckeEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class HauptsignalbrueckeRenderer extends GeoBlockRenderer<hauptsignalbrueckeEntity> {

    public HauptsignalbrueckeRenderer(BlockEntityRendererFactory.Context context) {
        super(new hauptsignalbrueckeModel());
        addRenderLayer(new SafeAutoGlowLayer<>(this));
    }

    @Override
    public int getRenderDistance() {
        return 512;
    }
}
