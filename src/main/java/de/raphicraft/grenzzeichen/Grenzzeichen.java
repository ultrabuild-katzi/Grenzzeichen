package de.raphicraft.grenzzeichen;

import de.raphicraft.grenzzeichen.block.ModBlocks;
import de.raphicraft.grenzzeichen.item.ModItemGroups;
import de.raphicraft.grenzzeichen.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Grenzzeichen implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("grenzzeichen");
    public static final String MOD_ID = "grenzzeichen";

    public static Identifier MOD_ID(String name) {return Identifier.of(MOD_ID, name);
    }

    @Override
    public void onInitialize() {
        ModItemGroups.registerItemGroups();
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();

        LOGGER.info("Strassenbahn_12 is gay (stimmt halt)");
    }


}