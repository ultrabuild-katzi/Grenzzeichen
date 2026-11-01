package de.raphicraft.grenzzeichen.client;

import de.raphicraft.grenzzeichen.block.ModBlocks;
import de.raphicraft.grenzzeichen.block.entity.ModBlockEntities;
import de.raphicraft.grenzzeichen.block.entity.client.HauptsignalblockRenderer;
import de.raphicraft.grenzzeichen.block.entity.client.HauptsignalbrueckeRenderer;
import de.raphicraft.grenzzeichen.block.entity.client.OrbyRenderer;
import de.raphicraft.grenzzeichen.block.entity.client.RotatedSignalBlockRenderer;
import de.raphicraft.grenzzeichen.block.entity.client.FuehrerstandRenderer;
import de.raphicraft.grenzzeichen.block.entity.client.VorsignalRenderer;
import de.raphicraft.grenzzeichen.client.render.RedGlowRenderer;
import de.raphicraft.grenzzeichen.network.PrecisePlacementNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class GrenzzeichenClient implements ClientModInitializer {
    private static boolean ctrlHeld = false;


    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntities.ORBY_ENTITY, OrbyRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.HAUPTSIGNALBRUECKEENTITY, HauptsignalbrueckeRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.HAUPTSIGNALBLOCKENTITY, HauptsignalblockRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.VORSIGNALENTITY, VorsignalRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.FUEHRERSTAND_ENTITY, FuehrerstandRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.ROTATED_SIGNAL, RotatedSignalBlockRenderer::new);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean held = client.player != null && Screen.hasControlDown();
            if (held != ctrlHeld) {
                ctrlHeld = held;
                if (client.player != null) {
                    ClientPlayNetworking.send(PrecisePlacementNetworking.CTRL_STATE,
                            PrecisePlacementNetworking.createPacket(held));
                }
            }
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ctrlHeld = false);
        WorldRenderEvents.AFTER_ENTITIES.register(SignalPlacementPreview::render);
    }
}
