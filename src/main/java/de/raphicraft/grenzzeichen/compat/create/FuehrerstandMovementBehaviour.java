package de.raphicraft.grenzzeichen.compat.create;

import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsMovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import net.minecraft.client.render.VertexConsumerProvider;

public class FuehrerstandMovementBehaviour extends ControlsMovementBehaviour {

    @Override
    public boolean renderAsNormalBlockEntity() {
        // Let Create render the carried BlockEntity, so the Gecko model/texture/glow is used on trains.
        return true;
    }

    @Override
    public void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, VertexConsumerProvider buffer) {
        // Intentionally empty: ControlsMovementBehaviour would render Create's default lever model here.
    }
}
