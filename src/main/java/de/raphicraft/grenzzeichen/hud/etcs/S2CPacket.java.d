package de.raphicraft.grenzzeichen.hud.etcs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

public interface S2CPacket {
  void write(PacketByteBuf buf);
  void handle(MinecraftClient mc);
}
