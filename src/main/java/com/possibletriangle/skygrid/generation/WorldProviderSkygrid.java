package com.possibletriangle.skygrid.generation;

import com.possibletriangle.skygrid.Skygrid;
import com.possibletriangle.skygrid.random.SkygridOptions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldProviderSkygrid extends WorldProvider {

    private BlockPos end_portal;
    public WorldProviderSkygrid() {
    }

    private Random random;

    @Override
    protected void init() {
        super.init();

        BlockPos offset = SkygridOptions.getOffset(getName());

        random = world == null ? new Random() : new Random(world.getSeed());
        int r = 1024;
        if(getDimensionType().getId() == 0)
            end_portal = new BlockPos(offset.getX() * random.nextInt(r*2) - r, 0, offset.getZ() * random.nextInt(r*2) - r);
    }

    @Override
    public final DimensionType getDimensionType() {
        return DimensionType.getById(getDimension());
    }

    @Override
    public final IChunkGenerator createChunkGenerator() {

        ResourceLocation dimension = getName();
        BlockPos offset = SkygridOptions.getOffset(dimension);

        if(random == null) random = new Random(world.getSeed());
        return new ChunkGeneratorSkygrid(world, getName(), random, offset, getSpawnCoordinate(), end_portal, getGridHeight());
    }

    public final ResourceLocation getName() {
        return new ResourceLocation(getDimensionType().getName());
    }

    @Nonnull
    @Override
    public final BlockPos getSpawnCoordinate() {
        return WorldProviderSkygrid.spawn(getName(), getGridHeight());
    }

    @Override
    public final BlockPos getSpawnPoint() {
        return getSpawnCoordinate();
    }

    @Override
    public final BlockPos getRandomizedSpawnPoint() {
        return getSpawnCoordinate();
    }

    public final int getGridHeight() {

        int height = SkygridOptions.getHeight(getName());
        return Math.min(Skygrid.WORLD_HEIGHT-3, height);

    }

    public static BlockPos spawn(ResourceLocation name, int gridHeight) {
        BlockPos offset = SkygridOptions.getOffset(name);
        int y = (gridHeight / 2);
        y -= y % offset.getY();
        return new BlockPos(0, y+1, 0).add(1, 1, 1);
    }

    @Override
    public final double getHorizon() {
        return 0;
    }
}
