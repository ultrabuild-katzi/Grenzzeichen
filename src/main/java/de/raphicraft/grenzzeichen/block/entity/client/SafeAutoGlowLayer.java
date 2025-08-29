package de.raphicraft.grenzzeichen.block.entity.client;

import de.raphicraft.grenzzeichen.block.entity.HauptsignalblockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.Optional;

public class SafeAutoGlowLayer extends GeoRenderLayer<HauptsignalblockEntity> {
    public SafeAutoGlowLayer(GeoRenderer<HauptsignalblockEntity> renderer) { super(renderer); }

    @Override
    public void render(MatrixStack matrices,
                       HauptsignalblockEntity animatable,
                       BakedGeoModel model,
                       RenderLayer renderType,                    // <- order matters
                       VertexConsumerProvider bufferSource,
                       VertexConsumer buffer,
                       float partialTick,
                       int packedLight,
                       int packedOverlay) {

        Identifier base = getRenderer().getTextureLocation(animatable);
        Identifier glow = appendGlowSuffix(base);
        if (!resourceExists(glow)) return; // skip if missing

        RenderLayer eyes = RenderLayer.getEyes(glow);
        VertexConsumer glowBuf = bufferSource.getBuffer(eyes);

        getRenderer().reRender(
                model, matrices, bufferSource, animatable,
                eyes, glowBuf,
                partialTick,
                0xF000F0,                    // fullbright
                OverlayTexture.DEFAULT_UV,
                1f, 1f, 1f, 1f
        );
    }

    private static Identifier appendGlowSuffix(Identifier base) {
        String path = base.getPath();
        if (path.endsWith(".png")) path = path.substring(0, path.length() - 4);
        return new Identifier(base.getNamespace(), path + "_glow.png");
    }

    private static boolean resourceExists(Identifier id) {
        try {
            Optional<Resource> res = MinecraftClient.getInstance()
                    .getResourceManager().getResource(id);
            return res.isPresent();
        } catch (Exception e) {
            return false;
        }
    }
}
