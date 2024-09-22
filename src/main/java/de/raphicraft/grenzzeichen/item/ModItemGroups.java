package de.raphicraft.grenzzeichen.item;

import de.raphicraft.grenzzeichen.Grenzzeichen;
import de.raphicraft.grenzzeichen.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup DEST = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Grenzzeichen.MOD_ID,"ruby"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.ruby"))
                    .icon(() -> new ItemStack(ModItems.RUBY))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.RUBY);
                        entries.add(ModBlocks.GRENZZEICHEN);

                    }).build());


    public static void registerItemGroups() {
        Grenzzeichen.LOGGER.info("Registering Item Groups for " + Grenzzeichen.MOD_ID);
    }
}

