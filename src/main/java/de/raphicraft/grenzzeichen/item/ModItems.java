package de.raphicraft.grenzzeichen.item;

import de.raphicraft.grenzzeichen.Grenzzeichen;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item RUBY = registerItem("ruby", new Item(new Item.Settings()));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Grenzzeichen.MOD_ID, name), item);
    }



    public static void registerModItems() {
        Grenzzeichen.LOGGER.info("Registering Mod Items for " + Grenzzeichen.MOD_ID);

    }
}
