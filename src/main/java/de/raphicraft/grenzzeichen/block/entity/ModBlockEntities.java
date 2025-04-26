package de.raphicraft.grenzzeichen.block.entity;

import com.mojang.datafixers.types.templates.Check;
import de.raphicraft.grenzzeichen.Grenzzeichen;
import de.raphicraft.grenzzeichen.block.ModBlocks;
import de.raphicraft.grenzzeichen.block.settings.HauptsignalBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static <T extends BlockEntityType<?>> T register(String path, T blockEntityType) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Grenzzeichen.MOD_ID, path), blockEntityType);
    }

    public static final BlockEntityType<HauptsignalBlockEntity> HAUPTSIGNAL_BLOCK_ENTITY_TYPE = register(
            "demo_block",
            FabricBlockEntityTypeBuilder.create(HauptsignalBlockEntity::new, ModBlocks.HAUPTSIGNAL).build()
    );



    public static BlockEntityType<OrbyEntity> ORBY_ENTITY;

    public static void registerAllBlockEntities() {
        ORBY_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Grenzzeichen.MOD_ID, "orby_entity"),
                FabricBlockEntityTypeBuilder.create(OrbyEntity::new,
                        ModBlocks.ORBY).build());
    }
}