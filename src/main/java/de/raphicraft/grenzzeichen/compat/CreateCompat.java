package de.raphicraft.grenzzeichen.compat;

import com.simibubi.create.content.trains.signal.SignalBlockEntity;
import de.raphicraft.grenzzeichen.util.Result;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CreateCompat {
    public static final boolean IS_CREATE_AVAILABLE = FabricLoader.getInstance().isModLoaded("create");
    private static final int SIGNAL_SEARCH_RADIUS = 2;

    // cached nearest signal per anchor
    private static final Map<RegistryKey<World>, Map<BlockPos, BlockPos>> CACHE = new HashMap<>();

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
        RED(new Identifier("grenzzeichen", "textures/block/hv_hauptsignal_red.png"),
                new Identifier("grenzzeichen", "textures/block/hauptsignal_bruecke_halb_red.png")),
        YELLOW(new Identifier("grenzzeichen", "textures/block/hv_hauptsignal_yellow.png"),
                new Identifier("grenzzeichen", "textures/block/hauptsignal_bruecke_halb_green.png")),
        GREEN(new Identifier("grenzzeichen", "textures/block/hv_hauptsignal_green.png"),
                new Identifier("grenzzeichen", "textures/block/hauptsignal_bruecke_halb_green.png")),
        INVALID(new Identifier("grenzzeichen", "textures/block/hv_hauptsignal.png"),
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
