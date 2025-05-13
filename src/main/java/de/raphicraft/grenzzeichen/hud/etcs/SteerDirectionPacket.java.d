package de.raphicraft.grenzzeichen.hud.etcs;


import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SteerDirectionPacket implements C2SPacket {
    // Define an enum for the key press types
    public enum KeyPressType {
        PLUS,
        MINUS,
        NONE
    }

    // Static map to store player key presses
    private static final Map<UUID, KeyPressType> playerKeyPresses = new HashMap<>();

    // The key being pressed in this packet
    private final KeyPressType keyPress;

    public SteerDirectionPacket(KeyPressType keyPress) {
        this.keyPress = keyPress;
    }

    // Constructor for deserialization
    public SteerDirectionPacket(PacketByteBuf buf) {
        this.keyPress = buf.readEnumConstant(KeyPressType.class);
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeEnumConstant(keyPress);
    }

    @Override
    public void handle(ServerPlayerEntity player) {
        // Store this player's key press in the map
        playerKeyPresses.put(player.getUuid(), keyPress);
    }

    public static SteerDirectionPacket read(PacketByteBuf buffer) {
        KeyPressType keys = buffer.readEnumConstant(KeyPressType.class);
        return new SteerDirectionPacket(keys);

    }

    // Static method to get a player's current key press state
    public static KeyPressType getPlayerKeyPress(UUID playerId) {
        return playerKeyPresses.getOrDefault(playerId, KeyPressType.NONE);
    }
    public static void  setPlayerKeyPresses(UUID playerId, KeyPressType keyPress) {
        playerKeyPresses.put(playerId, keyPress);
    }
}
