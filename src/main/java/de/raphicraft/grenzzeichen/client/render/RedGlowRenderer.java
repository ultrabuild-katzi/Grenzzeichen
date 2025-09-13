package de.raphicraft.grenzzeichen.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

public final class RedGlowRenderer {
    // tweak here
    private static final float R = 1.0f, G = 0.12f, B = 0.12f;
    private static final float ALPHA = 0.33f;
    private static final double RADIUS = 1.8;
    private static final int RANGE = 24;

    public static void init(java.util.function.Predicate<BlockState> isGlowBlock) {
        WorldRenderEvents.AFTER_ENTITIES.register(ctx -> {
            World world = ctx.world();
            if (world == null) return;

            var cam = ctx.camera();
            var camPos = cam.getPos();
            BlockPos base = BlockPos.ofFloored(camPos);

            var matrices = ctx.matrixStack();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableCull();
            RenderSystem.depthMask(false);

            for (int x = base.getX() - RANGE; x <= base.getX() + RANGE; x++) {
                for (int y = base.getY() - RANGE; y <= base.getY() + RANGE; y++) {
                    for (int z = base.getZ() - RANGE; z <= base.getZ() + RANGE; z++) {
                        BlockPos p = new BlockPos(x, y, z);
                        BlockState s = world.getBlockState(p);
                        if (!isGlowBlock.test(s)) continue;

                        double cx = x + 0.5 - camPos.x;
                        double cy = y + 0.6 - camPos.y;
                        double cz = z + 0.5 - camPos.z;

                        drawCrossedBillboards(matrices, cx, cy, cz, RADIUS, R, G, B, ALPHA);
                    }
                }
            }

            RenderSystem.depthMask(true);
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
        });
    }

    private static void drawCrossedBillboards(MatrixStack matrices, double cx, double cy, double cz,
                                              double radius, float r, float g, float b, float a) {
        matrices.push();
        matrices.translate(cx, cy, cz);

        var camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-camera.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));

        var matrix = matrices.peek().getPositionMatrix();
        var tess = Tessellator.getInstance();
        var buf = tess.getBuffer();

        RenderSystem.setShader(GameRenderer::getPositionColorProgram);

        float s = (float) radius;

        buf.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buf.vertex(matrix, -s, 0, -s).color(r, g, b, a).next();
        buf.vertex(matrix, -s, 0,  s).color(r, g, b, a).next();
        buf.vertex(matrix,  s, 0,  s).color(r, g, b, a).next();
        buf.vertex(matrix,  s, 0, -s).color(r, g, b, a).next();
        tess.draw();

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
        matrix = matrices.peek().getPositionMatrix();
        buf.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buf.vertex(matrix, -s, 0, -s).color(r, g, b, a).next();
        buf.vertex(matrix, -s, 0,  s).color(r, g, b, a).next();
        buf.vertex(matrix,  s, 0,  s).color(r, g, b, a).next();
        buf.vertex(matrix,  s, 0, -s).color(r, g, b, a).next();
        tess.draw();

        matrices.pop();
    }
}
