package de.raphicraft.grenzzeichen.client;

import de.raphicraft.grenzzeichen.block.ModBlocks;
import de.raphicraft.grenzzeichen.block.entity.ModBlockEntities;
import de.raphicraft.grenzzeichen.block.entity.client.HauptsignalblockRenderer;
import de.raphicraft.grenzzeichen.block.entity.client.HauptsignalbrueckeRenderer;
import de.raphicraft.grenzzeichen.block.entity.client.OrbyRenderer;
import de.raphicraft.grenzzeichen.block.entity.client.VorsignalRenderer;
import de.raphicraft.grenzzeichen.client.render.RedGlowRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class GrenzzeichenClient implements ClientModInitializer {



    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntities.ORBY_ENTITY, OrbyRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.HAUPTSIGNALBRUECKEENTITY, HauptsignalbrueckeRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.HAUPTSIGNALBLOCKENTITY, HauptsignalblockRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntities.VORSIGNALENTITY, VorsignalRenderer::new);
    }
}
