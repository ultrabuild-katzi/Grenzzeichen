package de.raphicraft.grenzzeichen.hud.etcs;
import net.minecraft.server.network.ServerPlayerEntity; // ServerPlayerEntity -> ServerPlayerEntityEntity
import net.minecraft.world.World; // Level -> World
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resources.ResourceKey; // ResourceKey -> RegistryKey
import net.minecraft.client.MinecraftClient; // Minecraft -> MinecraftClient
import net.minecraft.util.math.Vec3d; // Vec3 -> Vec3d
import com.simibubi.create.foundation.utility.Components;
import dev.architectury.injectables.annotations.ExpectPlatform; // Ensure Architectury API is correctly added to the project dependencies
import io.netty.buffer.Unpooled;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.World;




import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class RNetworking {
    private static final String VERSION = "4";
    private static int id = 0;

    private static final Map<Class<? extends C2SPacket>, Integer> c2sIdentifiers = new HashMap<>();
    private static final Map<Class<? extends S2CPacket>, Integer> s2cIdentifiers = new HashMap<>();
    private static final Map<Integer, Function<PacketByteBuf, ? extends C2SPacket>> c2sReaders = new HashMap<>();
    private static final Map<Integer, Function<PacketByteBuf, ? extends S2CPacket>> s2cReaders = new HashMap<>();

    private static class CheckVersionS2CPacket implements S2CPacket {
        private final String serverVersion;

        public CheckVersionS2CPacket(String serverVersion) {
            this.serverVersion = serverVersion;
        }

        public static CheckVersionS2CPacket read(PacketByteBuf buf) {
            return new CheckVersionS2CPacket(buf.readString());
        }

        @Override
        public void write(PacketByteBuf buf) {
            buf.writeString(serverVersion);
        }

        @Override
        public void handle(MinecraftClient mc) {

        }

        @Override
        public void handle(MinecraftClient mc) {
            if (RNetworking.VERSION.equals(serverVersion))
                return;

            if (mc.getNetworkHandler() != null) {
                mc.getNetworkHandler().getConnection().disconnect(
                        Components.literal(
                                "Create: Tramways network versions do not match! Server expected %s, client has %s"
                                        .formatted(serverVersion, RNetworking.VERSION)
                        )
                );
            }
        }
    }

    private static <T extends S2CPacket> void registerS2C(
            Class<T> clazz,
            Function<PacketByteBuf, T> read
    ) {
        int packetId = id++;
        s2cIdentifiers.put(clazz, packetId);
        s2cReaders.put(packetId, read);
    }

    private static <T extends C2SPacket> void registerC2S(
            Class<T> clazz,
            Function<PacketByteBuf, T> read
    ) {
        int packetId = id++;
        c2sIdentifiers.put(clazz, packetId);
        c2sReaders.put(packetId, read);
    }

    public static <T extends C2SPacket> void sendInternal(T message, Consumer<PacketByteBuf> consumer) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeVarInt(c2sIdentifiers.get(message.getClass()));
        message.write(buf);
        consumer.accept(buf);
    }

    public static <T extends S2CPacket> void sendInternal(T message, Consumer<PacketByteBuf> consumer) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeVarInt(s2cIdentifiers.get(message.getClass()));
        message.write(buf);
        consumer.accept(buf);
    }

    public static void handleInternal(PacketByteBuf buf, MinecraftClient mc) {
        int packetId = buf.readVarInt();
        S2CPacket packet = s2cReaders.get(packetId).apply(buf);
        mc.execute(() ->
                packet.handle(mc)
        );
    }

    public static void handleInternal(PacketByteBuf buf, ServerPlayerEntity player) {
        int packetId = buf.readVarInt();
        C2SPacket packet = c2sReaders.get(packetId).apply(buf);
        player.server.execute(() ->
                packet.handle(player)
        );
    }

    public static void onPlayerJoin(ServerPlayerEntity player) {
        sendToPlayer(new CheckVersionS2CPacket(RNetworking.VERSION), player);
    }

    @ExpectPlatform
    public static <T extends S2CPacket> void sendToAll(T message) {
        throw new AssertionError(); // Implementation must be provided per platform
    }

    @ExpectPlatform
    public static <T extends S2CPacket> void sendToNear(T message, Vec3d pos, int range, ResourceKey<World> dimension) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends S2CPacket> void sendToPlayer(T message, ServerPlayerEntity player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends C2SPacket> void sendToServer(T message) {
        throw new AssertionError();
    }

    public static void register() {
        registerS2C(
                CheckVersionS2CPacket.class,
                CheckVersionS2CPacket::read
        );
        registerS2C(
                ETCSSyncPacket.class,
                ETCSSyncPacket::read

        );
        registerC2S(
                SteerDirectionPacket.class,
                buf -> SteerDirectionPacket.read(buf)
        );

    }
}
