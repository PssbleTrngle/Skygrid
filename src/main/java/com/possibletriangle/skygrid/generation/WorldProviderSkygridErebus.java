package com.possibletriangle.skygrid.generation;

import com.possibletriangle.skygrid.Skygrid;
import com.possibletriangle.skygrid.random.SkygridOptions;
import erebus.world.WorldProviderErebus;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldProviderSkygridErebus extends WorldProviderErebus {

    @Override
    protected void init() {
        super.init();
        BlockPos offset = SkygridOptions.getOffset(getName());
    }

    @Override
    public final DimensionType getDimensionType() {
        return DimensionType.getById(getDimension());
    }

    @Override
    public final IChunkGenerator createChunkGenerator() {

        ResourceLocation dimension = getName();
        BlockPos offset = SkygridOptions.getOffset(dimension);

        return new ChunkGeneratorSkygrid(world, getName(), new Random(world.getSeed()), offset, getSpawnCoordinate().down(), null, getGridHeight());
    }

    public final ResourceLocation getName() {
        return new ResourceLocation(getDimensionType().getName());
    }

    @Nonnull
    @Override
    public BlockPos getSpawnCoordinate() {
        return WorldProviderSkygrid.spawn(getName(), getGridHeight());
    }

    @Override
    public BlockPos getSpawnPoint() {
        return getSpawnCoordinate();
    }

    @Override
    public BlockPos getRandomizedSpawnPoint() {
        return getSpawnCoordinate();
    }

    public int getGridHeight() {

        int height = SkygridOptions.getHeight(getName());
        return Math.min(Skygrid.WORLD_HEIGHT-3, height);

    }

    @Override
    public double getHorizon() {
        return 0;
    }

}
