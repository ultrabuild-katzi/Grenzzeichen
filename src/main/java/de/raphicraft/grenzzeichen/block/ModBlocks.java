package de.raphicraft.grenzzeichen.block;

import de.raphicraft.grenzzeichen.Grenzzeichen;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlocks {
    public static final Block GRENZZEICHEN = registerWithItem("grenzzeichen", new GrenzzeichenBlock(
            AbstractBlock.Settings.create().strength(1.5F, 6.0F)
                    .requiresTool()));



    public static <T extends Block> T register(String name, T block) {
        return Registry.register(Registries.BLOCK, Grenzzeichen.MOD_ID(name), block);
    }

    public static <T extends Block> T registerWithItem(String name, T block, Item.Settings settings) {
        T registered = register(name, block);
        Registry.register(Registries.ITEM, Grenzzeichen.MOD_ID(name), new BlockItem(registered, settings));
        return registered;
    }

    public static <T extends Block> T registerWithItem(String name, T block) {
        return registerWithItem(name, block, new Item.Settings());
    }

    public static void registerModBlocks() {
    }



}