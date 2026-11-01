package de.raphicraft.grenzzeichen.block.entity.client;

import de.raphicraft.grenzzeichen.block.entity.RotatedSignalBlockEntity;
import de.raphicraft.grenzzeichen.block.settings.RotatableSignalBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

/**
 * Draws a model-based signal block rotated by an arbitrary angle. The block's chunk model is
 * hidden (INVISIBLE render type) whenever the ROTATED property is set.
 */
public class RotatedSignalBlockRenderer implements BlockEntityRenderer<RotatedSignalBlockEntity> {
    public static final float GHOST_ALPHA = 0.5f;

    public RotatedSignalBlockRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(RotatedSignalBlockEntity be, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider consumers, int light, int overlay) {
        if (be.getWorld() == null) return;
        BlockState state = be.getCachedState();
        float remainder = MathHelper.wrapDegrees(be.getYaw() - RotatableSignalBlock.facingYaw(state));
        renderModel(be.getWorld(), be.getPos(), state, remainder, matrices, consumers, false);
    }

    /**
     * @param state     state whose baked model (already turned to its cardinal facing) is drawn
     * @param remainder extra clockwise rotation (seen from above) in degrees applied on top of the facing
     */
    public static void renderModel(BlockRenderView world, BlockPos pos, BlockState state, float remainder,
                                   MatrixStack matrices, VertexConsumerProvider consumers, boolean ghost) {
        MinecraftClient client = MinecraftClient.getInstance();
        BakedModel model = client.getBlockRenderManager().getModel(state);

        VertexConsumer consumer;
        if (ghost) {
            consumer = new AlphaVertexConsumer(
                    consumers.getBuffer(TexturedRenderLayers.getEntityTranslucentCull()), GHOST_ALPHA);
        } else {
            RenderLayer layer = RenderLayers.getEntityBlockLayer(state, false);
            consumer = consumers.getBuffer(layer);
        }

        matrices.push();
        matrices.translate(0.5, 0.0, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-remainder));
        matrices.translate(-0.5, 0.0, -0.5);
        client.getBlockRenderManager().getModelRenderer().render(world, model, state, pos, matrices, consumer,
                false, Random.create(), state.getRenderingSeed(pos), OverlayTexture.DEFAULT_UV);
        matrices.pop();
    }

    @Override
    public int getRenderDistance() {
        return 128;
    }

    /** Scales the alpha of everything written through it. */
    private static final class AlphaVertexConsumer implements VertexConsumer {
        private final VertexConsumer delegate;
        private final float alpha;

        AlphaVertexConsumer(VertexConsumer delegate, float alpha) {
            this.delegate = delegate;
            this.alpha = alpha;
        }

        @Override
        public VertexConsumer vertex(double x, double y, double z) {
            delegate.vertex(x, y, z);
            return this;
        }

        @Override
        public VertexConsumer color(int red, int green, int blue, int alpha) {
            delegate.color(red, green, blue, (int) (alpha * this.alpha));
            return this;
        }

        @Override
        public VertexConsumer texture(float u, float v) {
            delegate.texture(u, v);
            return this;
        }

        @Override
        public VertexConsumer overlay(int u, int v) {
            delegate.overlay(u, v);
            return this;
        }

        @Override
        public VertexConsumer light(int u, int v) {
            delegate.light(u, v);
            return this;
        }

        @Override
        public VertexConsumer normal(float x, float y, float z) {
            delegate.normal(x, y, z);
            return this;
        }

        @Override
        public void next() {
            delegate.next();
        }

        @Override
        public void fixedColor(int red, int green, int blue, int alpha) {
            delegate.fixedColor(red, green, blue, alpha);
        }

        @Override
        public void unfixColor() {
            delegate.unfixColor();
        }
    }
}
