package de.raphicraft.grenzzeichen.compat.create;

import com.simibubi.create.content.contraptions.actors.trainControls.ControlsMovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld;
import net.minecraft.client.render.VertexConsumerProvider;

public class FuehrerstandMovementBehaviour extends ControlsMovementBehaviour {

    @Override
    public void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, VertexConsumerProvider buffer) {
    }
}
