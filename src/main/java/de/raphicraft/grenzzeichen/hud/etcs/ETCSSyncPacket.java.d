package de.raphicraft.grenzzeichen.hud.etcs;

import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.CreateClient;
import de.raphicraft.grenzzeichen.Grenzzeichen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;



import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ETCSSyncPacket implements S2CPacket {
    private final UUID trainId;
    private final double distanceToSignal;
    private final double speedLimit;
    private final float needleRotation;
    private final boolean backward;
    private final double emergencyBrakingDist;
    private final double serviceBrakingDist;
    private final double warningBrakingDist;
    private final boolean cachedCurveIsDropping;
    private final List<ETCS.SpeedLimit> speedLimits;
    private final int zoom;

    public ETCSSyncPacket(UUID trainId, double distanceToSignal, double speedLimit, float needleRotation,
                          boolean backward, double emergencyBrakingDist, double serviceBrakingDist,
                          double warningBrakingDist, boolean cachedCurveIsDropping, List<ETCS.SpeedLimit> speedLimits, int zoom
    ) {
        this.trainId = trainId;
        this.distanceToSignal = distanceToSignal;
        this.speedLimit = speedLimit;
        this.needleRotation = needleRotation;
        this.backward = backward;
        this.emergencyBrakingDist = emergencyBrakingDist;
        this.serviceBrakingDist = serviceBrakingDist;
        this.warningBrakingDist = warningBrakingDist;
        this.cachedCurveIsDropping = cachedCurveIsDropping;
        this.speedLimits = speedLimits;
        this.zoom = zoom;
    }

    public static ETCSSyncPacket read(PacketByteBuf buffer) {
        UUID trainId = buffer.readUuid(); // readUUID -> readUuid
        double distanceToSignal = buffer.readDouble();
        double speedLimit = buffer.readDouble();
        float needleRotation = buffer.readFloat();
        boolean backward = buffer.readBoolean();
        double emergencyBrakingDist = buffer.readDouble();
        double serviceBrakingDist = buffer.readDouble();
        double warningBrakingDist = buffer.readDouble();
        boolean cachedCurveIsDropping = buffer.readBoolean();
        int speedLimitCount = buffer.readVarInt();
        List<ETCS.SpeedLimit> speedLimits = new ArrayList<>();
        for (int i = 0; i < speedLimitCount; i++) {
            speedLimits.add(ETCS.SpeedLimit.read(buffer));
        }
        int zoom = buffer.readVarInt();
        return new ETCSSyncPacket(
                trainId, distanceToSignal, speedLimit, needleRotation,
                backward, emergencyBrakingDist, serviceBrakingDist, warningBrakingDist,
                cachedCurveIsDropping, speedLimits, zoom
        );


    }


    @Override
    public void write(PacketByteBuf buffer) {
        buffer.writeUuid(trainId);
        buffer.writeDouble(distanceToSignal);
        buffer.writeDouble(speedLimit);
        buffer.writeFloat(needleRotation);
        buffer.writeBoolean(backward);
        buffer.writeDouble(emergencyBrakingDist);
        buffer.writeDouble(serviceBrakingDist);
        buffer.writeDouble(warningBrakingDist);
        buffer.writeBoolean(cachedCurveIsDropping);
        buffer.writeInt(zoom);

        // Write speed limits
        buffer.writeInt(speedLimits.size());
        for (ETCS.SpeedLimit limit : speedLimits) {
            buffer.writeDouble(limit.getDistance());
            buffer.writeDouble(limit.getSpeedLimit());
        }
    }

    @Override
    public void handle(MinecraftClient mc) {

        // Schedule task to run on the main client thread
        RenderSystem.recordRenderCall(() -> {
            try {
                // Get the train from the client-side train registry
                if (CreateClient.RAILWAYS.trains.get(trainId) != null && CreateClient.RAILWAYS.trains.get(trainId) instanceof ITrainInterface) {
                    ITrainInterface trainInterface = (ITrainInterface) CreateClient.RAILWAYS.trains.get(trainId);
                    if (trainInterface.realism$getETCS() == null) {
                        trainInterface.realism$setETCS(new ETCS(CreateClient.RAILWAYS.trains.get(trainId)));
                    }

                    trainInterface.realism$getETCS().updateFromNetwork(
                            distanceToSignal, speedLimit, needleRotation, backward,
                            emergencyBrakingDist, serviceBrakingDist, warningBrakingDist, cachedCurveIsDropping,speedLimits,zoom
                    );
                }
            } catch (Exception e) {
                Grenzzeichen.LOGGER.error("Error handling ETCS sync packet", e);
            }
        });
    }



}
