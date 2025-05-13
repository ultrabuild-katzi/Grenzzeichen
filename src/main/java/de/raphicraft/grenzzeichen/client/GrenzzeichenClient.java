package de.raphicraft.grenzzeichen.client;

import de.raphicraft.grenzzeichen.block.entity.ModBlockEntities;
import de.raphicraft.grenzzeichen.block.entity.client.OrbyRenderer;
import de.raphicraft.grenzzeichen.block.entity.client.hauptsignalbrueckeRenderer; // Import der fehlenden Klasse ergänzen
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class GrenzzeichenClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntities.ORBY_ENTITY, OrbyRenderer::new);

        // Korrigierter Renderer für Hauptsignalbruecke
        BlockEntityRendererFactories.register(ModBlockEntities.HAUPTSIGNALBRUECKEENTITY, hauptsignalbrueckeRenderer::new);
    }
}