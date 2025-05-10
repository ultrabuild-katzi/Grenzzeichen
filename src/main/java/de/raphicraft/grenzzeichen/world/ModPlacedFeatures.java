package de.raphicraft.grenzzeichen.world;

import de.raphicraft.grenzzeichen.Grenzzeichen;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.List;

/**
 * Handles world generation placement for mod features.
 */
public class ModPlacedFeatures {
    /** Standard Black Iron Ore placement */
    public static final RegistryKey<PlacedFeature> BLACK_IRON_ORE_PLACED_KEY = registerKey("black_iron_ore_placed");
    
    /** Small Black Iron Ore placement */
    public static final RegistryKey<PlacedFeature> BLACK_IRON_ORE_SMALL_PLACED_KEY = registerKey("black_iron_ore_small_placed");


    public static final RegistryKey<PlacedFeature> RED_STONE_PLACED_KEY = registerKey("red_stone_placed");


    public static final RegistryKey<PlacedFeature> WHITE_STONE_PLACED_KEY = registerKey("white_stone_placed");


    public static final RegistryKey<PlacedFeature> BLACK_STONE_PLACED_KEY = registerKey("black_stone_placed");

    /**
     * Sets up feature placement configurations.
     */
    public static void boostrap(Registerable<PlacedFeature> context) {
        var configuredFeatureRegistryLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        register(context, BLACK_IRON_ORE_PLACED_KEY, configuredFeatureRegistryLookup.getOrThrow(ModConfiguredFeatures.BLACK_IRON_ORE_KEY),
                ModOrePlacement.modifiersWithCount(12, // Veins per Chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-64), YOffset.fixed(319))));

        register(context, BLACK_IRON_ORE_SMALL_PLACED_KEY, configuredFeatureRegistryLookup.getOrThrow(ModConfiguredFeatures.BLACK_IRON_ORE_SMALL_KEY),
                ModOrePlacement.modifiersWithCount(3, // Veins per Chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-64), YOffset.fixed(319))));


        register(context, RED_STONE_PLACED_KEY, configuredFeatureRegistryLookup.getOrThrow(ModConfiguredFeatures.RED_STONE_KEY),
                ModOrePlacement.modifiersWithCount(1, // Veins per Chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(60))));


        register(context, WHITE_STONE_PLACED_KEY, configuredFeatureRegistryLookup.getOrThrow(ModConfiguredFeatures.WHITE_STONE_KEY),
                ModOrePlacement.modifiersWithCount(1, // Veins per Chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(60))));


        register(context, BLACK_STONE_PLACED_KEY, configuredFeatureRegistryLookup.getOrThrow(ModConfiguredFeatures.BLACK_STONE_KEY),
                ModOrePlacement.modifiersWithCount(1, // Veins per Chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(0), YOffset.fixed(60))));

    }

    /**
     * Creates a placed feature registry key.
     */
    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(Grenzzeichen.MOD_ID, name));
    }

    /**
     * Registers a placed feature.
     */
    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key, 
                               RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                               List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}