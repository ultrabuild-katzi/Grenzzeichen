package de.raphicraft.grenzzeichen.compat;

import com.simibubi.create.content.trains.signal.SignalBlockEntity;
import de.raphicraft.grenzzeichen.util.Result;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CreateCompat {
    private static final boolean IS_CREATE_AVAILABLE = FabricLoader.getInstance().isModLoaded("create");
    private static final int SIGNAL_SEARCH_RADIUS = 2;

    private static final Map<RegistryKey<World>, Set<BlockPos>> ANCHORS = new HashMap<>();
    private static final Map<RegistryKey<World>, Map<BlockPos, BlockPos>> CACHE = new HashMap<>();
    private static int tickCounter = 0;

    public static Result<GrenzzeichenSignalStates, SignalTextureResult> getSignalTexture(@NotNull World world, @Nullable BlockPos blockPos) {
        if (!IS_CREATE_AVAILABLE) {
            return Result.of(null, SignalTextureResult.CREATE_UNAVAILABLE);
        }
        if (blockPos == null) {
            return Result.of(null, SignalTextureResult.BLOCK_BROKEN);
        }

        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (!(blockEntity instanceof SignalBlockEntity signalBlockEntity)) {
            return Result.of(null, SignalTextureResult.BLOCK_BROKEN);
        }

        GrenzzeichenSignalStates result = switch (signalBlockEntity.getState()) {
            case RED -> GrenzzeichenSignalStates.RED;
            case YELLOW -> GrenzzeichenSignalStates.YELLOW;
            case GREEN -> GrenzzeichenSignalStates.GREEN;
            case INVALID -> GrenzzeichenSignalStates.INVALID;
        };

        return Result.of(result, null);
    }

    public static void registerAnchor(@NotNull World world, @NotNull BlockPos pos) {
        var key = world.getRegistryKey();
        ANCHORS.computeIfAbsent(key, k -> new HashSet<>()).add(pos.toImmutable());
    }

    public static void unregisterAnchor(@NotNull World world, @NotNull BlockPos pos) {
        var key = world.getRegistryKey();
        var set = ANCHORS.get(key);
        if (set != null) {
            set.remove(pos);
            if (set.isEmpty()) ANCHORS.remove(key);
        }
        var cache = CACHE.get(key);
        if (cache != null) {
            cache.remove(pos);
            if (cache.isEmpty()) CACHE.remove(key);
        }
    }

    public static void onEndWorldTick(ServerWorld world) {
        var key = world.getRegistryKey();
        var set = ANCHORS.get(key);
        if (set == null || set.isEmpty()) return;

        tickCounter++;
        if ((tickCounter % 20) != 0) return;

        for (BlockPos anchor : set) {
            if (world.isChunkLoaded(anchor.getX() >> 4, anchor.getZ() >> 4)) {
                BlockPos nearest = findNearestSignalPos(world, anchor);
                CACHE.computeIfAbsent(key, k -> new HashMap<>()).put(anchor, nearest);
            }
        }
    }

    public static Optional<BlockPos> tryFindSignal(@NotNull World world, @NotNull BlockPos anchorPos) {
        if (!IS_CREATE_AVAILABLE) return Optional.empty();

        var key = world.getRegistryKey();
        var cache = CACHE.get(key);
        if (cache != null && cache.containsKey(anchorPos)) {
            return Optional.ofNullable(cache.get(anchorPos));
        }

        BlockPos nearest = findNearestSignalPos(world, anchorPos);
        CACHE.computeIfAbsent(key, k -> new HashMap<>()).put(anchorPos, nearest);
        return Optional.ofNullable(nearest);
    }

    private static BlockPos findNearestSignalPos(@NotNull World world, @NotNull BlockPos origin) {
        SignalBlockEntity nearestSignal = null;
        int nearestDistSq = Integer.MAX_VALUE;

        for (int dx = -SIGNAL_SEARCH_RADIUS; dx <= SIGNAL_SEARCH_RADIUS; dx++) {
            for (int dy = -SIGNAL_SEARCH_RADIUS; dy <= SIGNAL_SEARCH_RADIUS; dy++) {
                for (int dz = -SIGNAL_SEARCH_RADIUS; dz <= SIGNAL_SEARCH_RADIUS; dz++) {
                    BlockPos checkPos = origin.add(dx, dy, dz);
                    BlockEntity be = world.getBlockEntity(checkPos);
                    if (be instanceof SignalBlockEntity sbe) {
                        int distSq = dx * dx + dy * dy + dz * dz;
                        if (distSq < nearestDistSq) {
                            nearestDistSq = distSq;
                            nearestSignal = sbe;
                        }
                    }
                }
            }
        }

        return nearestSignal != null ? nearestSignal.getPos() : null;
    }

    public enum SignalTextureResult {
        CREATE_UNAVAILABLE,
        BLOCK_BROKEN
    }

    public enum GrenzzeichenSignalStates {
        RED(new Identifier("grenzzeichen", "textures/block/hauptsignal_red.png"),
                new Identifier("grenzzeichen", "textures/block/hauptsignal_bruecke_halb_red.png")),
        YELLOW(new Identifier("grenzzeichen", "textures/block/hauptsignal_yellow.png"),
                new Identifier("grenzzeichen", "textures/block/hauptsignal_bruecke_halb_green.png")),
        GREEN(new Identifier("grenzzeichen", "textures/block/hauptsignal_green.png"),
                new Identifier("grenzzeichen", "textures/block/hauptsignal_bruecke_halb_green.png")),
        INVALID(new Identifier("grenzzeichen", "textures/block/hauptsignal.png"),
                new Identifier("grenzzeichen", "textures/block/hauptsignalbruecke.png"));

        private final Identifier hauptSignalTexture;
        private final Identifier hauptSignalBrueckeTexture;

        GrenzzeichenSignalStates(Identifier hauptSignalTexture, Identifier hauptSignalBrueckeTexture) {
            this.hauptSignalTexture = hauptSignalTexture;
            this.hauptSignalBrueckeTexture = hauptSignalBrueckeTexture;
        }

        public Identifier getHauptSignalTexture() {
            return this.hauptSignalTexture;
        }

        public Identifier getHauptSignalBrueckeTexture() {
            return this.hauptSignalBrueckeTexture;
        }
    }
}
