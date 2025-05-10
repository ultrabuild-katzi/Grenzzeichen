package de.raphicraft.grenzzeichen.world;

import de.raphicraft.grenzzeichen.Grenzzeichen;
import de.raphicraft.grenzzeichen.block.ModBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.List;

/**
 * Handles configuration and registration of custom ore generation features.
 */
public class ModConfiguredFeatures {
    /** Standard Black Iron Ore generation config */
    public static final RegistryKey<ConfiguredFeature<?, ?>> BLACK_IRON_ORE_KEY = registerKey("black_iron_ore");
    
    /** Small Black Iron Ore generation config */
    public static final RegistryKey<ConfiguredFeature<?, ?>> BLACK_IRON_ORE_SMALL_KEY = registerKey("black_iron_ore_small");


    public static final RegistryKey<ConfiguredFeature<?, ?>> RED_STONE_KEY = registerKey("red_stone");


    public static final RegistryKey<ConfiguredFeature<?, ?>> WHITE_STONE_KEY = registerKey("white_stone");


    public static final RegistryKey<ConfiguredFeature<?, ?>> BLACK_STONE_KEY = registerKey("black_stone");



    /**
     * Bootstraps ore generation configurations.
     */
    public static void boostrap(Registerable<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplacables = new TagMatchRuleTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplacables = new TagMatchRuleTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreFeatureConfig.Target> overworldBlackIronOres =
                List.of(OreFeatureConfig.createTarget(stoneReplacables, ModBlocks.BLACK_IRON_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(deepslateReplacables, ModBlocks.DEEPSLATE_BLACK_IRON_ORE.getDefaultState()));

        List<OreFeatureConfig.Target> overworldBlackIronOresSmall =
                List.of(OreFeatureConfig.createTarget(stoneReplacables, ModBlocks.BLACK_IRON_ORE.getDefaultState()),
                        OreFeatureConfig.createTarget(deepslateReplacables, ModBlocks.DEEPSLATE_BLACK_IRON_ORE.getDefaultState()));

        List<OreFeatureConfig.Target> overworldRedStone =
                List.of(OreFeatureConfig.createTarget(stoneReplacables, ModBlocks.REDSTONE.getDefaultState()));

        List<OreFeatureConfig.Target> overworldWhiteStone =
                List.of(OreFeatureConfig.createTarget(stoneReplacables, ModBlocks.WHITESTONE.getDefaultState()));


        List<OreFeatureConfig.Target> overworldBlackStone =
                List.of(OreFeatureConfig.createTarget(stoneReplacables, ModBlocks.BLACKSTONE.getDefaultState()));





        register(context, BLACK_IRON_ORE_KEY, Feature.ORE, new OreFeatureConfig(overworldBlackIronOres, 12));
        register(context, BLACK_IRON_ORE_SMALL_KEY, Feature.ORE, new OreFeatureConfig(overworldBlackIronOresSmall, 6));
        register(context, RED_STONE_KEY, Feature.ORE, new OreFeatureConfig(overworldRedStone, 32));
        register(context, WHITE_STONE_KEY, Feature.ORE, new OreFeatureConfig(overworldWhiteStone, 32));
        register(context, BLACK_STONE_KEY, Feature.ORE, new OreFeatureConfig(overworldBlackStone, 32));
    }

    /**
     * Creates a registry key for a configured feature.
     */
    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(Grenzzeichen.MOD_ID, name));
    }

    /**
     * Registers a configured feature.
     */
    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                 RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}