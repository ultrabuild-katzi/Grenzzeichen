package de.raphicraft.grenzzeichen.block.entity.client;

import de.raphicraft.grenzzeichen.block.entity.FuehrerstandEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class FuehrerstandRenderer extends GeoBlockRenderer<FuehrerstandEntity> {
    public FuehrerstandRenderer(BlockEntityRendererFactory.Context context) {
        super(new FuehrerstandModel<>());
        addRenderLayer(new SafeAutoGlowLayer<>(this));
    }

    @Override
    public int getRenderDistance() {
        return 512;
    }
}