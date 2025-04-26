package de.raphicraft.grenzzeichen.block.entity.client;

import de.raphicraft.grenzzeichen.block.entity.OrbyEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class OrbyRenderer extends GeoBlockRenderer<OrbyEntity> {
    public OrbyRenderer(BlockEntityRendererFactory.Context context) {
        super(new OrbyModel<>());
    }
}
