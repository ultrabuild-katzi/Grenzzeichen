package de.raphicraft.grenzzeichen.compat;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Method;
import java.util.Objects;

public class CreateCompat {

    public static Identifier getSignalTexture(World world, BlockPos pos) {
        try {
            double nearestDistSq = Double.MAX_VALUE;
            Object foundSignal = null;
            int radius = 2;

            BlockPos.Mutable searchPos = new BlockPos.Mutable();

            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        searchPos.set(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
                        BlockEntity be = world.getBlockEntity(searchPos);

                        if (be != null && be.getClass().getName().equals("com.simibubi.create.content.trains.signal.SignalBlockEntity")) {
                            double distSq = pos.getSquaredDistance(searchPos.getX(), searchPos.getY(), searchPos.getZ());
                            if (distSq < nearestDistSq) {
                                nearestDistSq = distSq;
                                foundSignal = be;
                            }
                        }
                    }
                }
            }

            if (foundSignal != null) {
                Class<?> signalClass = foundSignal.getClass();
                Method getStateMethod = signalClass.getMethod("getState");
                Object stateEnum = getStateMethod.invoke(foundSignal);

                String stateName = stateEnum.toString(); // should return "RED", "YELLOW", "GREEN", "INVALID"

                return switch (stateName) {
                    case "RED" -> new Identifier("grenzzeichen", "textures/block/hauptsignal_red.png");
                    case "YELLOW" -> new Identifier("grenzzeichen", "textures/block/hauptsignal_yellow.png");
                    case "GREEN" -> new Identifier("grenzzeichen", "textures/block/hauptsignal_green.png");
                    case "INVALID" -> new Identifier("grenzzeichen", "textures/block/hauptsignal.png");
                    default -> null;
                };
            }

        } catch (Throwable t) {
            System.out.println("[Hauptsignal] CreateCompat failed: " + t.getMessage());
        }

        return null;
    }
}
