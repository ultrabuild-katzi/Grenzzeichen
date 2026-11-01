package de.raphicraft.grenzzeichen.network;

import de.raphicraft.grenzzeichen.Grenzzeichen;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Tracks which players currently hold Ctrl so the server can place signals with 1 degree precision. */
public final class PrecisePlacementNetworking {
    public static final Identifier CTRL_STATE = new Identifier(Grenzzeichen.MOD_ID, "precise_placement");

    private static final Set<UUID> PRECISE_PLAYERS = ConcurrentHashMap.newKeySet();

    private PrecisePlacementNetworking() {}

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(CTRL_STATE, (server, player, handler, buf, responseSender) -> {
            boolean held = buf.readBoolean();
            UUID id = player.getUuid();
            server.execute(() -> {
                if (held) PRECISE_PLAYERS.add(id);
                else PRECISE_PLAYERS.remove(id);
            });
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                PRECISE_PLAYERS.remove(handler.getPlayer().getUuid()));
    }

    public static boolean isPrecise(PlayerEntity player) {
        return PRECISE_PLAYERS.contains(player.getUuid());
    }

    public static PacketByteBuf createPacket(boolean held) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(held);
        return buf;
    }
}
