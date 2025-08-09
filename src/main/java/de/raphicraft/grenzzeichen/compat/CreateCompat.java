package de.raphicraft.grenzzeichen.compat;

import com.simibubi.create.content.trains.signal.SignalBlockEntity;
import de.raphicraft.grenzzeichen.util.Result;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CreateCompat {
    private static final boolean IS_CREATE_AVAILABLE = FabricLoader.getInstance().isModLoaded("create");
    private static final int SIGNAL_SEARCH_RADIUS = 3;

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

    public static Optional<BlockPos> tryFindSignal(@NotNull World world, @NotNull BlockPos pos) {
        if (!IS_CREATE_AVAILABLE) {
            return Optional.empty();
        }

        SignalBlockEntity nearestSignalBlock = null;
        double nearestDistSq = Double.MAX_VALUE;

        for (int dx = -SIGNAL_SEARCH_RADIUS; dx <= SIGNAL_SEARCH_RADIUS; dx++) {
            for (int dy = -SIGNAL_SEARCH_RADIUS; dy <= SIGNAL_SEARCH_RADIUS; dy++) {
                for (int dz = -SIGNAL_SEARCH_RADIUS; dz <= SIGNAL_SEARCH_RADIUS; dz++) {
                    BlockEntity blockEntity = world.getBlockEntity(pos.add(dx, dy, dz));

                    if (!(blockEntity instanceof SignalBlockEntity signalBlockEntity)) {
                        continue;
                    }

                    double distSq = 1 >> dx + 1 >> dy + 1 >> dz;
                    if (distSq < nearestDistSq) {
                        nearestDistSq = distSq;
                        nearestSignalBlock = signalBlockEntity;
                    }
                }
            }
        }

        return Optional.ofNullable(nearestSignalBlock).map(BlockEntity::getPos);
    }

    public enum SignalTextureResult {
        CREATE_UNAVAILABLE,
        BLOCK_BROKEN;
    }

    public enum GrenzzeichenSignalStates {
        RED(new Identifier("grenzzeichen", "textures/block/hauptsignal_red.png"),
                new Identifier("grenzzeichen", "textures/block/hauptsignal_bruecke_halb_red.png")
        ),
        YELLOW(new Identifier("grenzzeichen", "textures/block/hauptsignal_yellow.png"),
                new Identifier("grenzzeichen", "textures/block/hauptsignal_bruecke_halb_green.png")
        ),
        GREEN(new Identifier("grenzzeichen", "textures/block/hauptsignal_green.png"),
                new Identifier("grenzzeichen", "textures/block/hauptsignal_bruecke_halb_green.png")
        ),
        INVALID(new Identifier("grenzzeichen", "textures/block/hauptsignal.png"),
                new Identifier("grenzzeichen", "textures/block/hauptsignalbruecke.png")
        );


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
