package de.raphicraft.grenzzeichen;

import de.raphicraft.grenzzeichen.block.ModBlocks;
import de.raphicraft.grenzzeichen.block.entity.ModBlockEntities;
import de.raphicraft.grenzzeichen.item.ModItemGroups;
import de.raphicraft.grenzzeichen.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.GeckoLib;

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

        ModBlockEntities.registerAllBlockEntities();

        GeckoLib.initialize();

        LOGGER.info("Loading Grenzzeichen Mod");
    }


}