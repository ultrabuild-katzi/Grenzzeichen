package de.raphicraft.grenzzeichen.block.entity.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.Optional;

/** Renders <base>_glow.png as fullbright, but only if the file exists (prevents black silhouettes). */
public class SafeAutoGlowLayer<T extends GeoAnimatable> extends GeoRenderLayer<T> {
    public SafeAutoGlowLayer(GeoRenderer<T> renderer) { super(renderer); }

    @Override
    public void render(MatrixStack matrices,
                       T animatable,
                       BakedGeoModel model,
                       RenderLayer renderType,                // GeckoLib 4.4.9 signature
                       VertexConsumerProvider bufferSource,
                       VertexConsumer buffer,
                       float partialTick,
                       int packedLight,
                       int packedOverlay) {

        Identifier base = getRenderer().getTextureLocation(animatable);
        Identifier glow = withGlowSuffix(base);
        if (!exists(glow)) return; // graceful no-op if missing

        RenderLayer eyes = RenderLayer.getEyes(glow);
        VertexConsumer glowBuf = bufferSource.getBuffer(eyes);

        // reRender(..., partialTick, packedLight, packedOverlay, r,g,b,a)
        getRenderer().reRender(
                model, matrices, bufferSource, animatable,
                eyes, glowBuf,
                partialTick,
                0xF000F0,                    // fullbright
                OverlayTexture.DEFAULT_UV,
                1f, 1f, 1f, 1f
        );
    }

    private static Identifier withGlowSuffix(Identifier base) {
        String path = base.getPath();
        if (path.endsWith(".png")) path = path.substring(0, path.length() - 4);
        return new Identifier(base.getNamespace(), path + "_glow.png");
    }

    private static boolean exists(Identifier id) {
        try {
            Optional<Resource> res = MinecraftClient.getInstance()
                    .getResourceManager().getResource(id);
            return res.isPresent();
        } catch (Exception e) {
            return false;
        }
    }
}
