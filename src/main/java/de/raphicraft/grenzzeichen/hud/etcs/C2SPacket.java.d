package de.raphicraft.grenzzeichen.hud.etcs;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public interface C2SPacket {
  void write(PacketByteBuf buf);
}