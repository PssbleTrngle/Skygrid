package com.possibletriangle.skygrid.generation;

import com.possibletriangle.skygrid.defaults.vanilla.DefaultsEnd;
import com.possibletriangle.skygrid.random.RandomCollection;
import com.possibletriangle.skygrid.random.SkygridOptions;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChunkGeneratorSkygrid implements IChunkGenerator {
    private final World world;
    private final ResourceLocation dimension;
    private final Random random;
    private final BlockPos offset, spawnBlock, endPortal;
    private final int gridHeight;

    public ChunkGeneratorSkygrid(World world, ResourceLocation dimension, Random random, BlockPos offset, BlockPos spawnBlock, BlockPos endPortal, int gridHeight) {
        this.world = world;
        this.dimension = dimension;
        this.random = random;
        this.offset = offset;
        this.spawnBlock = spawnBlock;
        this.endPortal = endPortal;
        this.gridHeight = gridHeight;
    }

    @Override
    public Chunk generateChunk(int chunkX, int chunkZ) {
        BlockFalling.fallInstantly = false;
        ChunkPrimer primer = new ChunkPrimer();

        for(int x = 0; x < 16; x++)
            for(int z = 0; z < 16; z++)
                for (int y = 0; y < gridHeight; y++) {
                    BlockPos p = new BlockPos((x+1)%16, y+1, (z+1)%16);
                    IBlockState fillBlock = SkygridOptions.getFillBlock(dimension, y);

                    if(primer.getBlockState(p.getX(), p.getY(), p.getZ()).getBlock() != Blocks.AIR)
                        continue;

                    if (y % offset.getY() == 0 && (z + chunkZ * 16) % offset.getZ() == 0 && (x + chunkX * 16) % offset.getX() == 0) {

                        if (p.up().equals(spawnBlock))
                            primer.setBlockState(p.getX(), p.getY(), p.getZ(), Blocks.BEDROCK.getDefaultState());
                        else if (endPortal != null && new BlockPos(x + chunkX * 16, y, z + chunkZ * 16).equals(endPortal))
                            DefaultsEnd.PORTAL.generateAt(primer, p.getX(), p.getZ(), p.getY(), random, fillBlock);
                        else if(y == 0)
                            primer.setBlockState(p.getX(), p.getY(), p.getZ(), Blocks.BEDROCK.getDefaultState());
                        else
                            SkygridOptions.next(dimension, random, y).generateAt(primer, p.getX(), p.getZ(), p.getY(), random, fillBlock);

                    } else if (primer.getBlockState(p.getX(), p.getY(), p.getZ()).getBlock() == Blocks.AIR)
                        primer.setBlockState(p.getX(), p.getY(), p.getZ(), fillBlock);
                }


        Chunk chunk = new Chunk(world, primer, chunkX, chunkZ);
        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public void populate(int chunkX, int chunkZ) {

        for(int x = 0; x < 16; x++)
            for(int z = 0; z < 16; z++)
                for (int y = 1; y < gridHeight; y++) {

                    BlockPos pos = new BlockPos(x, y, z).add(chunkX*16, 0, chunkZ*16);
                    IBlockState state = world.getBlockState(pos);
                    if(state.getBlock().hasTileEntity(state)) {

                        TileEntity te = world.getTileEntity(pos);
                        if(te != null) {

                            NBTTagCompound nbt = new NBTTagCompound();
                            te.writeToNBT(nbt);

                            if (new ResourceLocation("quark", "custom_chest").equals(state.getBlock().getRegistryName())) {
                                String[] type = new String[]{"spruce", "birch", "jungle", "dark_oak", "acacia"};
                                nbt.setString("type", type[random.nextInt(type.length)]);
                            }

                            nbt.setInteger("x", pos.getX());
                            nbt.setInteger("y", pos.getY());
                            nbt.setInteger("z", pos.getZ());
                            te.readFromNBT(nbt);

                            RandomCollection<ResourceLocation> loot = SkygridOptions.getLoot(dimension);
                            RandomCollection<ResourceLocation> mobs = SkygridOptions.getMobs(dimension);

                            if (te instanceof TileEntityLockableLoot && loot.size() != 0) {
                                ResourceLocation l = loot.next(random);
                                ((TileEntityLockableLoot) te).setLootTable(l, random.nextLong());
                            } else if (te instanceof TileEntityMobSpawner && mobs.size() != 0) {
                                ResourceLocation mob = mobs.next(random);
                                te.writeToNBT(nbt);
                                nbt.removeTag("SpawnPotentials");
                                te.readFromNBT(nbt);
                                ((TileEntityMobSpawner) te).getSpawnerBaseLogic().setEntityId(mob);
                            }

                            Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
                            te.rotate(rotation);
                            te.markDirty();
                        }
                    }
            }

    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(@Nonnull EnumCreatureType creatureType, @Nonnull BlockPos pos) {
        return new ArrayList<>();
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos( @Nonnull World world, @Nonnull String name, @Nonnull BlockPos position, boolean findUnexplored) {
        if ("Stronghold".equals(name) && endPortal != null) {
            return endPortal.add(1, findUnexplored ? 1 : position.getY(), 1);
        }

        return null;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {

    }

    @Override
    public boolean isInsideStructure(World world, String name, BlockPos pos) {
		BlockPos nearest = getNearestStructurePos(world, name, pos, true);
        return nearest != null && new Vec3d(nearest).distanceTo(new Vec3d(pos)) <= 2;
    }
}
