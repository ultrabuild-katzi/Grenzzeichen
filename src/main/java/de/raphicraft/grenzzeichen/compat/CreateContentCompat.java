package de.raphicraft.grenzzeichen.compat;

import com.simibubi.create.AllInteractionBehaviours;
import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsInteractionBehaviour;
import de.raphicraft.grenzzeichen.block.ModBlocks;
import de.raphicraft.grenzzeichen.compat.create.FuehrerstandMovementBehaviour;

public class CreateContentCompat {

//    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(Grenzzeichen.MOD_ID);

    public static void register() {
        AllMovementBehaviours.registerBehaviour(ModBlocks.FUEHRERSTAND, new FuehrerstandMovementBehaviour());
        AllInteractionBehaviours.registerBehaviour(ModBlocks.FUEHRERSTAND, new ControlsInteractionBehaviour());

//        REGISTRATE.register();
    }

}
