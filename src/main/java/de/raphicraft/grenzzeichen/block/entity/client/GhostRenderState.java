package de.raphicraft.grenzzeichen.block.entity.client;

/** Set by the placement preview while it renders a ghost block, so renderers draw it semi-transparent. */
public final class GhostRenderState {
    public static final float ALPHA = 0.5f;
    public static boolean active = false;

    private GhostRenderState() {}
}
