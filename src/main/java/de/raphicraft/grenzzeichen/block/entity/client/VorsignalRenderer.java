package de.raphicraft.grenzzeichen.block.entity.client;

import de.raphicraft.grenzzeichen.block.entity.VorsignalEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class VorsignalRenderer extends GeoBlockRenderer<VorsignalEntity> {

    public VorsignalRenderer(BlockEntityRendererFactory.Context context) {
        super(new VorsignalModel<>());
    }

    @Override
    public int getRenderDistance() {
        return 512;
    }
}