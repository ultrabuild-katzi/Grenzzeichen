package de.raphicraft.grenzzeichen.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

/**
 * Block entity that can store an exact (1 degree) placement yaw.
 * NO_PRECISE_YAW means the block uses the facing / fine_rotation block state instead.
 */
public abstract class PreciseRotationBlockEntity extends BlockEntity {
    public static final int NO_PRECISE_YAW = -1;
    private static final String NBT_KEY = "PreciseYaw";

    private int preciseYaw = NO_PRECISE_YAW;

    protected PreciseRotationBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public int getPreciseYaw() {
        return preciseYaw;
    }

    public boolean hasPreciseYaw() {
        return preciseYaw != NO_PRECISE_YAW;
    }

    public void setPreciseYaw(int yaw) {
        this.preciseYaw = yaw == NO_PRECISE_YAW ? NO_PRECISE_YAW : Math.floorMod(yaw, 360);
        markDirty();
        if (world != null && !world.isClient()) {
            world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt(NBT_KEY, preciseYaw);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        preciseYaw = nbt.contains(NBT_KEY) ? nbt.getInt(NBT_KEY) : NO_PRECISE_YAW;
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
