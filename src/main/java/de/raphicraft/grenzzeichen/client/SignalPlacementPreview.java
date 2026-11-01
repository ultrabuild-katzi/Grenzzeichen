package de.raphicraft.grenzzeichen.client;

import de.raphicraft.grenzzeichen.block.custom.SignalRotation;
import de.raphicraft.grenzzeichen.block.custom.hauptsignalblock;
import de.raphicraft.grenzzeichen.block.custom.hauptsignalbruecke;
import de.raphicraft.grenzzeichen.block.custom.vorsignal;
import de.raphicraft.grenzzeichen.block.entity.HauptsignalblockEntity;
import de.raphicraft.grenzzeichen.block.entity.PreciseRotationBlockEntity;
import de.raphicraft.grenzzeichen.block.entity.VorsignalEntity;
import de.raphicraft.grenzzeichen.block.entity.hauptsignalbrueckeEntity;
import de.raphicraft.grenzzeichen.block.entity.client.GhostRenderState;
import de.raphicraft.grenzzeichen.block.entity.client.RotatedSignalBlockRenderer;
import de.raphicraft.grenzzeichen.block.settings.RotatableSignalBlock;
import net.minecraft.util.math.MathHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

/** Draws a semi-transparent preview of Hauptsignal blocks at the spot where they would be placed. */
public final class SignalPlacementPreview {
    private static PreciseRotationBlockEntity ghost;
    private static BlockPos ghostPos;
    private static BlockState ghostState;
    private static int ghostYaw;

    private SignalPlacementPreview() {}

    public static void render(WorldRenderContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null || context.consumers() == null) return;
        if (!(client.crosshairTarget instanceof BlockHitResult hit) || hit.getType() != HitResult.Type.BLOCK) return;

        Hand hand = null;
        ItemStack stack = null;
        for (Hand h : Hand.values()) {
            ItemStack s = player.getStackInHand(h);
            if (s.getItem() instanceof BlockItem bi && isSignalBlock(bi.getBlock())) {
                hand = h;
                stack = s;
                break;
            }
        }
        if (stack == null) return;

        Block block = ((BlockItem) stack.getItem()).getBlock();
        ItemPlacementContext placement = new ItemPlacementContext(player, hand, stack, hit);
        if (!placement.canPlace()) return;
        BlockPos pos = placement.getBlockPos();
        BlockState state = block.getPlacementState(placement);
        if (state == null || !state.canPlaceAt(client.world, pos)) return;

        boolean precise = Screen.hasControlDown();
        Vec3d cam = context.camera().getPos();
        var matrices = context.matrixStack();
        int light = WorldRenderer.getLightmapCoordinates(client.world, pos);

        if (block instanceof RotatableSignalBlock) {
            float yaw = SignalRotation.placementYaw(player.getYaw(), precise);
            float remainder = MathHelper.wrapDegrees(yaw - RotatableSignalBlock.facingYaw(state));
            matrices.push();
            matrices.translate(pos.getX() - cam.x, pos.getY() - cam.y, pos.getZ() - cam.z);
            try {
                RotatedSignalBlockRenderer.renderModel(client.world, pos, state, remainder,
                        matrices, context.consumers(), true);
            } finally {
                matrices.pop();
            }
            return;
        }

        int yaw = precise ? Math.round(player.getYaw()) : PreciseRotationBlockEntity.NO_PRECISE_YAW;
        updateGhost(block, pos, state, yaw);

        BlockEntityRenderer<BlockEntity> renderer = client.getBlockEntityRenderDispatcher().get(ghost);
        if (renderer == null) return;

        matrices.push();
        matrices.translate(pos.getX() - cam.x, pos.getY() - cam.y, pos.getZ() - cam.z);
        GhostRenderState.active = true;
        try {
            renderer.render(ghost, context.tickDelta(), matrices, context.consumers(), light, OverlayTexture.DEFAULT_UV);
        } finally {
            GhostRenderState.active = false;
            matrices.pop();
        }
    }

    private static boolean isSignalBlock(Block block) {
        return block instanceof hauptsignalblock || block instanceof hauptsignalbruecke
                || block instanceof vorsignal || block instanceof RotatableSignalBlock;
    }

    private static void updateGhost(Block block, BlockPos pos, BlockState state, int yaw) {
        boolean sameType = ghost != null && ghost.getCachedState().getBlock() == block;
        if (sameType && pos.equals(ghostPos) && Objects.equals(state, ghostState) && yaw == ghostYaw) return;

        if (!sameType || !pos.equals(ghostPos)) {
            if (block instanceof hauptsignalblock) {
                ghost = new HauptsignalblockEntity(pos, state);
            } else if (block instanceof vorsignal) {
                ghost = new VorsignalEntity(pos, state);
            } else {
                ghost = new hauptsignalbrueckeEntity(pos, state);
            }
        } else {
            ghost.setCachedState(state);
        }
        ghost.setPreciseYaw(yaw);
        ghostPos = pos;
        ghostState = state;
        ghostYaw = yaw;
    }
}
