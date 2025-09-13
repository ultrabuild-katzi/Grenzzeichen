package de.raphicraft.grenzzeichen.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.joml.Vector3f;

public class RedBlock extends Block {
    public RedBlock(Settings settings) {
        super(settings.ticksRandomly().luminance(state -> 15));
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos,
                                  net.minecraft.util.math.random.Random random) {
        if (!world.isClient) return;

        if (random.nextFloat() < 0.25f) {
            double x = pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.6;
            double y = pos.getY() + 0.75 + (random.nextDouble() - 0.5) * 0.5;
            double z = pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.6;

            world.addParticle(
                    new DustParticleEffect(new Vector3f(1.0f, 0.08f, 0.08f), 1.0f),
                    x, y, z,
                    0, 0.003, 0
            );
        }
    }
}
