package de.raphicraft.grenzzeichen.compat;

import com.simibubi.create.content.trains.signal.SignalBlockEntity;
import de.raphicraft.grenzzeichen.util.Result;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CreateCompat {
    private static final boolean IS_CREATE_AVAILABLE = FabricLoader.getInstance().isModLoaded("create");
    private static final int SIGNAL_SEARCH_RADIUS = 2;

    // anchors to track per world
    private static final Map<RegistryKey<World>, Set<BlockPos>> ANCHORS = new HashMap<>();
    // cached nearest signal per anchor
    private static final Map<RegistryKey<World>, Map<BlockPos, BlockPos>> CACHE = new HashMap<>();
    // listeners per anchor
    private static final Map<RegistryKey<World>, Map<BlockPos, List<NearestSignalListener>>> LISTENERS = new HashMap<>();

    private static int tickCounter = 0;

    @FunctionalInterface
    public interface NearestSignalListener {
        void onNearestChanged(@Nullable BlockPos oldPos, @Nullable BlockPos newPos);
    }

    public static void initTickHook() {
        ServerTickEvents.END_WORLD_TICK.register(CreateCompat::onEndWorldTick);
    }

    public static Result<GrenzzeichenSignalStates, SignalTextureResult> getSignalTexture(@NotNull World world, @Nullable BlockPos blockPos) {
        if (!IS_CREATE_AVAILABLE) return Result.of(null, SignalTextureResult.CREATE_UNAVAILABLE);
        if (blockPos == null) return Result.of(null, SignalTextureResult.BLOCK_BROKEN);

        BlockEntity be = world.getBlockEntity(blockPos);
        if (!(be instanceof SignalBlockEntity sbe)) return Result.of(null, SignalTextureResult.BLOCK_BROKEN);

        GrenzzeichenSignalStates result = switch (sbe.getState()) {
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

    public static void registerAnchor(@NotNull World world, @NotNull BlockPos pos, @NotNull NearestSignalListener listener) {
        registerAnchor(world, pos);
        addNearestSignalListener(world, pos, listener);
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
        var lm = LISTENERS.get(key);
        if (lm != null) {
            lm.remove(pos);
            if (lm.isEmpty()) LISTENERS.remove(key);
        }
    }

    public static void addNearestSignalListener(@NotNull World world, @NotNull BlockPos pos, @NotNull NearestSignalListener listener) {
        var key = world.getRegistryKey();
        LISTENERS.computeIfAbsent(key, k -> new HashMap<>())
                .computeIfAbsent(pos.toImmutable(), p -> new ArrayList<>())
                .add(listener);
    }

    public static void removeNearestSignalListener(@NotNull World world, @NotNull BlockPos pos, @NotNull NearestSignalListener listener) {
        var key = world.getRegistryKey();
        var byPos = LISTENERS.get(key);
        if (byPos == null) return;
        var list = byPos.get(pos);
        if (list == null) return;
        list.remove(listener);
        if (list.isEmpty()) {
            byPos.remove(pos);
            if (byPos.isEmpty()) LISTENERS.remove(key);
        }
    }

    public static void onEndWorldTick(ServerWorld world) {
        if (!IS_CREATE_AVAILABLE) return;

        var key = world.getRegistryKey();
        var anchors = ANCHORS.get(key);
        if (anchors == null || anchors.isEmpty()) return;

        tickCounter++;
        if ((tickCounter % 20) != 0) return; // ~1s

        var cache = CACHE.computeIfAbsent(key, k -> new HashMap<>());
        var listenersByPos = LISTENERS.get(key);

        for (BlockPos anchor : anchors) {
            if (!world.isChunkLoaded(anchor.getX() >> 4, anchor.getZ() >> 4)) continue;

            BlockPos oldNearest = cache.get(anchor);
            BlockPos newNearest = findNearestSignalPos(world, anchor);

            if (!Objects.equals(oldNearest, newNearest)) {
                cache.put(anchor, newNearest);
                if (listenersByPos != null) {
                    var ls = listenersByPos.get(anchor);
                    if (ls != null) {
                        for (NearestSignalListener l : ls) {
                            try {
                                l.onNearestChanged(oldNearest, newNearest);
                            } catch (Throwable t) {
                                // swallow to avoid breaking tick loop
                            }
                        }
                    }
                }
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

    @Nullable
    public static BlockPos getLastNearestSignal(@NotNull World world, @NotNull BlockPos anchorPos) {
        var key = world.getRegistryKey();
        var cache = CACHE.get(key);
        return cache != null ? cache.get(anchorPos) : null;
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

        public Identifier getHauptSignalTexture() { return this.hauptSignalTexture; }
        public Identifier getHauptSignalBrueckeTexture() { return this.hauptSignalBrueckeTexture; }
    }
}
