package de.raphicraft.grenzzeichen.block.entity.client;

import de.raphicraft.grenzzeichen.block.entity.HauptsignalblockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class HauptsignalblockRenderer extends GeoBlockRenderer<HauptsignalblockEntity> {
    public HauptsignalblockRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new HauptsignalblockModel<>());
        addRenderLayer(new SafeAutoGlowLayer<>(this));
    }
    @Override public int getRenderDistance() { return 512; }
}
