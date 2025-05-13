package de.raphicraft.grenzzeichen.hud.etcs;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import de.raphicraft.grenzzeichen.Grenzzeichen;
import net.minecraft.block.Block;

public class RealismBlocks {
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(Grenzzeichen.MOD_ID);

	private static Object Block;
	public static final BlockEntry<Block> EXAMPLE_BLOCK = REGISTRATE.block("example_block", Block::new).register();

	public static void init() {
		// load the class and register everything
        Grenzzeichen.LOGGER.info("Registering blocks for " + Grenzzeichen.MOD_ID);
	}
}
