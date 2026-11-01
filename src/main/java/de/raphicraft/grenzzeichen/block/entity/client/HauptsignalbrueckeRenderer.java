package de.raphicraft.grenzzeichen.block.entity.client;

import de.raphicraft.grenzzeichen.block.custom.SignalRotation;
import de.raphicraft.grenzzeichen.block.entity.hauptsignalbrueckeEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.core.object.Color;

public class HauptsignalbrueckeRenderer extends GeoBlockRenderer<hauptsignalbrueckeEntity> {

    public HauptsignalbrueckeRenderer(BlockEntityRendererFactory.Context context) {
        super(new hauptsignalbrueckeModel());
        addRenderLayer(new SafeAutoGlowLayer<>(this));
    }

    @Override
    public int getRenderDistance() {
        return 512;
    }

    @Override
    public RenderLayer getRenderType(hauptsignalbrueckeEntity animatable, Identifier texture,
                                     @Nullable VertexConsumerProvider bufferSource, float partialTick) {
        return GhostRenderState.active
                ? RenderLayer.getEntityTranslucent(texture)
                : super.getRenderType(animatable, texture, bufferSource, partialTick);
    }

    @Override
    public Color getRenderColor(hauptsignalbrueckeEntity animatable, float partialTick, int packedLight) {
        return GhostRenderState.active
                ? Color.ofRGBA(255, 255, 255, Math.round(GhostRenderState.ALPHA * 255))
                : super.getRenderColor(animatable, partialTick, packedLight);
    }

    @Override
    protected void rotateBlock(Direction facing, MatrixStack poseStack) {
        var be = getAnimatable();
        if (be != null && be.hasPreciseYaw()) {
            poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(
                    SignalRotation.preciseRotationDegrees(be.getPreciseYaw())));
            return;
        }
        super.rotateBlock(facing, poseStack);
        if (be != null) {
            poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(
                    SignalRotation.extraRotationDegrees(be.getCachedState())));
        }
    }
}
