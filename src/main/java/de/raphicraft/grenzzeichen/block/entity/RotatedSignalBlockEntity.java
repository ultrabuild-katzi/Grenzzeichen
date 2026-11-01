package de.raphicraft.grenzzeichen.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

/** Stores the absolute placement yaw (player yaw convention) of a rotated, model-based signal block. */
public class RotatedSignalBlockEntity extends BlockEntity {
    private float yaw;

    public RotatedSignalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ROTATED_SIGNAL, pos, state);
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        markDirty();
        if (world != null && !world.isClient()) {
            world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putFloat("Yaw", yaw);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        yaw = nbt.getFloat("Yaw");
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
